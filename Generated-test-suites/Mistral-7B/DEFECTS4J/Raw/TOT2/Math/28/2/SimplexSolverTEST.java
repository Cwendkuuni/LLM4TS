import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testSimpleOptimization() {
        // given
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-2, 3}, new double[]{1, 2});
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{
            new LinearConstraint(1.0, new double[]{1, 1}, 0.0),
            new LinearConstraint(2.0, new double[]{2, 2}, 0.0)
        });
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);

        // when
        PointValuePair result = solver.optimize(obj, constraints, goalType);

        // then
        assertEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, result.getPoint(), 1e-6);
        assertEquals(-1.0 / 3.0, result.getValue(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        // given
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-2, 3}, new double[]{1, 2});
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);

        // when
        solver.optimize(obj, null, goalType);

        // then exception is thrown
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() {
        // given
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-2, 3}, new double[]{1, 2});
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{
            new LinearConstraint(1.0, new double[]{1, 1}, 0.0),
            new LinearConstraint(2.0, new double[]{2, 2}, 0.0)
        });
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-12, 10);

        // when
        try {
            solver.optimize(obj, constraints, goalType);
        } catch (NoFeasibleSolutionException e) {
            throw new RuntimeException(e);
        }

        // then exception is thrown
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        // given
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{0, 0}, new double[]{1, 2});
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{
            new LinearConstraint(1.0, new double[]{1, 1}, 0.0),
            new LinearConstraint(1.0, new double[]{2, 2}, 0.0)
        });
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);

        // when
        try {
            solver.optimize(obj, constraints, goalType);
        } catch (MaxCountExceededException e) {
            throw new RuntimeException(e);
        }

        // then exception is thrown
    }
}