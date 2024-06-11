package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {
    
    private static final double EPSILON = 1e-6;
    private static final int MAX_ULPS = 10;
    private SimplexSolver solver;
    
    @Before
    public void setUp() {
        solver = new SimplexSolver(EPSILON, MAX_ULPS);
    }
    
    @Test
    public void testGetPivotColumn() throws Exception {
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotCol = solver.getPivotColumn(tableau);
        assertNull(pivotCol);
        
        tableau.setEntry(0, 1, -1);
        tableau.setEntry(0, 2, -2);
        tableau.setEntry(0, 3, 0);
        pivotCol = solver.getPivotColumn(tableau);
        assertEquals(2, pivotCol.intValue());
    }
    
    @Test
    public void testGetPivotRow() throws Exception {
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotRow = solver.getPivotRow(tableau, 2);
        assertNull(pivotRow);
        
        tableau.setEntry(1, 0, 4);
        tableau.setEntry(2, 0, 2);
        tableau.setEntry(3, 0, 0);
        tableau.setEntry(1, 2, 2);
        tableau.setEntry(2, 2, 4);

        pivotRow = solver.getPivotRow(tableau, 2);
        assertEquals(2, pivotRow.intValue());
        
        tableau.setEntry(1, 2, 0);
        pivotRow = solver.getPivotRow(tableau, 2);
        assertNull(pivotRow);
    }
    
    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolution() throws Exception {
        SimplexTableau tableau = new SimplexTableau();
        solver.doIteration(tableau);
    }
    
    @Test
    public void testSolvePhase1() throws Exception {
        SimplexTableau tableau = new SimplexTableau();
        solver.solvePhase1(tableau);
        assertTrue(true);
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeNoFeasibleSolution() throws Exception {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, tableau.getRhsOffset(), 1);
        solver.solvePhase1(tableau);
        solver.doOptimize();
    }
    
    @Test
    public void testDoOptimize() throws Exception {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, tableau.getRhsOffset(), 3);
        PointValuePair result = solver.doOptimize();
        assertEquals(3, result.getValue(), EPSILON);
        assertEquals(0, result.getPoint()[0], EPSILON);
        assertEquals(1.5, result.getPoint()[1], EPSILON);
    }
}