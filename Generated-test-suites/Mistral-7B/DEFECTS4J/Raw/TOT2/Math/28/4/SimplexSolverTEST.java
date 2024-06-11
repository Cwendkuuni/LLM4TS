import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.GoalType;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testEmptySolver() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{}, new double[]{0.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair solution = solver.doOptimize();
        assertNull(solution);
    }

    @Test
    public void testSimpleSolver() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();

        double[] objectiveCoefficients = {1.0, 2.0};
        double[] objectiveConstant = {3.0};
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(objectiveCoefficients, objectiveConstant);

        LinearConstraint constraint1 = new LinearConstraint(1.0, new double[]{1.0, -1.0}, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(2.0, new double[]{-1.0, 1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet(constraint1, constraint2);

        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair solution = solver.doOptimize();
        assertEquals(0.0, solution.getValue(), 0.0);
        assertEquals(new double[]{1.0, -1.0}, solution.getPoint(), 0.0);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();

        double[] objectiveCoefficients = {1.0, 2.0};
        double[] objectiveConstant = {3.0};
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(objectiveCoefficients, objectiveConstant);

        LinearConstraint constraint1 = new LinearConstraint(1.0, new double[]{1.0, -1.0}, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(2.0, new double[]{1.0, 1.0}, 3.0);
        LinearConstraintSet constraints = new LinearConstraintSet(constraint1, constraint2);

        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair solution = solver.doOptimize();
        fail("NoFeasibleSolutionException expected");
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationCountExceeded() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver(0.0, 0);

        double[] objectiveCoefficients = {1.0, 2.0};
        double[] objectiveConstant = {3.0};
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(objectiveCoefficients, objectiveConstant);

        LinearConstraint constraint1 = new LinearConstraint(1.0, new double[]{1.0, -1.0}, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(2.0, new double[]{1.0, 1.0}, 3.0);
        LinearConstraintSet constraints = new LinearConstraintSet(constraint1, constraint2);

        GoalType goalType = GoalType.MINIMIZE;

        try {
            solver.doOptimize();
            fail("MaxCountExceededException expected");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }
}