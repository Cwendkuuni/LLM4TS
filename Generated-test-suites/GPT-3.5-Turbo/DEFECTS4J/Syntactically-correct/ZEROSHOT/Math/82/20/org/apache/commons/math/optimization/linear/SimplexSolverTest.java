import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {
    
    private static final double EPSILON = 1.0e-6;

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(EPSILON, solver.epsilon, 0);
    }

    @Test
    public void testConstructorWithEpsilon() {
        double newEpsilon = 0.0001;
        solver = new SimplexSolver(newEpsilon);
        assertEquals(newEpsilon, solver.epsilon, 0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, -3);
        assertEquals((Integer) 2, solver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        tableau.setEntry(1, 0, 8);
        tableau.setEntry(2, 0, 6);
        tableau.setEntry(3, 0, 4);
        assertEquals((Integer) 2, solver.getPivotRow(0, tableau));
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        tableau.setEntry(0, 0, -2);
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(2, 0, -2);
        solver.doIteration(tableau);
        assertEquals(1.0, tableau.getEntry(1, 0), EPSILON);
        assertEquals(-2.0, tableau.getEntry(2, 0), EPSILON);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        assertFalse(solver.isPhase1Solved(tableau));
        tableau.setEntry(0, 0, 2);
        tableau.setEntry(0, 1, 3);
        tableau.setEntry(0, 2, 4);
        assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, EPSILON);
        assertTrue(solver.isOptimal(tableau));
        tableau.setEntry(0, 0, -2);
        tableau.setEntry(0, 1, -3);
        tableau.setEntry(0, 2, -4);
        assertFalse(solver.isOptimal(tableau));
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        // TODO: Add test cases for doOptimize method
    }

}