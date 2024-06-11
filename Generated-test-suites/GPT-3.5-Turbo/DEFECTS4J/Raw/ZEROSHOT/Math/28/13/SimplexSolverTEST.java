import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        assertNull(pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        Integer pivotRow = simplexSolver.getPivotRow(tableau, 0);
        assertNull(pivotRow);
    }

    @Test
    public void testDoIteration() throws Exception {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        simplexSolver.doIteration(tableau);
    }

    @Test
    public void testSolvePhase1() throws Exception {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);
        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws Exception {
        PointValuePair pointValuePair = simplexSolver.doOptimize();
        assertNotNull(pointValuePair);
    }
}