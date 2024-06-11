package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testOptimizeSimpleExample() throws Exception {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{4.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0}, Relationship.LEQ, 2.0)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objFunction, constraints);
        assertEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 0.001);
        assertEquals(1.0 / 3.0, solution.getValue(), 0.001);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{0.0, 1.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0}, Relationship.LEQ, 2.0)
        };
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objFunction, constraints);
        fail("Expected UnboundedSolutionException to be thrown.");
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0}, Relationship.LEQ, 1.0)
        };
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objFunction, constraints);
        fail("Expected NoFeasibleSolutionException to be thrown.");
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{0.0, 0.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        solver.optimize(objFunction, constraints);
        fail("Expected MaxIterationsExceededException to be thrown.");
    }

    @Test
    public void testRestrictToNonNegative() throws Exception {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{4.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0}, Relationship.LEQ, 2.0)
        };
        SimplexSolver solver = new SimplexSolver(new SimpleBounds(0.0, Double.POSITIVE_INFINITY));
        PointValuePair solution = solver.optimize(objFunction, constraints);
        assertEquals(new double[]{0.0, 2.0}, solution.getPoint(), 0.001);
        assertEquals(4.0, solution.getValue(), 0.001);
    }
}