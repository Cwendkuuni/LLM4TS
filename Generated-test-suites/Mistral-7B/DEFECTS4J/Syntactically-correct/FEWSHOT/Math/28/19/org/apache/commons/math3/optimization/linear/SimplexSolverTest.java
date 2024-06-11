package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;

import java.util.Arrays;

public class SimplexSolverTest {

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1.0, -2.0, -3.0}, new PointValuePair(0, 0)),
                new LinearConstraint[]{},
                LinearOptimizer.GOAL_TYPE_MINIMIZE,
                true,
                1e-6,
                10);

        SimplexSolver solver = new SimplexSolver();
        try {
            solver.solvePhase1(tableau);
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // Expected
        }

        tableau.setObjectiveFunction(new LinearObjectiveFunction(new double[]{-1.0, -1.0, -1.0}, new PointValuePair(0, 0)));
        tableau.setConstraints(new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0, 1.0}, 0.0),
                new LinearConstraint(new double[]{-2.0, 1.0}, 0.0),
                new LinearConstraint(new double[]{-3.0, 1.0}, 0.0),
                new LinearConstraint(new double[]{0.0, 1.0}, 4.0),
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0)});

        solver.solvePhase1(tableau);
        assertArrayEquals(new int[]{0, 2, 1}, tableau.getBasicVariables());
        assertEquals(2.0, tableau.getEntry(0, 3), solver.epsilon);
        assertEquals(-1.0 / 3.0, tableau.getEntry(1, 3), solver.epsilon);
        assertEquals(-1.0 / 3.0, tableau.getEntry(2, 3), solver.epsilon);

        // Test with goal type MAXIMIZE
        tableau.setGoalType(LinearOptimizer.GOAL_TYPE_MAXIMIZE);
        tableau.setObjectiveFunction(new LinearObjectiveFunction(new double[]{1.0, 2.0, 3.0}, new PointValuePair(0, 0)));
        solver.solvePhase1(tableau);
        assertArrayEquals(new int[]{1, 0, 2}, tableau.getBasicVariables());
        assertEquals(-2.0 / 3.0, tableau.getEntry(0, 3), solver.epsilon);
        assertEquals(-1.0 / 3.0, tableau.getEntry(1, 3), solver.epsilon);
        assertEquals(-1.0 / 3.0, tableau.getEntry(2, 3), solver.epsilon);
    }

    @Test
    public void testSolve() {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1.0, -2.0, -3.0}, new PointValuePair(0, 0)),
                new LinearConstraint[]{},
                LinearOptimizer.GOAL_TYPE_MINIMIZE,
                true,
                1e-6,
                10);

        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize();
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // Expected
        }

        tableau.setObjectiveFunction(new LinearObjectiveFunction(new double[]{-1.0, -1.0, -1.0}, new PointValuePair(0, 0)));
        tableau.setConstraints(new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0, 1.0}, 0.0),
                new LinearConstraint(new double[]{-2.0, 1.0}, 0.0),
                new LinearConstraint(new double[]{-3.0, 1.0}, 0.0),
                new LinearConstraint(new double[]{0.0, 1.0}, 4.0),
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0)});

        PointValuePair solution = solver.doOptimize();
        assertEquals(2.0, solution.getValue(), Precision.safeEpsilon(0.0));
        assertArrayEquals(new double[]{2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), solver.epsilon);

        // Test with goal type MAXIMIZE
        tableau.setGoalType(LinearOptimizer.GOAL_TYPE_MAXIMIZE);
        tableau.setObjectiveFunction(new LinearObjectiveFunction(new double[]{1.0, 2.0, 3.0}, new PointValuePair(0, 0)));
        solution = solver.doOptimize();
        assertEquals(12.0, solution.getValue(), Precision.safeEpsilon(0.0));
        assertArrayEquals(new double[]{1.0, 1.0, 1.0}, solution.getPoint(), solver.epsilon);
    }

    @Test
    public void testMaxIterations() {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1.0, -2.0, -3.0}, new PointValuePair(0, 0)),
                new LinearConstraint[]{},
                LinearOptimizer.GOAL_TYPE_MINIMIZE,
                true,
                1e-6,
                1);

        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize();
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }
}