package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.SimplePointValuePair;
import org.apache.commons.math3.optim.points.PointValues;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizerTest;
import org.apache.commons.math3.optim.univariate.ValueChecker;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.initializer.AroundPointInitializer;
import org.apache.commons.math3.optim.initializer.RandomInitializer;
import org.apache.commons.math3.optim.initializer.SimpleRandomInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MINIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MAXIMIZE;
import org.apache.commons.math3.optim.points.Extrema;
import org.apache.commons.math3.optim.points.MaxEval;
import org.apache.commons.math3.optim.points.Points;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class PowellOptimizerTest {

    private static final double DELTA = 1e-9;
    private static final double EPSILON = 1e-12;
    private static final double RELATIVE_TOLERANCE = 1e-4;
    private static final double ABSOLUTE_TOLERANCE = 1e-6;
    private static final GoalType GOAL_TYPE = MINIMIZE;

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Testing with univariate functions.
                new Object[][]{new PolynomialFunction(3, 1), new double[]{1.5}, null},
                new Object[][]{new PolynomialFunction(-3, 1), new double[]{1.5}, new UnivariateOptimizer[]{new BrentOptimizer(EPSILON, EPSILON), new BrentOptimizer(DELTA, ABSOLUTE_TOLERANCE), new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE)}},
                new Object[][]{new PolynomialFunction(3, 1), new double[]{1.5}, new UnivariateOptimizer[]{new BrentOptimizer(EPSILON, EPSILON), new BrentOptimizer(DELTA, ABSOLUTE_TOLERANCE), new PowellOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE), new PowellOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE, new SimpleUnivariateValueChecker(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE))}},

                // Testing with multivariate functions.
                new Object[][]{new RosenbrockFunction(), new double[]{1.0, 1.0}, null},
                new Object[][]{new RastriginFunction(), new double[]{1.0, 1.0}, new UnivariateOptimizer[]{new BrentOptimizer(EPSILON, EPSILON), new BrentOptimizer(DELTA, ABSOLUTE_TOLERANCE), new PowellOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE), new PowellOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE, new SimpleUnivariateValueChecker(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE))}},
                new Object[][]{new RastriginFunction(), new double[]{1.0, 1.0}, new UnivariateOptimizer[]{new BrentOptimizer(EPSILON, EPSILON), new BrentOptimizer(DELTA, ABSOLUTE_TOLERANCE), new PowellOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE, new SimpleUnivariateValueChecker(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE)), new PowellOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE, new SimpleUnivariateValueChecker(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE), new ValueChecker() {
                        @Override
                        public boolean isSatisfied(PointValuePair p, int iteration) {
                            return p.getValue() < 10.0;
                        }
                    })}}});
    }

    private MultivariateFunction function;
    private double[] startPoint;
    private UnivariateOptimizer[] optimizers;

    public PowellOptimizerTest(MultivariateFunction function, double[] startPoint, UnivariateOptimizer... optimizers) {
        this.function = function;
        this.startPoint = startPoint;
        this.optimizers = optimizers;
    }

    @Test
    public void testOptimize() throws Exception {
        PowellOptimizer powellOptimizer = new PowellOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        PointValuePair result = powellOptimizer.optimize(function, startPoint);

        PointValuePair expectedResult = null;
        if (optimizers != null) {
            UnivariateOptimizer bestOptimizer = null;
            double bestError = Double.MAX_VALUE;
            for (UnivariateOptimizer optimizer : optimizers) {
                PointValuePair optimizerResult = optimizer.optimize(function, startPoint);
                double error = function.value(result.getPoint()) - optimizerResult.getValue();
                if (error < bestError) {
                    bestError = error;
                    bestOptimizer = optimizer;
                    expectedResult = optimizerResult;
                }
            }
            assertEquals(expectedResult.getValue(), result.getValue(), EPSILON);
            assertArrayEquals(expectedResult.getPoint(), result.getPoint(), EPSILON);
            assertTrue(bestOptimizer.getClass().isAssignableFrom(powellOptimizer.getClass()));
        } else {
            Extrema extrema = function.optimize(new SimpleBounds(0.0, null), MaxEval.MAX_EVAL, GOAL_TYPE);
            assertEquals(extrema.getPoint(), result.getPoint(), EPSILON);
            assertEquals(extrema.getValue(), result.getValue(), EPSILON);
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNegativeAbsoluteTolerance() {
        new PowellOptimizer(RELATIVE_TOLERANCE, -ABSOLUTE_TOLERANCE);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNegativeRelativeTolerance() {
        new PowellOptimizer(-RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
    }

    @Test
    public void testCustomChecker() {
        PowellOptimizer powellOptimizer1 = new PowellOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        PointValuePair result1 = powellOptimizer1.optimize(function, startPoint);

        PowellOptimizer powellOptimizer2 = new PowellOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE,
                new ValueChecker() {
                    @Override
                    public boolean isSatisfied(PointValuePair p, int iteration) {
                        return p.getValue() < 10.0;
                    }
                });
        PointValuePair result2 = powellOptimizer2.optimize(function, startPoint);

        assertTrue(result1.getValue() > result2.getValue());
    }

    @Test
    public void testCustomInitializer() {
        AroundMinimumInitializer initializer = new AroundMinimumInitializer(function, 1);
        double[] initialPoint = initializer.initialize(0.1);

        PowellOptimizer powellOptimizer = new PowellOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        PointValuePair result = powellOptimizer.optimize(function, initialPoint);

        PointValuePair expectedResult = optimizers[0].optimize(function, initialPoint);
        assertEquals(expectedResult.getValue(), result.getValue(), EPSILON);
        assertArrayEquals(expectedResult.getPoint(), result.getPoint(), EPSILON);
    }

    @Test
    public void testRandomInitializer() {
        SimpleRandomInitializer initializer = new SimpleRandomInitializer(10);
        double[] initialPoint = initializer.initialize(2);

        PowellOptimizer powellOptimizer = new PowellOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        PointValuePair result = powellOptimizer.optimize(function, initialPoint);

        PointValuePair expectedResult = optimizers[optimizers.length - 1].optimize(function, initialPoint);
        assertEquals(expectedResult.getValue(), result.getValue(), EPSILON);
        assertArrayEquals(expectedResult.getPoint(), result.getPoint(), EPSILON);
    }

    @Test
    public void testAroundPointInitializer() {
        AroundPointInitializer initializer = new AroundPointInitializer(function.value(startPoint), function, 1.0);
        double[] initialPoint = initializer.initialize(2);

        PowellOptimizer powellOptimizer = new PowellOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        PointValuePair result = powellOptimizer.optimize(function, initialPoint);

        PointValuePair expectedResult = optimizers[optimizers.length - 2].optimize(function, initialPoint);
        assertEquals(expectedResult.getValue(), result.getValue(), EPSILON);
        assertArrayEquals(expectedResult.getPoint(), result.getPoint(), EPSILON);
    }

    @Test
    public void testFindMinima() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        PointValues pointValues = powellOptimizer.optimize(function, startPoint, new MaxEval(1000));

        PointValues expectedPointValues = optimizers[optimizers.length - 3].optimize(function, Points.polyD(2, 10), new MaxEval(1000));
        assertArrayEquals(expectedPointValues.toArray(), pointValues.toArray());
    }

}

class RosenbrockFunction implements MultivariateFunction {

    @Override
    public double value(double[] x) {
        double sum = 0.0;
        for (int i = 0; i < x.length - 1; i++) {
            double temp = x[i] - 1.0;
            sum += temp * temp + 100.0 * (x[i + 1] - temp * x[i]) * (x[i + 1] - temp * x[i]);
        }
        return sum;
    }

}

class RastriginFunction implements MultivariateFunction {

    @Override
    public double value(double[] x) {
        double sum = 10.0 * x.length;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(x[i] - Math.cos(2 * Math.PI * i), 2) + Math.pow(Math.PI * i - Math.sin(2 * Math.PI * i), 2);
        }
        return sum;
    }

}