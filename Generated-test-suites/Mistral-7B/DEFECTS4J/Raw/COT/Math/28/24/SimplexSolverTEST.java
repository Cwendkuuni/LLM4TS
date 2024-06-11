import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.simple.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.nonlinear.simple.UnboundedSolutionException;
import org.apache.commons.math3.optim.simple.MaxCountExceededException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;
    private LinearObjectiveFunction objectiveFunction;
    private LinearConstraint[] constraints;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver();
        objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{3.0, 4.0});
        constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(new double[]{1.0, 1.0}, 5.0, '<');
        constraints[1] = new LinearConstraint(new double[]{-1.0, 1.0}, 2.0, '>');
    }

    @Test
    public void testConstruction() {
        Assert.assertNotNull(simplexSolver);
    }

    @Test
    public void testDoOptimize_GivenPositiveInput_ShouldReturnOptimalSolution() throws Exception {
        PointValuePair result = simplexSolver.doOptimize();
        Assert.assertNotNull(result);
        Assert.assertEquals(0.0, result.getPoint()[0], 0.0);
        Assert.assertEquals(5.0, result.getValue(), 0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoOptimize_MaxIterationsExceeded() throws Exception {
        simplexSolver = new SimplexSolver(1, 1);
        PointValuePair result = simplexSolver.doOptimize();
        Assert.assertNotNull(result);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_UnboundedSolution() throws Exception {
        LinearObjectiveFunction objectiveFunctionUnbounded = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0, 4.0});
        simplexSolver = new SimplexSolver();
        simplexSolver.doOptimize(objectiveFunctionUnbounded, constraints);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_NoFeasibleSolution() throws Exception {
        LinearObjectiveFunction objectiveFunctionNoFeasible = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{5.0, 4.0});
        simplexSolver = new SimplexSolver();
        simplexSolver.doOptimize(objectiveFunctionNoFeasible, constraints);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON, DEFAULT_ULPS);
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        Assert.assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON, DEFAULT_ULPS);
        Integer pivotRow = simplexSolver.getPivotRow(tableau, 1);
        Assert.assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() throws Exception {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON, DEFAULT_ULPS);
        simplexSolver.doIteration(tableau);

        double[] expectedPoint = {1.0 / 3.0, 1.0};
        Assert.assertArrayEquals(expectedPoint, tableau.getBasicSolution(), 0.0);
        Assert.assertEquals(5.0 / 3.0, tableau.getObjValue(), 0.0);
    }

    @Test
    public void testSolvePhase1() throws Exception {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON, DEFAULT_ULPS);
        simplexSolver.solvePhase1(tableau);

        double[] expectedPoint = {1.0 / 3.0, 1.0};
        Assert.assertArrayEquals(expectedPoint, tableau.getBasicSolution(), 0.0);
        Assert.assertEquals(5.0 / 3.0, tableau.getObjValue(), 0.0);
    }
}