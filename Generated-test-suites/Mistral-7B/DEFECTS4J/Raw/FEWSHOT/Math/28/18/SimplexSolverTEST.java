package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimplexTableau;
import org.apache.commons.math3.optimization.linear.SimplexSolver.NoFeasibleSolutionException;
import org.apache.commons.math3.util.Precision;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}),
                new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        Integer pivotCol = SimplexSolver.getInstance().getPivotColumn(tableau);

        assertTrue(pivotCol.intValue() == 0);
    }

    @Test
    public void testGetPivotColumn_MultipleMinimums() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, -2.0, 3.0}),
                new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        Integer pivotCol = SimplexSolver.getInstance().getPivotColumn(tableau);

        assertTrue(pivotCol.intValue() == 1);
    }

    @Test
    public void testGetPivotColumn_NoNegativeEntries() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{0.0, 2.0, 3.0}),
                new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        Integer pivotCol = SimplexSolver.getInstance().getPivotColumn(tableau);

        assertNull(pivotCol);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testGetPivotColumn_NoFeasibleSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)},
                LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        Integer pivotCol = SimplexSolver.getInstance().getPivotColumn(tableau);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}),
                new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        Integer pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 0);

        assertTrue(pivotRow.intValue() == 0);
    }

    @Test
    public void testGetPivotRow_MultipleMinimums() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, -2.0, 3.0}),
                new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        Integer pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 1);

        assertTrue(pivotRow.intValue() == 1);
    }

    @Test
    public void testGetPivotRow_NoNegativeEntries() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{0.0, 2.0, 3.0}),
                new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        Integer pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 0);

        assertNull(pivotRow);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testGetPivotRow_NoFeasibleSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)},
                LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        Integer pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 0);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}),
                new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        solver.doIteration(tableau);

        List<Double> expected = new ArrayList<>();
        expected.add(1.0);
        expected.add(0.0);
        expected.add(0.0);
        assertArrayEquals(tableau.getBasicVariables(), expected.stream().mapToDouble(d -> d).toArray(), 10);

        assertArrayEquals(new double[]{-1.0, 2.0, 0.0}, tableau.getObjectiveFunctionCoefficients(), 10);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, tableau.getConstraintCoefficients(0), 10);

        List<Double> expected2 = new ArrayList<>();
        expected2.add(0.0);
        expected2.add(1.0);
        expected2.add(0.0);
        assertArrayEquals(tableau.getBasicVariables(), expected2.stream().mapToDouble(d -> d).toArray(), 10);

        assertArrayEquals(new double[]{0.0, 0.0, 1.0}, tableau.getObjectiveFunctionCoefficients(), 10);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, tableau.getConstraintCoefficients(0), 10);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoIteration_MaxCountExceeded() throws MaxCountExceededException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}),
                new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver(1.0e-6, 0);
        solver.doIteration(tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolution() throws UnboundedSolutionException, MaxCountExceededException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)},
                LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        solver.doIteration(tableau);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}),
                new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        solver.solvePhase1(tableau);

        List<Double> expected = new ArrayList<>();
        expected.add(1.0);
        expected.add(0.0);
        expected.add(0.0);
        assertArrayEquals(tableau.getBasicVariables(), expected.stream().mapToDouble(d -> d).toArray(), 10);

        assertArrayEquals(new double[]{0.0, 0.0, 1.0}, tableau.getObjectiveFunctionCoefficients(), 10);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, tableau.getConstraintCoefficients(0), 10);

        List<Double> expected2 = new ArrayList<>();
        expected2.add(0.0);
        expected2.add(1.0);
        expected2.add(0.0);
        assertArrayEquals(tableau.getBasicVariables(), expected2.stream().mapToDouble(d -> d).toArray(), 10);

        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, tableau.getObjectiveFunctionCoefficients(), 10);
        assertArrayEquals(new double[]{0.0, 0.0, 1.0}, tableau.getConstraintCoefficients(0), 10);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSolvePhase1_MaxCountExceeded() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}),
                new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 0);

        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        solver.solvePhase1(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)},
                LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        solver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}),
                new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, null, 1.0e-6, 10);

        PointValuePair solution = SimplexSolver.getInstance().doOptimize();

        List<Double> expected = new ArrayList<>();
        expected.add(-1.0);
        expected.add(2.0);
        assertArrayEquals(solution.getPoint(), expected.stream().mapToDouble(d -> d).toArray(), 10);

        assertEquals(-3.0, solution.getValue(), 10);
    }

}