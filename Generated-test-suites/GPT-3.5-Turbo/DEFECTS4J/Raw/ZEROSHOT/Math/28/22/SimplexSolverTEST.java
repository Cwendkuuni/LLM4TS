import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(null, pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotRow = solver.getPivotRow(tableau, 0);
        assertEquals(null, pivotRow);
    }

    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        try {
            solver.doIteration(tableau);
        } catch (MaxCountExceededException e) {
            fail();
        } catch (UnboundedSolutionException e) {
            fail();
        }
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        try {
            solver.solvePhase1(tableau);
        } catch (MaxCountExceededException e) {
            fail();
        } catch (UnboundedSolutionException e) {
            fail();
        } catch (NoFeasibleSolutionException e) {
            fail();
        }
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        try {
            PointValuePair solution = solver.doOptimize();
            assertNotNull(solution);
        } catch (MaxCountExceededException e) {
            fail();
        } catch (UnboundedSolutionException e) {
            fail();
        } catch (NoFeasibleSolutionException e) {
            fail();
        }
    }
}