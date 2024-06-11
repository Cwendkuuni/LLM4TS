import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() throws OptimizationException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{3.0, 4.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0, -1.0}, 2.0),
                new LinearConstraint(new double[]{1.0, -1.0}, 1.0),
                new LinearConstraint(new double[]{1.0, 1.0}, 3.0)
        };
        SimplexSolver solver = new SimplexSolver(1e-6);
        RealPointValuePair solution = solver.optimize(objective, constraints, GoalType.MINIMIZE, null, null);
        PointValuePair expectedSolution = new PointValuePair(new double[]{2.0 / 3.0, 1.0 - 2.0 / 3.0}, 1.0 / 3.0);
        assertEquals(expectedSolution.getValue(), solution.getValue(), 1e-6);
        assertArrayEquals(expectedSolution.getPoint(), solution.getPoint(), 1e-6);
    }

    @Test(expected = OptimizationException.class)
    public void testNoFeasibleSolutionException() throws OptimizationException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{3.0, 4.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0, -1.0}, 2.0),
                new LinearConstraint(new double[]{1.0, -1.0}, 1.0),
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0)
        };
        SimplexSolver solver = new SimplexSolver(1e-6);
        solver.optimize(objective, constraints, GoalType.MINIMIZE, null, null);
    }

    @Test(expected = OptimizationException.class)
    public void testUnboundedSolutionException() throws OptimizationException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{0.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0}, Double.NEGATIVE_INFINITY),
                new LinearConstraint(new double[]{1.0}, Double.POSITIVE_INFINITY)
        };
        SimplexSolver solver = new SimplexSolver(1e-6);
        solver.optimize(objective, constraints, GoalType.MINIMIZE, null, null);
    }
}