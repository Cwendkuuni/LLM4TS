import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.linear.*;

public class SimplexTableauTest {

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 1}, 0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1, 0}, Relationship.LEQ, 1);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{0, 1}, Relationship.GEQ, 2);

        SimplexTableau tableau = new SimplexTableau(f, List.of(constraint1, constraint2), GoalType.MINIMIZE, true, 0.0001);

        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(tableau.constraints);

        assertEquals(2, normalizedConstraints.size());
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new double[]{1, 2}, 0);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{1, 2}, 0);

        SimplexTableau tableau1 = new SimplexTableau(f1, new ArrayList<>(), GoalType.MINIMIZE, true, 0.0001);
        SimplexTableau tableau2 = new SimplexTableau(f2, new ArrayList<>(), GoalType.MINIMIZE, true, 0.0001);

        assertEquals(1, tableau1.getNumObjectiveFunctions());
        assertEquals(1, tableau2.getNumObjectiveFunctions());
    }

    // Add more test cases for other methods as per the requirements

}