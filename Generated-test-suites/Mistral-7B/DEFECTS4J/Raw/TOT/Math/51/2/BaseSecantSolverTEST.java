package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test function for {@link BaseSecantSolver}.
     */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return x * x - 4;
        }
    };

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with the default constructor and {@link BaseSecantSolver.Method#REGULA_FALSI} method.
     */
    @Test
    public void testRegulaFalsiDefaultConstructor() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, Method.REGULA_FALSI);
        try {
            double root = solver.solve(10, TEST_FUNCTION, -5, 5, 0.0);
            assertEquals(-2.0, root, Precision.EPSILON);
        } catch (MaxIterationsExceededException e) {
            fail("Test should not throw MaxIterationsExceededException");
        } catch (NoBracketingException e) {
            fail("Test should not throw NoBracketingException");
        } catch (PrecisionException e) {
            fail("Test should not throw PrecisionException");
        }

        try {
            solver = new BaseSecantSolver(-1.0, Method.REGULA_FALSI);
            fail("Test should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with the constructor taking a relative accuracy, an absolute accuracy, and the {@link BaseSecantSolver.Method#REGULA_FALSI} method.
     */
    @Test
    public void testRegulaFalsiCustomAccuracyConstructor() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, Method.REGULA_FALSI);
        try {
            double root = solver.solve(10, TEST_FUNCTION, -5, 5, 0.0);
            assertEquals(-2.0, root, Precision.EPSILON);
        } catch (MaxIterationsExceededException e) {
            fail("Test should not throw MaxIterationsExceededException");
        } catch (NoBracketingException e) {
            fail("Test should not throw NoBracketingException");
        } catch (PrecisionException e) {
            fail("Test should not throw PrecisionException");
        }

        try {
            solver = new BaseSecantSolver(-1.0, 1.0, Method.REGULA_FALSI);
            fail("Test should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with the constructor taking a relative accuracy, an absolute accuracy, a function value accuracy, and the {@link BaseSecantSolver.Method#REGULA_FALSI} method.
     */
    @Test
    public void testRegulaFalsiCustomAccuracyFunctionValueConstructor() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-6;
        final double functionValueAccuracy = 1e-7;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.REGULA_FALSI);
        try {
            double root = solver.solve(10, TEST_FUNCTION, -5, 5, 0.0);
            assertEquals(-2.0, root, Precision.EPSILON);
        } catch (MaxIterationsExceededException e) {
            fail("Test should not throw MaxIterationsExceededException");
        } catch (NoBracketingException e) {
            fail("Test should not throw NoBracketingException");
        } catch (PrecisionException e) {
            fail("Test should not throw PrecisionException");
        }

        try {
            solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, -1.0, Method.REGULA_FALSI);
            fail("Test should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with the constructor taking a method {@link BaseSecantSolver.Method#ILLINOIS}.
     */
    @Test
    public void testIllinoisConstructor() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, Method.ILLINOIS);
        try {
            double root = solver.solve(10, TEST_FUNCTION, -5, 5, 0.0);
            assertEquals(-2.0, root, Precision.EPSILON);
        } catch (MaxIterationsExceededException e) {
            fail("Test should not throw MaxIterationsExceededException");
        } catch (NoBracketingException e) {
            fail("Test should not throw NoBracketingException");
        } catch (PrecisionException e) {
            fail("Test should not throw PrecisionException");
        }

        try {
            solver = new BaseSecantSolver(-1.0, Method.ILLINOIS);
            fail("Test should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with the constructor taking a method {@link BaseSecantSolver.Method#PEGASUS}.
     */
    @Test
    public void testPegasusConstructor() {
        final double relativeAccuracy = 1e-5;
        final double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, Method.PEGASUS);
        try {
            double root = solver.solve(10, TEST_FUNCTION, -5, 5, 0.0);
            assertEquals(-2.0, root, Precision.EPSILON);
        } catch (MaxIterationsExceededException e) {
            fail("Test should not throw MaxIterationsExceededException");
        } catch (NoBracketingException e) {
            fail("Test should not throw NoBracketingException");
        } catch (PrecisionException e) {
            fail("Test should not throw PrecisionException");
        }

        try {
            solver = new BaseSecantSolver(-1.0, Method.PEGASUS);
            fail("Test should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}