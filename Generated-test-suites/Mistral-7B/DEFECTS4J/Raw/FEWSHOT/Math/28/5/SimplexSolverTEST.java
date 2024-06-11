package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Test a simple example with a single constraint.
     * @throws Exception if the solver fails
     */
    @Test
    public void testSimpleExample() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertTrue(solution.isFeasible());
        assertTrue(solution.isOptimal());
        assertArrayEquals(new double[]{}, solution.getPrimal(), 0.0);
        assertArrayEquals(new double[]{-1.0}, solution.getDual(), 0.0);
    }

    /**
     * Test a simple example with a single constraint and a negative objective coefficient.
     * @throws Exception if the solver fails
     */
    @Test
    public void testNegativeObjective() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0}, new double[]{0.0});
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        PointValuePair solution = solver.doOptimize(objective, new ConstraintsSet());
        assertTrue(solution.isFeasible());
        assertTrue(solution.isOptimal());
        assertArrayEquals(new double[]{-1.0 / 2.0}, solution.getPrimal(), 1.0e-6);
        assertArrayEquals(new double[]{1.0 / 2.0}, solution.getDual(), 1.0e-6);
    }

    /**
     * Test a simple example with two constraints.
     * @throws Exception if the solver fails
     */
    @Test
    public void testTwoConstraints() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        PointValuePair solution = solver.doOptimize(objective, new ConstraintsSet(
                new LinearConstraint(0.0, new double[]{1.0, 2.0}, 2.0),
                new LinearConstraint(0.0, new double[]{1.0, 2.0}, 3.0)
        ));
        assertTrue(solution.isFeasible());
        assertTrue(solution.isOptimal());
        assertArrayEquals(new double[]{2.0 / 3.0, 1.0 / 3.0}, solution.getPrimal(), 1.0e-6);
        assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getDual(), 1.0e-6);
    }

    /**
     * Test a simple example with unbounded solution.
     * @throws Exception if the solver fails
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{0.0, 0.0});
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        solver.doOptimize(objective, new ConstraintsSet());
    }

    /**
     * Test a simple example with no feasible solution.
     * @throws Exception if the solver fails
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{0.0, 0.0});
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        solver.doOptimize(objective, new ConstraintsSet(
                new LinearConstraint(0.0, new double[]{1.0, 1.0}, 1.0)
        ));
    }

    /**
     * Test a simple example with max iterations exceeded.
     * @throws Exception if the solver fails
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
        SimplexSolver solver = new SimplexSolver(1.0e-6, 0);
        solver.doOptimize(objective, new ConstraintsSet());
    }
}