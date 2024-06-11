package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.AbstractScalarDifferentiableOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.UnivariateSolver.UnivariateSolverResult;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;
    private UnivariateFunction f;

    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                            new SimpleValueChecker(),
                                                            new BrentSolver());
        f = x -> Math.pow(x - 2, 3) + Math.pow(x + 2, 2);
    }

    /**
     * Test optimization with identity preconditioner.
     */
    @Test
    public void testIdentityPreconditioner() {
        double[] startPoint = new double[] {-1.0};
        PointValuePair result = optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        double expectedResult = 1.0;
        Assert.assertEquals(expectedResult, result.getValue(), 1e-9);
        Assert.assertArrayEquals(new double[] {-1.0 + Math.cbrt(2.0 / 3.0)}, result.getPoint(), 1e-9);
    }

    /**
     * Test optimization with custom preconditioner.
     */
    @Test
    public void testCustomPreconditioner() {
        Preconditioner preconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] preconditioned = new double[variables.length];
                for (int i = 0; i < variables.length; ++i) {
                    preconditioned[i] = r[i] * 2;
                }
                return preconditioned;
            }
        };
        double[] startPoint = new double[] {-1.0};
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                            new SimpleValueChecker(),
                                                            new BrentSolver(),
                                                            preconditioner);
        PointValuePair result = optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        double expectedResult = 1.0 / 4.0;
        Assert.assertEquals(expectedResult, result.getValue(), 1e-9);
        Assert.assertArrayEquals(new double[] {-1.0 + Math.cbrt(1.0 / 2.0)}, result.getPoint(), 1e-9);
    }

    /**
     * Test optimization with custom line search solver.
     */
    @Test
    public void testCustomLineSearchSolver() {
        UnivariateSolver customSolver = (x, n, f, a, b, maxEval, tolerance) -> {
            double x0 = x;
            double fx0 = f.value(x0);
            double step = 1.0;
            for (int i = 0; i < maxEval; ++i) {
                x = x0 + step;
                double fx = f.value(x);
                if (fx * fx0 < 0) {
                    return new UnivariateSolverResult(x, fx, i + 1);
                }
                step /= 2.0;
            }
            throw new UnsupportedOperationException("Line search not converged");
        };
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                            new SimpleValueChecker(),
                                                            customSolver);
        double[] startPoint = new double[] {-1.0};
        PointValuePair result = optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        double expectedResult = 1.0;
        Assert.assertEquals(expectedResult, result.getValue(), 1e-9);
        Assert.assertArrayEquals(new double[] {-1.0 + Math.cbrt(2.0 / 3.0)}, result.getPoint(), 1e-9);
    }

    /**
     * Test initialization with invalid update formula.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUpdateFormula() {
        new NonLinearConjugateGradientOptimizer(null, new SimpleValueChecker());
    }

    /**
     * Test initialization with null preconditioner.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullPreconditioner() {
        new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, null);
    }

    /**
     * Test initialization with null checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullChecker() {
        new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, null, new BrentSolver());
    }

    /**
     * Test initialization with null line search solver.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullLineSearchSolver() {
        new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                               new SimpleValueChecker(), null);
    }

    /**
     * Test initialization with null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                               new SimpleValueChecker(), new BrentSolver(), null);
    }

    /**
     * Test initialization with invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                               new SimpleValueChecker(), new BrentSolver(), null,
                                               GoalType.MAXIMIZE);
    }
}

class Preconditioner implements NonLinearConjugateGradientOptimizer.Preconditioner {

    @Override
    public double[] precondition(double[] variables, double[] r) {
        throw new UnsupportedOperationException("Not implemented");
    }
}