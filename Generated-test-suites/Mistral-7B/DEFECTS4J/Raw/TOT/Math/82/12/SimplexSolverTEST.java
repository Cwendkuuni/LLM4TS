import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleTableau;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearOptimizationFactory;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testSolverWithNoConstraints() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, 1.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(obj);
        assertEquals(1.0, solution.getPoint()[0], 0.001);
        assertEquals(-3.0, solution.getValue(), 0.001);
    }

    @Test
    public void testSolverWithOneConstraint() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, 1.0);
        LinearConstraint constraint = new LinearConstraint(new double[]{0.0}, 1.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new LinearConstraint[] {constraint}, obj);
        assertEquals(1.0, solution.getPoint()[0], 0.001);
        assertEquals(-4.0/3.0, solution.getValue(), 0.001);
    }

    @Test
    public void testSolverWithOneNegativeConstraint() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, 1.0);
        LinearConstraint constraint = new LinearConstraint(new double[]{0.0}, -1.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new LinearConstraint[] {constraint}, obj);
        assertEquals(-1.0, solution.getPoint()[0], 0.001);
        assertEquals(-3.0, solution.getValue(), 0.001);
    }

    @Test(expected = OptimizationException.class)
    public void testSolverWithUnboundedSolution() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, 1.0);
        LinearConstraint constraint = new LinearConstraint(new double[]{0.0}, 0.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new LinearConstraint[] {constraint}, obj);
        fail("Solver should have thrown an exception for unbounded solution");
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolverWithNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, 1.0);
        LinearConstraint constraint = new LinearConstraint(new double[]{0.0}, 2.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new LinearConstraint[] {constraint}, obj);
        fail("Solver should have thrown an exception for no feasible solution");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithNullConstraint() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, 1.0);
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(null, obj);
        fail("Solver should have thrown an exception for null constraint");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithNullObjectiveFunction() throws Exception {
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(constraints, null);
        fail("Solver should have thrown an exception for null objective function");
    }
}