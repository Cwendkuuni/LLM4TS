import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.optimization.linear.*;

public class SimplexTableauTest {

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2}, 0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 10));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 0.1, 10);
        
        assertEquals(3, tableau.getWidth());
        assertEquals(2, tableau.getHeight());
        assertEquals(1.0, tableau.getEntry(0, 0), 0.001);
        assertEquals(1.0, tableau.getEntry(1, 1), 0.001);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2}, 0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, -10));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 0.1, 10);

        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(constraints);
        LinearConstraint normalized = normalizedConstraints.get(0);

        assertEquals(-1.0, normalized.getValue(), 0.001);
        assertEquals(Relationship.GEQ, normalized.getRelationship());
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2}, 0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 0.1, 10);

        assertEquals(1, tableau.getNumObjectiveFunctions());
    }

    // Add more test cases for other methods...

}