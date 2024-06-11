import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver solver;
    private LinearObjectiveFunction objectiveFunction;
    private LinearConstraintSet constraints;
    private LinearConstraintSet bounds;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
        objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0});
        constraints = new LinearConstraintSet();
        bounds = new LinearConstraintSet();
    }

    @Test
    public void testEmptyConstraints() throws Exception {
        constraints.addConstraint(new LinearConstraint(new double[]{0.0}, new double[]{0.0}, null, null, null));
        LinearConstraint[] emptyConstraints = new LinearConstraint[0];
        constraints.setConstraints(emptyConstraints);

        PointValuePair solution = solver.optimize(objectiveFunction, bounds, constraints, GoalType.MINIMIZE);
        Assert.assertNull(solution);
    }

    @Test
    public void testSingleConstraint() throws Exception {
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, new double[]{1.0}, null, null, null);
        constraints.addConstraint(constraint);

        PointValuePair solution = solver.optimize(objectiveFunction, bounds, constraints, GoalType.MINIMIZE);
        Assert.assertNotNull(solution);
        Assert.assertEquals(0.0, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{0.0}, solution.getPoint(), 1e-6);
    }

    @Test
    public void testTwoConstraints() throws Exception {
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0}, new double[]{2.0}, null, null, null);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0}, new double[]{1.0}, null, null, null);
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);

        PointValuePair solution = solver.optimize(objectiveFunction, bounds, constraints, GoalType.MINIMIZE);
        Assert.assertNotNull(solution);
        Assert.assertEquals(0.5, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{0.5, 0.5}, solution.getPoint(), 1e-6);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws Exception {
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0}, new double[]{2.0}, null, null, null);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0}, new double[]{2.0}, null, null, null);
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);

        SimplexSolver solverWithMaxIterations = new SimplexSolver(1, 0);
        PointValuePair solution = solverWithMaxIterations.optimize(objectiveFunction, bounds, constraints, GoalType.MINIMIZE);
        Assert.assertNotNull(solution);

        // This should throw an exception
        solverWithMaxIterations.optimize(objectiveFunction, bounds, constraints, GoalType.MINIMIZE);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, new double[]{0.0}, null, null, null);
        constraints.addConstraint(constraint);

        PointValuePair solution = solver.optimize(objectiveFunction, bounds, constraints, GoalType.MINIMIZE);
        Assert.assertNull(solution);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0}, new double[]{-1.0}, null, null, null);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{0.0}, new double[]{0.0}, null, null, null);
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);

        PointValuePair solution = solver.optimize(objectiveFunction, bounds, constraints, GoalType.MINIMIZE);
        Assert.assertNull(solution);
    }

}