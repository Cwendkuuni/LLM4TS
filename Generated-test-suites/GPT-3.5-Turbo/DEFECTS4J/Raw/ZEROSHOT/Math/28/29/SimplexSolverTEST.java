import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        // Test case 1
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(new Integer(1), pivotColumn);

        // Test case 2
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(new Integer(0), pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        // Test case 1
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(2, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(1, 1, -4);
        tableau.setEntry(2, 1, -1);
        Integer pivotRow = solver.getPivotRow(tableau, 0);
        assertEquals(new Integer(1), pivotRow);

        // Test case 2
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(2, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(1, 1, 4);
        tableau.setEntry(2, 1, 1);
        pivotRow = solver.getPivotRow(tableau, 0);
        assertNull(pivotRow);
    }

    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(1, 2, 4);
        solver.doIteration(tableau);
        double[][] expectedTableau = {{1, 0, -2.0/3}, {0, 1, 5.0/3}};
        assertArrayEquals(expectedTableau, tableau.getTableau());
    }

    @Test(expected=UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolution() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(1, 1, -1);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(1, 2, 4);
        solver.doIteration(tableau);
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -1); // artificial variable
        tableau.setEntry(0, 1, 1);
        tableau.setEntry(0, 2, 1);
        tableau.setEntry(0, 3, 1);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(1, 2, 1);
        tableau.setEntry(2, 2, -2);
        tableau.setEntry(3, 0, 1); // artificial variable
        tableau.setEntry(3, 1, -1);
        tableau.setEntry(3, 2, 1);
        tableau.setEntry(3, 3, 1);
        try {
            solver.solvePhase1(tableau);
            assertEquals(3, tableau.getHeight());
            assertEquals(3, tableau.getWidth());
        } catch (NoFeasibleSolutionException e) {
            fail("NoFeasibleSolutionException thrown");
        } catch (UnboundedSolutionException e) {
            fail("UnboundedSolutionException thrown");
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException thrown");
        }
    }

    @Test(expected=NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -1); // artificial variable
        tableau.setEntry(0, 1, 1);
        tableau.setEntry(0, 2, 1);
        tableau.setEntry(0, 3, 2);
        solver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        double[] coefficients = {2, 3, 4};
        LinearObjectiveFunction function = new LinearObjectiveFunction(coefficients, 0);
        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
        // 2x1 + 3x2 + 4x3 <= 8
        constraints.add(new LinearConstraint(new double[]{2, 3, 4}, Relationship.LEQ, 8));
        // x1 + x2 <= 4
        constraints.add(new LinearConstraint(new double[]{1, 1, 0}, Relationship.LEQ, 4));
        // x2 + x3 <= 2
        constraints.add(new LinearConstraint(new double[]{0, 1, 1}, Relationship.LEQ, 2));
        try {
            PointValuePair solution = solver.optimize(function, constraints, GoalType.MAXIMIZE, true);
            double[] expectedPoint = {0, 2, 2};
            assertArrayEquals(expectedPoint, solution.getPoint(), solver.getEpsilon());
        } catch (MaxCountExceededException e) {
            fail("MaxCountExceededException thrown");
        } catch (UnboundedSolutionException e) {
            fail("UnboundedSolutionException thrown");
        } catch (NoFeasibleSolutionException e) {
            fail("NoFeasibleSolutionException thrown");
        }
    }

}