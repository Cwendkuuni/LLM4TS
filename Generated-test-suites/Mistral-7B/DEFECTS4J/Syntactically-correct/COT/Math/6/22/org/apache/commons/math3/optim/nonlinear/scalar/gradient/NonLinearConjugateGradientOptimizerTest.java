package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.UnivariateSolver;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.AbstractScalarDifferentiableOptimizer;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** Function for testing. */
    private static final UnivariateFunction FUNCTION = new MyFunction();

    /** Test optimizer. */
    private NonLinearConjugateGradientOptimizer optimizer;

    /** Convergence checker. */
    private ConvergenceChecker<PointValuePair> convergenceChecker;

    /** Conjugate gradient formula. */
    private ConjugateGradientFormula updateFormula;

    /** Preconditioner. */
    private Preconditioner preconditioner;

    /** Solver for line search. */
    private UnivariateSolver lineSearchSolver;

    /**
     * Sets up the test before each method execution.
     */
    @Before
    public void setUp() {
        convergenceChecker = new SimpleValueChecker(1e-8);
        updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        preconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                return r.clone();
            }
        };
        lineSearchSolver = new BrentSolver();

        optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula,
                convergenceChecker,
                lineSearchSolver,
                preconditioner);
    }

    /**
     * Tests constructor with default values.
     */
    @Test
    public void testDefaultConstructor() {
        optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        Assert.assertEquals(updateFormula, optimizer.getUpdateFormula());
        Assert.assertNull(optimizer.getConvergenceChecker());
        Assert.assertNull(optimizer.getSolver());
        Assert.assertNull(optimizer.getPreconditioner());
        Assert.assertEquals(1.0, optimizer.getInitialStep(), 1e-8);
    }

    /**
     * Tests constructor with custom values.
     */
    @Test
    public void testCustomConstructor() {
        final ConvergenceChecker<PointValuePair> customChecker = new CustomConvergenceChecker();
        optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula,
                convergenceChecker,
                lineSearchSolver,
                preconditioner);

        optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula,
                customChecker,
                lineSearchSolver,
                preconditioner);

        Assert.assertSame(customChecker, optimizer.getConvergenceChecker());
        Assert.assertSame(lineSearchSolver, optimizer.getSolver());
        Assert.assertSame(preconditioner, optimizer.getPreconditioner());
        Assert.assertEquals(1.0, optimizer.getInitialStep(), 1e-8);
    }

    /**
     * Tests setInitialStep method.
     */
    @Test
    public void testSetInitialStep() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula,
                convergenceChecker,
                lineSearchSolver,
                preconditioner);

        optimizer.setInitialStep(2.0);
        Assert.assertEquals(2.0, optimizer.getInitialStep(), 1e-8);
    }

    /**
     * Test optimization method.
     */
    @Test
    public void testOptimize() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula,
                convergenceChecker,
                lineSearchSolver,
                preconditioner);

        optimizer.optimize(FUNCTION, new PointValuePair(0.0, FUNCTION.value(0.0)), GoalType.MINIMIZE);
        PointValuePair result = optimizer.getCurrentValue();
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(new double[] {0.1, 0.2}, result.getPoint(), 1e-8);
        Assert.assertEquals(-1.01, result.getValue(), 1e-8);
    }

    /**
     * Test optimization with custom goal.
     */
    @Test
    public void testOptimizeCustomGoal() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula,
                convergenceChecker,
                lineSearchSolver,
                preconditioner);

        optimizer.optimize(FUNCTION, new PointValuePair(0.0, FUNCTION.value(0.0)), GoalType.MAXIMIZE);
        PointValuePair result = optimizer.getCurrentValue();
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(new double[] {1.1, 1.2}, result.getPoint(), 1e-8);
        Assert.assertEquals(1.01, result.getValue(), 1e-8);
    }

    /**
     * Test for case when optimization fails to converge.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeDivergence() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula,
                convergenceChecker,
                lineSearchSolver,
                preconditioner);

        optimizer.optimize(FUNCTION, new PointValuePair(0.0, FUNCTION.value(0.0)), GoalType.MINIMIZE);
        optimizer.optimize(FUNCTION, new PointValuePair(0.0, FUNCTION.value(0.0)), GoalType.MINIMIZE);
        optimizer.optimize(FUNCTION, new PointValuePair(0.0, FUNCTION.value(0.0)), GoalType.MINIMIZE);
    }

    /**
     * Test for case when optimization fails to find a root.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeNoRoot() {
        final UnivariateFunction noRootFunction = new MyNoRootFunction();
        optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula,
                convergenceChecker,
                lineSearchSolver,
                preconditioner);

        optimizer.optimize(noRootFunction, new PointValuePair(0.0, noRootFunction.value(0.0)), GoalType.MINIMIZE);
    }

    /**
     * Custom convergence checker for testing.
     */
    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {

        @Override
        public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
            return current.getValue() < 0.0 && previous.getValue() > 0.0;
        }

    }

    /**
     * Test function for optimization.
     */
    private static class MyFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return -FastMath.pow(x, 2) + 1.0;
        }

        @Override
        public double value(double[] x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double[] derivative(double x) {
            return new double[] {-2 * x};
        }

        @Override
        public double[] derivative(double[] x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * Test function for optimization without a root.
     */
    private static class MyNoRootFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return FastMath.sin(x) + FastMath.cos(x);
        }

        @Override
        public double[] derivative(double x) {
            return new double[] {FastMath.cos(x) - FastMath.sin(x)};
        }

        @Override
        public double[] derivative(double[] x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}