import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {{-1, -2, -3}, {1, 2, 3}, {4, 5, 6}};
        SimplexTableau tableau = new SimplexTableau(tableauData);
        
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(1), pivotColumn);
    }
    
    @Test
    public void testGetPivotColumn_NoNegativeCoefficient() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        SimplexTableau tableau = new SimplexTableau(tableauData);
        
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertNull(pivotColumn);
    }
    
    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        SimplexTableau tableau = new SimplexTableau(tableauData);
        
        Integer pivotRow = solver.getPivotRow(tableau, 1);
        assertEquals(Integer.valueOf(2), pivotRow);
    }
    
    @Test
    public void testGetPivotRow_NoPositiveEntry() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {{-1, -2, -3}, {-4, -5, -6}, {-7, -8, -9}};
        SimplexTableau tableau = new SimplexTableau(tableauData);
        
        Integer pivotRow = solver.getPivotRow(tableau, 1);
        assertNull(pivotRow);
    }
    
    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {{1, 2, 3, 4}, {0, 1, 1, 1}, {1, 2, 1, 5}};
        SimplexTableau tableau = new SimplexTableau(tableauData);
        
        solver.doIteration(tableau);
        
        double[][] expectedTableauData = {{-0.5, 1.5, 1.5, 2.5}, {0, 1, 1, 1}, {0.5, 0.5, -0.5, 2.5}};
        double[][] actualTableauData = tableau.getData();
        assertArrayEquals(expectedTableauData, actualTableauData);
    }
    
    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {{1, -2, 1, 4}, {0, -1, 1, 1}, {-1, 5, -1, 6}};
        SimplexTableau tableau = new SimplexTableau(tableauData);
        
        solver.doIteration(tableau);
    }
    
    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {{-1, -1, 1, 0, 1}, {-2, -1, 0, 1, 1}};
        SimplexTableau tableau = new SimplexTableau(tableauData);
        
        solver.solvePhase1(tableau);
        
        double[][] expectedTableauData = {{0, 1, 1, -1, 1}, {1, 1, 0, 2, 1}};
        double[][] actualTableauData = tableau.getData();
        assertArrayEquals(expectedTableauData, actualTableauData);
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableauData = {{-1, -1, 1, 0, 1}, {-2, -1, 0, 1, 0}};
        SimplexTableau tableau = new SimplexTableau(tableauData);
        
        solver.solvePhase1(tableau);
    }
    
    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        double[] c = {-2, -3};
        double[][] a = {{-2, -9}, {-1, -1}, {-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        Relationship[] relationships = {Relationship.LEQ, Relationship.LEQ, Relationship.LEQ, Relationship.LEQ, Relationship.GEQ, Relationship.GEQ};
        double[] b = {-5, -3, 0, 0, 2, 2};
        
        LinearObjectiveFunction function = new LinearObjectiveFunction(c, 0);
        LinearConstraintSet constraints = new LinearConstraintSet(a, relationships, b);
        GoalType goalType = GoalType.MAXIMIZE;
        
        PointValuePair result = solver.doOptimize(function, constraints, goalType, true);
        
        double[] expectedPoint = {1.5, -0.5};
        double[] expectedValue = {-7.0};
        double[] actualPoint = result.getPoint();
        double[] actualValue = {result.getValue()};
        
        assertArrayEquals(expectedPoint, actualPoint, 0.0001);
        assertArrayEquals(expectedValue, actualValue, 0.0001);
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_NoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        double[] c = {1, 1};
        double[][] a = {{2, 1}, {4, 5}};
        Relationship[] relationships = {Relationship.LEQ, Relationship.LEQ};
        double[] b = {1, 2};
        
        LinearObjectiveFunction function = new LinearObjectiveFunction(c, 0);
        LinearConstraintSet constraints = new LinearConstraintSet(a, relationships, b);
        GoalType goalType = GoalType.MAXIMIZE;
        
        solver.doOptimize(function, constraints, goalType, true);
    }
}