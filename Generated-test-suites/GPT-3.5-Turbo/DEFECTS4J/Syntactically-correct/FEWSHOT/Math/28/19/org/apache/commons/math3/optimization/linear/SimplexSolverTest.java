
package org.apache.commons.math3.optimization.linear;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SimplexSolverTest {
    private static final double EPSILON = 1e-6;
    private static final int MAX_ULPS = 10;

    @Test
    public void testGetPivotColumn() {
        double[][] tableauData = {
            {1, 0, 0, 0, 0, 0},
            {-1, 1, 1, 0, 0, 0},
            {1, -1, 0, 1, 0, 0},
            {0, 1, 0, 0, 1, 2}
        };
        SimplexTableau tableau = createTableau(tableauData);

        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);

        assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        double[][] tableauData = {
            {0, 1, 0, 0, 0, 5},
            {1, -1, 1, 0, 0, 2},
            {2, 1, 0, 1, 0, 6},
            {3, 0, 0, 0, 1, 7}
        };
        SimplexTableau tableau = createTableau(tableauData);

        Integer pivotRow = new SimplexSolver().getPivotRow(tableau, 1);

        assertEquals(2, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() throws UnboundedSolutionException, MaxCountExceededException {
        double[][] tableauData = {
            {1, 0, 0, 0, 0, 0},
            {-1, 1, 1, 0, 0, 1},
            {1, -1, 0, 1, 0, 1},
            {0, 1, 0, 0, 1, 2}
        };
        SimplexTableau tableau = createTableau(tableauData);

        new SimplexSolver().doIteration(tableau);

        assertArrayEquals(new double[]{1, 0, 0, 0, 0, 0}, tableau.getRow(0), EPSILON);
        assertArrayEquals(new double[]{0, 0, 1, 0, -1, 1}, tableau.getRow(1), EPSILON);
        assertArrayEquals(new double[]{0, 1, -1, 1, 1, 1}, tableau.getRow(2), EPSILON);
        assertArrayEquals(new double[]{0, 0, 1, 0, 1, 2}, tableau.getRow(3), EPSILON);
    }

    @Test
    public void testSolvePhase1() throws NoFeasibleSolutionException, MaxCountExceededException, UnboundedSolutionException {
        double[][] tableauData = {
            {1, 0, 0, 0, 0, 0, 1.0},
            {1, -1, 1, 0, 0, 0, 1.0},
            {2, 1, 0, 1, 0, 0, 2.0},
            {3, 0, 0, 0, 1, 0, 3.0},
            {0, 1, 0, 0, 0, 1, 4.1}
        };
        SimplexTableau tableau = createTableau(tableauData);

        new SimplexSolver().solvePhase1(tableau);

        assertArrayEquals(new double[]{0, 0, 0, 0, 0, 0, 0}, tableau.getRow(0), EPSILON);
        assertArrayEquals(new double[]{1, 0, 0, 0, 0, 0, 1}, tableau.getRow(1), EPSILON);
        assertArrayEquals(new double[]{0, 1, 0, 0, 0, 0, 1}, tableau.getRow(2), EPSILON);
        assertArrayEquals(new double[]{0, 0, 1, 0, 0, 0, 2}, tableau.getRow(3), EPSILON);
        assertArrayEquals(new double[]{0, 0, 0, 1, 0, 0, 3}, tableau.getRow(4), EPSILON);
        assertArrayEquals(new double[]{0, 0, 0, 0, 1, 0, 4.1}, tableau.getRow(5), EPSILON);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Define the optimization problem
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{3, 2}, 0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new double[]{1, 0}, Relationship.GEQ, 0));
        constraints.add(new LinearConstraint(new double[]{0, 1}, Relationship.GEQ, 0));
        constraints.add(new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 4));

        // Solve the problem
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objective, constraints, GoalType.MAXIMIZE);

        // Verify the solution
        assertArrayEquals(new double[]{2, 2}, solution.getPoint(), EPSILON);
        assertEquals(10, solution.getValue(), EPSILON);
    }

    private SimplexTableau createTableau(double[][] tableauData) {
        int numVariables = tableauData[0].length;
        int numConstraints = tableauData.length - 1;
        SimplexTableau tableau = new SimplexTableau(numConstraints, numVariables);

        for (int i = 0; i < tableauData.length; i++) {
            tableau.setRow(i, tableauData[i]);
        }

        return tableau;
    }
}
