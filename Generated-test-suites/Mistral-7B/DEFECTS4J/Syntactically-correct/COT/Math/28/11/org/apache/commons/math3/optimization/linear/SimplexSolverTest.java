import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearOptimizationProblem;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optimization.nonlinear.scalar.MaxCountExceededException;
import org.apache.commons.math3.optimization.nonlinear.scalar.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.nonlinear.scalar.UnboundedSolutionException;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(1.0e-6, solver.epsilon, 0.0);
        Assert.assertEquals(10, solver.maxUlps);
    }

    @Test
    public void testCustomConstructor() {
        SimplexSolver solver = new SimplexSolver(0.001, 5);
        Assert.assertEquals(0.001, solver.epsilon, 0.0);
        Assert.assertEquals(5, solver.maxUlps);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotColumnWithUnboundedProblem() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-Double.MAX_VALUE});
        LinearConstraint[] constraints = {};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MINIMIZE, false, solver.epsilon, solver.maxUlps);
        solver.solvePhase1(tableau);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsReached() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{-2.0, -3.0});
        LinearConstraint[] constraints = {new LinearConstraint(new double[]{0.0}, new double[]{1.0}, Relation.LEQ)};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MINIMIZE, false, solver.epsilon, solver.maxUlps);
        for (int i = 0; i < 10000; i++) {
            solver.doIteration(tableau);
        }
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints = {new LinearConstraint(new double[]{0.0, 1.0}, new double[]{1.0, 1.0}, Relation.GREATER_EQUAL)};
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MINIMIZE, false, solver.epsilon, solver.maxUlps);
        solver.solvePhase1(tableau);
    }

    @Test
    public void testOptimize() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{-3.0, -1.0});
        LinearConstraint[] constraints = {new LinearConstraint(new double[]{0.0}, new double[]{1.0}, Relation.LEQ),
            new LinearConstraint(new double[]{1.0}, new double[]{3.0}, Relation.LEQ),
            new LinearConstraint(new double[]{1.0}, new double[]{1.0}, Relation.LEQ)};
        SimplexSolver solver = new SimplexSolver();
        LinearOptimizationProblem problem = new LinearOptimizationProblem(obj, constraints, GoalType.MINIMIZE);
        PointValuePair solution = solver.doOptimize();
        Assert.assertEquals(-3.0, solution.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 0.0);
    }
}