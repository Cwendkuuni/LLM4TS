import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
    }

    @Test
    public void testCustomConstructor() {
        double epsilon = 1.0e-8;
        int maxUlps = 21;
        SimplexSolver solver = new SimplexSolver(epsilon, maxUlps);
        PointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[1];
        LinearConstraint c1 = new LinearConstraint(new double[]{-1.0, 1.0}, 0.0);
        constraints[0] = c1;
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize(new LinearProblem(function, constraints, null, null));
        } catch (NoFeasibleSolutionException e) {
            // expected: no feasible solution
        }
        // unbounded solution should throw an exception
        solver.doIteration(new SimplexTableau(function, constraints, null, null, epsilon, maxUlps));
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsException() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[1];
        LinearConstraint c1 = new LinearConstraint(new double[]{-1.0, 1.0}, 0.0);
        constraints[0] = c1;
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize(new LinearProblem(function, constraints, null, null));
        solver.setMaxIterations(1);
        try {
            solver.doIteration(new SimplexTableau(function, constraints, null, null, epsilon, maxUlps));
        } catch (MaxCountExceededException e) {
            // expected: max iterations exceeded
        }
    }

    @Test
    public void testNoFeasibleSolutionException() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[1];
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 1.0}, 0.0);
        constraints[0] = c1;
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize(new LinearProblem(function, constraints, null, null));
            Assert.fail();
        } catch (NoFeasibleSolutionException e) {
            // expected: no feasible solution
        }
    }

    @Test
    public void testOptimalSolution() throws Exception {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{1.0, -1.0});
        LinearConstraint[] constraints = new LinearConstraint[1];
        LinearConstraint c1 = new LinearConstraint(new double[]{-1.0, 1.0}, 0.0);
        constraints[0] = c1;
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize(new LinearProblem(function, constraints, null, null));
        double[] expectedSolution = new double[]{1.0, 2.0};
        Assert.assertArrayEquals(expectedSolution, solution.getPoint(), epsilon);
    }

}