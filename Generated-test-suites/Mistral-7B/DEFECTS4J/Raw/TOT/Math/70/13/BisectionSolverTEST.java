package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionReachedException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    /**
     * Test solver with a simple function where the root is known.
     */
    @Test
    public void testSimpleFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        try {
            double root = solver.solve(f, -2.5, 2.5);
            assertEquals(0.0, root, 1e-5);
        } catch (MaxIterationsExceededException e) {
            fail("Expected to find the root within the given iteration limit.");
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation should not throw an exception.");
        }
    }

    /**
     * Test solver with a simple function where the root is not bracketed initially.
     */
    @Test
    public void testUnbracketedRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        try {
            BisectionSolver solver = new BisectionSolver();
            solver.solve(f, 0.0, 1.0);
            fail("Expected MaxIterationsExceededException as the root is not bracketed.");
        } catch (MaxIterationsExceededException ignored) {
            // Expected
        }
    }

    /**
     * Test solver with a function where the root is at a machine epsilon boundary.
     */
    @Test
    public void testRootAtMachineEpsilonBoundary() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 1.0e-10 * x;
            }
        };

        try {
            BisectionSolver solver = new BisectionSolver();
            solver.solve(f, 0.0, 1.0);
            double root = solver.getResult();
            assertTrue(Precision.isClose(0.0, root, 1e-13));
        } catch (MaxIterationsExceededException e) {
            fail("Expected to find the root within the given iteration limit.");
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation should not throw an exception.");
        } catch (NumberIsTooSmallException e) {
            fail("Expected the root to be found, not too small.");
        } catch (PrecisionReachedException e) {
            fail("Expected to find the root within the given tolerance.");
        }
    }

    /**
     * Test solver with a function where the root is not found within the given tolerance.
     */
    @Test
    public void testRootNotFoundWithinTolerance() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        try {
            BisectionSolver solver = new BisectionSolver();
            solver.solve(f, 0.0, 1.0, 1e-16);
            fail("Expected PrecisionReachedException as the root is not found within the given tolerance.");
        } catch (PrecisionReachedException ignored) {
            // Expected
        } catch (MaxIterationsExceededException e) {
            fail("Expected to find the root within the given iteration limit.");
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation should not throw an exception.");
        }
    }

    /**
     * Test solver with a function where the root is at a boundary of the interval.
     */
    @Test
    public void testRootAtIntervalBoundary() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        try {
            BisectionSolver solver = new BisectionSolver();
            double root = solver.solve(f, -3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211000333088511);
            assertTrue(Precision.isClose(Math.PI / 2.0, root, 1e-5));
        } catch (MaxIterationsExceededException e) {
            fail("Expected to find the root within the given iteration limit.");
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation should not throw an exception.");
        }
    }

    /**
     * Test solver with a function where the root is outside the interval.
     */
    @Test
    public void testRootOutsideInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            BisectionSolver solver = new BisectionSolver();
            solver.solve(f, 0.0, 2.0);
            fail("Expected ZeroException as the root is outside the interval.");
        } catch (ZeroException e) {
            // Expected
        } catch (MaxIterationsExceededException e) {
            fail("Expected to find the root within the given iteration limit.");
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation should not throw an exception.");
        }
    }
}