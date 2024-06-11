import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, nonNegative, epsilon, maxUlps);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertNull(pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, nonNegative, epsilon, maxUlps);
        Integer pivotRow = solver.getPivotRow(tableau, 0);
        assertNull(pivotRow);
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, nonNegative, epsilon, maxUlps);
        try {
            solver.doIteration(tableau);
        } catch (MaxCountExceededException e) {
            e.printStackTrace();
        } catch (UnboundedSolutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, nonNegative, epsilon, maxUlps);
        try {
            solver.solvePhase1(tableau);
        } catch (MaxCountExceededException e) {
            e.printStackTrace();
        } catch (UnboundedSolutionException e) {
            e.printStackTrace();
        } catch (NoFeasibleSolutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoOptimize() {
        PointValuePair result = null;
        try {
            result = solver.doOptimize();
        } catch (MaxCountExceededException e) {
            e.printStackTrace();
        } catch (UnboundedSolutionException e) {
            e.printStackTrace();
        } catch (NoFeasibleSolutionException e) {
            e.printStackTrace();
        }
        assertEquals(expectedPoint, result.getPoint());
        assertEquals(expectedValue, result.getValue(), epsilon);
    }
}