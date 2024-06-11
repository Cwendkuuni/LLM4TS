import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        // Test with two objective functions
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2}, 0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 5));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, true, 0.0001);
        
        // Check column labels length
        assertEquals(6, tableau.getColumnLabels().size());
        
        // Check specific column labels
        assertEquals("W", tableau.getColumnLabels().get(0));
        assertEquals("Z", tableau.getColumnLabels().get(1));
        assertEquals("x0", tableau.getColumnLabels().get(2));
    }

    @Test
    public void testCreateTableau() {
        // Test when objective function needs to be minimized
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{2, 3}, 0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new double[]{1, -1}, Relationship.GEQ, -1));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 0.0001);
        
        // Check tableau dimensions
        assertEquals(3, tableau.getHeight());
        assertEquals(6, tableau.getWidth());
        
        // Check specific entries
        assertEquals(1.0, tableau.getEntry(0, 3), 0.0001);
        assertEquals(-1.0, tableau.getEntry(0, 4), 0.0001);
        assertEquals(1.0, tableau.getEntry(0, 5), 0.0001);
    }

    // Additional test cases for other methods can be added here

}