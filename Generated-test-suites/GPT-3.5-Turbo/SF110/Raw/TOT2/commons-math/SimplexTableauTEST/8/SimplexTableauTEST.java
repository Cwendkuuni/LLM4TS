import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        // Test with two objective functions
        SimplexTableau simplexTableau = new SimplexTableau(new LinearObjectiveFunction(), new ArrayList<>(), GoalType.MAXIMIZE, true, 0.1);
        simplexTableau.initializeColumnLabels();
        assertEquals("W", simplexTableau.getColumnLabels().get(0));
        assertEquals("Z", simplexTableau.getColumnLabels().get(1));
        assertEquals("x0", simplexTableau.getColumnLabels().get(2));
        assertEquals("x-", simplexTableau.getColumnLabels().get(3));

        // Test with one objective function
        simplexTableau = new SimplexTableau(new LinearObjectiveFunction(), new ArrayList<>(), GoalType.MINIMIZE, false, 0.05);
        simplexTableau.initializeColumnLabels();
        assertEquals("Z", simplexTableau.getColumnLabels().get(0));
        assertEquals("x0", simplexTableau.getColumnLabels().get(1));
        assertEquals("x-", simplexTableau.getColumnLabels().get(2));
    }

    @Test
    public void testNormalizeConstraints() {
        SimplexTableau simplexTableau = new SimplexTableau(new LinearObjectiveFunction(), new ArrayList<>(), GoalType.MAXIMIZE, true, 0.1);
        List<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, -2.0}), Relationship.GEQ, 5.0));

        List<LinearConstraint> normalizedConstraints = simplexTableau.normalizeConstraints(originalConstraints);
        assertEquals(1, normalizedConstraints.size());
        LinearConstraint normalizedConstraint = normalizedConstraints.get(0);
        assertEquals(new ArrayRealVector(new double[]{-1.0, 2.0}), normalizedConstraint.getCoefficients());
        assertEquals(Relationship.LEQ, normalizedConstraint.getRelationship());
        assertEquals(-5.0, normalizedConstraint.getValue(), 0.0);
    }

    // Add more test cases for other methods as needed

}