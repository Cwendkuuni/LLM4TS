import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
        tableau.initializeColumnLabels();
        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals("W", tableau.columnLabels.get(0));
        assertEquals("Z", tableau.columnLabels.get(1));
        assertEquals("x0", tableau.columnLabels.get(2));
        assertEquals("x1", tableau.columnLabels.get(3));
        assertEquals("s0", tableau.columnLabels.get(4));
        assertEquals("s1", tableau.columnLabels.get(5));
        assertEquals("a0", tableau.columnLabels.get(6));
        assertEquals("RHS", tableau.columnLabels.get(7));
    }

    @Test
    public void testCreateTableau() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
        boolean maximize = true;
        RealMatrix expectedMatrix = { {-1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0},
                                      {1.0, 1.0, 2.0, -1.0, 1.0, 0.0, 0.0, 6.0},
                                      {1.0, -1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 4.0},
                                      {1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 1.0, 7.0} };
        RealMatrix result = tableau.createTableau(maximize);
        assertArrayEquals(expectedMatrix, result.getData());
    }

    // ... Additional test cases for each method

}