import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.linear.SimplexTableau.RHS_OFFSET;
import static org.apache.commons.math3.optim.linear.SimplexTableau.objFunctionOffset;

public class SimplexSolverTest {

    @Test
    public void testSimplexSolverConstructorWithDefaultEpsilon() {
        SimplexSolver simplexSolver = new SimplexSolver();
        Assert.assertEquals(1.0e-6, simplexSolver.epsilon, 0);
    }

    @Test
    public void testSimplexSolverConstructorWithCustomEpsilon() {
        double customEpsilon = 0.001;
        SimplexSolver simplexSolver = new SimplexSolver(customEpsilon);
        Assert.assertEquals(customEpsilon, simplexSolver.epsilon, 0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2, -1, 3}),
                new LinearConstraint[]{}, LinearObjectiveFunction.Type.MINIMIZE, false, 1e-6);
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        Assert.assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRowWithPositiveEntry() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2, -1, 3}),
                new LinearConstraint[]{}, LinearObjectiveFunction.Type.MINIMIZE, false, 1e-6);
        Integer pivotRow = new SimplexSolver().getPivotRow(1, tableau);
        Assert.assertEquals(1, pivotRow.intValue());
    }

    @Test(expected = OptimizationException.class)
    public void testGetPivotRowWithNegativeEntry() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2, 1, 3}),
                new LinearConstraint[]{}, LinearObjectiveFunction.Type.MINIMIZE, false, 1e-6);
        new SimplexSolver().getPivotRow(1, tableau);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2, -1, 3}),
                new LinearConstraint[]{}, LinearObjectiveFunction.Type.MINIMIZE, false, 1e-6);
        new SimplexSolver().doIteration(tableau);

        // Check if the pivot element is 1
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i == 1) {
                Assert.assertEquals(1, tableau.getEntry(i, tableau.getWidth() - 1), 0);
            } else {
                Assert.assertEquals(0, tableau.getEntry(i, tableau.getWidth() - 1), 0);
            }
        }

        // Check if the rest of the pivot column is 0
        for (int j = 0; j < tableau.getWidth(); j++) {
            if (j != tableau.getWidth() - 1) {
                Assert.assertEquals(0, tableau.getEntry(1, j), 0);
            }
        }
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationWithUnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2, -1, 3}),
                new LinearConstraint[]{}, LinearObjectiveFunction.Type.MINIMIZE, false, 1e-6);
        new SimplexSolver().doIteration(tableau);
        new SimplexSolver().doIteration(tableau);
        new SimplexSolver().doIteration(tableau);
        new SimplexSolver().doIteration(tableau);
        new SimplexSolver().doIteration(tableau);

        new SimplexSolver().doIteration(tableau); // This should throw an exception
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2, -1, 3}),
                new LinearConstraint[]{}, LinearObjectiveFunction.Type.MINIMIZE, false, 1e-6);
        tableau.setEntry(1, objFunctionOffset, -1);
        Assert.assertFalse(new SimplexSolver().isPhase1Solved(tableau));

        tableau.setEntry(1, objFunctionOffset, 0);
        for (int i = 0; i < tableau.getWidth() - 1; i++) {
            tableau.setEntry(i + tableau.getNumObjectiveFunctions(), i, 0);
        }
        Assert.assertTrue(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testIsOptimalWithInfeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2, -1, 3}),
                new LinearConstraint[]{}, LinearObjectiveFunction.Type.MINIMIZE, false, 1e-6);
        tableau.setEntry(0, RHS_OFFSET, 10);
        new SimplexSolver().isOptimal(tableau);
    }

    @Test(expected = OptimizationException.class)
    public void testIsOptimalWithUnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2, -1, 3}),
                new LinearConstraint[]{}, LinearObjectiveFunction.Type.MINIMIZE, false, 1e-6);
        tableau.setEntry(0, objFunctionOffset, Double.NEGATIVE_INFINITY);
        new SimplexSolver().isOptimal(tableau);
    }

    @Test
    public void testOptimize() throws OptimizationException {
        LinearObjectiveFunction linearObjectiveFunction = new LinearObjectiveFunction(new double[]{-2, -1, 3});
        LinearConstraint[] linearConstraints = new LinearConstraint[0];
        SimplexTableau tableau = new SimplexTableau(linearObjectiveFunction, linearConstraints,
                LinearObjectiveFunction.Type.MINIMIZE, false, 1e-6);
        PointValuePair solution = new SimplexSolver().doOptimize();
        Assert.assertEquals(0, solution.getPoint()[0], 0);
        Assert.assertEquals(0, solution.getValue(), 0.01);
    }
}