
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testConstructor1() {
        LinearObjectiveFunction f = new LinearObjectiveFunction();
        Collection<LinearConstraint> constraints = new ArrayList<>();
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 0.001;
        int maxUlps = 10;

        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);

        assertNotNull(tableau);
        // Add more assertions as needed
    }

    @Test
    public void testConstructor2() {
        LinearObjectiveFunction f = new LinearObjectiveFunction();
        Collection<LinearConstraint> constraints = new ArrayList<>();
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 0.01;
        int maxUlps = 5;

        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);

        assertNotNull(tableau);
        // Add more assertions as needed
    }
}

