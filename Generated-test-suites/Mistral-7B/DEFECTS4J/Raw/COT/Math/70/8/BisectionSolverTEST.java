package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.PrecisionRequiredException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;

import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverImpl;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BisectionSolverTest {

    @SuppressWarnings("unchecked")
    private UnivariateRealFunction f1 = new PolynomialFunction(new double[] {-1.0, 1.0, 1.0, 1.0, 1.0}, new double[] {1.0, 0.0});

    @SuppressWarnings("unchecked")
    private UnivariateRealFunction f2 = new PolynomialFunction(new double[] {-1.0, 1.0, 0.0, 1.0}, new double[] {1.0, 2.0, 1.0, 0.0});

    @SuppressWarnings("unchecked")
    private UnivariateRealFunction f3 = new PolynomialFunction(new double[] {-1.0, 1.0, 1.0, 0.0, 1.0}, new double[] {0.0, 1.0, 1.0, 1.0, 0.0});

    @SuppressWarnings("unchecked")
    private UnivariateRealFunction f4 = new PolynomialFunction(new double[] {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, new double[] {-1.0, -1.0, 1.0, 1.0, 1.0, 1.0});

    @SuppressWarnings("unchecked")
    private UnivariateRealFunction f5 = new PolynomialFunction(new double[] {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, new double[] {-1.0, -1.0, 1.0, 1.0, 1.0, 1.0, 1.0});

    @SuppressWarnings("unchecked")
    private UnivariateRealFunction fInvalid = new UnivariateFunction() {
        @Override
        public double value(double x) {
            throw new UnsupportedOperationException();
        }
    };

    @org.junit.Test
    public void testConstructorWithFunction() {
        BisectionSolver solver = new BisectionSolver(f1);
        UnivariateRealSolverImpl solverImpl = (UnivariateRealSolverImpl) solver;
        assertEquals("Expected 100 iterations.", solverImpl.getMaxIterations(), 100);
        assertEquals("Expected 1e-6 accuracy.", solverImpl.getAbsoluteAccuracy(), 1e-6, 0.0);
    }

    @org.junit.Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealSolverImpl solverImpl = (UnivariateRealSolverImpl) solver;
        assertEquals("Expected 100 iterations.", solverImpl.getMaxIterations(), 100);
        assertEquals("Expected 1e-6 accuracy.", solverImpl.getAbsoluteAccuracy(), 1e-6, 0.0);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSolveWithInvalidFunction() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(fInvalid, 0.0, 1.0);
    }

    @org.junit.Test
    public void testSolveWithMinGreaterThanMax() {
        BisectionSolver solver = new BisectionSolver();
        assertThrows("Expected FunctionEvaluationException.", FunctionEvaluationException.class, () -> solver.solve(f1, 1.0, 0.0));
    }

    @org.junit.Test
    public void testSolveWithInitialOutsideInterval() {
        BisectionSolver solver = new BisectionSolver();
        double initial = 2.0;
        assertThrows("Expected FunctionEvaluationException.", FunctionEvaluationException.class, () -> solver.solve(f1, 0.0, 1.0, initial));
    }

    @org.junit.Test
    public void testSolveSimple() {
        BisectionSolver solver = new BisectionSolver();
        double result;

        result = solver.solve(f1, 0.0, 1.0);
        assertEquals("Expected solution is approximately 0.3665.", result, 0.3664866231365118, 0.0);
    }

    @org.junit.Test
    public void testSolveWithLargeInterval() {
        BisectionSolver solver = new BisectionSolver();
        double result;

        result = solver.solve(f2, 0.0, 3.0);
        assertEquals("Expected solution is approximately 1.0.", result, 1.0, 0.0);
    }

    @org.junit.Test
    public void testSolveWithInitialInInterval() {
        BisectionSolver solver = new BisectionSolver();
        double initial = 0.5;
        double result;

        result = solver.solve(f3, 0.0, 1.0, initial);
        assertEquals("Expected solution is approximately 0.7142857142857143.", result, 0.7142857142857143, 0.0);
    }

    @org.junit.Test
    public void testSolveWithMaxIterationsReached() {
        BisectionSolver solver = new BisectionSolver();
        solver.setMaxIterations(1);
        double result;

        result = solver.solve(f4, 0.0, 1.0);
        assertThrows("Expected MaxIterationsExceededException.", MaxIterationsExceededException.class, () -> solver.solve(f4, 0.0, 1.0));
    }

    @org.junit.Test
    public void testSolveWithPrecision() {
        BisectionSolver solver = new BisectionSolver();
        double precision = 1e-8;
        solver.setAbsoluteAccuracy(precision);
        double result;

        result = solver.solve(f5, 0.0, 1.0);
        assertEquals("Expected solution is approximately 0.5877853033333853.", result, 0.5877853033333854, 0.0);
    }

    @org.junit.Test
    public void testSolveWithNoRoots() {
        BisectionSolver solver = new BisectionSolver();
        assertThrows("Expected NoRootsException.", NoRootsException.class, () -> solver.solve(f3, -1.0, 0.0));
    }
}