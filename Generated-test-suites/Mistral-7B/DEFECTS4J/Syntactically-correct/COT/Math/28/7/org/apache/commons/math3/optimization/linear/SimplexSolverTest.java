import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;
    private LinearObjectiveFunction objectiveFunction;
    private LinearConstraintSet constraints;

    @Before
    public void setup() {
        simplexSolver = new SimplexSolver();

        double[] coefficients = {1.0, 2.0, -3.0};
        double constant = 5.0;
        objectiveFunction = new LinearObjectiveFunction(coefficients, constant);

        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 2.0}, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 1.0}, 2.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{3.0, -1.0}, 4.0);
        constraints = new LinearConstraintSet(Arrays.asList(constraint1, constraint2, constraint3));
    }

    @Test
    public void testEmptyConstraints() {
        LinearConstraintSet emptyConstraints = new LinearConstraintSet();
        try {
            simplexSolver.doOptimize();
            Assert.fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testSolveBasicExample() {
        PointValuePair solution = simplexSolver.doOptimize();
        double[] x = solution.getPoint();
        double fx = solution.getValue();

        Assert.assertArrayEquals(new double[]{2.0 / 3.0, 1.0}, x, 0.1);
        Assert.assertEquals(1.0 / 3.0 * 2.0 + 1.0, fx, 0.1);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        double[] coefficients = {1.0, 2.0, -1.0};
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(coefficients, Double.POSITIVE_INFINITY);
        simplexSolver = new SimplexSolver(Double.POSITIVE_INFINITY, Integer.MAX_VALUE);
        simplexSolver.doOptimize();
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws InterruptedException {
        simplexSolver = new SimplexSolver(1.0e-12, Integer.MAX_VALUE);
        simplexSolver.setMaxIterations(1);
        simplexSolver.doOptimize();
    }

    @Test
    public void testNegativeConstraints() {
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 2.0}, -3.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 1.0}, -2.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{3.0, -1.0}, -4.0);
        LinearConstraintSet constraints = new LinearConstraintSet(Arrays.asList(constraint1, constraint2, constraint3));

        PointValuePair solution = simplexSolver.doOptimize();
        double[] x = solution.getPoint();
        double fx = solution.getValue();

        Assert.assertEquals(-2.0 / 3.0, x[0], 0.1);
        Assert.assertEquals(-1.0 / 3.0, x[1], 0.1);
        Assert.assertEquals(1.0 / 3.0 * (-2.0) + (-1.0), fx, 0.1);
    }

    @Test
    public void testOptimizeWithDegeneracy() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 0.0}, 0.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 0.0}, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 1.0}, 1.0);
        LinearConstraintSet constraints = new LinearConstraintSet(Arrays.asList(constraint1, constraint2));

        PointValuePair solution = simplexSolver.doOptimize();
        double[] x = solution.getPoint();
        double fx = solution.getValue();

        Assert.assertEquals(0.0, x[0], 0.1);
        Assert.assertEquals(1.0, x[1], 0.1);
        Assert.assertEquals(0.0, fx, 0.1);
    }

    @Test
    public void testNoSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 0.0}, Double.POSITIVE_INFINITY);
        LinearConstraintSet constraints = new LinearConstraintSet();

        try {
            simplexSolver.doOptimize();
            Assert.fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testFeasibleSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{0.0, 1.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet(Arrays.asList(new LinearConstraint(new double[]{1.0, 1.0}, 0.0)));

        PointValuePair solution = simplexSolver.doOptimize();
        double[] x = solution.getPoint();
        double fx = solution.getValue();

        Assert.assertEquals(1.0, x[0], 0.1);
        Assert.assertEquals(1.0, x[1], 0.1);
        Assert.assertEquals(0.0, fx, 0.1);
    }
}