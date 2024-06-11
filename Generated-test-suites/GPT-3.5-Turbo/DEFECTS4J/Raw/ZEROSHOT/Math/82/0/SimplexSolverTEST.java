import org.junit.*;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @After
    public void tearDown() {
        solver = null;
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        Integer result = solver.getPivotColumn(tableau);
        assertNull(result);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        Integer result = solver.getPivotRow(0, tableau);
        assertNull(result);
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau();
        try {
            solver.doIteration(tableau);
        } catch (OptimizationException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau();
        boolean result = solver.isPhase1Solved(tableau);
        assertTrue(result);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau();
        boolean result = solver.isOptimal(tableau);
        assertTrue(result);
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau();
        try {
            solver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDoOptimize() {
        try {
            RealPointValuePair result = solver.doOptimize();
            assertNotNull(result);
        } catch (OptimizationException e) {
            fail("Exception should not be thrown");
        }
    }

}