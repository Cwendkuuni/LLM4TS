package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.identity.IdentityPreconditioner;
import org.apache.commons.math3.optimization.convergence.ConvergenceCriteria;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** Test function. */
    private static final UnivariateFunction TEST_FUNCTION = new TestFunction();

    /** Test optimizer. */
    private NonLinearConjugateGradientOptimizer optimizer;

    /** Test goal type. */
    private GoalType goal;

    /** Test convergence checker. */
    private ConvergenceChecker<PointValuePair> convergenceChecker;

    /** Test value checker. */
    private SimpleValueChecker<PointValuePair> valueChecker;

    /** Test preconditioner. */
    private Preconditioner preconditioner;

    /** Test solver. */
    private UnivariateSolver solver;

    /** Test initial step. */
    private double initialStep;

    /** Test point. */
    private double[] testPoint;

    /** Test maximum evaluations. */
    private int maxEvaluations;

    /** Test convergence criteria. */
    private ConvergenceCriteria<PointValuePair> convergenceCriteria;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                            new SimpleValueChecker<PointValuePair>(),
                                                            new BrentSolver(),
                                                            new IdentityPreconditioner());
        goal = GoalType.MINIMIZE;
        convergenceChecker = new ConvergenceChecker<PointValuePair>(1e-8, Double.POSITIVE_INFINITY, 1000);
        valueChecker = new SimpleValueChecker<PointValuePair>();
        preconditioner = new IdentityPreconditioner();
        solver = new BrentSolver();
        initialStep = 1.0;
        testPoint = new double[] { 0.1, 0.2, 0.3 };
        maxEvaluations = 1000;
        convergenceCriteria = new ConvergenceCriteria<PointValuePair>() {
            @Override
            public boolean isSatisfied(PointValuePair current, PointValuePair previous) {
                return convergenceChecker.isSatisfied(current, previous);
            }
        };
    }

    /**
     * Test basic functionality of the optimizer.
     */
    @Test
    public void testOptimize() {
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, testPoint, goal, convergenceCriteria);
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(testPoint, result.getPoint(), 1e-8);
        Assert.assertTrue(valueChecker.isZero(result.getValue(), 1e-8));
    }

    /**
     * Test optimizer with custom preconditioner and solver.
     */
    @Test
    public void testCustomPreconditionerAndSolver() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                            convergenceChecker,
                                                            solver,
                                                            new MyPreconditioner());
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, testPoint, goal, convergenceCriteria);
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(testPoint, result.getPoint(), 1e-8);
        Assert.assertTrue(valueChecker.isZero(result.getValue(), 1e-8));
    }

    /**
     * Test optimizer with initial step.
     */
    @Test
    public void testInitialStep() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                            convergenceChecker,
                                                            solver,
                                                            preconditioner);
        optimizer.setInitialStep(0.5);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, testPoint, goal, convergenceCriteria);
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(testPoint, result.getPoint(), 1e-8);
        Assert.assertTrue(valueChecker.isZero(result.getValue(), 1e-8));
    }

    /**
     * Test optimizer with invalid initial step.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testInvalidInitialStep() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                            convergenceChecker,
                                                            solver,
                                                            preconditioner);
        optimizer.setInitialStep(-1);
        optimizer.optimize(TEST_FUNCTION, testPoint, goal, convergenceCriteria);
    }

    /**
     * Test optimizer with custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                            new MyConvergenceChecker(),
                                                            solver,
                                                            preconditioner);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, testPoint, goal, convergenceCriteria);
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(testPoint, result.getPoint(), 1e-8);
        Assert.assertTrue(valueChecker.isZero(result.getValue(), 1e-8));
    }

    /**
     * Test optimizer with invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                            convergenceChecker,
                                                            solver,
                                                            preconditioner);
        optimizer.optimize(TEST_FUNCTION, testPoint, GoalType.MAXIMIZE, convergenceCriteria);
    }

    /**
     * Test optimizer with maximum evaluations.
     */
    @Test
    public void testMaxEvaluations() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                            convergenceChecker,
                                                            solver,
                                                            preconditioner);
        optimizer.setMaxEvaluations(1);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, testPoint, goal, convergenceCriteria);
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(testPoint, result.getPoint(), 1e-8);
        Assert.assertTrue(valueChecker.isZero(result.getValue(), 1e-8));
    }

    /**
     * Test optimizer with invalid maximum evaluations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxEvaluations() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                            convergenceChecker,
                                                            solver,
                                                            preconditioner);
        optimizer.setMaxEvaluations(-1);
        optimizer.optimize(TEST_FUNCTION, testPoint, goal, convergenceCriteria);
    }

    /**
     * Test optimizer with custom convergence criteria.
     */
    @Test
    public void testCustomConvergenceCriteria() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                            convergenceChecker,
                                                            solver,
                                                            preconditioner);
        optimizer.setConvergenceCriteria(convergenceCriteria);
        PointValuePair result = optimizer.optimize(TEST_FUNCTION, testPoint, goal, convergenceCriteria);
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(testPoint, result.getPoint(), 1e-8);
        Assert.assertTrue(valueChecker.isZero(result.getValue(), 1e-8));
    }

    /**
     * Test function for optimization.
     */
    private static class TestFunction implements UnivariateFunction {

        /** {@inheritDoc} */
        public double value(double x) {
            double sum = 0;
            for (int i = 0; i < testPoint.length; ++i) {
                sum += Math.pow(x - testPoint[i], 2);
            }
            return sum;
        }

        /** {@inheritDoc} */
        public double derivative(double x) {
            double[] grad = new double[testPoint.length];
            for (int i = 0; i < testPoint.length; ++i) {
                grad[i] = 2 * (x - testPoint[i]);
            }
            return FastMath.arraySum(grad);
        }
    }

    /**
     * Custom preconditioner.
     */
    private static class MyPreconditioner implements Preconditioner {

        /** {@inheritDoc} */
        public double[] precondition(double[] variables, double[] r) {
            double[] identity = new double[variables.length];
            for (int i = 0; i < variables.length; ++i) {
                identity[i] = 1;
            }
            return FastMath.add(FastMath.multiply(identity, -1), r);
        }
    }

    /**
     * Custom convergence checker.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {

        /** {@inheritDoc} */
        public boolean isSatisfied(PointValuePair current, PointValuePair previous) {
            return (current.getValue() - previous.getValue()) < 1e-8;
        }
    }
}