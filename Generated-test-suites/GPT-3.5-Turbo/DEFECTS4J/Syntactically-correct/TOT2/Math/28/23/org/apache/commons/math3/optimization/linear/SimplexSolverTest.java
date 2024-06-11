import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test when all coefficients are positive
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(0, 2, 2.0);
        tableau.setEntry(0, 3, 3.0);
        Integer pivotCol = simplexSolver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(1), pivotCol);
        
        // Test when there are negative coefficients
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(0, 2, -2.0);
        tableau.setEntry(0, 3, -3.0);
        pivotCol = simplexSolver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(3), pivotCol);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test when there is no feasible solution
        assertNull(simplexSolver.getPivotRow(tableau, 1));
        
        // Test when there is a pivot row with the minimum ratio
        tableau.setEntry(1, 1, 2.0);
        tableau.setEntry(1, 2, 1.0);
        tableau.setEntry(1, 3, 4.0);
        Integer pivotRow = simplexSolver.getPivotRow(tableau, 1);
        assertEquals(Integer.valueOf(1), pivotRow);
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.setMaxIterations(100);
        simplexSolver.setConvergenceChecker(new SimplexSolver.SimplexConvergenceChecker());
        PointValuePair result = simplexSolver.doOptimize();

        // Test the resulting PointValuePair
        double[] expectedPoint = new double[]{1.0, 2.0, 3.0};
        double[] resultPoint = result.getPoint();
        for (int i = 0; i < expectedPoint.length; i++) {
            assertEquals(expectedPoint[i], resultPoint[i], 0.0);
        }

        // Test the resulting value
        assertEquals(0.0, result.getValue(), 0.0);
    }

}