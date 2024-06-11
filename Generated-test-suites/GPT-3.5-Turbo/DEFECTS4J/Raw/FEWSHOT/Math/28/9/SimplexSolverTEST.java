package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {
    private SimplexSolver simplexSolver;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        assertNull(simplexSolver.getPivotColumn(tableau));

        tableau.setEntry(0, 0, -1);
        assertEquals(0, simplexSolver.getPivotColumn(tableau).intValue());

        tableau.setEntry(0, 1, -2);
        assertEquals(1, simplexSolver.getPivotColumn(tableau).intValue());

        tableau.setEntry(0, 2, -3);
        assertEquals(2, simplexSolver.getPivotColumn(tableau).intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        assertNull(simplexSolver.getPivotRow(tableau, 0));

        tableau.setEntry(0, 0, 1);
        assertNull(simplexSolver.getPivotRow(tableau, 0));

        tableau.setEntry(1, 0, 1);
        assertEquals(1, simplexSolver.getPivotRow(tableau, 0).intValue());

        tableau.setEntry(2, 0, 1);
        assertEquals(1, simplexSolver.getPivotRow(tableau, 0).intValue());

        tableau.setEntry(1, 0, -1);
        assertEquals(1, simplexSolver.getPivotRow(tableau, 0).intValue());

        tableau.setEntry(1, 0, 2);
        assertEquals(2, simplexSolver.getPivotRow(tableau, 0).intValue());

        tableau.setEntry(2, 0, 0);
        assertNull(simplexSolver.getPivotRow(tableau, 0));
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        simplexSolver.doIteration(tableau);
        assertTrue(tableau.isOptimal());

        tableau = new SimplexTableau(null, null, null, false, 0, 0);
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, 2);
        tableau.setEntry(2, 0, 3);
        simplexSolver.doIteration(tableau);
        assertTrue(tableau.isOptimal());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolutionException() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        tableau.setEntry(0, 0, 1);
        simplexSolver.doIteration(tableau);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, 2);
        tableau.setEntry(2, 0, 3);
        simplexSolver.solvePhase1(tableau);
        assertTrue(tableau.isOptimal());

        tableau = new SimplexTableau(null, null, null, false, 0, 0);
        tableau.setEntry(0, 0, -1);
        simplexSolver.solvePhase1(tableau);
        assertTrue(tableau.isOptimal());
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolutionException() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        tableau.setEntry(0, 0, -1);
        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, 2);
        tableau.setEntry(2, 0, 3);
        PointValuePair result = simplexSolver.doOptimize();
        assertNotNull(result);
    }
}