import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnboundedSolutionException;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearOptimizationFactory;
import org.apache.commons.math3.optim.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.linear.SimpleTableau;
import org.apache.commons.math3.optim.nonlinear.GoalType;
import org.apache.commons.math3.optim.nonlinear.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testBasicFunctionality() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {1.0}}, new double[]{1.0, -3.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        LinearConstraint goal = new LinearConstraint(new double[]{1.0}, 0.0, GoalType.MINIMIZE);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(LinearOptimizationFactory.linearOptimization(f, constraints, goal));
        assertEquals(-3.0, solution.getValue(), 0.0);
        assertEquals(new double[]{2.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 0.0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {1.0}}, new double[]{1.0, 0.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        LinearConstraint goal = new LinearConstraint(new double[]{1.0}, 0.0, GoalType.MINIMIZE);
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(LinearOptimizationFactory.linearOptimization(f, constraints, goal));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, -1.0}, {1.0, 1.0}}, new double[]{0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, null), new LinearConstraint(new double[]{1.0, 1.0}, 0.0, null)};
        LinearConstraint goal = new LinearConstraint(new double[]{1.0}, 0.0, GoalType.MINIMIZE);
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(LinearOptimizationFactory.linearOptimization(f, constraints, goal));
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}}, new double[]{1.0, 1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        LinearConstraint goal = new LinearConstraint(new double[]{1.0}, 0.0, GoalType.MINIMIZE);
        SimplexSolver solver = new SimplexSolver(0.0, 0);
        solver.optimize(LinearOptimizationFactory.linearOptimization(f, constraints, goal));
    }
}