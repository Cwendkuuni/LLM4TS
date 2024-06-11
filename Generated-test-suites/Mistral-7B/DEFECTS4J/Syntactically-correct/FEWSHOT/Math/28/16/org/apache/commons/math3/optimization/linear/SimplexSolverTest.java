package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;

public class SimplexSolverTest {

    @Test
    public void testSolveSimpleExample() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0, 1.0}}, 3.0);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, -1.0}, 3.0),
            new LinearConstraint(new double[]{1.0, 1.0}, 2.0)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, constraints);
        assertEquals(new double[]{0.6, 0.4}, solution.getPoint(), 0.001);
        assertEquals(1.4142135625, solution.getValue(), 0.001);
    }

    @Test
    public void testSolveUnboundedExample() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0, 1.0}}, 3.0);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, Double.NEGATIVE_INFINITY),
            new LinearConstraint(new double[]{1.0, 1.0}, 2.0)
        };
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(f, constraints);
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testSolveInfeasibleExample() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0, 1.0}}, 3.0);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, -1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0}, 2.0)
        };
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(f, constraints);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testSolveOptimalExample() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0, 1.0}}, 3.0);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, -1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0}, 2.0),
            new LinearConstraint(new double[]{0.0, 1.0}, 1.0)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, constraints);
        assertEquals(new double[]{0.6, 0.4}, solution.getPoint(), 0.001);
        assertEquals(1.4142135625, solution.getValue(), 0.001);
    }

    @Test
    public void testSolveMaxIterationsExample()
        throws MaxCountExceededException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0, 1.0}}, 3.0);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, -1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0}, 2.0)
        };
        SimplexSolver solver = new SimplexSolver(1, 1);
        try {
            solver.optimize(f, constraints);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

}