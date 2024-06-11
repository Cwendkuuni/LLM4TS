import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;
    private LinearObjectiveFunction objectiveFunction;
    private LinearConstraint[] constraints;

    @Before
    public void setup() {
        simplexSolver = new SimplexSolver();
        objectiveFunction = new LinearObjectiveFunction(new double[]{-2.0, 5.0}, new double[]{1.0, 1.0});
        constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 1.0),
            new LinearConstraint(new double[]{1.0, 1.0}, Relationship.LEQ, 3.0)
        };
    }

    @Test
    public void testEmptyConstraints() {
        PointValuePair solution = simplexSolver.optimize(objectiveFunction);
        Assert.assertEquals(0.0, solution.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, solution.getPoint(), 0.0);
    }

    @Test
    public void testSingleConstraint() {
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, Relationship.EQ, 1.0);
        constraints = new LinearConstraint[]{constraint};
        PointValuePair solution = simplexSolver.optimize(objectiveFunction, constraints);
        Assert.assertEquals(5.0, solution.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{5.0 / 6.0, 1.0 - 5.0 / 6.0}, solution.getPoint(), 0.0);
    }

    @Test
    public void testMultipleConstraints() {
        PointValuePair solution = simplexSolver.optimize(objectiveFunction, constraints);
        Assert.assertEquals(3.0, solution.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{3.0 / 4.0, 1.0 - 3.0 / 4.0}, solution.getPoint(), 0.0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction unboundedObjectiveFunction = new LinearObjectiveFunction(new double[]{2.0, 3.0}, new double[]{1.0, 1.0});
        simplexSolver.optimize(unboundedObjectiveFunction);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        SimplexSolver simplexSolverMaxIter = new SimplexSolver(1, 0);
        simplexSolverMaxIter.optimize(objectiveFunction);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction noFeasibleSolutionObjectiveFunction = new LinearObjectiveFunction(new double[]{-2.0, 5.0}, new double[]{1.0, 1.0});
        constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0}, Relationship.LEQ, 0.0)
        };
        simplexSolver.optimize(noFeasibleSolutionObjectiveFunction, constraints);
    }
}