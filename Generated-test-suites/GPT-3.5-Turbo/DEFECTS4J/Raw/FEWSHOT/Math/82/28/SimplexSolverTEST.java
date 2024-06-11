package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.optimization.NoFeasibleSolutionException;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimplexSolverTest {

    private static SimplexSolver solver;
    
    @BeforeClass
    public static void setup() {
        solver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0);
        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(0, 1, -3.0);
        tableau.setEntry(0, 2, -1.0);
        tableau.setEntry(0, 3, -4.0);
        assertEquals(new Integer(1), solver.getPivotColumn(tableau));
        
        tableau.setEntry(0, 0, 2.0);
        tableau.setEntry(0, 1, 3.0);
        tableau.setEntry(0, 2, 1.0);
        tableau.setEntry(0, 3, 4.0);
        assertNull(solver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0);
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        tableau.setEntry(0, 3, 4.0);
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(1, 1, 4.0);
        tableau.setEntry(1, 2, 6.0);
        tableau.setEntry(1, 3, 8.0);
        assertEquals(new Integer(0), solver.getPivotRow(1, tableau));
        
        tableau.setEntry(0, 0, 0.0);
        tableau.setEntry(0, 1, 0.0);
        tableau.setEntry(0, 2, 0.0);
        tableau.setEntry(0, 3, 0.0);
        tableau.setEntry(1, 0, 0.0);
        tableau.setEntry(1, 1, 0.0);
        tableau.setEntry(1, 2, 0.0);
        tableau.setEntry(1, 3, 0.0);
        assertNull(solver.getPivotRow(1, tableau));
    }

    @Test
    public void testDoIteration() throws NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0);
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.0);
        tableau.setEntry(0, 3, -4.0);   
        
        try {
            solver.doIteration(tableau);
            assertEquals(-0.25, tableau.getEntry(0, 0), 0.0001);
            assertEquals(-0.5, tableau.getEntry(0, 1), 0.0001);
            assertEquals(-0.75, tableau.getEntry(0, 2), 0.0001);
            assertEquals(-1.0, tableau.getEntry(0, 3), 0.0001);
        } catch (NoFeasibleSolutionException e) {
            fail("NoFeasibleSolutionException should not be thrown");
        }
        
        SimplexTableau tableau2 = new SimplexTableau(null, null, null, false, 0);
        tableau2.setEntry(0, 0, 0.0);
        tableau2.setEntry(0, 1, 0.0);
        tableau2.setEntry(0, 2, 0.0);
        tableau2.setEntry(0, 3, 0.0);
        
        try {
            solver.doIteration(tableau2);
            assertEquals(0.0, tableau2.getEntry(0, 0), 0.0001);
            assertEquals(0.0, tableau2.getEntry(0, 1), 0.0001);
            assertEquals(0.0, tableau2.getEntry(0, 2), 0.0001);
            assertEquals(0.0, tableau2.getEntry(0, 3), 0.0001);
        } catch (NoFeasibleSolutionException e) {
            fail("NoFeasibleSolutionException should not be thrown");
        }
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0);
        tableau.setEntry(0, 0, 0.0);
        tableau.setEntry(0, 1, 0.0);
        tableau.setEntry(0, 2, 0.0);
        tableau.setEntry(0, 3, 0.0);
        assertTrue(solver.isPhase1Solved(tableau));
        
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        tableau.setEntry(0, 3, 4.0);
        assertTrue(!solver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0);
        tableau.setEntry(0, 0, 0.0);
        tableau.setEntry(0, 1, 0.0);
        tableau.setEntry(0, 2, 0.0);
        tableau.setEntry(0, 3, 0.0);
        assertTrue(solver.isOptimal(tableau));
        
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        tableau.setEntry(0, 3, 4.0);
        assertTrue(!solver.isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0);
        tableau.setEntry(0, 4, 1.0);
        try {
            solver.solvePhase1(tableau);
            fail("NoFeasibleSolutionException should be thrown");
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }
        
        SimplexTableau tableau2 = new SimplexTableau(null, null, null, false, 0);
        tableau2.setEntry(0, 4, 0.0);
        try {
            solver.solvePhase1(tableau2);
            assertTrue(solver.isPhase1Solved(tableau2));
        } catch (NoFeasibleSolutionException e) {
            fail("NoFeasibleSolutionException should not be thrown");
        }
    }

    @Test
    public void testDoOptimize() {
        fail("Not yet implemented");
    }

}