import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testSimplexSolverConstruction() {
        SimplexSolver solver = new SimplexSolver();
        SimplexSolver solverWithEpsilon = new SimplexSolver(0.001);
        assertEquals(DEFAULT_EPSILON, solver.epsilon, 0.0);
        assertEquals(0.001, solverWithEpsilon.epsilon, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimplexSolverNegativeEpsilon() {
        new SimplexSolver(-0.01);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(
            new LinearObjectiveFunction(new double[]{-1, -2}),
            new LinearConstraint[]{},
            null,
            false,
            DEFAULT_EPSILON
        );
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotColumnNegativeCoefficient() {
        SimplexTableau tableau = new SimplexTableau(
            new LinearObjectiveFunction(new double[]{-1, 2}),
            new LinearConstraint[]{},
            null,
            false,
            DEFAULT_EPSILON
        );
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotColumnZeroCoefficient() {
        SimplexTableau tableau = new SimplexTableau(
            new LinearObjectiveFunction(new double[]{0, -2}),
            new LinearConstraint[]{},
            null,
            false,
            DEFAULT_EPSILON
        );
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertNull(pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(
            new LinearObjectiveFunction(new double[]{-1, -2}),
            new LinearConstraint[]{},
            null,
            false,
            DEFAULT_EPSILON
        );
        Integer pivotRow = new SimplexSolver().getPivotRow(0, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testGetPivotRowZeroEntry() {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{0, -2}),
                new LinearConstraint[]{},
                null,
                false,
                DEFAULT_EPSILON
        );
        Integer pivotRow = new SimplexSolver().getPivotRow(0, tableau);
        assertNull(pivotRow);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, -2}),
                new LinearConstraint[]{},
                null,
                false,
                DEFAULT_EPSILON
        );
        new SimplexSolver().doIteration(tableau);
        assertEquals(-1, tableau.getEntry(1, 0), epsilon);
        assertEquals(0, tableau.getEntry(1, 1), epsilon);
        assertEquals(1, tableau.getEntry(1, 2), epsilon);
        assertEquals(0, tableau.getEntry(0, 1), epsilon);
        assertEquals(0, tableau.getEntry(0, 2), epsilon);
        Assert.assertArrayEquals(new double[]{0, 1}, tableau.getColumnVector(1), epsilon);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, -2}),
                new LinearConstraint[]{new LinearConstraint(2, 2, 3, Double.NEGATIVE_INFINITY)},
                null,
                false,
                DEFAULT_EPSILON
        );
        new SimplexSolver().doIteration(tableau);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, 0, -2}),
                new LinearConstraint[]{new LinearConstraint(1, 1, 2, Double.NEGATIVE_INFINITY), new LinearConstraint(1, 2, 2, Double.POSITIVE_INFINITY)},
                null,
                false,
                DEFAULT_EPSILON
        );
        assertFalse(new SimplexSolver().isPhase1Solved(tableau));
        tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, 0, -2}),
                new LinearConstraint[]{new LinearConstraint(1, 1, 2, 1), new LinearConstraint(1, 2, 2, 1)},
                null,
                false,
                DEFAULT_EPSILON
        );
        assertTrue(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, 0, -2}),
                new LinearConstraint[]{new LinearConstraint(1, 1, 2, Double.NEGATIVE_INFINITY), new LinearConstraint(1, 2, 2, Double.POSITIVE_INFINITY)},
                null,
                false,
                DEFAULT_EPSILON
        );
        new SimplexSolver().solvePhase1(tableau);
        assertTrue(new SimplexSolver().isPhase1Solved(tableau));
        assertEquals(0, tableau.getEntry(1, 1), epsilon);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{0, -2}),
                new LinearConstraint[]{new LinearConstraint(1, 1, 2, Double.NEGATIVE_INFINITY), new LinearConstraint(1, 2, 2, Double.POSITIVE_INFINITY)},
                null,
                false,
                DEFAULT_EPSILON
        );
        new SimplexSolver().solvePhase1(tableau);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, 0, -2}),
                new LinearConstraint[]{new LinearConstraint(1, 1, 2, 1), new LinearConstraint(1, 2, 2, 1)},
                null,
                false,
                DEFAULT_EPSILON
        );
        assertFalse(new SimplexSolver().isOptimal(tableau));
        tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, 0, 0}),
                new LinearConstraint[]{new LinearConstraint(1, 1, 2, 1), new LinearConstraint(1, 2, 2, 1)},
                null,
                false,
                DEFAULT_EPSILON
        );
        assertTrue(new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{2, 1});
        LinearConstraint constraint1 = new LinearConstraint(1, 1, 2, 1);
        LinearConstraint constraint2 = new LinearConstraint(1, 2, 2, 1);
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[]{constraint1, constraint2}, null, false, DEFAULT_EPSILON);
        new SimplexSolver().solvePhase1(tableau);
        tableau.discardArtificialVariables();
        new SimplexSolver().doIteration(tableau);
        PointValuePair solution = new SimplexSolver().doOptimize();
        assertEquals(1.0, solution.getPoint()[0], epsilon);
        assertEquals(1.5, solution.getValue(), epsilon);
    }

    @Test(expected = OptimizationException.class)
    public void testDoOptimizeInvalidInput() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
                null,
                null,
                null,
                false,
                DEFAULT_EPSILON
        );
        new SimplexSolver().doOptimize();
    }

    @Test
    public void testDoOptimizeZeroObjectiveFunction() throws OptimizationException {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{0, 0});
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[]{new LinearConstraint(1, 1, 2, 1), new LinearConstraint(1, 2, 2, 1)}, null, false, DEFAULT_EPSILON);
        new SimplexSolver().solvePhase1(tableau);
        tableau.discardArtificialVariables();
        new SimplexSolver().doOptimize();
        PointValuePair solution = tableau.getSolution();
        assertEquals(0.0, solution.getPoint()[0], epsilon);
        assertEquals(0.0, solution.getPoint()[1], epsilon);
        assertEquals(0.0, solution.getValue(), epsilon);
    }
}