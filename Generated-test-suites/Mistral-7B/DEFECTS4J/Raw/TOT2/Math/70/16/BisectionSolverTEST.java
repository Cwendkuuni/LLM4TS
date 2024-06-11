import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.solvers.BisectionSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class BisectionSolverTest {

    /**
     * Test for the constructor with no arguments.
     */
    @Test
    public void testEmptyConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1E-6, solver.getRelativeAccuracy(), 0);
    }

    /**
     * Test for the constructor with a function.
     */
    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1E-6, solver.getRelativeAccuracy(), 0);
    }

    /**
     * Test solving a simple function.
     */
    @Test
    public void testSolveSimpleFunction() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        double root = solver.solve(function, -3.0, 3.0);
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test solving a function with roots at the interval boundaries.
     */
    @Test
    public void testSolveWithRootsAtBounds() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        double root = solver.solve(function, -2.0, 2.0);
        assertEquals(-2.0, root);
    }

    /**
     * Test solving a function with roots between the interval boundaries.
     */
    @Test
    public void testSolveWithRootsInside() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        double root = solver.solve(function, -1.5, 1.5);
        assertEquals(-0.7071067811865475, root, 1e-6);
    }

    /**
     * Test solving a function with roots at the same position.
     */
    @Test
    public void testSolveWithRootsSamePosition() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        double root = solver.solve(function, -2.0, -2.0);
        assertEquals(-2.0, root);
    }

    /**
     * Test solving a function with roots at the same position and relative accuracy
     * less than the absolute accuracy.
     */
    @Test
    public void testSolveWithRootsSamePositionAndLessAccuracy() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        solver.setRelativeAccuracy(1e-8);
        try {
            solver.solve(function, -2.0, -2.0);
            assertFalse(true);
        } catch (Exception e) {
            assertEquals(NoRootsException.class, e.getClass());
        }
    }

    /**
     * Test solving a function with roots close to the interval boundaries.
     */
    @Test
    public void testSolveWithRootsCloseToBounds() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        double root = solver.solve(function, -1.1, 1.1);
        assertEquals(-1.0, root, 1e-6);
    }

    /**
     * Test solving a function with roots far from the interval boundaries.
     */
    @Test
    public void testSolveWithRootsFarFromBounds() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 16.0;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        double root = solver.solve(function, -5.0, 5.0);
        assertEquals(-4.0, root, 1e-6);
    }

    /**
     * Test solving a function with non-finite values at the interval boundaries.
     */
    @Test
    public void testSolveWithNonFiniteValuesAtBounds() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(x);
            }
        };
        try {
            BisectionSolver solver = new BisectionSolver(function);
            solver.solve(function, -1.0, 0.0);
            assertFalse(true);
        } catch (Exception e) {
            assertEquals(NumberIsTooSmallException.class, e.getClass());
        }

        try {
            BisectionSolver solver = new BisectionSolver(function);
            solver.solve(function, 0.0, 1.0);
            assertFalse(true);
        } catch (Exception e) {
            assertEquals(NumberIsTooSmallException.class, e.getClass());
        }
    }

    /**
     * Test solving a function with non-finite values inside the interval.
     */
    @Test
    public void testSolveWithNonFiniteValuesInside() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, -0.5);
            }
        };
        try {
            BisectionSolver solver = new BisectionSolver(function);
            solver.solve(function, -1.0, 1.0);
            assertFalse(true);
        } catch (Exception e) {
            assertEquals(TooManyIterationsException.class, e.getClass());
        }
    }

    /**
     * Test solving a function that doesn't have real roots.
     */
    @Test
    public void testSolveWithNoRealRoots() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x) * Math.sin(Math.PI / x);
            }
        };
        try {
            BisectionSolver solver = new BisectionSolver(function);
            solver.solve(function, -1.0, 1.0);
            assertFalse(true);
        } catch (Exception e) {
            assertEquals(NoRootsException.class, e.getClass());
        }
    }

    /**
     * Test solving a function with complex roots.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithComplexRoots() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.pow(x, 2) + 1.0) * Math.sin(Math.PI / x);
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        solver.solve(function, -1.0, 1.0);
    }

    /**
     * Test solving a function with a very large interval.
     */
    @Test
    public void testSolveWithLargeInterval() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x);
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        double root = solver.solve(function, -1000.0, 1000.0);
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test solving a function with a very small interval.
     */
    @Test
    public void testSolveWithSmallInterval() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - 1.0e-12;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        try {
            solver.solve(function, 1.0e-12, 1.0e-12);
            assertFalse(true);
        } catch (Exception e) {
            assertEquals(MaxIterationsExceededException.class, e.getClass());
        }
    }

    /**
     * Test solving a function with a very large maximum iteration count.
     */
    @Test
    public void testSolveWithMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        solver.setMaxIterations(1000000);
        try {
            solver.solve(function, -Math.PI, Math.PI);
            assertFalse(true);
        } catch (Exception e) {
            assertEquals(MaxIterationsExceededException.class, e.getClass());
        }
    }
}