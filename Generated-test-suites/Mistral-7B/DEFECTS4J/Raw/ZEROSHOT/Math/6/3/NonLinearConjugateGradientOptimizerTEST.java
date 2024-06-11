package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.preconditions.IdentityPreconditioner;
import org.apache.commons.math3.optim.point.PointValues;
import org.apache.commons.math3.optim.univariate.BrentSolver;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** Test quadratic function. */
    private static final UnivariateFunction FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    };

    /** Optimizer under test. */
    private NonLinearConjugateGradientOptimizer optimizer;

    /** Initial point. */
    private double[] initialPoint;

    /**
     * Set up.
     */
    @Before
    public void setUp() {
        initialPoint = new double[] {1.0};

        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        optimizer.setInitialStep(0.1);
    }

    /**
     * Test constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxIterations());
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getMaxEvaluations());
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getStepSize(), 0.0);
        assertNull(optimizer.getPreconditioner());
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test constructor with default arguments and custom goal type.
     */
    @Test
    public void testDefaultConstructorWithCustomGoalType() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                GoalType.MAXIMIZE);
        assertNotNull(optimizer);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxIterations());
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getMaxEvaluations());
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getStepSize(), 0.0);
        assertNull(optimizer.getPreconditioner());
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getInitialStep(), 0.0);
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
    }

    /**
     * Test constructor with custom arguments.
     */
    @Test
    public void testCustomConstructor() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner(),
                100,
                10,
                1.0,
                new SimpleValueChecker(1e-5, 100));
        assertNotNull(optimizer);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertEquals(100, optimizer.getMaxIterations());
        assertEquals(10, optimizer.getMaxEvaluations());
        assertEquals(1.0, optimizer.getStepSize(), 0.0);
        assertNotNull(optimizer.getPreconditioner());
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
        assertNotNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test set initial step.
     */
    @Test
    public void testSetInitialStep() {
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test optimization of a simple function.
     */
    @Test
    public void testOptimizeFunction() {
        final double tolerance = 1e-5;
        optimizer.optimize(FUNCTION, initialPoint);
        final PointValuePair result = optimizer.getCurrentPoint();
        final double x = result.getPoint()[0];
        final double fx = FUNCTION.value(x);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxIterations(), optimizer.getIterations());
        assertTrue(optimizer.isConverged());
        assertFalse(optimizer.hasExceptionOccurred());
        assertEquals(PointValues.MIN_VALUE, optimizer.getFunctionValue(), tolerance);
        assertEquals(PointValues.MIN_VALUE, fx, tolerance);
        assertEquals(initialPoint.length, optimizer.getIterationsFromStart(), optimizer.getIterations());
        assertEquals(optimizer.getMaxEvaluations(), optimizer.getFunctionEvaluations(), optimizer.getIterations());
    }

    /**
     * Test optimization of a simple function with custom goal type.
     */
    @Test
    public void testOptimizeFunctionWithCustomGoalType() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                GoalType.MAXIMIZE);

        final double tolerance = 1e-5;
        optimizer.optimize(FUNCTION, initialPoint);
        final PointValuePair result = optimizer.getCurrentPoint();
        final double x = result.getPoint()[0];
        final double fx = FUNCTION.value(x);
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxIterations(), optimizer.getIterations());
        assertTrue(optimizer.isConverged());
        assertFalse(optimizer.hasExceptionOccurred());
        assertEquals(PointValues.MAX_VALUE, optimizer.getFunctionValue(), tolerance);
        assertEquals(PointValues.MAX_VALUE, fx, tolerance);
        assertEquals(initialPoint.length, optimizer.getIterationsFromStart(), optimizer.getIterations());
        assertEquals(optimizer.getMaxEvaluations(), optimizer.getFunctionEvaluations(), optimizer.getIterations());
    }

    /**
     * Test optimization of a simple function with custom convergence checker.
     */
    @Test
    public void testOptimizeFunctionWithCustomConvergenceChecker() {
        final double tolerance = 1e-5;
        final double maxIterations = 100;
        final ConvergenceChecker<PointValuePair> customConvergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair currentValue, PointValuePair previousValue) {
                return Precision.abs(currentValue.getFunctionValue() - previousValue.getFunctionValue()) <= tolerance
                        || iteration >= maxIterations;
            }
        };
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                customConvergenceChecker,
                new BrentSolver(),
                new IdentityPreconditioner());

        optimizer.optimize(FUNCTION, initialPoint);
        final PointValuePair result = optimizer.getCurrentPoint();
        final double x = result.getPoint()[0];
        final double fx = FUNCTION.value(x);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(maxIterations, optimizer.getMaxIterations(), optimizer.getIterations());
        assertTrue(optimizer.isConverged());
        assertFalse(optimizer.hasExceptionOccurred());
        assertEquals(PointValues.MIN_VALUE, optimizer.getFunctionValue(), tolerance);
        assertEquals(PointValues.MIN_VALUE, fx, tolerance);
        assertEquals(initialPoint.length, optimizer.getIterationsFromStart(), optimizer.getIterations());
        assertEquals(optimizer.getMaxEvaluations(), optimizer.getFunctionEvaluations(), optimizer.getIterations());
    }

    /**
     * Test optimization of a simple function with custom preconditioner and
     * custom convergence checker.
     */
    @Test
    public void testOptimizeFunctionWithCustomPreconditionerAndConvergenceChecker() {
        final double tolerance = 1e-5;
        final int maxIterations = 100;
        final int maxEvaluations = 500;
        final Preconditioner customPreconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                double[] result = new double[variables.length];
                for (int i = 0; i < result.length; ++i) {
                    result[i] = r[i] * 2;
                }
                return result;
            }
        };
        final ConvergenceChecker<PointValuePair> customConvergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair currentValue, PointValuePair previousValue) {
                return Precision.abs(currentValue.getFunctionValue() - previousValue.getFunctionValue()) <= tolerance
                        || iteration >= maxIterations;
            }
        };
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                customConvergenceChecker,
                new BrentSolver(),
                customPreconditioner);

        optimizer.setMaxIterations(maxIterations);
        optimizer.setMaxEvaluations(maxEvaluations);
        optimizer.optimize(FUNCTION, initialPoint);
        final PointValuePair result = optimizer.getCurrentPoint();
        final double x = result.getPoint()[0];
        final double fx = FUNCTION.value(x);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(maxIterations, optimizer.getMaxIterations(), optimizer.getIterations());
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations(), optimizer.getFunctionEvaluations());
        assertTrue(optimizer.isConverged());
        assertFalse(optimizer.hasExceptionOccurred());
        assertEquals(PointValues.MIN_VALUE, optimizer.getFunctionValue(), tolerance);
        assertEquals(PointValues.MIN_VALUE, fx, tolerance);
        assertEquals(initialPoint.length, optimizer.getIterationsFromStart(), optimizer.getIterations());
        assertEquals(optimizer.getMaxEvaluations(), optimizer.getFunctionEvaluations(), optimizer.getIterations());
    }

    /**
     * Test optimization of a simple function with a custom function.
     */
    @Test
    public void testOptimizeFunctionWithCustomFunction() {
        final double tolerance = 1e-5;
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner(),
                new CustomFunction(),
                100,
                10,
                1.0,
                new SimpleValueChecker(1e-5, 100));

        final double[] initialGuess = new double[] {1.0};
        optimizer.optimize(InitialGuess.valueOf(initialGuess), new MaxEval(1000));
        final PointValuePair result = optimizer.getCurrentPoint();
        final double x = result.getPoint()[0];
        final double fx = optimizer.getFunctionValue();
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxIterations(), optimizer.getIterations());
        assertTrue(optimizer.isConverged());
        assertFalse(optimizer.hasExceptionOccurred());
        assertEquals(PointValues.MIN_VALUE, fx, tolerance);
        assertArrayEquals(new double[] {-1.0}, optimizer.getGradient(), 0.0);
        assertEquals(initialGuess.length, optimizer.getIterationsFromStart(), optimizer.getIterations());
        assertEquals(optimizer.getMaxEvaluations(), optimizer.getFunctionEvaluations(), optimizer.getIterations());
    }

    /**
     * Custom function for testing.
     */
    private static class CustomFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return PrecisionUtils.pow(x, 2) + Precision.sin(x);
        }

        @Override
        public double derivative(double x) {
            double fx = value(x);
            return 2 * x + PrecisionUtils.cos(x);
        }
    }
}