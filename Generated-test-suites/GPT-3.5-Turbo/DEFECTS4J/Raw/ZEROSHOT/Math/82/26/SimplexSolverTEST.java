import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.*;

public class SimplexSolverTest {

    // Create instance of SimplexSolver
    private SimplexSolver solver;

    @Test
    public void testDefaultConstructor() {
        solver = new SimplexSolver();
        Assert.assertEquals(SimplexSolver.DEFAULT_EPSILON, solver.epsilon, 0);
    }

    @Test
    public void testParameterizedConstructor() {
        double epsilon = 0.0001;
        solver = new SimplexSolver(epsilon);
        Assert.assertEquals(epsilon, solver.epsilon, 0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 5);
        tableau.setEntry(0, 1, -3);
        tableau.setEntry(0, 2, -2);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        Assert.assertEquals(Integer.valueOf(2), pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(1, 2, 0.5);
        tableau.setEntry(2, 2, 1.0);
        Integer pivotRow = solver.getPivotRow(2, tableau);
        Assert.assertEquals(Integer.valueOf(1), pivotRow);
    }

    @Test (expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 5);
        tableau.setEntry(0, 1, 3);
        solver.doIteration(tableau);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 2);
        tableau.setEntry(1, 0, 3);
        tableau.setEntry(0, 1, 1);
        solver.doIteration(tableau);
        Assert.assertEquals(0, tableau.getEntry(0, 0), 0);
        Assert.assertEquals(3, tableau.getEntry(1, 0), 0);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 2, 1);
        boolean isPhase1Solved = solver.isPhase1Solved(tableau);
        Assert.assertFalse(isPhase1Solved);
        tableau.setEntry(0, 2, 0);
        isPhase1Solved = solver.isPhase1Solved(tableau);
        Assert.assertTrue(isPhase1Solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 2, 1);
        boolean isOptimal = solver.isOptimal(tableau);
        Assert.assertFalse(isOptimal);
        tableau.setEntry(0, 2, 0);
        isOptimal = solver.isOptimal(tableau);
        Assert.assertTrue(isOptimal);
    }

    @Test (expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        solver.solvePhase1(tableau);
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(1, 2, -1);
        solver.solvePhase1(tableau);
    }
    
    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        RealPointValuePair result = solver.doOptimize();
        Assert.assertNotNull(result);
    }

}