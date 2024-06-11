package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optimization.nonlinear.scalar.SimpleValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** Optimizer under test. */
    private NonLinearConjugateGradientOptimizer optimizer;

    /** Test function. */
    private static final UnivariateFunction TEST_FUNCTION = new TestFunction();

    /** Test goal type. */
    private static final GoalType GOAL_TYPE = GoalType.MINIMIZE;

    /** Test checker. */
    private static final SimpleValueChecker CHECKER = new SimpleValueChecker(1.0e-9);

    /** Test maximum evaluations. */
    private static final int MAX_EVALUATIONS = 100;

    /** Test initial step. */
    private static final double INITIAL_STEP = 1.0;

    /** Test preconditioner. */
    private static final Preconditioner IDENTITY = new IdentityPreconditioner();

    /** Test solver. */
    private static final UnivariateSolver LINE_SEARCH_SOLVER = new BrentSolver();

    /** Test update formula. */
    private static final ConjugateGradientFormula UPDATE_FORMULA = ConjugateGradientFormula.FLETCHER_REEVES;

    /** Test convergence checker. */
    private ConvergenceChecker<PointValuePair> CONVERGENCE_CHECKER;

    /** Test point. */
    private double[] testPoint;

    /** Test objective value. */
    private double testObjectiveValue;

    /** Test objective gradient. */
    private double[] testObjectiveGradient;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(UPDATE_FORMULA, CHECKER, LINE_SEARCH_SOLVER, IDENTITY);
        optimizer.setInitialStep(INITIAL_STEM);
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        testPoint = new double[]{-1.0, 1.0};
        testObjectiveValue = TEST_FUNCTION.value(testPoint);
        testObjectiveGradient = TEST_FUNCTION.valuePartials(testPoint);
    }

    /**
     * Test optimization with initial guess equal to starting point.
     */
    @Test
    public void testOptimizationWithInitialGuessEqualToStartingPoint() {
        final PointValuePair result = optimizer.optimize(testPoint, null);
        Assert.assertEquals(GOAL_TYPE, result.getGoalType());
        Assert.assertEquals(testObjectiveValue, result.getValue(), CHECKER.getAbsoluteTolerance());
    }

    /**
     * Test optimization with initial guess not equal to starting point.
     */
    @Test
    public void testOptimizationWithInitialGuessNotEqualToStartingPoint() {
        final PointValuePair result = optimizer.optimize(new InitialGuess[] {new InitialGuess(testPoint[0] + 1.0, testPoint[1])}, null);
        Assert.assertEquals(GOAL_TYPE, result.getGoalType());
        Assert.assertEquals(testObjectiveValue, result.getValue(), CHECKER.getAbsoluteTolerance());
    }

    /**
     * Test optimization with invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithInvalidGoalType() {
        optimizer = new NonLinearConjugateGradientOptimizer(UPDATE_FORMULA, CHECKER, LINE_SEARCH_SOLVER, IDENTITY);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.optimize(testPoint, null);
    }

    /**
     * Test optimization with maximum evaluations reached.
     */
    @Test
    public void testOptimizationWithMaxEvaluationsReached() {
        optimizer.setMaxEvaluations(1);
        final PointValuePair result = optimizer.optimize(testPoint, null);
        Assert.assertNull(result);
    }

    /**
     * Test optimization with invalid preconditioner.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithInvalidPreconditioner() {
        optimizer = new NonLinearConjugateGradientOptimizer(UPDATE_FORMULA, CHECKER, LINE_SEARCH_SOLVER, null);
    }

    /**
     * Test optimization with invalid solver.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithInvalidSolver() {
        optimizer = new NonLinearConjugateGradientOptimizer(UPDATE_FORMULA, CHECKER, null, IDENTITY);
    }

    /**
     * Test optimization with invalid update formula.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithInvalidUpdateFormula() {
        optimizer = new NonLinearConjugateGradientOptimizer(null, CHECKER, LINE_SEARCH_SOLVER, IDENTITY);
    }

    /**
     * Test optimization with invalid convergence checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithInvalidConvergenceChecker() {
        optimizer = new NonLinearConjugateGradientOptimizer(UPDATE_FORMULA, null, LINE_SEARCH_SOLVER, IDENTITY);
    }

    /**
     * Test optimization with invalid initial step.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithInvalidInitialStep() {
        optimizer = new NonLinearConjugateGradientOptimizer(UPDATE_FORMULA, CHECKER, LINE_SEARCH_SOLVER, IDENTITY);
        optimizer.setInitialStep(-1.0);
        optimizer.optimize(testPoint, null);
    }

    /**
     * Test optimization with invalid maximum evaluations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithInvalidMaxEvaluations() {
        optimizer = new NonLinearConjugateGradientOptimizer(UPDATE_FORMULA, CHECKER, LINE_SEARCH_SOLVER, IDENTITY);
        optimizer.setMaxEvaluations(-1);
        optimizer.optimize(testPoint, null);
    }

    /**
     * Test function for testing.
     */
    private static class TestFunction implements UnivariateFunction {
        /** {@inheritDoc} */
        public double value(double[] x) {
            final double x1 = x[0];
            final double x2 = x[1];
            return Math.pow(x1, 2) + Math.pow(x2, 2) - x1 * x2 + 1;
        }

        /** {@inheritDoc} */
        public double[] valuePartials(double[] x) {
            final double x1 = x[0];
            final double x2 = x[1];
            final double[] result = new double[2];
            result[0] = 2 * x1 + x2 - x2;
            result[1] = 2 * x2 - x1;
            return result;
        }
    }
}