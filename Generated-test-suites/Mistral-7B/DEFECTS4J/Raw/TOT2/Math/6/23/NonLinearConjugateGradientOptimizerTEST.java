package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugate.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugate.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugate.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugate.PolakRibiereConjugateGradient;
import org.apache.commons.math3.optim.nonlinear.conjugate.FletcherReevesConjugateGradient;
import org.apache.commons.math3.optim.nonlinear.solvers.BrentSolver;
import org.apache.commons.math3.optim.nonlinear.solvers.LevenbergMarquardtIterativeSolver;
import org.junit.Test;

/**
 * Test for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test optimization using Fletcher-Reeves update formula and default solver.
     */
    @Test
    public void testFletcherReevesOptimization() {
        UnivariateFunction func = x -> Math.pow(x - 3.0, 3.0);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(500);
        optimizer.setConvergenceChecker(new SimpleValueChecker(1e-6));
        PointValuePair solution = optimizer.optimize(func, 1.0);
        assertEquals(2.0, solution.getPoint()[0], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    /**
     * Test optimization using Polak-Ribière update formula and custom solver.
     */
    @Test
    public void testPolakRibiereOptimization() {
        UnivariateFunction func = x -> Math.pow(x - 3.0, 3.0);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(500);
        optimizer.setConvergenceChecker(new SimpleValueChecker(1e-6));
        optimizer.setSolver(new LevenbergMarquardtIterativeSolver());
        PointValuePair solution = optimizer.optimize(func, 1.0);
        assertEquals(2.0, solution.getPoint()[0], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    /**
     * Test optimization with invalid update formula.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(-1);
    }

    /**
     * Test optimization with null convergence checker.
     */
    @Test(expected = NullPointerException.class)
    public void testNullConvergenceChecker() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setConvergenceChecker(null);
    }

    /**
     * Test optimization with null line search solver.
     */
    @Test(expected = NullPointerException.class)
    public void testNullLineSearchSolver() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setSolver(null);
    }

    /**
     * Test optimization with null preconditioner.
     */
    @Test
    public void testNullPreconditioner() {
        UnivariateFunction func = x -> Math.pow(x - 3.0, 3.0);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(500);
        optimizer.setConvergenceChecker(new SimpleValueChecker(1e-6));
        optimizer.setSolver(new BrentSolver());
        optimizer.setPreconditioner(null);
        PointValuePair solution = optimizer.optimize(func, 1.0);
        assertEquals(2.0, solution.getPoint()[0], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    /**
     * Test optimization with invalid initial step.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(-1.0);
    }

    /**
     * Test optimization with invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        UnivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setGoalType("INVALID");
    }

    /**
     * Test optimization with null UnivariateFunction.
     */
    @Test(expected = NullPointerException.class)
    public void testNullFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.optimize(null, 1.0);
    }

    /**
     * Test optimization with null starting point.
     */
    @Test(expected = NullPointerException.class)
    public void testNullStartingPoint() {
        UnivariateFunction func = x -> Math.pow(x - 3.0, 3.0);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.optimize(func, null);
    }
}