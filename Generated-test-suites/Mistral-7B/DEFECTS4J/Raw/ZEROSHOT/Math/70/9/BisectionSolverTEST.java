package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;

public class BisectionSolverTest {

    @org.junit.Test
    public void testEmptyConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @org.junit.Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction f = new PolynomialFunction(1, new double[]{-1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver(f);
        assertNotNull(solver);
        assertEquals(f, solver.getFunction());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullFunction() {
        new BisectionSolver(null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullMin() {
        UnivariateRealFunction f = new PolynomialFunction(1, new double[]{0.0, 1.0, 1.0});
        BisectionSolver solver = new BisectionSolver();
        solver.setFunction(f);
        solver.solve(null, 0.0, 1.0);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullMax() {
        UnivariateRealFunction f = new PolynomialFunction(1, new double[]{0.0, 1.0, 1.0});
        BisectionSolver solver = new BisectionSolver();
        solver.setFunction(f);
        solver.solve(0.0, null);
    }

    @org.junit.Test
    public void testSolveWithSimpleFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(1, new double[]{-1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        solver.setFunction(f);
        double root = solver.solve(0.0, 1.0);
        assertEquals(0.0, root, Precision.SAFE_EPSILON);
    }

    @org.junit.Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterations() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(1, new double[]{0.0, 1.0, 1.0});
        BisectionSolver solver = new BisectionSolver();
        solver.setFunction(f);
        solver.setMaxIterations(0);
        solver.solve(0.0, 1.0);
    }

    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testSolveWithDivergentFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(1, new double[]{0.0, 1.0, Double.POSITIVE_INFINITY});
        BisectionSolver solver = new BisectionSolver();
        solver.setFunction(f);
        solver.solve(0.0, 1.0);
    }

    @org.junit.Test(expected = NoRootsException.class)
    public void testSolveWithNoRootsFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(1, new double[]{0.0, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY});
        BisectionSolver solver = new BisectionSolver();
        solver.setFunction(f);
        solver.solve(0.0, 1.0);
    }

    @org.junit.Test
    public void testSolveWithZeroAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(1, new double[]{-1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        solver.setFunction(f);
        solver.setAbsoluteAccuracy(0.0);
        double root = solver.solve(0.0, 1.0);
        assertEquals(0.0, root, Precision.SAFE_EPSILON);
    }

    @org.junit.Test
    public void testSolveWithMaxIterationsAndZeroAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(1, new double[]{-1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        solver.setFunction(f);
        solver.setMaxIterations(0);
        solver.setAbsoluteAccuracy(0.0);
        try {
            solver.solve(0.0, 1.0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testSolveWithLargeAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException, PrecisionException {
        UnivariateRealFunction f = new PolynomialFunction(1, new double[]{-1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        solver.setFunction(f);
        solver.setAbsoluteAccuracy(Precision.PRECISION_EPSILON);
        double root = solver.solve(0.0, 1.0);
        double expected = Math.sqrt(2.0) - 1.0;
        assertEquals(expected, root, Precision.SAFE_EPSILON);
    }

    @org.junit.Test
    public void testSolveWithInitialGuess() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(1, new double[]{-1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        solver.setFunction(f);
        double initialGuess = 1.1;
        double root = solver.solve(0.0, 1.0, initialGuess);
        double expected = 0.0;
        assertEquals(expected, root, Precision.SAFE_EPSILON);
    }
}