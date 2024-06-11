import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.linear.*;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(), new ArrayList<>(), GoalType.MAXIMIZE, false, 0.0, 10);
        tableau.initializeColumnLabels();
        assertEquals("W", tableau.columnLabels.get(0));
        assertEquals("Z", tableau.columnLabels.get(1));
        assertEquals("x0", tableau.columnLabels.get(2));
        assertEquals("x1", tableau.columnLabels.get(3));
        // Add more assertions as needed
    }

    @Test
    public void testNormalizeConstraints() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(), new ArrayList<>(), GoalType.MINIMIZE, true, 0.0, 10);
        LinearConstraint constraint = new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.GEQ, 0.0);
        tableau.constraints.add(constraint);
        assertEquals(1, tableau.normalizeConstraints(tableau.constraints).size());
        // Add more assertions as needed
    }

    // Add more test cases for other methods

}