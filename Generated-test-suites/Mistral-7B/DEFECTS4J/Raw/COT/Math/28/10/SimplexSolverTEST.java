import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.MaxCountExceededException;
import org.apache.commons.math3.optimization.UnboundedSolutionException;
import org.apache.commons.math3.optimization.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver();
        SimplexSolver solverWithArgs = new SimplexSolver(1e-7, 20);
        assertEquals(1e-6, solver.epsilon, 0);
        assertEquals(10, solver.maxUlps);
        assertEquals(1e-7, solverWithArgs.epsilon, 0);
        assertEquals(20, solverWithArgs.maxUlps);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{new double[]{1.0}, new double[]{2.0}}, new double[]{3.0}), new LinearConstraintSet(), null, null);
        Integer result = SimplexSolver.getInstance().getPivotColumn(tableau);
        assertEquals(0, result.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{new double[]{1.0}, new double[]{2.0}}, new double[]{3.0}), new LinearConstraintSet(), null, null);
        Integer result = SimplexSolver.getInstance().getPivotRow(tableau, 0);
        assertEquals(1, result.intValue());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolvePhase1Unbounded() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{new double[]{1.0}, new double[]{2.0}}, new double[]{3.0}), new LinearConstraintSet(), null, null);
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSolvePhase1MaxIterations() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{new double[]{1.0}, new double[]{2.0}}, new double[]{3.0}), new LinearConstraintSet(), null, null);
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        try {
            solver.solvePhase1(tableau);
        } catch (NoFeasibleSolutionException e) {
            // ignore
        }
    }

    @Test
    public void testDoOptimize() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{new double[]{1.0}, new double[]{2.0}}, new double[]{3.0});
        LinearConstraint constraint1 = new LinearConstraint(1d, new double[]{1.0, 1.0}, 2d);
        LinearConstraint constraint2 = new LinearConstraint(2d, new double[]{2.0, 3.0}, 3d);
        LinearConstraintSet constraints = new LinearConstraintSet(constraint1, constraint2);

        SimplexTableau tableau = new SimplexTableau(function, constraints, null, null, 1e-6, 10);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();

        assertEquals(0.0, solution.getPoint()[0], 1e-6);
        assertEquals(0.0, solution.getPoint()[1], 1e-6);
        assertEquals(3.0, solution.getValue(), 1e-6);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeNoFeasibleSolution() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{new double[]{1.0}, new double[]{2.0}}, new double[]{3.0});
        LinearConstraint constraint1 = new LinearConstraint(1d, new double[]{1.0, 1.0}, Double.NEGATIVE_INFINITY);
        LinearConstraint constraint2 = new LinearConstraint(-1d, new double[]{2.0, 3.0}, Double.NEGATIVE_INFINITY);
        LinearConstraintSet constraints = new LinearConstraintSet(constraint1, constraint2);

        SimplexTableau tableau = new SimplexTableau(function, constraints, null, null, 1e-6, 10);
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeUnboundedSolution() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{new double[]{1.0}, new double[]{2.0}}, new double[]{3.0});
        LinearConstraint constraint1 = new LinearConstraint(0d, new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY);
        LinearConstraint constraint2 = new LinearConstraint(0d, new double[]{2.0, 3.0}, Double.POSITIVE_INFINITY);
        LinearConstraintSet constraints = new LinearConstraintSet(constraint1, constraint2);

        SimplexTableau tableau = new SimplexTableau(function, constraints, null, null, 1e-6, 10);
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }
}