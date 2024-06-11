package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSimpleExample() throws NoFeasibleSolutionException, UnboundedSolutionException, MaxCountExceededException {
        LinearConstraint[] constraints = {
            new LinearConstraint(1d, 1d, 1d, 5d),
            new LinearConstraint(1d, 1d, -1d, 3d),
            new LinearConstraint(2d, -1d, 1d, 5d),
            new LinearConstraint(2d, -1d, -1d, 3d),
            new LinearConstraint(3d, 1d, -1d, 1d),
            new LinearConstraint(3d, 1d, 1d, 1d)
        };
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1d, -1d, 3d, 2d, 1d, 1d});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objective, constraints);
        Assert.assertEquals(1d, solution.getPoint()[0], 0.001);
        Assert.assertEquals(1d, solution.getPoint()[1], 0.001);
        Assert.assertEquals(4d, solution.getValue(), 0.001);
    }

    @Test
    public void testFeasibleSolution() throws NoFeasibleSolutionException, UnboundedSolutionException, MaxCountExceededException {
        LinearConstraint[] constraints = {
            new LinearConstraint(1d, 1d, 1d, 5d),
            new LinearConstraint(1d, 1d, -1d, 3d),
            new LinearConstraint(2d, -1d, 1d, 5d),
            new LinearConstraint(2d, -1d, -1d, 3d),
            new LinearConstraint(3d, 1d, -1d, 1d),
            new LinearConstraint(3d, 1d, 1d, 1d)
        };
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1d, -1d, 2d, 2d, 1d, 1d});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objective, constraints);
        Assert.assertEquals(1.5d, solution.getPoint()[0], 0.001);
        Assert.assertEquals(1.5d, solution.getPoint()[1], 0.001);
        Assert.assertEquals(3.5d, solution.getValue(), 0.001);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws NoFeasibleSolutionException, UnboundedSolutionException, MaxCountExceededException {
        LinearConstraint[] constraints = {
            new LinearConstraint(1d, 1d, 1d, 5d),
            new LinearConstraint(1d, 1d, -1d, 3d),
            new LinearConstraint(2d, -1d, 1d, 5d),
            new LinearConstraint(2d, -1d, -1d, 3d),
            new LinearConstraint(3d, 1d, -1d, 1d),
            new LinearConstraint(3d, 1d, 1d, 1d),
            new LinearConstraint(4d, 1d, 1d, 1d)
        };
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1d, -1d, 3d, 2d, 1d, 1d});
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objective, constraints);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws NoFeasibleSolutionException, UnboundedSolutionException, MaxCountExceededException {
        LinearConstraint[] constraints = {
            new LinearConstraint(1d, 1d, 1d, 5d),
            new LinearConstraint(1d, 1d, -1d, 3d),
            new LinearConstraint(2d, -1d, 1d, 5d),
            new LinearConstraint(2d, -1d, -1d, 3d),
            new LinearConstraint(3d, 1d, 1d, 1d)
        };
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1d, -1d, 3d, 2d, 1d, 1d});
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objective, constraints);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() throws NoFeasibleSolutionException, UnboundedSolutionException, MaxCountExceededException {
        LinearConstraint[] constraints = {
            new LinearConstraint(1d, 1d, 1d, 5d),
            new LinearConstraint(1d, 1d, -1d, 3d),
            new LinearConstraint(2d, -1d, 1d, 5d),
            new LinearConstraint(2d, -1d, -1d, 3d),
            new LinearConstraint(3d, 1d, -1d, 1d),
            new LinearConstraint(3d, 1d, 1d, 1d),
            new LinearConstraint(4d, 1d, 1d, 1d),
            new LinearConstraint(5d, 1d, 1d, 1d),
            new LinearConstraint(6d, 1d, 1d, 1d),
            new LinearConstraint(7d, 1d, 1d, 1d),
            new LinearConstraint(8d, 1d, 1d, 1d)
        };
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1d, -1d, 3d, 2d, 1d, 1d});
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(10); // Set max iterations to a low number to trigger this exception
        solver.optimize(objective, constraints);
    }

}