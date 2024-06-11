package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link SimplexSolver} class.
 */
public class SimplexSolverTest {

    private LinearObjectiveFunction f;
    private LinearConstraint[] c;
    private SimplexSolver solver;

    @Before
    public void setUp() {
        f = new LinearObjectiveFunction(new double[]{1, 2}, new double[]{-3, -7});
        c = new LinearConstraint[]{
            new LinearConstraint(new double[]{1, 1}, 2),
            new LinearConstraint(new double[]{1, -1}, 2),
            new LinearConstraint(new double[]{1, 1}, 3),
            new LinearConstraint(new double[]{-1, 1}, 3),
            new LinearConstraint(new double[]{-1, -1}, 0)
        };
        solver = new SimplexSolver();
    }

    /**
     * Tests the optimization of a simple problem with two variables.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimpleProblem() throws Exception {
        PointValuePair solution = solver.optimize(f, c, null, null, null, null);
        Assert.assertEquals(new double[]{0, 2 / 3}, solution.getPoint(), 1e-9);
        Assert.assertEquals(-11 / 3, solution.getValue(), 1e-9);
    }

    /**
     * Tests the optimization of a problem with unbounded solution.
     * @throws Exception if an error occurs
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction fUnbounded = new LinearObjectiveFunction(new double[]{1}, new double[]{0});
        SimplexSolver solverUnbounded = new SimplexSolver(1e-5, 10);
        solverUnbounded.optimize(fUnbounded, null, null, null, null, null);
    }

    /**
     * Tests the optimization of a problem with infeasible solution.
     * @throws Exception if an error occurs
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testInfeasibleSolution() throws Exception {
        LinearObjectiveFunction fInfeasible = new LinearObjectiveFunction(new double[]{1, 1}, new double[]{-2, -2});
        SimplexSolver solverInfeasible = new SimplexSolver(1e-5, 10);
        solverInfeasible.optimize(fInfeasible, c, null, null, null, null);
    }

    /**
     * Tests the optimization of a problem with a maximal number of iterations.
     * @throws Exception if an error occurs
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws Exception {
        SimplexSolver solverMaxIterations = new SimplexSolver(1e-5, 10);
        solverMaxIterations.setMaxIterations(2);
        solverMaxIterations.optimize(f, c, null, null, null, null);
    }

}