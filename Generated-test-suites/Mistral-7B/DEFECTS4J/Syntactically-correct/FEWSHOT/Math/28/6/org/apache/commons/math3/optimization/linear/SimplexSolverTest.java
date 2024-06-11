package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import static org.apache.commons.math3.util.Precision.*;

import java.util.List;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;

public class SimplexSolverTest {

    @Test
    public void testSolveSimpleProblem() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, 5.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), null));
        constraints.addConstraint(new LinearConstraint(2.0, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), null));

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(function, constraints, GoalType.MINIMIZE);
        assertEquals(new double[]{2.0, 1.0}, solution.getPoint(), 1e-6);
        assertEquals(5.0, solution.getValue(), 1e-6);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, 5.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver(1, Integer.MAX_VALUE);
        solver.optimize(function, constraints, GoalType.MINIMIZE);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {0.0, 0.0}}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), null));
        constraints.addConstraint(new LinearConstraint(2.0, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), null));

        SimplexSolver solver = new SimplexSolver();
        solver.optimize(function, constraints, GoalType.MINIMIZE);
    }

    @Test
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-1.0, 1.0}}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), null));
        constraints.addConstraint(new LinearConstraint(2.0, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), null));

        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(function, constraints, GoalType.MINIMIZE);
            fail();
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testOptimalSolution() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, 2.0, 1.0}, {3.0, 4.0, 1.0}}, 12.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, new SimpleBounds(0.0, 3.0), null));
        constraints.addConstraint(new LinearConstraint(2.0, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), null));
        constraints.addConstraint(new LinearConstraint(3.0, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), null));

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(function, constraints, GoalType.MINIMIZE);
        assertEquals(new double[]{1.0, 2.0, 1.0}, solution.getPoint(), 1e-6);
        assertEquals(12.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testOptimalSolutionWithInequalityConstraints() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, 2.0, 1.0}, {3.0, 4.0, 1.0}}, 12.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, new SimpleBounds(0.0, 3.0), null));
        constraints.addConstraint(new LinearConstraint(2.0, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), null));
        constraints.addConstraint(new LinearConstraint(3.0, new SimpleBounds(Double.NEGATIVE_INFINITY, 0.0), null));

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(function, constraints, GoalType.MINIMIZE);
        assertEquals(new double[]{1.0, 2.0, 1.0}, solution.getPoint(), 1e-6);
        assertEquals(12.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testOptimalSolutionWithBounds() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, 5.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, new SimpleBounds(0.0, 1.0), null));
        constraints.addConstraint(new LinearConstraint(2.0, new SimpleBounds(1.0, 2.0), null));

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(function, constraints, GoalType.MINIMIZE);
        assertEquals(new double[]{1.0, 1.0}, solution.getPoint(), 1e-6);
        assertEquals(5.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testOptimalSolutionWithNonNegativeConstraints() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, 2.0, 1.0}, {3.0, 4.0, 1.0}}, 12.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, SimpleBounds.UNBOUNDED, SimpleBounds.UNBOUNDED, true));
        constraints.addConstraint(new LinearConstraint(2.0, SimpleBounds.UNBOUNDED, SimpleBounds.UNBOUNDED, true));
        constraints.addConstraint(new LinearConstraint(3.0, SimpleBounds.UNBOUNDED, SimpleBounds.UNBOUNDED, true));

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(function, constraints, GoalType.MINIMIZE);
        assertEquals(new double[]{1.0, 2.0, 1.0}, solution.getPoint(), 1e-6);
        assertEquals(12.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testOptimalSolutionWithDuplicateConstraints() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, 2.0, 1.0}, {3.0, 4.0, 1.0}}, 12.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, new SimpleBounds(0.0, 3.0), null));
        constraints.addConstraint(new LinearConstraint(1.0, new SimpleBounds(0.0, 3.0), null));
        constraints.addConstraint(new LinearConstraint(2.0, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), null));
        constraints.addConstraint(new LinearConstraint(3.0, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), null));

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(function, constraints, GoalType.MINIMIZE);
        assertEquals(new double[]{1.0, 2.0, 1.0}, solution.getPoint(), 1e-6);
        assertEquals(12.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testOptimalSolutionWithDuplicateObjectiveFunctionEntries() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, 2.0, 1.0}, {3.0, 4.0, 1.0}, {1.0, 2.0, 1.0}}, 12.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, new SimpleBounds(0.0, 3.0), null));
        constraints.addConstraint(new LinearConstraint(2.0, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), null));
        constraints.addConstraint(new LinearConstraint(3.0, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), null));

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(function, constraints, GoalType.MINIMIZE);
        assertEquals(new double[]{1.0, 2.0, 1.0}, solution.getPoint(), 1e-6);
        assertEquals(12.0, solution.getValue(), 1e-6);
    }
}