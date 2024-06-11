package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the SimplexSolver class.
 */
public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        double[][] coefficients = {{1.0, 2.0, -1.0}, {-2.0, 1.0, 3.0}, {3.0, -1.0, 2.0}};
    int[][] constraints = {{0, 0, 1}, {-1, 1, 0}, {1, -1, 1}};
    LinearObjectiveFunction goal = new LinearObjectiveFunction(new double[]{0.0, 1.0, 2.0});
    solver = new SimplexSolver(1.0e-6);
    }

    /**
     * Test that the solver correctly solves the example problem given in the Javadoc.
     */
    @Test
    public void testExampleProblem() {
        try {
            RealPointValuePair result = solver.doOptimize();
            assertEquals(new double[]{3.0 / 7.0, 1.0 / 7.0, 2.0 / 7.0}, result.getPoint(), 1.0e-6);
            assertEquals(1.0, result.getValue(), 1.0e-6);
        } catch (OptimizationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test that the solver correctly handles the case where the problem has no solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        double[][] coefficients = {{1.0, 2.0, -1.0}, {-2.0, 1.0, 3.0}, {3.0, -1.0, 2.0}};
        int[][] constraints = {{0, 0, 1}, {-1, 1, 0}, {1, -1, 1}, {-1, 1, 1}};
        LinearObjectiveFunction goal = new LinearObjectiveFunction(new double[]{0.0, 1.0, 2.0});
        try {
            solver.doOptimize();
            fail("Unexpected result");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    /**
     * Test that the solver correctly handles the case where the problem is unbounded.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        double[][] coefficients = {{1.0, 2.0, -1.0}, {-2.0, 1.0, 3.0}, {3.0, -1.0, 2.0}};
        int[][] constraints = {{0, 0, 1}, {-1, 1, 0}, {1, -1, 1}};
        LinearObjectiveFunction goal = new LinearObjectiveFunction(new double[]{0.0, 1.0, 2.0});
        try {
            solver.doOptimize();
            fail("Unexpected result");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    /**
     * Test that the solver correctly handles the case where the maximal number of iterations is exceeded.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        SimplexSolver solver = new SimplexSolver(1.0e-12);
        double[][] coefficients = {{1.0, 2.0, -1.0}, {-2.0, 1.0, 3.0}, {3.0, -1.0, 2.0}};
        int[][] constraints = {{0, 0, 1}, {-1, 1, 0}, {1, -1, 1}};
        LinearObjectiveFunction goal = new LinearObjectiveFunction(new double[]{0.0, 1.0, 2.0});
        try {
            solver.doOptimize();
            fail("Unexpected result");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }
}