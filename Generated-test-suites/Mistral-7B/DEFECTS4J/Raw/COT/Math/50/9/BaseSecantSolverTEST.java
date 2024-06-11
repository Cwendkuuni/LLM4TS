package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.RootNotFoundException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** Test function for testing the root finding techniques. */
    private static final UnivariateFunction TEST_FUNCTION = new UnivariateFunction() {

        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 2;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    };

    /** Test solver instance. */
    private BaseSecantSolver solver;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        double[] testData = {-5.0, -1.0, 1.0, 5.0};
        solver = new IllinoisSolver(1e-6, TEST_FUNCTION);
        for (double testDataPoint : testData) {
            try {
                solver.solve(10, TEST_FUNCTION, -5.0, 5.0, testDataPoint);
            } catch (MaxEvalExceededException e) {
                // Do nothing, as it's expected for some test cases
            }
        }
    }

    /**
     * Tests the constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Tests the constructor with specified method and accuracies.
     */
    @Test
    public void testConstructorWithMethodAndAccuracies() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.PEGASUS, 1e-6, 1e-6);
        assertNotNull(solver);
    }

    /**
     * Tests the constructor with specified method, accuracies, and function
     * value accuracy.
     */
    @Test
    public void testConstructorWithMethodAccuraciesAndFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS, 1e-6, 1e-6, 1e-6);
        assertNotNull(solver);
    }

    /**
     * Tests the {@link #solve(int, UnivariateRealFunction, double, double, double)}
     * method with default arguments.
     */
    @Test
    public void testSolveWithDefaultArguments() {
        double root = solver.solve(10, TEST_FUNCTION, -5.0, 5.0);
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Tests the {@link #solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with specified arguments.
     */
    @Test
    public void testSolveWithSpecifiedArguments() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, TEST_FUNCTION);
        double root = solver.solve(10, TEST_FUNCTION, -5.0, 5.0, 1.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(1.0, root, 1e-6);
    }

    /**
     * Tests the {@link #solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that doesn't bracket the root.
     */
    @Test(expected = RootNotFoundException.class)
    public void testSolveWithFunctionThatDoesntBracketRoot() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, TEST_FUNCTION);
        solver.solve(10, TEST_FUNCTION, 5.0, 5.0, 5.0);
    }

    /**
     * Tests the {@link #solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that doesn't converge.
     */
    @Test(expected = TooManyIterationsException.class)
    public void testSolveWithFunctionThatDoesntConverge() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, TEST_FUNCTION);
        solver.solve(10, TEST_FUNCTION, -5.0, 5.0, 0.0);
    }

    /**
     * Tests the {@link #solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that doesn't have a root in the given interval.
     */
    @Test
    public void testSolveWithFunctionThatDoesntHaveARoot() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        });
        double root = solver.solve(10, this.TEST_FUNCTION, -5.0, 5.0);
        assertNull(root);
    }

    /**
     * Tests the {@link #solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that has multiple roots.
     */
    @Test
    public void testSolveWithFunctionThatHasMultipleRoots() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, TEST_FUNCTION);
        double root1 = solver.solve(10, TEST_FUNCTION, -5.0, 5.0);
        assertEquals(0.0, root1, 1e-6);

        double root2 = solver.solve(10, TEST_FUNCTION, 5.0, 5.0);
        assertEquals(0.0, root2, 1e-6);

        TestUtils.assertEquals(new double[] { 0.0, 0.0 },
                new double[] { root1, root2 }, Precision.SAFE_EQ);
    }

    /**
     * Tests the {@link #solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that has a root at the boundary.
     */
    @Test
    public void testSolveWithFunctionThatHasRootAtBoundary() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, TEST_FUNCTION);
        double root = solver.solve(10, TEST_FUNCTION, 1.0, 5.0);
        assertEquals(1.0, root, 1e-6);
    }

    /**
     * Tests the {@link #solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that has a root at the start of the interval.
     */
    @Test
    public void testSolveWithFunctionThatHasRootAtStartOfInterval() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, TEST_FUNCTION);
        double root = solver.solve(10, TEST_FUNCTION, -5.0, -5.0);
        assertEquals(-5.0, root, 1e-6);
    }

    /**
     * Tests the {@link #solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that has a root at the end of the interval.
     */
    @Test
    public void testSolveWithFunctionThatHasRootAtEndOfInterval() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, TEST_FUNCTION);
        double root = solver.solve(10, TEST_FUNCTION, 5.0, 5.0);
        assertEquals(5.0, root, 1e-6);
    }

    /**
     * Tests the {@link #solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that has a root with a very small difference from
     * the bounds.
     */
    @Test
    public void testSolveWithFunctionThatHasRootNearBounds() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, TEST_FUNCTION);
        double root = solver.solve(10, TEST_FUNCTION, 4.9999999, 5.0000001);
        assertEquals(5.0, root, 1e-6);
    }
}