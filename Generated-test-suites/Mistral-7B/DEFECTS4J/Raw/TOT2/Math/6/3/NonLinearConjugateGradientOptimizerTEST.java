package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LevenbergMarquardtSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.convergence.*;
import org.apache.commons.math3.optim.nonlinear.scalar.preconditioner.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.preconditioner.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.preconditioner.cholesky.CholeskyPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.preconditioner.jacobi.JacobiPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.preconditioner.ssor.SSORPreconditioner;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test the constructor with default settings.
     * @throws UnsupportedOperationException if the default constructor is not supported.
     */
    @Test
    public void testDefaultConstructor() throws UnsupportedOperationException {
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(op);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, op.getUpdateFormula());
        assertEquals(new SimpleValueChecker(), op.getChecker());
        assertNull(op.getSolver());
        assertNull(op.getPreconditioner());
        assertEquals(1.0, op.getInitialStep(), 0.0);
    }

    /**
     * Test the constructor with default settings and custom update formula.
     * @throws UnsupportedOperationException if the default constructor is not supported.
     */
    @Test
    public void testDefaultConstructorWithUpdateFormula() throws UnsupportedOperationException {
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        assertNotNull(op);
        assertEquals(ConjugateGradientFormula.POLAK_RIBIERE, op.getUpdateFormula());
        assertEquals(new SimpleValueChecker(), op.getChecker());
        assertNull(op.getSolver());
        assertNull(op.getPreconditioner());
        assertEquals(1.0, op.getInitialStep(), 0.0);
    }

    /**
     * Test the constructor with custom settings.
     */
    @Test
    public void testCustomConstructor() {
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new RMSNormChecker(1e-5),
                new BrentSolver(),
                new CholeskyPreconditioner());
        assertNotNull(op);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, op.getUpdateFormula());
        assertNotNull(op.getChecker());
        assertEquals(new BrentSolver(), op.getSolver());
        assertNotNull(op.getPreconditioner());
        assertInstanceOf(CholeskyPreconditioner.class, op.getPreconditioner());
        assertEquals(1.0, op.getInitialStep(), 0.0);
    }

    /**
     * Test optimization of a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        final UnivariateFunction f = x -> Math.pow(x - 3.0, 2) + 2.0;
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new RMSNormChecker(1e-5),
                new BrentSolver(),
                new IdentityPreconditioner());
        PointValuePair result = op.optimize(f, 0.0, null);
        assertEquals(3.0, result.getPoint()[0], 1e-5);
        assertEquals(1.0, result.getValue(), 1e-5);
    }

    /**
     * Test optimization of a more complex quadratic function.
     */
    @Test
    public void testQuadraticFunctionComplex() {
        final UnivariateFunction f = x -> (Math.pow(x - 2.0, 2) + Math.pow(x - 4.0, 2) + Math.pow(x - 6.0, 2) + 2.0) / 3.0;
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new RMSNormChecker(1e-5),
                new BrentSolver(),
                new IdentityPreconditioner());
        PointValuePair result = op.optimize(f, 0.0, null);
        assertEquals(4.0, result.getPoint()[0], 1e-5);
        assertEquals(0.3333333333333333, result.getValue(), 1e-5);
    }

    /**
     * Test optimization of a non-quadratic function.
     */
    @Test
    public void testNonQuadraticFunction() {
        final UnivariateFunction f = x -> Math.exp(-x) * Math.sin(x);
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new RMSNormChecker(1e-5),
                new LevenbergMarquardtSolver(),
                new SSORPreconditioner());
        PointValuePair result = op.optimize(f, 0.0, null);
        assertEquals(0.7853981633974483, result.getPoint()[0], 1e-5);
        assertEquals(-1.0, result.getValue(), 1e-5);
    }

    /**
     * Test the setInitialStep method.
     */
    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new RMSNormChecker(1e-5),
                new BrentSolver(),
                new IdentityPreconditioner());
        op.setInitialStep(2.0);
        assertEquals(2.0, op.getInitialStep(), 0.0);
    }

    /**
     * Test optimization failure with an incorrect update formula.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalUpdateFormula() {
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.INVALID,
                new RMSNormChecker(1e-5),
                new BrentSolver(),
                new IdentityPreconditioner());
    }

    /**
     * Test optimization failure with a null solver.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullSolver() {
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new RMSNormChecker(1e-5),
                null,
                new IdentityPreconditioner());
    }

    /**
     * Test optimization failure with a null preconditioner.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullPreconditioner() {
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new RMSNormChecker(1e-5),
                new BrentSolver(),
                null);
    }

    /**
     * Test optimization failure with a non-converging solver.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvergingSolver() {
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new RMSNormChecker(1e-5),
                new UnivariateSolver() {
                    @Override
                    public double solve(int maxEvaluations, UnivariateFunction f, double guess, double tolerance) throws ConvergenceException {
                        throw new ConvergenceException("This solver is not supposed to converge.");
                    }
                },
                new IdentityPreconditioner());
        op.optimize(x -> x, 0.0, null);
    }

    /**
     * Test optimization failure with an incorrect preconditioner.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPreconditioner() {
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new RMSNormChecker(1e-5),
                new BrentSolver(),
                new Object());
    }

    /**
     * Test optimization failure with an incorrect convergence checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidChecker() {
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                null,
                new BrentSolver(),
                new IdentityPreconditioner());
    }

    /**
     * Test optimization failure with a null optimization function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullObjectiveFunction() {
        NonLinearConjugateGradientOptimizer op = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new RMSNormChecker(1e-5),
                new BrentSolver(),
                new IdentityPreconditioner());
        try {
            op.optimize(null, 0.0, null);
        } catch (MathIllegalStateException e) {
            fail("Expected IllegalArgumentException but got MathIllegalStateException.");
        }
    }
}