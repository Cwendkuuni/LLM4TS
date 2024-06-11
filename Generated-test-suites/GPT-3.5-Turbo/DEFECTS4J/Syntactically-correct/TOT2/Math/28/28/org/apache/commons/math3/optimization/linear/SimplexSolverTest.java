import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0.0, 0);
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        assertNull(pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0.0, 0);
        Integer pivotRow = simplexSolver.getPivotRow(tableau, 0);
        assertNull(pivotRow);
    }

    @Test
    public void testDoIteration() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0.0, 0);
        assertThrows(MaxCountExceededException.class, () -> simplexSolver.doIteration(tableau));
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0.0, 0);
        assertThrows(MaxCountExceededException.class, () -> simplexSolver.solvePhase1(tableau));
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver simplexSolver = new SimplexSolver();
        assertThrows(MaxCountExceededException.class, () -> simplexSolver.doOptimize());
    }
}