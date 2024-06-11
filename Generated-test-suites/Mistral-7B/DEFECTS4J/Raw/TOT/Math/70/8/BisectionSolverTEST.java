package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    private UnivariateRealFunction testFunction;
    private BisectionSolver solver;
    private double eps = 1e-10;

    @Before
    public void setUp() {
        testFunction = new TestFunction();
        solver = new BisectionSolver();
    }

    @Test
    public void testSimpleRootWithinInterval() throws Exception {
        double root = solver.solve(testFunction, -1.0, 1.0);
        assertEquals("Root not found.", testFunction.root(), root, eps);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        solver.setMaxIterations(1);
        solver.solve(testFunction, -1.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        testFunction.throwFunctionEvaluationExceptionAtPoint(0.5);
        assertFalse("Expected FunctionEvaluationException.", solver.solve(testFunction, -1.0, 1.0) instanceof Number);
    }

    @Test
    public void testZeroFunction() throws Exception {
        UnivariateRealFunction zeroFunction = new ZeroFunction();
        assertNull("Expected NoRootsException for zero function.", solver.solve(zeroFunction, -1.0, 1.0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithArray() throws Exception {
        solver.solve(testFunction, new double[]{-1.0, 1.0});
    }

    @Test
    public void testSolveWithSingularMatrixException() throws Exception {
        UnivariateRealFunction singularFunction = new SingularFunction();
        try {
            solver.solve(singularFunction, -1.0, 1.0);
            fail("Expected SingularMatrixException.");
        } catch (SingularMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithNumberIsTooSmallException() throws Exception {
        UnivariateRealFunction smallFunction = new SmallFunction();
        try {
            solver.solve(smallFunction, -1.0, 1.0);
            fail("Expected NumberIsTooSmallException.");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    private static class TestFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.sin(x) - 0.5;
        }

        public double root() {
            return Math.PI / 2;
        }

        public void throwFunctionEvaluationExceptionAtPoint(double x) {
            throw new FunctionEvaluationException("Exception at point " + x);
        }
    }

    private static class ZeroFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return 0.0;
        }

        public double root() {
            return Double.NaN;
        }
    }

    private static class SingularFunction implements UnivariateRealFunction {

        private RealMatrix jacobian;

        @Override
        public double value(double x) {
            return Math.sin(x) / (x * x);
        }

        @Override
        public RealVector gradient(double x) {
            if (jacobian == null) {
                jacobian = new RealMatrix(1, 2)
                        .setEntry(0, 0, -2.0 / (Math.pow(Math.PI, 2) * Math.pow(Math.E, Math.PI)) * Math.cos(Math.PI))
                        .setEntry(0, 1, 2.0 / (Math.pow(Math.PI, 2) * Math.pow(Math.E, Math.PI)) * Math.sin(Math.PI));
            }
            return jacobian.getColumnVector(0);
        }
    }

    private static class SmallFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            if (Math.abs(x) < 1e-20) {
                throw new NumberIsTooSmallException();
            }
            return Math.pow(Math.sin(x), 2) / (x * x * x);
        }
    }
}