import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.optimization.linear.*;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new RealVector(new double[]{1.0, 2.0}), Relationship.LEQ, 5.0),
                new LinearConstraint(new RealVector(new double[]{3.0, 4.0}), Relationship.LEQ, 10.0)
        );
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0}), 0);
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, true, 0.0001, 10);
        
        tableau.initializeColumnLabels();
        
        assertEquals("W", tableau.columnLabels.get(0));
        assertEquals("Z", tableau.columnLabels.get(1));
        assertEquals("x0", tableau.columnLabels.get(2));
        assertEquals("x1", tableau.columnLabels.get(3));
        assertFalse(tableau.columnLabels.contains("x-"));
    }

    @Test
    public void testNormalizeConstraints() {
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new RealVector(new double[]{1.0, 2.0}), Relationship.LEQ, 5.0),
                new LinearConstraint(new RealVector(new double[]{-1.0, -2.0}), Relationship.GEQ, -10.0)
        );
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0}), 0);
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, true, 0.0001, 10);
        
        List<LinearConstraint> expected = Arrays.asList(
                new LinearConstraint(new RealVector(new double[]{1.0, 2.0}), Relationship.LEQ, 5.0),
                new LinearConstraint(new RealVector(new double[]{1.0, 2.0}), Relationship.GEQ, 10.0)
        );
        List<LinearConstraint> normalized = tableau.normalizeConstraints(constraints);
        
        assertEquals(expected, normalized);
    }

    // Additional test cases for other methods
    
}