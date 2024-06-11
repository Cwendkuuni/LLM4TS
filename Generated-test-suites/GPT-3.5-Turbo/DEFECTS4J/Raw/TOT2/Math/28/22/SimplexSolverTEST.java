import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1e-6, 10);
        
        Integer pivotColumn = solver.getPivotColumn(tableau);
        
        assertNotNull(pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1e-6, 10);
        
        Integer pivotRow = solver.getPivotRow(tableau, 0);
        
        assertNull(pivotRow);
    }

    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1e-6, 10);

        try {
            solver.doIteration(tableau);
        } catch (MaxCountExceededException e) {
            fail("Unexpected exception");
        } catch (UnboundedSolutionException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1e-6, 10);

        try {
            solver.solvePhase1(tableau);
        } catch (MaxCountExceededException e) {
            fail("Unexpected exception");
        } catch (UnboundedSolutionException e) {
            fail("Unexpected exception");
        } catch (NoFeasibleSolutionException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        
        try {
            PointValuePair result = solver.doOptimize();
            assertNotNull(result);
        } catch (MaxCountExceededException e) {
            fail("Unexpected exception");
        } catch (UnboundedSolutionException e) {
            fail("Unexpected exception");
        } catch (NoFeasibleSolutionException e) {
            fail("Unexpected exception");
        }
    }
}