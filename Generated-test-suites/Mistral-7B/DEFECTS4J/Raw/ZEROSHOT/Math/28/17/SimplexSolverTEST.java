import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.SimpleMaxEval;
import org.apache.commons.math3.optimization.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSimpleExample() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0d, 2.0d}, {3.0d, 4.0d}}, new double[]{5.0d, 7.0d});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[][]{{1.0d, 1.0d}, {1.0d, 1.0d}}, Relationship.LEQ, new double[]{1.0d, 1.0d}),
                new LinearConstraint(new double[][]{{2.0d, 1.0d}, {3.0d, 1.0d}}, Relationship.LEQ, new double[]{2.0d, 3.0d})
        };

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, constraints, null);

        Assert.assertEquals(0.0, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, solution.getPoint(), 1e-6);
    }

    @Test
    public void testMaxCountExceeded() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0d, 2.0d}, {3.0d, 4.0d}}, new double[]{5.0d, 7.0d});
        LinearConstraint[] constraints = new LinearConstraint[0];

        SimplexSolver solver = new SimplexSolver();

        try {
            solver.optimize(f, constraints, new MaxIterations(10));
            Assert.fail();
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testUnboundedSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0d, 2.0d}, {3.0d, 4.0d}}, new double[]{5.0d, 7.0d});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[][]{{1.0d, 1.0d}, {1.0d, 1.0d}}, Relationship.LEQ, new double[]{1.0d, 1.0d}),
                new LinearConstraint(new double[][]{{2.0d, 1.0d}, {3.0d, 1.0d}}, Relationship.LEQ, new double[]{Double.NEGATIVE_INFINITY, 3.0d})
        };

        SimplexSolver solver = new SimplexSolver();

        try {
            solver.optimize(f, constraints, null);
            Assert.fail();
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0d, 2.0d}, {3.0d, 4.0d}}, new double[]{5.0d, 7.0d});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[][]{{1.0d, 1.0d}, {1.0d, 1.0d}}, Relationship.LEQ, new double[]{1.0d, 1.0d}),
                new LinearConstraint(new double[][]{{2.0d, 1.0d}, {3.0d, 1.0d}}, Relationship.LEQ, new double[]{Double.NEGATIVE_INFINITY, 1.0d})
        };

        SimplexSolver solver = new SimplexSolver();

        try {
            solver.optimize(f, constraints, null);
            Assert.fail();
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }
}