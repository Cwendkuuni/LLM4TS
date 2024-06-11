package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunctionGradient;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Test;

/**
 * Test cases for the SimplexSolver class
 */
public class SimplexSolverTest {

    /**
     * Test constructor with default settings
     */
    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.getEpsilon(), 0);
        assertEquals(10, solver.getMaxUlps());
    }

    /**
     * Test constructor with specified settings
     */
    @Test
    public void testCustomConstructor() {
        SimplexSolver solver = new SimplexSolver(1.0e-8, 5);
        assertEquals(1.0e-8, solver.getEpsilon(), 0);
        assertEquals(5, solver.getMaxUlps());
    }

    /**
     * Test getPivotColumn method
     */
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, 0),
                                                   new LinearConstraint[]{},
                                                   SimplexSolver.MINIMIZE,
                                                   true, 1.0e-6, 10);

        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());

        tableau.setEntry(0, 1, -3.0);
        pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(1, pivotColumn.intValue());

        tableau.setEntry(0, 1, -1.0);
        pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(1, pivotColumn.intValue());

        tableau.setEntry(0, 1, 0.0);
        pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());
    }

    /**
     * Test getPivotRow method with no pivot found
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotRowNoPivot() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, 0),
                                                   new LinearConstraint[]{},
                                                   SimplexSolver.MINIMIZE,
                                                   true, 1.0e-6, 10);

        solver.getPivotRow(tableau, 0);
    }

    /**
     * Test getPivotRow method with multiple pivots found
     */
    @Test
    public void testGetPivotRowMultiple() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}, 0),
                                                   new LinearConstraint[]{},
                                                   SimplexSolver.MINIMIZE,
                                                   true, 1.0e-6, 10);

        List<Integer> pivotRows = new ArrayList<>();
        Integer pivotRow = solver.getPivotRow(tableau, 0);
        pivotRows.add(pivotRow);

        tableau.setEntry(1, 0, -1.0);
        pivotRow = solver.getPivotRow(tableau, 0);
        pivotRows.add(pivotRow);

        tableau.setEntry(2, 0, -1.0);
        pivotRow = solver.getPivotRow(tableau, 0);
        pivotRows.add(pivotRow);

        assertEquals(3, pivotRows.size());
        assertTrue(pivotRows.contains(0));
        assertTrue(pivotRows.contains(1));
        assertTrue(pivotRows.contains(2));
    }

    /**
     * Test doIteration method
     */
    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}, 0),
                                                   new LinearConstraint[]{},
                                                   SimplexSolver.MINIMIZE,
                                                   true, 1.0e-6, 10);

        solver.doIteration(tableau);

        assertEquals(1.0 / 3.0, tableau.getEntry(1, 0), 0);
        assertEquals(1.0 / 3.0, tableau.getEntry(2, 0), 0);
        assertEquals(0, tableau.getEntry(0, 0), 0);
        assertEquals(0, tableau.getEntry(1, 1), 0);
        assertEquals(0, tableau.getEntry(2, 1), 0);
        assertEquals(2.0 / 3.0, tableau.getEntry(0, 1), 0);
        assertEquals(2.0 / 3.0, tableau.getEntry(0, 2), 0);
        assertEquals(-1.0 / 3.0, tableau.getEntry(1, 1), 0);
        assertEquals(-1.0 / 3.0, tableau.getEntry(1, 2), 0);
        assertEquals(1.0 / 3.0, tableau.getEntry(2, 1), 0);
        assertEquals(1.0 / 3.0, tableau.getEntry(2, 2), 0);

        solver.doIteration(tableau);

        assertEquals(1.0, tableau.getEntry(1, 0), 0);
        assertEquals(0, tableau.getEntry(1, 1), 0);
        assertEquals(0, tableau.getEntry(1, 2), 0);
        assertEquals(1.0, tableau.getEntry(2, 0), 0);
        assertEquals(0, tableau.getEntry(2, 1), 0);
        assertEquals(0, tableau.getEntry(2, 2), 0);
        assertEquals(0, tableau.getEntry(0, 0), 0);
        assertEquals(1.0, tableau.getEntry(0, 1), 0);
        assertEquals(1.0, tableau.getEntry(0, 2), 0);
        assertEquals(0, tableau.getEntry(0, 3), 0);
        assertEquals(0, tableau.getEntry(1, 3), 0);
        assertEquals(0, tableau.getEntry(2, 3), 0);
    }

    /**
     * Test solvePhase1 method
     */
    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}, 0),
                                                   new LinearConstraint[]{{1.0, 2.0}, {3.0, 4.0}},
                                                   SimplexSolver.MINIMIZE,
                                                   true, 1.0e-6, 10);

        solver.solvePhase1(tableau);

        assertEquals(1.0 / 3.0, tableau.getEntry(1, 0), 0);
        assertEquals(1.0 / 3.0, tableau.getEntry(2, 0), 0);
        assertEquals(0, tableau.getEntry(0, 0), 0);
        assertEquals(0, tableau.getEntry(1, 1), 0);
        assertEquals(0, tableau.getEntry(2, 1), 0);
        assertEquals(2.0 / 3.0, tableau.getEntry(0, 1), 0);
        assertEquals(2.0 / 3.0, tableau.getEntry(0, 2), 0);
        assertEquals(-1.0 / 3.0, tableau.getEntry(1, 1), 0);
        assertEquals(-1.0 / 3.0, tableau.getEntry(1, 2), 0);
        assertEquals(1.0 / 3.0, tableau.getEntry(2, 1), 0);
        assertEquals(1.0 / 3.0, tableau.getEntry(2, 2), 0);

        tableau.dropPhase1Objective();

        solver.solvePhase1(tableau);

        assertEquals(1.0, tableau.getEntry(1, 0), 0);
        assertEquals(0, tableau.getEntry(1, 1), 0);
        assertEquals(0, tableau.getEntry(1, 2), 0);
        assertEquals(1.0, tableau.getEntry(2, 0), 0);
        assertEquals(0, tableau.getEntry(2, 1), 0);
        assertEquals(0, tableau.getEntry(2, 2), 0);
        assertEquals(0, tableau.getEntry(0, 0), 0);
        assertEquals(1.0, tableau.getEntry(0, 1), 0);
        assertEquals(1.0, tableau.getEntry(0, 2), 0);
        assertEquals(0, tableau.getEntry(0, 3), 0);
        assertEquals(0, tableau.getEntry(1, 3), 0);
        assertEquals(0, tableau.getEntry(2, 3), 0);
    }

    /**
     * Test doOptimize method
     */
    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 2.0}, 1.0),
                                                                 new LinearConstraint(new double[]{3.0, 4.0}, 2.0)};
        PointValuePair optimizationResult = solver.doOptimize();

        assertEquals(1.0 / 3.0, optimizationResult.getPoint()[0], 0);
        assertEquals(1.0 / 3.0, optimizationResult.getPoint()[1], 0);
        assertEquals(1.0 / 3.0, optimizationResult.getValue(), 0);
    }

    /**
     * Test doOptimize method with unbounded solution
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeUnbounded() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}, 5.0);
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 2.0}, 1.0),
                                                                 new LinearConstraint(new double[]{3.0, 4.0}, 2.0)};
        solver.doOptimize();
    }

    /**
     * Test doOptimize method with no feasible solution
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeNoFeasible() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 3.0}, 1.0),
                                                                 new LinearConstraint(new double[]{2.0, 4.0}, 1.0)};
        solver.doOptimize();
    }

    /**
     * Test doOptimize method with simple constraint violation
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeSimpleConstraintViolation() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}, 0);
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0, 2.0}, 2.0);
        LinearConstraint[] constraints = new LinearConstraint[]{constraint};
        solver.doOptimize();
    }

    /**
     * Test doOptimize method with complex constraint violation
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeComplexConstraintViolation() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}, 0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 2.0}, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 3.0}, 2.0);
        LinearConstraint[] constraints = new LinearConstraint[]{constraint1, constraint2};
        solver.doOptimize();
    }
}