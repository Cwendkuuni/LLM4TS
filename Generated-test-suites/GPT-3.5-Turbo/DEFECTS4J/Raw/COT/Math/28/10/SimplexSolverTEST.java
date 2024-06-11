import org.junit.*;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {

        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        // Test case 1
        tableau.setData(new double[][]{{1, 2, -3}, {4, 5, 6}, {7, 8, 9}});
        Integer pivotCol = solver.getPivotColumn(tableau);
        assertEquals(new Integer(2), pivotCol);

        // Test case 2
        tableau.setData(new double[][]{{-1, -2, -3}, {-4, -5, -6}, {-7, -8, -9}});
        pivotCol = solver.getPivotColumn(tableau);
        assertEquals(new Integer(0), pivotCol);

        // Test case 3
        tableau.setData(new double[][]{{-1, -2, -3}, {4, 5, 6}, {-7, -8, -9}});
        pivotCol = solver.getPivotColumn(tableau);
        assertEquals(new Integer(0), pivotCol);
    }

    @Test
    public void testGetPivotRow() {

        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        // Test case 1
        tableau.setData(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        Integer pivotRow = solver.getPivotRow(tableau, 2);
        assertEquals(new Integer(1), pivotRow);

        // Test case 2
        tableau.setData(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, -9}});
        pivotRow = solver.getPivotRow(tableau, 2);
        assertEquals(new Integer(2), pivotRow);

        // Test case 3
        tableau.setData(new double[][]{{1, 2, 3}, {4, 5, 6}, {-7, -8, 9}});
        pivotRow = solver.getPivotRow(tableau, 2);
        assertEquals(new Integer(0), pivotRow);
    }

    @Test
    public void testDoIteration() throws Exception {

        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        // Test case 1
        tableau.setData(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        solver.doIteration(tableau);
        double[][] expected1 = {{1, 0, -1}, {-4, 5, 6}, {-7, 8, 9}};
        double[][] actual1 = tableau.getData();
        assertArrayEquals(expected1, actual1);

        // Test case 2
        tableau.setData(new double[][]{{1, 2, 3}, {4, 5, -6}, {7, 8, 9}});
        solver.doIteration(tableau);
        double[][] expected2 = {{1, 2, 3}, {4, -3, -6}, {7, 8, 9}};
        double[][] actual2 = tableau.getData();
        assertArrayEquals(expected2, actual2);

        // Test case 3
        tableau.setData(new double[][]{{1, 2, 3}, {4, -5, -6}, {-7, 8, 9}});
        solver.doIteration(tableau);
        double[][] expected3 = {{1, 2, 3}, {4, -4, -6}, {7, 0, 9}};
        double[][] actual3 = tableau.getData();
        assertArrayEquals(expected3, actual3);
    }

    @Test
    public void testSolvePhase1() throws Exception {

        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        // Test case 1
        tableau.setData(new double[][]{{-1, -2, -3}, {-4, -5, -6}, {-7, -8, -9}});
        try {
            solver.solvePhase1(tableau);
            fail("NoFeasibleSolutionException should be thrown");
        } catch (NoFeasibleSolutionException e) {
            // Expected exception
        }

        // Test case 2
        tableau.setData(new double[][]{{-1, 2, 3}, {-4, 5, 6}, {-7, 8, 9}});
        solver.solvePhase1(tableau);
        double[][] expected2 = {{Decimal.valueOf(5).divide(Decimal.valueOf(8)), Decimal.valueOf(-2).divide(Decimal.valueOf(8)), Decimal.valueOf(-3).divide(Decimal.valueOf(8))}, {-7, 8, 9}};
        double[][] actual2 = tableau.getData();
        assertArrayEquals(expected2, actual2);

        // Test case 3
        tableau.setData(new double[][]{{-1, -2, 3}, {4, 5, 6}, {-7, -8, 9}});
        solver.solvePhase1(tableau);
        double[][] expected3 = {{Decimal.valueOf(2).divide(Decimal.valueOf(9)), Decimal.valueOf(4).divide(Decimal.valueOf(9)), Decimal.valueOf(8).divide(Decimal.valueOf(9))}, {4, 5, BigDecimal.ZERO}};
        double[][] actual3 = tableau.getData();
        assertArrayEquals(expected3, actual3);
    }

    @Test
    public void testDoOptimize() throws Exception {

        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        // Test case 1
        tableau.setData(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        PointValuePair result = solver.doOptimize();
        assertEquals(new PointValuePair(new double[]{0, 0, 0}, 0), result);

        // Test case 2
        tableau.setData(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, -9}});
        try {
            solver.doOptimize();
            fail("NoFeasibleSolutionException should be thrown");
        } catch (NoFeasibleSolutionException e) {
            // Expected exception
        }

        // Test case 3
        tableau.setData(new double[][]{{1, -2, 3}, {4, 5, 6}, {7, 8, 9}});
        result = solver.doOptimize();
        double[] expected3 = {Decimal.valueOf(5).divide(Decimal.valueOf(8)), Decimal.valueOf(0).divide(Decimal.valueOf(8)), Decimal.valueOf(-3).divide(Decimal.valueOf(8))};
        assertArrayEquals(expected3, result.getPoint(), 0);

    }

}