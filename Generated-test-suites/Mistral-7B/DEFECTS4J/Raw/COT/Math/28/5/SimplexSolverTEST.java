import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleTableau;
import org.apache.commons.math3.optimization.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(1.0e-6, solver.epsilon, 0.0);
        Assert.assertEquals(10, solver.maxUlps);
    }

    @Test
    public void testCustomConstructor() {
        double epsilon = 1.0e-8;
        int maxUlps = 5;
        SimplexSolver solver = new SimplexSolver(epsilon, maxUlps);
        Assert.assertEquals(epsilon, solver.epsilon, 0.0);
        Assert.assertEquals(maxUlps, solver.maxUlps);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(1, 3);
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        Assert.assertNotNull(pivotColumn);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotColumnUnboundedSolution() {
        SimplexTableau tableau = new SimplexTableau(1, 3);
        new SimplexSolver().getPivotColumn(tableau);
        tableau.setEntry(0, tableau.getWidth() - 1, Double.POSITIVE_INFINITY);
        new SimplexSolver().getPivotColumn(tableau);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(3, 3);
        Integer pivotRow = new SimplexSolver().getPivotRow(tableau, 0);
        Assert.assertNotNull(pivotRow);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testGetPivotRowNoFeasibleSolution() {
        SimplexTableau tableau = new SimplexTableau(3, 3);
        new SimplexSolver().getPivotRow(tableau, 2);
        tableau.setEntry(tableau.getHeight() - 1, tableau.getWidth() - 1, Double.POSITIVE_INFINITY);
        new SimplexSolver().getPivotRow(tableau, 2);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(3, 3);
        new SimplexSolver().doIteration(tableau);
        Assert.assertFalse(tableau.isOptimal());
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoIterationMaxCountExceeded() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(3, 3);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            new SimplexSolver().doIteration(tableau);
        }
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objectiveFunction = new Array2DRowRealVectorFunction(new double[][]{{1.0, 2.0, -3.0}}, new int[]{0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new Array2DRowRealVector(new double[][]{{-1.0, 1.0}, {-1.0, 1.0}}), Relationship.LEQ, 1.0),
                new LinearConstraint(new Array2DRowRealVector(new double[][]{{-1.0, -1.0}, {1.0, 1.0}}), Relationship.LEQ, 1.0)
        };
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, null, 1.0e-6, 10);
        new SimplexSolver().solvePhase1(tableau);
        Assert.assertTrue(tableau.isOptimal());
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objectiveFunction = new Array2DRowRealVectorFunction(new double[][]{{1.0, 2.0, -3.0}}, new int[]{0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new Array2DRowRealVector(new double[][]{{-1.0, 1.0}, {-1.0, 1.0}}), Relationship.LEQ, 1.0),
                new LinearConstraint(new Array2DRowRealVector(new double[][]{{-1.0, -1.0}, {1.0, 1.0}}), Relationship.LEQ, 1.0),
                new LinearConstraint(new Array2DRowRealVector(new double[][]{{0.0, 0.0}}), Relationship.EQ, 0.0)
        };
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, null, 1.0e-6, 10);
        new SimplexSolver().solvePhase1(tableau);
        Assert.assertFalse(tableau.isOptimal());
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objectiveFunction = new Array2DRowRealVectorFunction(new double[][]{{1.0, 2.0, -3.0}}, new int[]{0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new Array2DRowRealVector(new double[][]{{-1.0, 1.0}, {-1.0, 1.0}}), Relationship.LEQ, 1.0),
                new LinearConstraint(new Array2DRowRealVector(new double[][]{{-1.0, -1.0}, {1.0, 1.0}}), Relationship.LEQ, 1.0)
        };
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, null, 1.0e-6, 10);
        PointValuePair solution = new SimplexSolver().doOptimize();
        Assert.assertNotNull(solution);
        Assert.assertFalse(Double.isNaN(solution.getValue()));
    }

}