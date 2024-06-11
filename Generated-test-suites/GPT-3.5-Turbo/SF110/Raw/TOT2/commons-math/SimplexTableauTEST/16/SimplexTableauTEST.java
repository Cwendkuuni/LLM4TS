import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        SimplexTableau simplexTableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, 2}), new ArrayList<>(), GoalType.MAXIMIZE, true, 0.01);
        simplexTableau.initializeColumnLabels();
        // Test the initialization of column labels
        assertEquals(7, simplexTableau.getColumnLabels().size());
        assertEquals("W", simplexTableau.getColumnLabels().get(0));
        assertEquals("Z", simplexTableau.getColumnLabels().get(1));
        assertEquals("x0", simplexTableau.getColumnLabels().get(2));
        assertEquals("x1", simplexTableau.getColumnLabels().get(3));
        assertEquals("x-", simplexTableau.getColumnLabels().get(4));
        assertEquals("s0", simplexTableau.getColumnLabels().get(5));
        assertEquals("RHS", simplexTableau.getColumnLabels().get(6));
    }

    @Test
    public void testNormalizeConstraints() {
        LinearConstraint constraint = new LinearConstraint(new double[]{1, -1}, Relationship.LEQ, 0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint);

        SimplexTableau simplexTableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1}), constraints, GoalType.MAXIMIZE, true, 0.01);
        List<LinearConstraint> normalizedConstraints = simplexTableau.normalizeConstraints(constraints);
        // Test normalization of constraints
        assertEquals(1, normalizedConstraints.size());
        assertEquals(new LinearConstraint(new double[]{-1, 1}, Relationship.GEQ, 0), normalizedConstraints.get(0));
    }

    // Add more test cases for each public method

}