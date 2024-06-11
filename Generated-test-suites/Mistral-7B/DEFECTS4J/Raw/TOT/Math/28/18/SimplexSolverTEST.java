package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnboundedSolutionException;
import org.apache.commons.math3.optim.MaxCountExceededException;
import org.apache.commons.math3.optim.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxEval;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstraints() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0}, new double[]{1.0});
        SimplexSolver solver = new SimplexSolver();
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint emptyConstraint = new LinearConstraint(new double[]{1.0}, 0.0);
        constraints.addConstraint(emptyConstraint);
        PointValuePair result = solver.optimize(f, constraints, null, null);
        assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 0.0);
        assertEquals(new double[]{Double.POSITIVE_INFINITY}, result.getPoint(), 0.0);
    }

    @Test
    public void testOneConstraint() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0}, new double[]{1.0});
        SimplexSolver solver = new SimplexSolver();
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0, 1.0}, 0.0);
        constraints.addConstraint(constraint);
        PointValuePair result = solver.optimize(f, constraints, null, null);
        assertEquals(-3.0, result.getValue(), 0.01);
        assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, result.getPoint(), 0.01);
    }

    @Test
    public void testMultipleConstraints() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0, 1.0}, new double[]{1.0, 1.0, 1.0});
        SimplexSolver solver = new SimplexSolver();
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 0.0);
        constraints.addConstraint(constraint1);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, -1.0}, 0.0);
        constraints.addConstraint(constraint2);
        PointValuePair result = solver.optimize(f, constraints, null, null);
        assertEquals(-3.0 / 2.0, result.getValue(), 0.01);
        assertArrayEquals(new double[]{1.0 / 2.0, -1.0 / 2.0, 1.0 / 2.0}, result.getPoint(), 0.01);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsReached() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, -1.0}, new double[]{1.0, 1.0});
        SimplexSolver solver = new SimplexSolver(0, 0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0, 1.0}, 0.0);
        constraints.addConstraint(constraint);
        solver.setMaxIterations(0);
        solver.optimize(f, constraints, null, null);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0.0}, new double[]{1.0});
        SimplexSolver solver = new SimplexSolver();
        LinearConstraintSet constraints = new LinearConstraintSet();
        PointValuePair result = solver.optimize(f, constraints, null, null);
        fail("Unbounded solution should not be returned");
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 1.0});
        SimplexSolver solver = new SimplexSolver();
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0, 1.0}, 0.0);
        constraints.addConstraint(constraint);
        solver.optimize(f, constraints, null, null);
    }
}