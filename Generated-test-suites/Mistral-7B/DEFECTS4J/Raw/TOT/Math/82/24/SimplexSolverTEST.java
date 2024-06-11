package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_SimpleExample() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1, 2}, {3, 4}}, new double[]{-5, -2});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[] {1}, Relationship.LEQ, 5),
            new LinearConstraint(new double[] {2}, Relationship.LEQ, 3),
            new LinearConstraint(new double[] {1, 2}, Relationship.EQ, 3)
        };
        SimplexSolver solver = new SimplexSolver(1e-6);
        RealPointValuePair solution = solver.doOptimize();
        PointValuePair expectedSolution = new PointValuePair(new double[] {1.5, 1.5}, -3.5);
        assertEquals(expectedSolution.getValue(), solution.getValue(), 1e-6);
        assertArrayEquals(expectedSolution.getPoint(), solution.getPoint(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_UnboundedSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1, 2}, {3, 4}}, new double[]{-5, -2});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[] {1}, Relationship.LEQ, 5),
            new LinearConstraint(new double[] {2}, Relationship.LEQ, 3)
        };
        SimplexSolver solver = new SimplexSolver(1e-6);
        solver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_NoFeasibleSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1, 2}, {3, 4}}, new double[]{-5, -2});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[] {1}, Relationship.LEQ, 5),
            new LinearConstraint(new double[] {2}, Relationship.LEQ, 3),
            new LinearConstraint(new double[] {1, 2}, Relationship.EQ, 6)
        };
        SimplexSolver solver = new SimplexSolver(1e-6);
        solver.doOptimize();
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testDoOptimize_MaxIterationsExceeded() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1, 2}, {3, 4}}, new double[]{-5, -2});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver(1e-6);
        solver.setMaxIterations(0);
        solver.doOptimize();
    }
}