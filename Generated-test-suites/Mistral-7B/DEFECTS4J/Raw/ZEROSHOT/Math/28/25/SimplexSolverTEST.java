import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optimization.nonlinear.scalar.LinearConstraintSetFoo;
import org.apache.commons.math3.optimization.nonlinear.scalar.LinearObjectiveFunctionFoo;
import org.apache.commons.math3.optimization.nonlinear.scalar.LinearConstraintSetFoo.Constraint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link SimplexSolver} class.
 */
public class SimplexSolverTest {

    private LinearObjectiveFunctionFoo objFunc;
    private LinearConstraintSetFoo constraintSet;

    @Before
    public void setup() {
        // define objective function and constraints
        objFunc = new LinearObjectiveFunctionFoo(new double[]{-1, 2}, 0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1, 1}, 2);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1, 1}, 3);
        constraintSet = new LinearConstraintSetFoo();
        constraintSet.addConstraint(constraint1);
        constraintSet.addConstraint(constraint2);
    }

    /**
     * Test optimization with bounded solution.
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testOptimizationBoundedSolution() throws Exception {
        // create optimizer with default settings
        SimplexSolver solver = new SimplexSolver();

        // define problem bounds
        SimpleBounds bounds = new SimpleBounds(new double[]{Double.NEGATIVE_INFINITY, 0},
                                                new double[]{0, Double.POSITIVE_INFINITY});

        // define optimization problem
        LinearConstraintSet lcs = new LinearConstraintSet();
        lcs.add(constraint1);
        lcs.add(constraint2);
        LinearObjectiveFunction lof = new LinearObjectiveFunction(objFunc.getGradient(), objFunc.getValue());
        LinearObjectiveFunctionSet lofs = new LinearObjectiveFunctionSet(lof);

        PointValuePair solution = solver.optimize(new LinearOptimizationProblem(lofs, lcs, GoalType.MINIMIZE, bounds));

        // check the solution
        Assert.assertEquals("Incorrect solution", new PointValuePair(-0.5, -1.5), solution);
    }

    /**
     * Test optimization with unbounded solution.
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testOptimizationUnboundedSolution() throws Exception {
        // create optimizer with default settings
        SimplexSolver solver = new SimplexSolver();

        // define optimization problem with unbounded solution
        LinearConstraintSet lcs = new LinearConstraintSet();
        LinearObjectiveFunctionSet lofs = new LinearObjectiveFunctionSet(new LinearObjectiveFunctionFoo(new double[]{0, 1}, 0));

        // attempt optimization
        solver.optimize(new LinearOptimizationProblem(lofs, lcs, GoalType.MINIMIZE, null));
    }

    /**
     * Test optimization with no feasible solution.
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testOptimizationNoFeasibleSolution() throws Exception {
        // create optimizer with default settings
        SimplexSolver solver = new SimplexSolver();

        // define optimization problem with no feasible solution
        LinearConstraintSet lcs = new LinearConstraintSet();
        LinearObjectiveFunctionSet lofs = new LinearObjectiveFunctionSet(new LinearObjectiveFunctionFoo(new double[]{1, 1}, 1));

        // attempt optimization
        solver.optimize(new LinearOptimizationProblem(lofs, lcs, GoalType.MINIMIZE, null));
    }

    /**
     * Test optimization with maximum iterations exceeded.
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = MaxCountExceededException.class)
    public void testOptimizationMaxIterationsExceeded() throws Exception {
        // create optimizer with limited number of iterations
        SimplexSolver solver = new SimplexSolver(1, 0);

        // define optimization problem with unbounded solution
        LinearConstraintSet lcs = new LinearConstraintSet();
        LinearObjectiveFunctionSet lofs = new LinearObjectiveFunctionSet(new LinearObjectiveFunctionFoo(new double[]{0, 1}, 0));

        // attempt optimization
        solver.optimize(new LinearOptimizationProblem(lofs, lcs, GoalType.MINIMIZE, null));
    }
}