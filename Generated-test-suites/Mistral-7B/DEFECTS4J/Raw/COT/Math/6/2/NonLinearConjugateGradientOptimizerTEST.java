package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.AbstractScalarDifferentiableOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer.IdentityPreconditioner;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer.LineSearchFunction;

import java.util.Arrays;

/**
 * Test class for NonLinearConjugateGradientOptimizer.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test constructor with default parameters.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        assertNull(optimizer.getPreconditioner());
        assertNull(optimizer.getSolver());
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test constructor with given parameters.
     */
    @org.junit.Test
    public void testConstructor() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);
        assertSame(checker, optimizer.getConvergenceChecker());
        assertSame(solver, optimizer.getSolver());
        assertSame(preconditioner, optimizer.getPreconditioner());
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test setInitialStep method.
     */
    @org.junit.Test
    public void testSetInitialStep() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 0.0);
        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test optimization with default values.
     */
    @org.junit.Test
    public void testOptimizationWithDefaultValues() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);
        double[] startPoint = {0.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(Math.PI / 2, result.getPoint()[0], 0.001);
        assertEquals(-1.0, result.getValue(), 0.001);
    }

    /**
     * Test optimization with given values.
     */
    @org.junit.Test
    public void testOptimizationWithGivenValues() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);
        double[] startPoint = {0.1};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(Math.PI / 2 - 0.1363, result.getPoint()[0], 0.001);
        assertEquals(-1.0 - Math.sin(Math.PI / 2 - 0.1363) * Math.sin(Math.PI / 2 - 0.1363), result.getValue(), 0.001);
    }

    /**
     * Test optimization with given convergence checker.
     */
    @org.junit.Test
    public void testOptimizationWithGivenConvergenceChecker() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
                double difference = 0;
                for (int i = 0; i < current.getPoint().length; i++) {
                    difference += Math.pow(current.getPoint()[i] - previous.getPoint()[i], 2);
                }
                return difference < 0.001;
            }
        };
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);
        double[] startPoint = {0.1};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(Math.PI / 2 - 0.1363, result.getPoint()[0], 0.001);
        assertEquals(-1.0 - Math.sin(Math.PI / 2 - 0.1363) * Math.sin(Math.PI / 2 - 0.1363), result.getValue(), 0.001);
    }

    /**
     * Test optimization with given solver.
     */
    @org.junit.Test
    public void testOptimizationWithGivenSolver() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new SecantSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);
        double[] startPoint = {0.1};
        PointValuePair result = optimizer.optimize(function, startPoint);
        throw new UnsupportedOperationException("Secant solver is not supported.");
    }

    /**
     * Test optimization with given preconditioner.
     */
    @org.junit.Test
    public void testOptimizationWithGivenPreconditioner() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new JacobiPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);
        double[] startPoint = {0.1};
        PointValuePair result = optimizer.optimize(function, startPoint);
        throw new UnsupportedOperationException("Jacobi preconditioner is not supported.");
    }

    /**
     * Test optimization with given update formula.
     */
    @org.junit.Test
    public void testOptimizationWithGivenUpdateFormula() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);
        double[] startPoint = {0.1};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(Math.PI / 2 - 0.1363, result.getPoint()[0], 0.001);
        assertEquals(-1.0 - Math.sin(Math.PI / 2 - 0.1363) * Math.sin(Math.PI / 2 - 0.1363), result.getValue(), 0.001);
    }

    /**
     * Test findUpperBound method with given function and initial step.
     */
    @org.junit.Test
    public void testFindUpperBound() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        double a = 0.0;
        double h = 1.0;
        double b = NonLinearConjugateGradientOptimizer.this.findUpperBound(function, a, h);
        double yA = function.value(a);
        double yB = function.value(b);
        assertTrue(yA * yB < 0);
    }

    /**
     * Test findUpperBound method with given function, initial step and bracketing failure.
     */
    @org.junit.Test(expected = MathIllegalStateException.class)
    public void testFindUpperBoundFailure() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        double a = 0.0;
        double h = 1.0;
        NonLinearConjugateGradientOptimizer.this.findUpperBound(function, a, h);
    }

    /**
     * Test precondition method with given variables and residuals.
     */
    @org.junit.Test
    public void testPrecondition() {
        double[] variables = {1.0, 2.0};
        double[] residuals = {1.0, 2.0};
        double[] expected = {1.0, 2.0};
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] result = preconditioner.precondition(variables, residuals);
        assertArrayEquals(expected, result);
    }
}