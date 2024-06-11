import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void getPivotColumn_Test() {
        SimplexTableau tableau = new SimplexTableau();
        
        // Test when all entries in tableau are positive
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        Integer expected = 0;
        Integer actual = getPivotColumn(tableau);
        assertEquals(expected, actual);
        
        // Test when some entries in tableau are negative
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, -3);
        expected = 2;
        actual = getPivotColumn(tableau);
        assertEquals(expected, actual);
        
        // Test when all entries in tableau are negative
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, -3);
        expected = 2;
        actual = getPivotColumn(tableau);
        assertEquals(expected, actual);
    }
    
    @Test
    public void getPivotRow_Test() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setNumObjectiveFunctions(1);
        tableau.setHeight(4);
        tableau.setWidth(4);
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(2, 0, 2);
        tableau.setEntry(3, 0, 3);
        
        // Test when all entries in col are non-zero
        Integer expected = 2;
        Integer actual = getPivotRow(tableau, 0);
        assertEquals(expected, actual);
        
        // Test when some entries in col are zero
        tableau.setEntry(1, 0, 0);
        expected = 3;
        actual = getPivotRow(tableau, 0);
        assertEquals(expected, actual);
        
        // Test when all entries in col are zero
        tableau.setEntry(2, 0, 0);
        expected = null;
        actual = getPivotRow(tableau, 0);
        assertEquals(expected, actual);
    }
    
    @Test
    public void doIteration_Test() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setHeight(4);
        tableau.setWidth(4);
        tableau.setEntry(1, 1, 2);
        tableau.setEntry(2, 2, -1.5);
        tableau.setEntry(3, 3, 3);
        
        double expected1 = 1.0 / 2.0;
        double expected2 = (-1.5) / 2.0;
        double expected3 = 3.0 / 2.0;
        doIteration(tableau);
        double actual1 = tableau.getEntry(1, 1);
        double actual2 = tableau.getEntry(2, 2);
        double actual3 = tableau.getEntry(3, 3);
        assertEquals(expected1, actual1, 0.0001);
        assertEquals(expected2, actual2, 0.0001);
        assertEquals(expected3, actual3, 0.0001);
    }
    
    @Test
    public void solvePhase1_Test() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setNumArtificialVariables(2);
        tableau.setHeight(4);
        tableau.setWidth(5);
        tableau.setRhsOffset(4);
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(1, 4, 2);
        tableau.setEntry(2, 0, 2);
        tableau.setEntry(2, 4, -3);
        tableau.setEntry(3, 0, 3);
        tableau.setEntry(3, 4, 5);
        
        // Test when W is zero
        solvePhase1(tableau);
        double expected = 0.0;
        double actual = tableau.getEntry(0, tableau.getRhsOffset());
        assertEquals(expected, actual, 0.0001);
        
        // Test when W is not zero
        tableau.setEntry(0, tableau.getRhsOffset(), 1);
        try {
            solvePhase1(tableau);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Test passed
        }
    }
    
    @Test
    public void doOptimize_Test() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setFunction(1);
        tableau.setConstraints(3);
        tableau.setGoalType(1);
        tableau.setRestrictToNonNegative(false);
        tableau.setEpsilon(0.00001);
        tableau.setMaxUlps(20);
        tableau.setHeight(4);
        tableau.setWidth(5);
        tableau.setRhsOffset(4);
        tableau.setNumArtificialVariables(2);
        tableau.setNumObjectiveFunctions(1);
        tableau.setNumArtificialVariables(2);
        tableau.setArtificialVariableOffset(1);
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(0, 3, 4);
        tableau.setEntry(0, 4, 5);
        tableau.setEntry(1, 0, 5);
        tableau.setEntry(1, 1, 4);
        tableau.setEntry(1, 2, 3);
        tableau.setEntry(1, 3, 2);
        tableau.setEntry(1, 4, 1);
        tableau.setEntry(2, 0, 1);
        tableau.setEntry(2, 1, 2);
        tableau.setEntry(2, 2, 3);
        tableau.setEntry(2, 3, 4);
        tableau.setEntry(2, 4, 5);
        tableau.setEntry(3, 0, 3);
        tableau.setEntry(3, 1, 2);
        tableau.setEntry(3, 2, 1);
        tableau.setEntry(3, 3, 0);
        tableau.setEntry(3, 4, 3);
        
        PointValuePair expected = new PointValuePair(new double[]{0.0, 0.0, 0.0, 0.0, 0.0}, 0.0);
        PointValuePair actual = doOptimize();
        double[] expectedPoint = expected.getPoint();
        double[] actualPoint = actual.getPoint();
        for(int i=0; i<expectedPoint.length; i++) {
            assertEquals(expectedPoint[i], actualPoint[i], 0.0001);
        }
        assertEquals(expected.getValue(), actual.getValue(), 0.0001);
    }
}