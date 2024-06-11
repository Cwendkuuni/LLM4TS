import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.linear.LinearConstraint.Type.EQ;
import static org.apache.commons.math3.optim.linear.LinearConstraint.Type.LE;
import static org.apache.commons.math3.optim.linear.LinearConstraint.Type.GE;
import static org.apache.commons.math3.optim.linear.LinearObjectiveFunction.Direction.MAXIMIZE;
import static org.apache.commons.math3.optim.linear.LinearObjectiveFunction.Direction.MINIMIZE;

public class SimplexSolverTest {

    @Test
    public void testConstructorWithDefaultSettings() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(1.0e-6, solver.epsilon, 0);
    }

    @Test
    public void testConstructorWithCustomSettings() {
        double customEpsilon = 1.0e-8;
        SimplexSolver solver = new SimplexSolver(customEpsilon);
        Assert.assertEquals(customEpsilon, solver.epsilon, 0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotColumnThrowsUnboundedSolutionExceptionWhenNoNegativeCoefficients() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(MINIMIZE, new double[]{-1, 0}),
                new LinearConstraint[]{new LinearConstraint(LE, 0, new double[]{0, 1})}, 1, null, 1.0e-6);
        SimplexSolver solver = new SimplexSolver();
        solver.getPivotColumn(tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotRowThrowsUnboundedSolutionExceptionWhenNegativeEntryAndPositiveRHS() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(MINIMIZE, new double[]{-1, 0}),
                new LinearConstraint[]{
                        new LinearConstraint(LE, 0, new double[]{0, 1}),
                        new LinearConstraint(LE, 1, new double[]{Double.POSITIVE_INFINITY, 2})}
                , 1, null, 1.0e-6);
        int pivotCol = solver.getPivotColumn(tableau).intValue();
        Integer pivotRow = solver.getPivotRow(pivotCol, tableau);
        Assert.assertNull(pivotRow);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(MINIMIZE, new double[]{-3, 1}),
                new LinearConstraint[]{
                        new LinearConstraint(LE, 0, new double[]{0, 1}),
                        new LinearConstraint(LE, 1, new double[]{1, 1}),
                        new LinearConstraint(GE, 1, new double[]{2, 1})}
                , 1, null, 1.0e-6);
        SimplexSolver solver = new SimplexSolver();
        Integer pivotCol = solver.getPivotColumn(tableau);
        Integer pivotRow = solver.getPivotRow(pivotCol.intValue(), tableau);
        double pivotVal = tableau.getEntry(pivotRow.intValue(), pivotCol.intValue());
        solver.doIteration(tableau);
        Assert.assertEquals(1.0 / pivotVal, tableau.getEntry(pivotRow.intValue(), pivotCol.intValue()), 0);
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != pivotRow.intValue()) {
                double multiplier = tableau.getEntry(i, pivotCol.intValue());
                Assert.assertEquals(-multiplier / pivotVal, tableau.getEntry(i, pivotCol.intValue()), 0);
            }
        }
    }

    @Test
    public void testIsPhase1Solved() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(MINIMIZE, new double[]{-3, 1}),
                new LinearConstraint[]{
                        new LinearConstraint(LE, 0, new double[]{0, 1}),
                        new LinearConstraint(LE, 1, new double[]{1, 1}),
                        new LinearConstraint(GE, 1, new double[]{2, 1})}
                , 1, null, 1.0e-6);
        SimplexSolver solver = new SimplexSolver();
        Assert.assertFalse(solver.isPhase1Solved(tableau));

        solver.doIteration(tableau);
        Assert.assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1ThrowsNoFeasibleSolutionExceptionWhenRHSIsNotZero() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(MINIMIZE, new double[]{-3, 1}),
                new LinearConstraint[]{
                        new LinearConstraint(LE, 0, new double[]{0, 1}),
                        new LinearConstraint(LE, 1, new double[]{1, 1}),
                        new LinearConstraint(GE, 1, new double[]{-1, 1})}
                , 1, null, 1.0e-6);
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
    }

    @Test
    public void testIsOptimal() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(MINIMIZE, new double[]{-3, 1}),
                new LinearConstraint[]{
                        new LinearConstraint(LE, 0, new double[]{0, 1}),
                        new LinearConstraint(LE, 1, new double[]{1, 1}),
                        new LinearConstraint(GE, 1, new double[]{2, 1})}
                , 1, null, 1.0e-6);
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        tableau.discardArtificialVariables();
        Assert.assertTrue(solver.isOptimal(tableau));

        tableau.setEntry(1, 1, tableau.getEntry(1, 1) + 1.0e-5);
        Assert.assertFalse(solver.isOptimal(tableau));
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(MINIMIZE, new double[]{-3, 1}),
                new LinearConstraint[]{
                        new LinearConstraint(LE, 0, new double[]{0, 1}),
                        new LinearConstraint(LE, 1, new double[]{1, 1}),
                        new LinearConstraint(GE, 1, new double[]{2, 1})}
                , 1, null, 1.0e-6);
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize();
        Assert.assertEquals(0.0, solution.getPoint()[0], 1.0e-6);
        Assert.assertEquals(-3.0, solution.getValue(), 1.0e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeThrowsUnboundedSolutionExceptionWhenProblemHasNoSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(MINIMIZE, new double[]{-3, 1}),
                new LinearConstraint[]{
                        new LinearConstraint(LE, 0, new double[]{0, 1}),
                        new LinearConstraint(LE, 1, new double[]{1, 1}),
                        new LinearConstraint(GE, 1, new double[]{Double.POSITIVE_INFINITY, 1})}
                , 1, null, 1.0e-6);
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

}