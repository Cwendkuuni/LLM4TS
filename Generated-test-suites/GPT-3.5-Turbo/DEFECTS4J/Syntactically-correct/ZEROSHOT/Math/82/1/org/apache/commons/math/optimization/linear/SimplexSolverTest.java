import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null);
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        assertEquals(null, pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null);
        Integer pivotRow = simplexSolver.getPivotRow(0, tableau);
        assertEquals(null, pivotRow);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolutionException() throws OptimizationException {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null);
        simplexSolver.doIteration(tableau);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null);
        tableau.divideRow(0, 1.0);
        tableau.subtractRow(1, 0, 1.0);
        simplexSolver.doIteration(tableau);
        double expectedEntry00 = 1.0;
        double expectedEntry10 = -1.0;
        double expectedEntry11 = 1.0;
        assertEquals(expectedEntry00, tableau.getEntry(0, 0), 0.00001);
        assertEquals(expectedEntry10, tableau.getEntry(1, 0), 0.00001);
        assertEquals(expectedEntry11, tableau.getEntry(1, 1), 0.00001);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null);
        boolean isPhase1Solved = simplexSolver.isPhase1Solved(tableau);
        assertEquals(true, isPhase1Solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null);
        boolean isOptimal = simplexSolver.isOptimal(tableau);
        assertEquals(true, isOptimal);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolutionException() throws OptimizationException {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null);
        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null);
        simplexSolver.solvePhase1(tableau);
        double expectedEntry = 0.0;
        assertEquals(expectedEntry, tableau.getEntry(0, tableau.getRhsOffset()), 0.00001);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexSolver simplexSolver = new SimplexSolver();
        RealPointValuePair result = simplexSolver.doOptimize();
        assertEquals(null, result);
    }
}