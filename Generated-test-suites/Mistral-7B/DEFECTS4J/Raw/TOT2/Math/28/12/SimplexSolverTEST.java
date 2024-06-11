import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SimplexSolverTest {

    private SimplexSolver solver;
    private LinearObjectiveFunction objectiveFunction;
    private LinearConstraintSet constraints;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
        objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{0.0, 1.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 1.0, GoalType.LEQ);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, -1.0}, 3.0, GoalType.LEQ);
        constraints = new LinearConstraintSet(Arrays.asList(constraint1, constraint2));
    }

    @Test
    public void testBasicFunctionality() {
        PointValuePair result = solver.optimize(objectiveFunction, constraints);
        Assert.assertEquals(new double[]{1.0 / 3.0, 1.0}, result.getPoint(), 0.0);
        Assert.assertEquals(-1.0 / 3.0, result.getValue(), 0.0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{0.0}, new double[]{0.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        solver.optimize(objectiveFunction, constraints);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 3.0, 1.0}, new double[]{0.0, 1.0, Double.MAX_VALUE});
        LinearConstraintSet constraints = new LinearConstraintSet();
        solver.setMaxIterations(0);
        solver.optimize(objectiveFunction, constraints);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{0.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet(Arrays.asList(
                new LinearConstraint(new double[]{1.0, 1.0}, 0.0, GoalType.EQ),
                new LinearConstraint(new double[]{1.0, 1.0}, 1.0, GoalType.EQ)
        ));
        solver.optimize(objectiveFunction, constraints);
    }
}