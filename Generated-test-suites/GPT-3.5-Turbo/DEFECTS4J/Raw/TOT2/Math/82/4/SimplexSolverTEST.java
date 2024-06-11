import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        assertNotNull(pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        assertNotNull(pivotColumn);
        Integer pivotRow = simplexSolver.getPivotRow(pivotColumn, tableau);
        assertNotNull(pivotRow);
    }

    @Test
    public void testDoIteration() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        try {
            simplexSolver.doIteration(tableau);
        } catch (OptimizationException e) {
            fail("Exception thrown during iteration");
        }
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        boolean phase1Solved = simplexSolver.isPhase1Solved(tableau);
        assertTrue(phase1Solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        boolean optimal = simplexSolver.isOptimal(tableau);
        assertTrue(optimal);
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        try {
            simplexSolver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            fail("Exception thrown during Phase 1 solving");
        }
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver simplexSolver = new SimplexSolver();
        try {
            RealPointValuePair solution = simplexSolver.doOptimize();
            assertNotNull(solution);
        } catch (OptimizationException e) {
            fail("Exception thrown during optimization");
        }
    }
}