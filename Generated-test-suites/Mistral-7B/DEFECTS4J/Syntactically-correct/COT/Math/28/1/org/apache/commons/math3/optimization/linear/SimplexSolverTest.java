import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(1.0e-6, solver.epsilon, 0);
        Assert.assertEquals(10, solver.maxUlps);

        SimplexSolver solverWithCustomSettings = new SimplexSolver(0.001, 5);
        Assert.assertEquals(0.001, solverWithCustomSettings.epsilon, 0);
        Assert.assertEquals(5, solverWithCustomSettings.maxUlps);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(createMockObjectiveFunction(), new LinearConstraint[0], null, false, 1.0e-6, 10);

        int pivotColumn = SimplexSolver.getInstance().getPivotColumn(tableau);
        Assert.assertNotNull(pivotColumn);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotColumnWithUnboundedSolution() {
        SimplexTableau tableau = new SimplexTableau(createMockObjectiveFunction(), new LinearConstraint[0], null, false, 1.0e-6, 10);
        tableau.setEntry(0, 0, Double.NEGATIVE_INFINITY);

        SimplexSolver.getInstance().getPivotColumn(tableau);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(createMockObjectiveFunction(), new LinearConstraint[0], null, false, 1.0e-6, 10);

        Integer pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 0);
        Assert.assertNotNull(pivotRow);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testGetPivotRowWithNoFeasibleSolution() {
        SimplexTableau tableau = new SimplexTableau(createMockObjectiveFunction(), new LinearConstraint[0], null, false, 1.0e-6, 10);
        tableau.setEntry(0, tableau.getWidth() - 1, Double.POSITIVE_INFINITY);

        SimplexSolver.getInstance().getPivotRow(tableau, 0);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(createMockObjectiveFunction(), new LinearConstraint[0], null, false, 1.0e-6, 10);

        Integer pivotCol = 0;
        Integer pivotRow = 0;
        SimplexSolver.getInstance().getPivotColumn(tableau);
        SimplexSolver.getInstance().getPivotRow(tableau, pivotCol);

        SimplexSolver.getInstance().doIteration(tableau);

        // check if the pivot element has been set to 1
        Assert.assertEquals(1.0, tableau.getEntry(pivotRow, pivotCol), 0);

        // check if the rest of the pivot column has been set to 0
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != pivotRow) {
                Assert.assertEquals(0.0, tableau.getEntry(i, pivotCol), 0);
            }
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoIterationWithMaxCountExceeded() throws MaxCountExceededException {
        SimplexTableau tableau = new SimplexTableau(createMockObjectiveFunction(), new LinearConstraint[0], null, false, 1.0e-6, 10);
        tableau.setIterationsCounter(1000);

        SimplexSolver.getInstance().doIteration(tableau);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(createMockObjectiveFunction(), new LinearConstraint[0], null, false, 1.0e-6, 10);

        SimplexSolver.getInstance().solvePhase1(tableau);

        // check if the tableau is optimal
        Assert.assertEquals(0.0, tableau.getEntry(0, tableau.getWidth() - 1), 0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSolvePhase1WithMaxCountExceeded() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(createMockObjectiveFunction(), new LinearConstraint[0], null, false, 1.0e-6, 10);
        tableau.setIterationsCounter(1000);

        SimplexSolver.getInstance().solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objectiveFunction = createMockObjectiveFunction();
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, false, 1.0e-6, 10);

        PointValuePair result = solver.doOptimize();

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getPoint());
        Assert.assertNotNull(result.getValue());
    }

    private LinearObjectiveFunction createMockObjectiveFunction() {
        return new LinearObjectiveFunction() {
            @Override
            public int getNumberOfVariables() {
                return 3;
            }

            @Override
            public double value(double[] x) {
                return x[0] + 2 * x[1] + 3 * x[2];
            }

            @Override
            public int getNumberOfConstraints() {
                return 0;
            }

            @Override
            public LinearConstraint getConstraint(int i) {
                throw new IndexOutOfBoundsException();
            }
        };
    }
}