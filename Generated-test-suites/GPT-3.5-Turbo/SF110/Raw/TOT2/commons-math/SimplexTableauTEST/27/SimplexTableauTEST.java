import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {
    private SimplexTableau simplexTableau;

    @Before
    public void setUp() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2}, 0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 5));
        constraints.add(new LinearConstraint(new double[]{2, 1}, Relationship.GEQ, 2));
        simplexTableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, true, 1.0E-12, 10);
    }

    @Test
    public void testInitializeColumnLabels() {
        simplexTableau.initializeColumnLabels();
        List<String> expectedColumnLabels = Arrays.asList("Z", "x0", "x1", "s0", "s1", "a0", "RHS");
        assertEquals(expectedColumnLabels, simplexTableau.getColumnLabels());
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        int expectedNumObjectiveFunctions = 1;
        assertEquals(expectedNumObjectiveFunctions, simplexTableau.getNumObjectiveFunctions());
    }

    @Test
    public void testNormalizeConstraints() {
        List<LinearConstraint> normalizedConstraints = simplexTableau.normalizeConstraints(simplexTableau.getConstraints());
        assertEquals(simplexTableau.getConstraints(), normalizedConstraints);
    }

    // Add more test cases for other methods as needed

}