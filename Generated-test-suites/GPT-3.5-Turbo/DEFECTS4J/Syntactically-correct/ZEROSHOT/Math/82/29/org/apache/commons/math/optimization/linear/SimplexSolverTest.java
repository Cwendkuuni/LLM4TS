import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.SimplexTableau;
import org.apache.commons.math.optimization.linear.UnboundedSolutionException;
import org.apache.commons.math.optimization.linear.NoFeasibleSolutionException;

public class SimplexSolverTest {

    private static final double EPSILON = 1.0e-6;

    private SimplexSolver solver;

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = createTableau();
        
        Integer pivotColumn = solver.getPivotColumn(tableau);
        
        assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = createTableau();
        
        Integer pivotRow = solver.getPivotRow(1, tableau);
        
        assertEquals(2, pivotRow.intValue());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnbounded() {
        SimplexTableau tableau = createTableau();
        
        solver.doIteration(tableau);
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = createTableau();
        fillTableau(tableau);
        
        solver.doIteration(tableau);
        
        assertTrue(Math.abs(tableau.getEntry(2, 1) - 1.0) < EPSILON);
        assertTrue(Math.abs(tableau.getEntry(0, 1) - 0.5) < EPSILON);
        assertTrue(Math.abs(tableau.getEntry(1, 1) - 0.5) < EPSILON);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = createTableau();
        fillTableau(tableau);
        
        boolean solved = solver.isPhase1Solved(tableau);
        
        assertFalse(solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = createTableau();
        fillTableau(tableau);
        
        boolean optimal = solver.isOptimal(tableau);
        
        assertFalse(optimal);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasible() {
        SimplexTableau tableau = createTableau();
        
        solver.solvePhase1(tableau);
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = createTableau();
        fillTableau(tableau);
        
        solver.solvePhase1(tableau);
        
        assertTrue(Math.abs(tableau.getEntry(0, 0) - 1.0) < EPSILON);
        assertTrue(Math.abs(tableau.getEntry(1, 1) - 1.0) < EPSILON);
        assertTrue(Math.abs(tableau.getEntry(2, 2) - 1.0) < EPSILON);
        assertTrue(Math.abs(tableau.getEntry(3, 3) - 1.0) < EPSILON);
        assertTrue(Math.abs(tableau.getEntry(4, 4) - 1.0) < EPSILON);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = createTableau();
        fillTableau(tableau);
        fillGoalVector(tableau);
        
        RealPointValuePair solution = solver.doOptimize();
        
        assertTrue(Math.abs(solution.getPoint()[0] - 20.0) < EPSILON);
        assertTrue(Math.abs(solution.getPoint()[1] - 16.0) < EPSILON);
    }

    private SimplexTableau createTableau() {
        double[] f = {1, 1};
        double[][] constraints = {
            {-2, -4, 1, 0, 0, 0, 0},
            {-3,  2, 0, 1, 0, 0, 0},
            { 0,  1, 0, 0, 1, 0, 0},
            {-1,  1, 0, 0, 0, 1, 0},
            {-2, -1, 0, 0, 0, 0, 1}
        };
        boolean[] nonNegative = { false, false, false, false, false };
        SimplexTableau tableau = new SimplexTableau(f, constraints, nonNegative, EPSILON);
        return tableau;
    }
    
    private void fillTableau(SimplexTableau tableau) {
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(1, 0, 3.0);
        tableau.setEntry(2, 0, 0.0);
        tableau.setEntry(3, 0, 1.0);
        tableau.setEntry(4, 0, -5.0);
        
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(1, 1, -2.0);
        tableau.setEntry(2, 1, -3.0);
        tableau.setEntry(3, 1,  4.0);
        tableau.setEntry(4, 1,  1.0);
    }

    private void fillGoalVector(SimplexTableau tableau) {
        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(1, 0, -3.0);
        tableau.setEntry(2, 0, -4.0);
        tableau.setEntry(3, 0,  4.0);
        tableau.setEntry(4, 0,  3.0);
    }
    
    @Before
    public void setUp() {
        solver = new SimplexSolver(EPSILON);
    }
    
}