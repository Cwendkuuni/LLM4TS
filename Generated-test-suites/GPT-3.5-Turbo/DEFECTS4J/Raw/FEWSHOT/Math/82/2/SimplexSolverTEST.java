import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.optimizers.LinearObjectiveFunction;
import org.apache.commons.math.optimization.optimizers.linear.LinearConstraint;
import org.apache.commons.math.optimization.optimizers.linear.Relationship;
import org.junit.Test;

public class SimplexSolverTest {
    @Test
    public void testGetPivotColumn() {
        // Create a SimplexSolver instance
        SimplexSolver solver = new SimplexSolver();

        // Create a SimplexTableau
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{1, -2}, 0),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relationship.GEQ, 2),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 4)
                },
                GoalType.MAXIMIZE,
                false,
                1.0e-6
        );

        // Call the getPivotColumn method
        Integer pivotColumn = solver.getPivotColumn(tableau);

        // Assert the result
        assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        // Create a SimplexSolver instance
        SimplexSolver solver = new SimplexSolver();

        // Create a SimplexTableau
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{1, -2}, 0),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relationship.GEQ, 2),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 4)
                },
                GoalType.MAXIMIZE,
                false,
                1.0e-6
        );

        // Call the getPivotRow method
        Integer pivotRow = solver.getPivotRow(1, tableau);

        // Assert the result
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testIsPhase1Solved() {
        // Create a SimplexSolver instance
        SimplexSolver solver = new SimplexSolver();

        // Create a SimplexTableau
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{1, -2}, 0),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relationship.GEQ, 2),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 4)
                },
                GoalType.MAXIMIZE,
                false,
                1.0e-6
        );

        // Call the isPhase1Solved method
        boolean phase1Solved = solver.isPhase1Solved(tableau);

        // Assert the result
        assertEquals(false, phase1Solved);
    }

    @Test
    public void testIsOptimal() {
        // Create a SimplexSolver instance
        SimplexSolver solver = new SimplexSolver();

        // Create a SimplexTableau
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{1, -2}, 0),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relationship.GEQ, 2),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 4)
                },
                GoalType.MAXIMIZE,
                false,
                1.0e-6
        );

        // Call the isOptimal method
        boolean optimal = solver.isOptimal(tableau);

        // Assert the result
        assertEquals(false, optimal);
    }

    @Test
    public void testDoOptimize() {
        // Create a SimplexSolver instance
        SimplexSolver solver = new SimplexSolver();

        // Create a SimplexTableau
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{1, -2}, 0),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{1, 1}, Relationship.GEQ, 2),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 4)
                },
                GoalType.MAXIMIZE,
                false,
                1.0e-6
        );

        try {
            // Call the doOptimize method
            RealPointValuePair solution = solver.doOptimize();

            // Assert the result
            assertEquals(2.0, solution.getPoint()[0], 1.0e-6);
            assertEquals(0.0, solution.getPoint()[1], 1.0e-6);
            assertEquals(-4.0, solution.getValue(), 1.0e-6);
        } catch (OptimizationException e) {
            fail("OptimizationException should not be thrown");
        }
    }
}