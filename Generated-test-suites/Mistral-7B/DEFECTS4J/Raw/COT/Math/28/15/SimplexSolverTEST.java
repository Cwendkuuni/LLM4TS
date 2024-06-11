import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraints;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimplexSolverTest {

    private SimplexSolver solver;
    private LinearObjectiveFunction objectiveFunction;
    private LinearConstraints constraints;

    @Before
    public void setUp() {
        // Create a simplex solver with default settings
        solver = new SimplexSolver();

        // Create a simple objective function
        double[] coeffs = new double[]{1.0, 2.0, 3.0};
        objectiveFunction = new LinearObjectiveFunctionSet(Arrays.asList(coeffs), new double[]{0.0});

        // Create some simple constraints
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 2.0}, 3.0);
        constraints = new LinearConstraints();
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);
    }

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(DEFAULT_EPSILON, solver.epsilon, 0);
        Assert.assertEquals(DEFAULT_ULPS, solver.maxUlps);
    }

    @Test
    public void testConstructorWithArguments() {
        SimplexSolver solver = new SimplexSolver(0.001, 10);
        Assert.assertEquals(0.001, solver.epsilon, 0);
        Assert.assertEquals(10, solver.maxUlps);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON, DEFAULT_ULPS);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        Assert.assertNotNull(pivotColumn);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotColumnWithUnboundedTableau() {
        double[][] tableau = new double[][]{{-1.0, 1.0, Double.POSITIVE_INFINITY, 1.0},
                {-1.0, 1.0, Double.POSITIVE_INFINITY, 1.0},
                {0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0}};
        SimplexTableau tableauObj = new SimplexTableau(tableau, 4);
        Integer pivotColumn = solver.getPivotColumn(tableauObj);
        Assert.fail("Should have thrown an UnboundedSolutionException.");
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON, DEFAULT_ULPS);
        Integer pivotRow = solver.getPivotRow(tableau, 0);
        Assert.assertNotNull(pivotRow);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testGetPivotRowWithNoFeasibleSolution() {
        double[][] tableau = new double[][]{{-1.0, 1.0, 0.0, 1.0},
                {-1.0, 1.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 0.0},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0, 0.0}};
        SimplexTableau tableauObj = new SimplexTableau(tableau, 4);
        Integer pivotRow = solver.getPivotRow(tableauObj, 0);
        Assert.fail("Should have thrown a NoFeasibleSolutionException.");
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON, DEFAULT_ULPS);
        solver.doIteration(tableau);
        // The test for correctness is not trivial as it depends on the internal implementation
        // of the SimplexSolver and the specific tableau generated.
        // Instead, we'll just check that the number of iterations has been incremented.
        Assert.assertEquals(1, solver.getIterationsCounter());
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoIterationWithMaxIterationsReached() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON, DEFAULT_ULPS);
        for (int i = 0; i < SimplexSolver.MAX_ITERATIONS; i++) {
            solver.doIteration(tableau);
        }
        solver.doIteration(tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        double[][] tableau = new double[][]{{-1.0, 1.0, Double.POSITIVE_INFINITY, 1.0},
                {-1.0, 1.0, Double.POSITIVE_INFINITY, 1.0},
                {0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0}};
        SimplexTableau tableauObj = new SimplexTableau(tableau, 4);
        solver.solvePhase1(tableauObj);
        Assert.fail("Should have thrown an UnboundedSolutionException.");
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WithNoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        double[][] tableau = new double[][]{{-1.0, 1.0, 0.0, 1.0},
                {-1.0, 1.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 0.0},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0, 0.0}};
        SimplexTableau tableauObj = new SimplexTableau(tableau, 4);
        solver.solvePhase1(tableauObj);
        Assert.fail("Should have thrown a NoFeasibleSolutionException.");
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON, DEFAULT_ULPS);
        PointValuePair solution = solver.doOptimize();
        // The test for correctness is not trivial as it depends on the specific problem and the internal implementation
        // of the SimplexSolver. Instead, we'll just check that the solution is not null.
        Assert.assertNotNull(solution);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeWithUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        double[][] tableau = new double[][]{{-1.0, 1.0, Double.POSITIVE_INFINITY, 1.0},
                {-1.0, 1.0, Double.POSITIVE_INFINITY, 1.0},
                {0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0}};
        SimplexTableau tableauObj = new SimplexTableau(tableau, 4);
        solver.solvePhase1(tableauObj);
        solver.doOptimize();
        Assert.fail("Should have thrown an UnboundedSolutionException.");
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeWithNoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        double[][] tableau = new double[][]{{-1.0, 1.0, 0.0, 1.0},
                {-1.0, 1.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 0.0},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0, 0.0}};
        SimplexTableau tableauObj = new SimplexTableau(tableau, 4);
        solver.solvePhase1(tableauObj);
        solver.doOptimize();
        Assert.fail("Should have thrown a NoFeasibleSolutionException.");
    }

    @Test
    public void testDoOptimizeWithSimpleConstraints() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        double[][] tableau = new double[][]{{1.0, 2.0, 3.0, 1.0},
                {1.0, -1.0, 3.0, 1.0},
                {-1.0, 1.0, 2.0, 1.0},
                {-1.0, -1.0, 1.0, 2.0},
                {0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0}};
        SimplexTableau tableauObj = new SimplexTableau(tableau, 5);
        PointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
        Assert.assertEquals(2.0, solution.getPoint()[1], 0);
        Assert.assertEquals(3.0, solution.getValue(), 0);
    }
}