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
import org.apache.commons.math3.optimization.LineSearchFunction;
import org.apache.commons.math3.optimization.ScalarDifferentiableOptimizer;
import org.apache.commons.math3.optimization.InitialGuess;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** Seed for random number generator. */
    private static final long SEED = 1L;

    /** Random number generator. */
    private static final Random RANDOM = new Random(SEED);

    /** Test function. */
    private static final UnivariateFunction TEST_FUNCTION = new TestFunction();

    /** Test function gradient. */
    private static final UnivariateFunction TEST_FUNCTION_GRADIENT = new TestFunctionGradient();

    /** Checker for optimization result. */
    private static final ConvergenceChecker<PointValuePair> CHECKER = new SimpleValueChecker(1.0e-12);

    /** Preconditioner. */
    private static final Preconditioner IDENTITY_PRECONDITIONER = new IdentityPreconditioner();

    /** Solver for line search. */
    private static final UnivariateSolver LINE_SEARCH_SOLVER = new BrentSolver();

    /** Optimizer under test. */
    private NonLinearConjugateGradientOptimizer optimizer;

    /** Initial guess. */
    private double[] initialGuess;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                CHECKER,
                LINE_SEARCH_SOLVER,
                IDENTITY_PRECONDITIONER);
        optimizer.setMaxEvaluations(10000);
        optimizer.setConvergenceChecker(CHECKER);
        initialGuess = new double[]{-1.0, 1.0};
    }

    /**
     * Tears down the test.
     */
    @After
    public void tearDown() {
        optimizer = null;
    }

    /**
     * Test optimization of test function with default settings.
     */
    @Test
    public void testOptimizeDefaultSettings() {
        optimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, initialGuess);
        PointValuePair result = optimizer.getPointValuePair();
        Assert.assertArrayEquals(new double[]{-0.75, -0.75}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 1.0e-12);
    }

    /**
     * Test optimization of test function with a custom initial step.
     */
    @Test
    public void testOptimizeCustomInitialStep() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                CHECKER,
                LINE_SEARCH_SOLVER,
                IDENTITY_PRECONDITIONER);
        optimizer.setMaxEvaluations(10000);
        optimizer.setConvergenceChecker(CHECKER);
        optimizer.setInitialStep(2.0);
        optimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, initialGuess);
        PointValuePair result = optimizer.getPointValuePair();
        Assert.assertArrayEquals(new double[]{-0.75, -0.75}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 1.0e-12);
    }

    /**
     * Test optimization of test function with a custom solver.
     */
    @Test
    public void testOptimizeCustomSolver() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                CHECKER,
                new CustomSolver(),
                IDENTITY_PRECONDITIONER);
        optimizer.setMaxEvaluations(10000);
        optimizer.setConvergenceChecker(CHECKER);
        optimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, initialGuess);
        PointValuePair result = optimizer.getPointValuePair();
        Assert.assertArrayEquals(new double[]{-0.75, -0.75}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 1.0e-12);
    }

    /**
     * Test optimization of test function with a custom preconditioner.
     */
    @Test
    public void testOptimizeCustomPreconditioner() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                CHECKER,
                LINE_SEARCH_SOLVER,
                new CustomPreconditioner());
        optimizer.setMaxEvaluations(10000);
        optimizer.setConvergenceChecker(CHECKER);
        optimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, initialGuess);
        PointValuePair result = optimizer.getPointValuePair();
        Assert.assertArrayEquals(new double[]{-0.75, -0.75}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 1.0e-12);
    }

    /**
     * Test optimization of test function with a custom update formula.
     */
    @Test
    public void testOptimizeCustomUpdateFormula() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                CHECKER,
                LINE_SEARCH_SOLVER,
                IDENTITY_PRECONDITIONER);
        optimizer.setMaxEvaluations(10000);
        optimizer.setConvergenceChecker(CHECKER);
        optimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, initialGuess);
        PointValuePair result = optimizer.getPointValuePair();
        Assert.assertArrayEquals(new double[]{-0.75, -0.75}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 1.0e-12);
    }

    /**
     * Test optimization of test function with a custom goal type.
     */
    @Test
    public void testOptimizeCustomGoalType() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                CHECKER,
                LINE_SEARCH_SOLVER,
                IDENTITY_PRECONDITIONER);
        optimizer.setMaxEvaluations(10000);
        optimizer.setConvergenceChecker(CHECKER);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.optimize(TEST_FUNCTION, GoalType.MAXIMIZE, initialGuess);
        PointValuePair result = optimizer.getPointValuePair();
        Assert.assertArrayEquals(new double[]{0.75, 0.75}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, -result.getValue(), 1.0e-12);
    }

    /**
     * Test optimization of test function with a custom initial guess.
     */
    @Test
    public void testOptimizeCustomInitialGuess() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                CHECKER,
                LINE_SEARCH_SOLVER,
                IDENTITY_PRECONDITIONER);
        optimizer.setMaxEvaluations(10000);
        optimizer.setConvergenceChecker(CHECKER);
        optimizer.setInitialGuess(new double[]{1.0, -1.0});
        optimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, initialGuess);
        PointValuePair result = optimizer.getPointValuePair();
        Assert.assertArrayEquals(new double[]{-0.75, -0.75}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 1.0e-12);
    }

    /**
     * Custom solver for line search.
     */
    private static class CustomSolver implements UnivariateSolver {
        @Override
        public double solve(int maxEvaluations, UnivariateFunction f, double xLowerBound, double xUpperBound, double tolerance) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public double[] getBracketing(UnivariateFunction f, double[] x) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public int getEvaluations() {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    /**
     * Custom preconditioner.
     */
    private static class CustomPreconditioner implements Preconditioner {
        @Override
        public double[] precondition(double[] variables, double[] r) {
            double[] result = new double[variables.length];
            for (int i = 0; i < variables.length; ++i) {
                result[i] = RANDOM.nextDouble() * Math.abs(variables[i]) + RANDOM.nextDouble() * Math.abs(r[i]);
            }
            return result;
        }
    }

    /**
     * Test function.
     */
    private static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            double[] testPoint = InitialGuess.ONE_DIMENSION.getInitialPoint();
            testPoint[0] = x;
            double[] gradient = TEST_FUNCTION_GRADIENT.evaluate(testPoint);
            return 0.5 * testPoint[0] * testPoint[0] - testPoint[0] * testPoint[0] * testPoint[0] + gradient[0];
        }

        @Override
        public double[] gradient(double x) {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    /**
     * Test function gradient.
     */
    private static class TestFunctionGradient implements UnivariateFunction {
        @Override
        public double value(double[] x) {
            return 2.0 * x[0] * x[0] - 3.0 * x[0] * x[0] * x[0] + 1.0;
        }

        @Override
        public double[] gradient(double[] x) {
            double[] result = new double[1];
            result[0] = 6.0 * x[0] * x[0] - 9.0 * x[0] * x[0] * x[0];
            return result;
        }
    }
}