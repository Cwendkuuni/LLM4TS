package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestConstants;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** Test function for the solver. */
    private UnivariateRealFunction testFunction;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        testFunction = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return x * x - 2 * x + 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x - 2;
            }
        };
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolve() throws Exception {
        final double relativeAccuracy = 1e-6;
        final double absoluteAccuracy = 1e-10;
        final double functionValueAccuracy = 1e-11;
        final BaseSecantSolver solver = new IllinoisSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);

        // Test with provided bounds.
        double result = solver.solve(100, testFunction, -1.0, 1.0);
        double expected = -0.5;
        assertEquals(expected, result, Precision.EPSILON);

        // Test with incorrect bounds.
        try {
            solver.solve(100, testFunction, 1.0, -1.0);
            fail("Expected a NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with maxEval exceeded.
        try {
            solver.solve(0, testFunction, -1.0, 1.0);
            fail("Expected a MaxEvalExceededException");
        } catch (MaxEvalExceededException e) {
            // Expected
        }

        // Test with function value too large.
        try {
            solver.solve(100, new UnivariateRealFunction() {

                @Override
                public double value(double x) {
                    if (x > 0) {
                        return Double.POSITIVE_INFINITY;
                    }
                    return 0.0;
                }

                @Override
                public double derivative(double x) {
                    return Double.NaN;
                }
            }, -1.0, 1.0);
            fail("Expected a NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        // Test with unsupported operation.
        try {
            solver.solve(100, new UnivariateRealFunction() {

                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public double derivative(double x) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            }, -1.0, 1.0);
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveNoStartValue() throws Exception {
        final double relativeAccuracy = 1e-6;
        final double absoluteAccuracy = 1e-10;
        final double functionValueAccuracy = 1e-11;
        final BaseSecantSolver solver = new IllinoisSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);

        // Test with provided bounds.
        double result = solver.solve(100, testFunction, -1.0, 1.0);
        double expected = -0.5;
        assertEquals(expected, result, Precision.EPSILON);

        // Test with incorrect bounds.
        try {
            solver.solve(100, testFunction, 1.0, -1.0);
            fail("Expected a NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with maxEval exceeded.
        try {
            solver.solve(0, testFunction, -1.0, 1.0);
            fail("Expected a MaxEvalExceededException");
        } catch (MaxEvalExceededException e) {
            // Expected
        }

        // Test with function value too large.
        try {
            solver.solve(100, new UnivariateRealFunction() {

                @Override
                public double value(double x) {
                    if (x > 0) {
                        return Double.POSITIVE_INFINITY;
                    }
                    return 0.0;
                }

                @Override
                public double derivative(double x) {
                    return Double.NaN;
                }
            }, -1.0, 1.0);
            fail("Expected a NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        // Test with unsupported operation.
        try {
            solver.solve(100, new UnivariateRealFunction() {

                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public double derivative(double x) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            }, -1.0, 1.0);
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test without startValue.
        try {
            solver.solve(100, testFunction, -1.0, 1.0, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveWithStartValue() throws Exception {
        final double relativeAccuracy = 1e-6;
        final double absoluteAccuracy = 1e-10;
        final double functionValueAccuracy = 1e-11;
        final double startValue = 0.5;
        final BaseSecantSolver solver = new IllinoisSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);

        // Test with provided bounds.
        double result = solver.solve(100, testFunction, -1.0, 1.0, startValue);
        double expected = -0.5;
        assertEquals(expected, result, 1e-5);

        // Test with incorrect bounds.
        try {
            solver.solve(100, testFunction, 1.0, -1.0, startValue);
            fail("Expected a NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with maxEval exceeded.
        try {
            solver.solve(0, testFunction, -1.0, 1.0, startValue);
            fail("Expected a MaxEvalExceededException");
        } catch (MaxEvalExceededException e) {
            // Expected
        }

        // Test with function value too large.
        try {
            solver.solve(100, new UnivariateRealFunction() {

                @Override
                public double value(double x) {
                    if (x > 0) {
                        return Double.POSITIVE_INFINITY;
                    }
                    return 0.0;
                }

                @Override
                public double derivative(double x) {
                    return Double.NaN;
                }
            }, -1.0, 1.0, startValue);
            fail("Expected a NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        // Test with unsupported operation.
        try {
            solver.solve(100, new UnivariateRealFunction() {

                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public double derivative(double x) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            }, -1.0, 1.0, startValue);
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with startValue within bounds.
        try {
            solver.solve(100, testFunction, -1.0, 1.0, 0.25);
            fail("Expected a RootFoundException");
        } catch (RootFoundException e) {
            // Expected
        }

        // Test with startValue outside bounds.
        try {
            solver.solve(100, testFunction, -1.0, 1.0, -1.1);
            fail("Expected a NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with startValue very close to a root.
        try {
            solver.solve(100, testFunction, -1.0, 1.0, 0.4999999);
            fail("Expected a RootFoundException");
        } catch (RootFoundException e) {
            // Expected
        }

        // Test with startValue very close to a root (left).
        try {
            solver.solve(100, testFunction, -1.0, 1.0, 0.49999995);
            fail("Expected a RootFoundException");
        } catch (RootFoundException e) {
            // Expected
        }

        // Test with startValue very close to a root (right).
        try {
            solver.solve(100, testFunction, -1.0, 1.0, 0.50000005);
            fail("Expected a RootFoundException");
        } catch (RootFoundException e) {
            // Expected
        }

        // Test with startValue very close to a root (below).
        try {
            solver.solve(100, testFunction, -1.0, 1.0, 0.49999999);
            fail("Expected a RootFoundException");
        } catch (RootFoundException e) {
            // Expected
        }

        // Test with startValue very close to a root (above).
        try {
            solver.solve(100, testFunction, -1.0, 1.0, 0.50000001);
            fail("Expected a RootFoundException");
        } catch (RootFoundException e) {
            // Expected
        }
    }
}