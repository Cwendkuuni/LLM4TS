package org.apache.commons.math3.optimization.direct;

import static org.apache.commons.math3.optim.PointValuePair.Type.MINIMIZE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    private PowellOptimizer optimizer;
    private MultivariateFunction function;
    private double[] startPoint;
    private double[] expectedResult;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        optimizer = new PowellOptimizer(1e-4, 1e-4);
        function = new RosenbrockFunction();
        startPoint = new double[]{-1.2, -1.2};
        expectedResult = new double[]{-1.0, -1.0};
    }

    /**
     * Tests basic functionality of {@link PowellOptimizer}.
     */
    @Test
    public void testBasicFunctionality() {
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(MINIMIZE, result.getType());
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(function.value(expectedResult), result.getValue(), 0.001);
    }

    /**
     * Tests exception handling when a negative threshold is provided.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeThresholds() {
        PowellOptimizer optimizer = new PowellOptimizer(-1e-4, 1e-4);
    }

    /**
     * Tests user-defined convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customChecker = (iter, previous, current) -> false;
        optimizer = new PowellOptimizer(1e-4, 1e-4, customChecker);
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertFalse(customChecker.converged(iter, previous, current));
    }

    /**
     * Tests convergence checking procedure.
     */
    @Test
    public void testConvergenceCheckingProcedure() {
        ConvergenceChecker<PointValuePair> customChecker = (iter, previous, current) -> iter > 10;
        optimizer = new PowellOptimizer(1e-4, 1e-4, customChecker);
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertTrue(customChecker.converged(result.getIteration(), null, null));
    }

    /**
     * Tests user-defined line search tolerances.
     */
    @Test
    public void testCustomLineSearchTolerances() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4, 0.1, 0.1);
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertArrayEquals(expectedResult, result.getPoint(), 0.01);
    }

    /**
     * Tests custom line search.
     */
    @Test
    public void testCustomLineSearch() {
        final double epsilon = 1e-4;
        final double[][] direc = new double[][]{{1.0 / FastMath.SQRT_2, 1.0 / FastMath.SQRT_2},
                {-1.0 / FastMath.SQRT_2, 1.0 / FastMath.SQRT_2},
                {-1.0 / FastMath.SQRT_2, -1.0 / FastMath.SQRT_2},
                {1.0 / FastMath.SQRT_2, -1.0 / FastMath.SQRT_2}};
        final double tolerance = 1e-6;

        optimizer = new PowellOptimizer(1e-4, 1e-4) {
            @Override
            protected PointValuePair doOptimize() {
                double[] x = startPoint.clone();
                double fVal = computeObjectiveValue(x);
                double[] x1 = x.clone();
                int iter = 0;
                while (true) {
                    ++iter;
                    double fX = fVal;
                    double fX2 = 0;
                    double delta = 0;
                    int bigInd = 0;
                    double alphaMin = 0;
                    for (int i = 0; i < x.length; i++) {
                        final double[] d = MathArrays.copyOf(direc[i]);
                        final UnivariatePointValuePair optimum = lineSearch(x, d, tolerance);
                        fVal = optimum.getValue();
                        alphaMin = optimum.getPoint();
                        double[][] result = newPointAndDirection(x, d, alphaMin);
                        x = result[0];
                        if ((fX2 - fVal) > delta) {
                            delta = fX2 - fVal;
                            bigInd = i;
                        }
                    }
                    double[] d = new double[x.length];
                    double[] x2 = new double[x.length];
                    for (int i = 0; i < x.length; i++) {
                        d[i] = x[i] - x1[i];
                        x2[i] = 2 * x[i] - x1[i];
                    }
                    x1 = x.clone();
                    fX2 = computeObjectiveValue(x2);
                    if (fX > fX2) {
                        UnivariatePointValuePair optimum = lineSearch(x, d, tolerance);
                        fVal = optimum.getValue();
                        alphaMin = optimum.getPoint();
                        double[][] result = newPointAndDirection(x, d, alphaMin);
                        x = result[0];
                        direc[bigInd] = direc[x.length - 1];
                        direc[x.length - 1] = result[1];
                    }
                    if (FastMath.abs(fX - fVal) <= epsilon * (FastMath.abs(fX) + FastMath.abs(fVal))) {
                        break;
                    }
                }
                return new PointValuePair(x, fVal);
            }

            private UnivariatePointValuePair lineSearch(double[] p, double[] d, double tolerance) {
                BracketFinder bracketFinder = new BracketFinder();
                UnivariateFunction f = new UnivariateFunction() {
                    @Override
                    public double value(double alpha) {
                        double[] x = new double[p.length];
                        for (int i = 0; i < p.length; i++) {
                            x[i] = p[i] + alpha * d[i];
                        }
                        return PowellOptimizer.this.computeObjectiveValue(x);
                    }
                };
                UnivariatePointValuePair result = optimize(new MaxEval(100), f, MINIMIZE,
                        bracketFinder.search(f, MINIMIZE, 0, 1)[1],
                        bracketFinder.search(f, MINIMIZE, 0, 1)[2],
                        bracketFinder.search(f, MINIMIZE, 0, 1)[0]);
                return result;
            }
        };
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
    }

    /**
     * Tests default convergence checking procedure with a custom function.
     */
    @Test
    public void testDefaultConvergenceCheckingProcedureWithCustomFunction() {
        MultivariateFunction function = new RastriginFunction();
        optimizer = new PowellOptimizer(1e-4, 1e-4);
        PointValuePair result = optimizer.optimize(function, new double[]{1.0, 1.0});
        assertEquals(MINIMIZE, result.getType());
        assertEquals(0.0, result.getValue(), 0.001);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
    }

}

class RosenbrockFunction implements MultivariateFunction {
    @Override
    public double value(double[] point) {
        double sum = 0.0;
        for (int i = 0; i < point.length - 1; i++) {
            double diff = point[i + 1] - point[i] * point[i];
            sum += diff * diff + 100 * (1.0 - point[i]) * (1.0 - point[i]);
        }
        return sum;
    }
}

class RastriginFunction implements MultivariateFunction {
    @Override
    public double value(double[] point) {
        double sum = 0.0;
        for (int i = 0; i < point.length; i++) {
            sum += Math.pow(point[i] - Math.cos(2 * FastMath.PI * i), 2.0)
                  + 10.0 * (Math.PI * i - Math.sin(2 * FastMath.PI * i));
        }
        sum *= 2;
        return sum;
    }
}