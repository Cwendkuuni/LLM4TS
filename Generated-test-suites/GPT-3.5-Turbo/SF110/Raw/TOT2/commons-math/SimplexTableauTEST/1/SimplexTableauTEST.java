import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        SimplexTableau tableau = new SimplexTableau(null, null, GoalType.MAXIMIZE, false, 0.0, 10);
        
        assertTrue(tableau.columnLabels.contains("Z"));
        assertTrue(tableau.columnLabels.contains("x0"));
        assertEquals(7, tableau.columnLabels.size());
    }
    
    @Test
    public void testNormalizeConstraints() {
        SimplexTableau tableau = new SimplexTableau(null, null, GoalType.MAXIMIZE, false, 0.0, 10);
        
        LinearConstraint constraint = new LinearConstraint(null, Relationship.LEQ, -5.0);
        tableau.constraints.add(constraint);
        
        assertEquals(-5.0, tableau.normalizeConstraints(tableau.constraints).get(0).getValue(), 0.0);
    }
    
    // Additional test cases for other methods can be added here
    
}