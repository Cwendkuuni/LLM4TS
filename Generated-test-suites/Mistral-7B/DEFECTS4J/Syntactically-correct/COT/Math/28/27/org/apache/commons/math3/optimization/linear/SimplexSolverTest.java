import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearPointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(1.0e-6, solver.getEpsilon(), 0);
        Assert.assertEquals(10, solver.getMaxUlps());
    }

    @Test
    public void testConstructorWithArgs() {
        double epsilon = 1.1e-5;
        int maxUlps = 5;
        SimplexSolver solver = new SimplexSolver(epsilon, maxUlps);
        Assert.assertEquals(epsilon, solver.getEpsilon(), 0);
        Assert.assertEquals(maxUlps, solver.getMaxUlps());
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1, 1}, 0, 1),
                        new LinearConstraint(new double[]{1, 1}, 0, Double.POSITIVE_INFINITY)});

        Integer pivotColumn = SimplexSolver.getInstance().getPivotColumn(tableau);
        Assert.assertEquals(0, pivotColumn.intValue());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotRowWithUnboundedSolutionException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, 1}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1, 1}, 0, Double.POSITIVE_INFINITY),
                        new LinearConstraint(new double[]{1, 1}, 0, 1)});
        SimplexSolver.getInstance().getPivotRow(tableau, 0);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testGetPivotRowWithNoFeasibleSolutionException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -1}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1, 1}, 0, 1),
                        new LinearConstraint(new double[]{1, 1}, 0, 1)});
        SimplexSolver.getInstance().getPivotRow(tableau, 0);
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1, 1}, 0, 1),
                        new LinearConstraint(new double[]{1, 1}, 0, Double.POSITIVE_INFINITY)});

        SimplexSolver solver = SimplexSolver.getInstance();

        solver.setEpsilon(1.1e-5);
        solver.setMaxUlps(5);

        Integer pivotCol = solver.getPivotColumn(tableau);
        Integer pivotRow = solver.getPivotRow(tableau, pivotCol.intValue());

        solver.doIteration(tableau);

        Assert.assertNotEquals(pivotVal, tableau.getEntry(pivotRow, pivotCol), 0);
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != pivotRow) {
                Assert.assertTrue(Math.abs(tableau.getEntry(i, pivotCol)) < 1.1e-5);
            }
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoIterationWithMaxCountExceededException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1, 1}, 0, 1),
                        new LinearConstraint(new double[]{1, 1}, 0, Double.POSITIVE_INFINITY)});

        SimplexSolver solver = SimplexSolver.getInstance();

        solver.setIterationLimit(1);

        solver.doOptimize();

        solver.doIteration(tableau);
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1, 1}, 0, 1),
                        new LinearConstraint(new double[]{1, 1}, 0, Double.POSITIVE_INFINITY)});

        SimplexSolver solver = SimplexSolver.getInstance();

        solver.solvePhase1(tableau);

        Assert.assertFalse(tableau.isOptimal());
        Assert.assertFalse(Precision.equals(tableau.getEntry(0, tableau.getRhsOffset()), 0d, solver.getEpsilon()));
    }

    @Test
    public void testDoOptimize() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1, -2});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1, 1}, 0, 1),
                new LinearConstraint(new double[]{1, 1}, 0, Double.POSITIVE_INFINITY)};

        SimplexSolver solver = SimplexSolver.getInstance();

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, solver.getEpsilon(), solver.getMaxUlps());
        solver.solvePhase1(tableau);
        tableau.dropPhase1Objective();

        LinearPointValuePair solution = solver.doOptimize();

        Assert.assertTrue(tableau.isOptimal());
        Assert.assertFalse(Double.isNaN(solution.getPoint()[0]));
        Assert.assertFalse(Double.isNaN(solution.getValue()));
    }
}