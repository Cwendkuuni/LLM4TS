import static org.junit.Assert.*;

import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Test;

public class SimplexSolverTest {

    private static final double EPSILON = 1e-6;
    
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, EPSILON);
        Integer pivotCol = tableau.getPivotColumn();
        // write your test case here
    }
    
    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, EPSILON);
        Integer pivotRow = tableau.getPivotRow(col, tableau);
        // write your test case here
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, EPSILON);
        // write your test case here
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, EPSILON);
        boolean isSolved = tableau.isPhase1Solved();
        // write your test case here
    }
    
    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, EPSILON);
        boolean isOptimal = tableau.isOptimal();
        // write your test case here
    }
    
    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, EPSILON);
        // write your test case here
    }
    
    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver(EPSILON);
        RealPointValuePair result = solver.doOptimize();
        // write your test case here
    }
    
}