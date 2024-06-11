import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstraints() throws Exception {
        ObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1., 1.}, new double[]{1.});
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objFunction, constraints, goalType);
        assertNull(solution);
    }

    @Test
    public void testSimpleTestProblem() throws Exception {
        ObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1., 1.}, new double[]{1.});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(1.0, new double[]{1., 1.}, Relationship.LEQUAL, 0.),
                new LinearConstraint(1.0, new double[]{1., -1.}, Relationship.LEQUAL, 0.)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objFunction, constraints, goalType);
        assertEquals(new double[]{1., 1.}, solution.getPoint(), 1e-6);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws Exception {
        ObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1., 1.}, new double[]{1.});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(1.0, new double[]{1., 1.}, Relationship.LEQUAL, 0.),
                new LinearConstraint(1.0, new double[]{1., -1.}, Relationship.LEQUAL, 0.)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(0, 0);
        solver.optimize(objFunction, constraints, goalType);
    }

    @Test
    public void testNoFeasibleSolution() throws Exception {
        ObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1., 1.}, new double[]{1.});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(1.0, new double[]{1., 1.}, Relationship.LEQUAL, 1.),
                new LinearConstraint(1.0, new double[]{1., -1.}, Relationship.LEQUAL, 0.)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(objFunction, constraints, goalType);
            assertTrue(false);
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testUnboundedSolution() throws Exception {
        ObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1., 1.}, new double[]{1.});
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(objFunction, constraints, goalType);
            assertTrue(false);
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testBasicFunctionAndConstraints() throws Exception {
        ObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{1.}, new double[]{1., 1.});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(1.0, new double[]{1., 1.}, Relationship.LEQUAL, 0.),
                new LinearConstraint(1.0, new double[]{1., -1.}, Relationship.LEQUAL, 0.)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objFunction, constraints, goalType);
        assertEquals(new double[]{0., 0.}, solution.getPoint(), 1e-6);
    }

    @Test
    public void testFunctionWithNegativeCoefficient() throws Exception {
        ObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{1., -1.}, new double[]{1.});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(1.0, new double[]{1., 1.}, Relationship.LEQUAL, 0.),
                new LinearConstraint(1.0, new double[]{1., -1.}, Relationship.LEQUAL, 0.)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objFunction, constraints, goalType);
        assertEquals(new double[]{1., -1.}, solution.getPoint(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() throws Exception {
        ObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{1.}, new double[]{1.});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objFunction, constraints, GoalType.MAXIMIZE);
    }
}