import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, -3.0}, new double[]{1.0, 2.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{-1.0, -1.0}, 2.0),
            new LinearConstraint(new double[]{-3.0, 1.0}, 2.0),
            new LinearConstraint(new double[]{3.0, 1.0}, Double.POSITIVE_INFINITY),
            new LinearConstraint(new double[]{1.0, 2.0}, Double.POSITIVE_INFINITY)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        try {
            PointValuePair result = solver.optimize(new LinearConstraint[]{}, f, constraints, goalType, null, null);
            assertEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, result.getPoint(), 0.001);
            assertEquals(-2.0 / 3.0, result.getValue(), 0.001);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testUnboundedSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, -3.0}, new double[]{1.0, 2.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{1.0, 1.0}, Double.NEGATIVE_INFINITY)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        try {
            solver.optimize(new LinearConstraint[]{}, f, constraints, goalType, null, null);
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, -3.0}, new double[]{1.0, 2.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{1.0, 1.0}, 2.0),
            new LinearConstraint(new double[]{3.0, 1.0}, 1.0),
            new LinearConstraint(new double[]{1.0, 2.0}, Double.POSITIVE_INFINITY)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        try {
            solver.optimize(new LinearConstraint[]{}, f, constraints, goalType, null, null);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testMaxIterationsExceeded() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{};
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1, 0);

        try {
            solver.optimize(new LinearConstraint[]{}, f, constraints, goalType, null, null);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }
}