import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        SimplexTableau st = new SimplexTableau();
        st.initializeColumnLabels();

        assertEquals(10, st.columnLabels.size());
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction();
        Collection<LinearConstraint> constraints = new ArrayList<>();
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 0.001;

        SimplexTableau st = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

        assertNotNull(st.tableau);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction();
        Collection<LinearConstraint> constraints = new ArrayList<>();
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 0.001;

        SimplexTableau st = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);

        List<LinearConstraint> normalized = st.normalizeConstraints(constraints);

        assertEquals(constraints.size(), normalized.size());
    }

    // Add more test cases for other methods...

}