import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SimplexSolverTest {

    @Test
    public void testConstructorWithDefaultSettings() {
        SimplexSolver simplexSolver = new SimplexSolver();
        Assert.assertEquals(SimplexSolver.DEFAULT_EPSILON, simplexSolver.epsilon, 0.0);
    }

    @Test
    public void testConstructorWithSpecifiedSettings() {
        double epsilon = 1.0e-8;
        SimplexSolver simplexSolver = new SimplexSolver(epsilon);
        Assert.assertEquals(epsilon, simplexSolver.epsilon, 0.0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotColumnWithUnboundedTableau() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, -1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, null, null, null, Double.POSITIVE_INFINITY)});
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.doIteration(tableau);
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        Assert.assertNull(pivotColumn);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPivotRowWithNullTableau() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = null;
        simplexSolver.getPivotRow(0, tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationWithUnboundedTableau() throws OptimizationException {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, -1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, null, null, null, Double.POSITIVE_INFINITY)});
        simplexSolver.doIteration(tableau);
    }

    @Test
    public void testIsPhase1SolvedWithSolvedTableau() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{0.0, 0.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, null, null, null, 0.0)});
        SimplexSolver simplexSolver = new SimplexSolver();
        Assert.assertTrue(simplexSolver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsPhase1SolvedWithUnsolvedTableau() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, -1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, null, null, null, 0.0)});
        SimplexSolver simplexSolver = new SimplexSolver();
        Assert.assertFalse(simplexSolver.isPhase1Solved(tableau));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testIsOptimalWithUnfeasibleTableau() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{0.0, 0.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, null, null, null, Double.POSITIVE_INFINITY)});
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.solvePhase1(tableau);
        Assert.assertFalse(simplexSolver.isOptimal(tableau));
    }

    @Test
    public void testIsOptimalWithOptimalTableau() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{0.0, 0.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, null, null, null, 0.0)});
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.solvePhase1(tableau);
        simplexSolver.doIteration(tableau);
        Assert.assertTrue(simplexSolver.isOptimal(tableau));
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolvePhase1WithMaxIterationsExceeded() throws OptimizationException {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, -1.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, null, null, null, 0.0);
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraint1);
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.setMaxIterations(0);
        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 1.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, null, null, null, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 1.0}, 0.0, null, null, null, 0.0);
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraint1, constraint2);
        SimplexSolver simplexSolver = new SimplexSolver();
        RealPointValuePair solution = simplexSolver.doOptimize();
        PointValuePair expectedSolution = new PointValuePair(new double[]{1.0 / 3.0, 1.0 / 3.0}, 1.0 / 3.0);
        Assert.assertEquals(expectedSolution.getValue(), solution.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 0.0);
    }

}