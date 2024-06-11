import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[]{1, -2, 3}, new double[][]{{2, -1, 1}, {1, 0, -1}}, true, 0.0001);
        
        Integer pivotColumn = solver.getPivotColumn(tableau);
        
        assertEquals(new Integer(1), pivotColumn);
    }
    
    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[]{1, -2, 3}, new double[][]{{2, -1, 1}, {1, 0, -1}}, true, 0.0001);
        int pivotColumn = 1;
        
        Integer pivotRow = solver.getPivotRow(pivotColumn, tableau);
        
        assertEquals(new Integer(0), pivotRow);
    }
    
    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[]{1, -2, 3}, new double[][]{{2, -1, 1}, {1, 0, -1}}, true, 0.0001);
        
        solver.doIteration(tableau);
        
        double[][] expectedTableauData = {{1, -0.75, 0.25}, {0.5, -0.5, 0.5}};
        assertArrayEquals(expectedTableauData, tableau.getData());
    }
    
    @Test
    public void testIsPhase1Solved() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau1 = new SimplexTableau(new double[]{1, -2, 3}, new double[][]{{2, -1, 1}, {1, 0, -1}}, true, 0.0001);
        SimplexTableau tableau2 = new SimplexTableau(new double[]{1, -2, 3}, new double[][]{{0, -1, 1}, {0, 0, -1}}, true, 0.0001);
        
        boolean isPhase1Solved1 = solver.isPhase1Solved(tableau1);
        boolean isPhase1Solved2 = solver.isPhase1Solved(tableau2);
        
        assertEquals(false, isPhase1Solved1);
        assertEquals(true, isPhase1Solved2);
    }
    
    @Test
    public void testIsOptimal() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau1 = new SimplexTableau(new double[]{1, -2, 3}, new double[][]{{2, -1, 1}, {1, 0, -1}}, true, 0.0001);
        SimplexTableau tableau2 = new SimplexTableau(new double[]{1, -2, 3}, new double[][]{{0, -1, 1}, {0, 0, -1}}, true, 0.0001);
        
        boolean isOptimal1 = solver.isOptimal(tableau1);
        boolean isOptimal2 = solver.isOptimal(tableau2);
        
        assertEquals(false, isOptimal1);
        assertEquals(true, isOptimal2);
    }
    
    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[]{1, -2, 3}, new double[][]{{2, -1, 1}, {1, 0, -1}}, true, 0.0001);
        
        solver.solvePhase1(tableau);
        
        double[][] expectedTableauData = {{1, 0, 4}, {-0.5, 1, -0.5}};
        assertArrayEquals(expectedTableauData, tableau.getData());
    }
    
    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        double[] f = {1, -2, 3};
        double[][] constraints1 = {{2, -1, 1}, {1, 0, -1}};
        double[][] constraints2 = {{0, -1, 1}, {0, 0, -1}};
        
        RealPointValuePair result1 = solver.doOptimize(f, constraints1, true, 0.0001);
        RealPointValuePair result2 = solver.doOptimize(f, constraints2, true, 0.0001);
        
        double[] expectedSolution1 = {-1, 1, 0};
        double[] expectedSolution2 = {-1, 0, -3};
        
        assertArrayEquals(expectedSolution1, result1.getValue(), 0);
        assertArrayEquals(expectedSolution2, result2.getValue(), 0);
    }
}