package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleValueChecker;
import org.apache.commons.math3.optim.preconditions.ConjugateGradientFormula;
import org.apache.commons.math3.optim.preconditions.IdentityPreconditioner;
import org.apache.commons.math3.optim.preconditions.Preconditioner;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testConstructorWithDefaults() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        assertNotNull(optimizer.getPreconditioner());
        assertInstanceOf(IdentityPreconditioner.class, optimizer.getPreconditioner());
        assertNull(optimizer.getSolver());
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testConstructorWithCheckerAndSolver() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker);
        assertNull(optimizer.getPreconditioner());
        assertNull(optimizer.getSolver());
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testConstructorWithCheckerSolverAndPreconditioner() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);
        assertNotNull(optimizer.getPreconditioner());
        assertSame(preconditioner, optimizer.getPreconditioner());
        assertSame(solver, optimizer.getSolver());
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testConstructorWithInvalidUpdateFormula() {
        ConjugateGradientFormula updateFormula = null;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
    }

    @Test
    public void testSetInitialStep() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testOptimize_Minimization() throws OptimizationException {
        UnivariateFunction function = x -> Math.pow(x, 2);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimize(function, 0.1, 100, goalType);
        assertEquals(-1.0, result.getPoint()[0], 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    @Test
    public void testOptimize_Maximization() throws OptimizationException {
        UnivariateFunction function = x -> Math.pow(x, 2);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        GoalType goalType = GoalType.MAXIMIZE;
        PointValuePair result = optimizer.optimize(function, 0.1, 100, goalType);
        assertEquals(1.0, result.getPoint()[0], 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimize_FailedConvergence() throws OptimizationException {
        UnivariateFunction function = x -> Math.pow(x, 2);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES) {
            @Override
            protected PointValuePair doOptimize() throws OptimizationException {
                throw new OptimizationException("Test exception");
            }
        };
        optimizer.optimize(function, 0.1, 1);
    }
}