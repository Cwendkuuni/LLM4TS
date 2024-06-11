package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.simple.SquaredErrorFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxEval;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() throws Exception {
        // Test with a simple linear optimization problem
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-2.0, 3.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, new double[]{1.0, 1.0}, Relationship.GREATER_EQUAL, 0.0));
        constraints.addConstraint(new LinearConstraint(1.0, new double[]{-1.0, 2.0}, Relationship.LESS_EQUAL, 0.0));
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize();
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 0.0);
        assertEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, result.getPoint(), 0.0);

        // Test with a more complex linear optimization problem
        SquaredErrorFunction errorFunction = new SquaredErrorFunction(new double[]{1.0, 1.0}, 2.0);
        LinearConstraintSet constraints2 = new LinearConstraintSet();
        constraints2.addConstraint(new LinearConstraint(1.0, new double[]{1.0, 1.0}, Relationship.GREATER_EQUAL, 0.0));
        constraints2.addConstraint(new LinearConstraint(1.0, new double[]{-1.0, 2.0}, Relationship.LESS_EQUAL, 0.0));
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{-2.0, 2.0}, 0.0);
        SimplexSolver solver2 = new SimplexSolver();
        PointValuePair result2 = solver2.doOptimize(new LinearOptimizationProblem(f2, constraints2));
        assertNotNull(result2);
        assertEquals(0.0, result2.getValue(), 0.0);
        assertEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, result2.getPoint(), 0.0);

        // Test with a problem that has no feasible solution
        LinearObjectiveFunction f3 = new LinearObjectiveFunction(new double[]{0.0, 1.0}, 0.0);
        LinearConstraintSet constraints3 = new LinearConstraintSet();
        constraints3.addConstraint(new LinearConstraint(1.0, new double[]{1.0, -1.0}, Relationship.LESS_EQUAL, 0.0));
        Solver solver3 = new SimplexSolver();
        try {
            solver3.doOptimize(new LinearOptimizationProblem(f3, constraints3));
            fail("An UnfeasibleSolutionException should have been thrown.");
        } catch (UnfeasibleSolutionException e) {
            // Expected
        }

        // Test with a problem that has no optimal solution
        LinearObjectiveFunction f4 = new LinearObjectiveFunction(new double[]{-2.0, 3.0}, 0.0);
        LinearConstraintSet constraints4 = new LinearConstraintSet();
        constraints4.addConstraint(new LinearConstraint(1.0, new double[]{1.0, 1.0}, Relationship.GREATER_EQUAL, 0.0));
        constraints4.addConstraint(new LinearConstraint(1.0, new double[]{-1.0, 2.0}, Relationship.LESS_EQUAL, 0.0));
        constraints4.addConstraint(new LinearConstraint(1.0, new double[]{1.0, 1.0}, Relationship.EQUAL, 1.0));
        Solver solver4 = new SimplexSolver();
        try {
            solver4.doOptimize(new LinearOptimizationProblem(f4, constraints4));
            fail("A NoFeasibleSolutionException or a UnboundedSolutionException should have been thrown.");
        } catch (Exception e) {
            // Expected
        }
    }
}