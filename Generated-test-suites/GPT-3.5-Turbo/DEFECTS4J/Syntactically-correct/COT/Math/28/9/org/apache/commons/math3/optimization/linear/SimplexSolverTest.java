import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(1, 2);
        
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        
        assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(1, 2);
        
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(1, 0, 1.0);
        tableau.setEntry(1, 1, -2.0);
        tableau.setEntry(2, 0, -1.0);
        tableau.setEntry(2, 1, -2.0);
        
        Integer pivotRow = simplexSolver.getPivotRow(tableau, 1);
        
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(1, 2);
        
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(1, 0, -1.0);
        tableau.setEntry(1, 1, -2.0);
        
        simplexSolver.doIteration(tableau);
        
        assertEquals(1.0, tableau.getEntry(0, 0), 0.0001);
        assertEquals(2.0, tableau.getEntry(0, 1), 0.0001);
        assertEquals(-1.0, tableau.getEntry(1, 0), 0.0001);
        assertEquals(-2.0, tableau.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(1, 2);
        
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(1, 1, -2.0);
        tableau.setEntry(2, tableau.getRhsOffset(), 1.0);
        
        simplexSolver.solvePhase1(tableau);
        
        assertEquals(0.0, tableau.getEntry(0, tableau.getRhsOffset()), 0.0001);
        assertEquals(0.0, tableau.getEntry(1, tableau.getRhsOffset()), 0.0001);
        assertEquals(0.0, tableau.getEntry(2, tableau.getRhsOffset()), 0.0001);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(1, 2);
        
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(1, 0, -1.0);
        tableau.setEntry(1, 1, -2.0);
        
        tableau.setEntry(2, tableau.getRhsOffset(), 1.0);
        
        PointValuePair solution = simplexSolver.doOptimize();
        
        assertEquals(0.0, solution.getPoint()[0], 0.0001);
        assertEquals(0.0, solution.getPoint()[1], 0.0001);
    }
  
}