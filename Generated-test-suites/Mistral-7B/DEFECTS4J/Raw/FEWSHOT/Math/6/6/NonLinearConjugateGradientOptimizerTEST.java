package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.UnivariateSolver;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.preconditioners.IdentityPreconditioner;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optimization.nonlinear.scalar.LinearSearchFunction;
import org.apache.commons.math3.optimization.nonlinear.scalar.LinearSearchSolver;
import org.apache.commons.math3.optimization.nonlinear.scalar.Preconditioner;

import org.junit.Test;

import java.util.Arrays;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testConstructorWithDefaults() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertEquals(new SimpleValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(new BrentSolver(), optimizer.getSolver());
        assertEquals(new IdentityPreconditioner(), optimizer.getPreconditioner());
    }

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertEquals(checker, optimizer.getConvergenceChecker());
        assertEquals(new BrentSolver(), optimizer.getSolver());
        assertEquals(new IdentityPreconditioner(), optimizer.getPreconditioner());
    }

    @Test
    public void testConstructorWithSolver() {
        UnivariateSolver solver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), solver);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertEquals(new SimpleValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(solver, optimizer.getSolver());
        assertEquals(new IdentityPreconditioner(), optimizer.getPreconditioner());
    }

    @Test
    public void testConstructorWithPreconditioner() {
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), preconditioner);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertEquals(new SimpleValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(new BrentSolver(), optimizer.getSolver());
        assertEquals(preconditioner, optimizer.getPreconditioner());
    }

    @Test
    public void testConstructorWithAllParams() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, solver, preconditioner);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertEquals(checker, optimizer.getConvergenceChecker());
        assertEquals(solver, optimizer.getSolver());
        assertEquals(preconditioner, optimizer.getPreconditioner());
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep());
    }

    @Test
    public void testSetInitialStepNegative() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep());
    }

    @Test
    public void testOptimize_Minimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        double[] startPoint = new double[]{1.0, 2.0};

        optimizer.optimize(startPoint, GoalType.MINIMIZE);

        assertArrayEquals(new double[]{1.0, 2.0}, startPoint, 0.001);
    }

    @Test
    public void testOptimize_Maximize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        double[] startPoint = new double[]{1.0, 2.0};

        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.optimize(startPoint, GoalType.MAXIMIZE);

        assertArrayEquals(new double[]{1.0, 2.0}, startPoint, 0.001);
    }

    @Test
    public void testOptimize_Minimize_Converged() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        double[] startPoint = new double[]{1.0, 2.0};

        optimizer.optimize(startPoint, GoalType.MINIMIZE);

        ConvergenceChecker<PointValuePair> checker = optimizer.getConvergenceChecker();
        PointValuePair previous = null;
        PointValuePair current = optimizer.getLastSolution();

        assertTrue(checker.isConverged(0, previous, current));
    }

    @Test
    public void testOptimize_Minimize_MaxEvaluationsExceeded() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.setMaxEvaluations(0);
        double[] startPoint = new double[]{1.0, 2.0};

        assertThrows(MaxEvaluationsExceededException.class, () -> optimizer.optimize(startPoint, GoalType.MINIMIZE));
    }

    @Test
    public void testOptimize_Minimize_MathIllegalStateException() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.getSolver().setMaxIterations(0);
        double[] startPoint = new double[]{1.0, 2.0};

        assertThrows(MathIllegalStateException.class, () -> optimizer.optimize(startPoint, GoalType.MINIMIZE));
    }
}