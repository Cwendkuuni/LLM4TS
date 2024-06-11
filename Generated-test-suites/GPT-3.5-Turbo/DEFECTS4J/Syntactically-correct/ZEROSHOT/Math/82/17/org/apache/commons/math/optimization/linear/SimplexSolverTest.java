import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test case 1
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        Integer result1 = getPivotColumn(tableau);
        Integer expected1 = 1;
        assertEquals(expected1, result1);
        
        // Test case 2
        tableau.setEntry(0, 0, -3);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, -1);
        Integer result2 = getPivotColumn(tableau);
        Integer expected2 = 0;
        assertEquals(expected2, result2);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test case 1
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, -2);
        tableau.setEntry(2, 0, 3);
        tableau.setEntry(2, 1, -6);
        Integer result1 = getPivotRow(1, tableau);
        Integer expected1 = 1;
        assertEquals(expected1, result1);
        
        // Test case 2
        tableau.setEntry(1, 0, -3);
        tableau.setEntry(1, 1, 6);
        tableau.setEntry(2, 0, 1);
        tableau.setEntry(2, 1, -2);
        Integer result2 = getPivotRow(0, tableau);
        Integer expected2 = 2;
        assertEquals(expected2, result2);
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test case 1
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(1, 0, 3);
        tableau.setEntry(1, 1, -6);
        doIteration(tableau);
        double result1 = tableau.getEntry(1, 0);
        double expected1 = -0.5;
        assertEquals(expected1, result1, epsilon);
        
        // Test case 2
        tableau.setEntry(0, 0, -3);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, -2);
        doIteration(tableau);
        double result2 = tableau.getEntry(1, 0);
        double expected2 = 2.5;
        assertEquals(expected2, result2, epsilon);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test case 1
        tableau.setEntry(0, 0, 0);
        tableau.setEntry(0, 1, 0);
        boolean result1 = isPhase1Solved(tableau);
        assertTrue(result1);
        
        // Test case 2
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, 0);
        boolean result2 = isPhase1Solved(tableau);
        assertFalse(result2);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test case 1
        tableau.setEntry(0, 0, 0);
        tableau.setEntry(0, 1, 0);
        boolean result1 = isOptimal(tableau);
        assertTrue(result1);
        
        // Test case 2
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, 0);
        boolean result2 = isOptimal(tableau);
        assertFalse(result2);
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test case 1
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(1, 0, 3);
        tableau.setEntry(1, 1, -6);
        solvePhase1(tableau);
        double result1 = tableau.getEntry(0, 0);
        double expected1 = 0;
        assertEquals(expected1, result1, epsilon);
        
        // Test case 2
        tableau.setEntry(0, 0, -3);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, -2);
        solvePhase1(tableau);
        double result2 = tableau.getEntry(0, 0);
        double expected2 = 0;
        assertEquals(expected2, result2, epsilon);
    }

    @Test
    public void testDoOptimize() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        RealPointValuePair result = doOptimize();
        double[] expectedPoint = new double[f.length];
        double expectedValue = 0;
        assertArrayEquals(expectedPoint, result.getPoint(), epsilon);
        assertEquals(expectedValue, result.getValue(), epsilon);

    }

}