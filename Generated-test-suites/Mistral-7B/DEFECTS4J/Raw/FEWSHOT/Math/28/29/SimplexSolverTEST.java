package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Test;

/**
 * Test cases for SimplexSolver class.
 */
public class SimplexSolverTest {

    /**
     * Test case for solving a simple linear optimization problem.
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testSimpleLinearOptimization()
        throws Exception {

        // Define a simple linear optimization problem.
        double[][] constraints = {{1.0, 2.0, -1.0}, {2.0, 1.0, -1.0}};
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(1.0, new double[]{-3.0, -2.0});
        SimplexSolver solver = new SimplexSolver();

        // Solve the problem.
        PointValuePair solution = solver.optimize(objectiveFunction, constraints);

        // Check the solution.
        assertNotNull(solution);
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 1e-6);
    }

    /**
     * Test case for solving a linear optimization problem with unbounded solution.
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution()
        throws Exception {

        // Define a linear optimization problem with an unbounded solution.
        double[][] constraints = {{1.0, 2.0, -1.0}, {2.0, 1.0, -1.0}};
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(1.0, new double[]{-3.0, -2.0});
        SimplexSolver solver = new SimplexSolver();

        // Solve the problem.
        solver.optimize(objectiveFunction, constraints);

        // The solution should not be returned.
    }

    /**
     * Test case for solving a linear optimization problem with no feasible solution.
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution()
        throws Exception {

        // Define a linear optimization problem with no feasible solution.
        double[][] constraints = {{1.0, 2.0, -1.0}, {2.0, 1.0, -1.0}, {-1.0, -1.0, 0.0}};
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(1.0, new double[]{-3.0, -2.0});
        SimplexSolver solver = new SimplexSolver();

        // Solve the problem.
        solver.optimize(objectiveFunction, constraints);

        // The solution should not be returned.
    }

    /**
     * Test case for solving a linear optimization problem with a maximal iteration count exceeded.
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsExceeded()
        throws Exception {

        // Define a linear optimization problem that requires an excessive number of iterations to solve.
        double[][] constraints = new double[100000][3];
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(1.0, new double[]{0.0, 0.0});
        SimplexSolver solver = new SimplexSolver(1e-12, 50);

        // Solve the problem.
        solver.optimize(objectiveFunction, constraints);

        // The solution should not be returned.
    }
}