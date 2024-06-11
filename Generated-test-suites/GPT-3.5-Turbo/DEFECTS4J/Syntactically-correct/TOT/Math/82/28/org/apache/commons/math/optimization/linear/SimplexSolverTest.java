import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {
  
    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    // Test getPivotColumn method
    @Test
    public void testGetPivotColumn() {
        double[][] matrix = {{1, 2, -1},
                             {3, 4, 2},
                             {5, 6, 3}};
        SimplexTableau tableau = new SimplexTableau(matrix);
        Integer pivotCol = solver.getPivotColumn(tableau);
        assertEquals(new Integer(2), pivotCol);
    }

    // Test getPivotRow method
    @Test
    public void testGetPivotRow() {
        double[][] matrix = {{1, 2, -1},
                             {3, 4, 2},
                             {5, 6, 3}};
        SimplexTableau tableau = new SimplexTableau(matrix);
        Integer pivotRow = solver.getPivotRow(2, tableau);
        assertEquals(new Integer(1), pivotRow);
    }

    // Test doIteration method
    @Test
    public void testDoIteration() throws OptimizationException {
        double[][] matrix = {{1, 2, -1},
                             {3, 4, 2},
                             {5, 6, 3}};
        SimplexTableau tableau = new SimplexTableau(matrix);
        solver.doIteration(tableau);
        double[][] expected = {{1, 0, -2},
                               {0, 1, 1},
                               {2, 0, 4}};
        assertArrayEquals(expected, tableau.getMatrix());
    }

    // Test isPhase1Solved method
    @Test
    public void testIsPhase1Solved() {
        double[][] matrix = {{1, 2, -1},
                             {3, 4, 2},
                             {5, 6, 3}};
        SimplexTableau tableau = new SimplexTableau(matrix);
        boolean phase1Solved = solver.isPhase1Solved(tableau);
        assertFalse(phase1Solved);
    }

    // Test isOptimal method
    @Test
    public void testIsOptimal() {
        double[][] matrix = {{1, 2, -1},
                             {3, 4, 2},
                             {5, 6, 3}};
        SimplexTableau tableau = new SimplexTableau(matrix);
        boolean optimal = solver.isOptimal(tableau);
        assertFalse(optimal);
    }

    // Test solvePhase1 method
    @Test
    public void testSolvePhase1() throws OptimizationException {
        double[][] matrix = {{1, 2, -1},
                             {3, 4, 2},
                             {5, 6, 3}};
        SimplexTableau tableau = new SimplexTableau(matrix);
        solver.solvePhase1(tableau);
        double[][] expected = {{1, 0, -2},
                               {0, 1, 1},
                               {2, 0, 4}};
        assertArrayEquals(expected, tableau.getMatrix());
    }

    // Test doOptimize method
    @Test
    public void testDoOptimize() throws OptimizationException {
        double[][] matrix = {{1, 2, -1},
                             {3, 4, 2},
                             {5, 6, 3}};
        SimplexTableau tableau = new SimplexTableau(matrix);
        RealPointValuePair result = solver.doOptimize(tableau);
        RealPointValuePair expected = new RealPointValuePair(new double[]{-2, 1}, 4);
        assertEquals(expected, result);
    }

}