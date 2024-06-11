package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.NonlinearConstraint;
import org.apache.commons.math3.optim.nonlinear.NonlinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.NonlinearOptimizationProblem;
import org.apache.commons.math3.optim.nonlinear.simple.intervals.Interval;
import org.apache.commons.math3.optim.nonlinear.simple.intervals.IntervalUtil;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest extends AbstractTest {

    /** Test function for the optimization algorithm. */
    private static final UnivariateFunction TEST_FUNCTION = new TestFunction();

    /** Convergence tolerance. */
    private static final double CONVERGENCE_TOLERANCE = 1e-10;

    /** Maximum number of function evaluations. */
    private static final int MAX_EVAL = 1000;

    /** Test for default constructor. */
    @Test
    public void testDefaultConstructor() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer.getSolver());
        assertNotNull(optimizer.getPreconditioner());
    }

    /** Test for constructor with custom convergence checker. */
    @Test
    public void testCustomCheckerConstructor() {
        final SimpleValueChecker checker = new SimpleValueChecker(CONVERGENCE_TOLERANCE);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, checker);
        assertSame(checker, optimizer.getChecker());
    }

    /** Test for constructor with custom line search solver. */
    @Test
    public void testCustomLineSearchSolverConstructor() {
        final BrentSolver lineSearchSolver = new BrentSolver();
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(CONVERGENCE_TOLERANCE),
                lineSearchSolver);
        assertSame(lineSearchSolver, optimizer.getSolver());
    }

    /** Test for constructor with custom preconditioner. */
    @Test
    public void testCustomPreconditionerConstructor() {
        final Preconditioner preconditioner = new TestPreconditioner();
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(CONVERGENCE_TOLERANCE),
                new BrentSolver(), preconditioner);
        assertSame(preconditioner, optimizer.getPreconditioner());
    }

    /** Test for optimization with default constructor. */
    @Test
    public void testOptimizationWithDefaultConstructor() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        final NonlinearOptimizationProblem problem = new NonlinearOptimizationProblem(TEST_FUNCTION, new PointValuePair(0.5, 2.5));
        final PointValuePair result = optimizer.optimize(problem, MAX_EVAL);
        assertNotNull(result);
        assertEquals(TEST_FUNCTION.value(0), result.getValue(), 1e-6);
    }

    /** Test for optimization with custom convergence checker. */
    @Test
    public void testOptimizationWithCustomCheckerConstructor() {
        final SimpleValueChecker checker = new SimpleValueChecker(CONVERGENCE_TOLERANCE);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, checker);
        final NonlinearOptimizationProblem problem = new NonlinearOptimizationProblem(TEST_FUNCTION, new PointValuePair(0.5, 2.5));
        final PointValuePair result = optimizer.optimize(problem, MAX_EVAL);
        assertNotNull(result);
        assertEquals(TEST_FUNCTION.value(0), result.getValue(), 1e-6);
    }

    /** Test for optimization with custom line search solver. */
    @Test
    public void testOptimizationWithCustomLineSearchSolverConstructor() {
        final BrentSolver lineSearchSolver = new BrentSolver();
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(CONVERGENCE_TOLERANCE),
                lineSearchSolver);
        final NonlinearOptimizationProblem problem = new NonlinearOptimizationProblem(TEST_FUNCTION, new PointValuePair(0.5, 2.5));
        final PointValuePair result = optimizer.optimize(problem, MAX_EVAL);
        assertNotNull(result);
        assertEquals(TEST_FUNCTION.value(0), result.getValue(), 1e-6);
    }

    /** Test for optimization with custom preconditioner. */
    @Test
    public void testOptimizationWithCustomPreconditionerConstructor() {
        final Preconditioner preconditioner = new TestPreconditioner();
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(CONVERGENCE_TOLERANCE),
                new BrentSolver(), preconditioner);
        final NonlinearOptimizationProblem problem = new NonlinearOptimizationProblem(TEST_FUNCTION, new PointValuePair(0.5, 2.5));
        final PointValuePair result = optimizer.optimize(problem, MAX_EVAL);
        assertNotNull(result);
        assertEquals(TEST_FUNCTION.value(0), result.getValue(), 1e-6);
    }

    /** Test for optimization with custom convergence checker and custom line search solver. */
    @Test
    public void testOptimizationWithCustomCheckerAndLineSearchSolverConstructor() {
        final SimpleValueChecker checker = new SimpleValueChecker(CONVERGENCE_TOLERANCE);
        final BrentSolver lineSearchSolver = new BrentSolver();
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, checker, lineSearchSolver);
        final NonlinearOptimizationProblem problem = new NonlinearOptimizationProblem(TEST_FUNCTION, new PointValuePair(0.5, 2.5));
        final PointValuePair result = optimizer.optimize(problem, MAX_EVAL);
        assertNotNull(result);
        assertEquals(TEST_FUNCTION.value(0), result.getValue(), 1e-6);
    }

    /** Test for optimization with custom convergence checker, custom line search solver, and custom preconditioner. */
    @Test
    public void testOptimizationWithCustomCheckerAndLineSearchSolverAndPreconditionerConstructor() {
        final SimpleValueChecker checker = new SimpleValueChecker(CONVERGENCE_TOLERANCE);
        final BrentSolver lineSearchSolver = new BrentSolver();
        final Preconditioner preconditioner = new TestPreconditioner();
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, checker, lineSearchSolver, preconditioner);
        final NonlinearOptimizationProblem problem = new NonlinearOptimizationProblem(TEST_FUNCTION, new PointValuePair(0.5, 2.5));
        final PointValuePair result = optimizer.optimize(problem, MAX_EVAL);
        assertNotNull(result);
        assertEquals(TEST_FUNCTION.value(0), result.getValue(), 1e-6);
    }

    /** Test for optimization with constraints. */
    @Test
    public void testOptimizationWithConstraints() {
        final Preconditioner preconditioner = new TestPreconditioner();
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(CONVERGENCE_TOLERANCE),
                new BrentSolver(), preconditioner);
        final NonlinearConstraintSet constraints = new NonlinearConstraintSet();
        constraints.add(new NonlinearConstraint(new LinearConstraint(1.0), Interval.open(1.0, 2.0)));
        constraints.add(new NonlinearConstraint(new LinearConstraint(2.0), Interval.open(-1.0, 1.0)));
        final NonlinearOptimizationProblem problem = new NonlinearOptimizationProblem(TEST_FUNCTION, new PointValuePair(0.5, 2.5), constraints);

        try {
            optimizer.optimize(problem, MAX_EVAL);
            fail("Expected MathIllegalStateException for violated constraints.");
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    /** Test for optimization with initial step less than zero. */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithInitialStepLessThanZero() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(-1.0);
        final NonlinearOptimizationProblem problem = new NonlinearOptimizationProblem(TEST_FUNCTION, new PointValuePair(0.5, 2.5));
        optimizer.optimize(problem, MAX_EVAL);
    }

    /** Test function for optimization tests. */
    private static class TestFunction implements UnivariateFunction {

        /** {@inheritDoc} */
        public double value(double x) {
            return Math.pow(x - 2.0, 2.0) + Math.pow(x - 3.0, 2.0) - 1.0;
        }

        /** {@inheritDoc} */
        public double derivative(double x) {
            return 4.0 * (x - 2.0) - 4.0 * (x - 3.0);
        }
    }

    /** Test preconditioner for optimization tests. */
    private static class TestPreconditioner implements Preconditioner {

        /** {@inheritDoc} */
        public double[] precondition(double[] variables, double[] r) {
            for (int i = 0; i < variables.length; ++i) {
                variables[i] += r[i];
            }
            return r;
        }
    }
}