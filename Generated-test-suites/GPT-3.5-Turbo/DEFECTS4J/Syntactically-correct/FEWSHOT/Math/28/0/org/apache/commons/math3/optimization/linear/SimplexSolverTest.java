import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Test;

public class SimplexSolverTest {
    
    private static final double EPSILON = 1e-6;
    private static final int MAX_ULPS = 10;
    
    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(SimplexSolver.DEFAULT_EPSILON, solver.getEpsilon(), EPSILON);
        assertEquals(SimplexSolver.DEFAULT_ULPS, solver.getMaxUlps());
        
        solver = new SimplexSolver(0.1, 20);
        assertEquals(0.1, solver.getEpsilon(), EPSILON);
        assertEquals(20, solver.getMaxUlps());
    }
    
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = createSimplexTableau();
        SimplexSolver solver = new SimplexSolver();
        
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());
    }
    
    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = createSimplexTableau();
        SimplexSolver solver = new SimplexSolver();
        
        Integer pivotRow = solver.getPivotRow(tableau, 0);
        assertEquals(1, pivotRow.intValue());
    }
    
    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = createSimplexTableau();
        SimplexSolver solver = new SimplexSolver();
        
        solver.doIteration(tableau);
    }
    
    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = createSimplexTableau();
        SimplexSolver solver = new SimplexSolver();
        
        solver.solvePhase1(tableau);
        
        assertEquals(0.0, tableau.getEntry(0, tableau.getRhsOffset()), EPSILON);
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = createInfeasibleSimplexTableau();
        SimplexSolver solver = new SimplexSolver();
        
        solver.solvePhase1(tableau);
    }
    
    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = createSimplexTableau();
        SimplexSolver solver = new SimplexSolver();
        
        PointValuePair solution = solver.doOptimize();
        
        assertEquals(5.0, solution.getPoint()[0], EPSILON);
        assertEquals(3.0, solution.getPoint()[1], EPSILON);
        assertEquals(9.0, solution.getPoint()[2], EPSILON);
    }
    
    private SimplexTableau createSimplexTableau() {
        double[] coefficients = new double[] {1.0, 2.0, 3.0};
        double[] rhs = new double[] {4.0, 5.0};
        double[][] coefficientsMatrix = new double[][] {{1.0, -1.0, 1.0}, {0.0, 1.0, -1.0}};
        
        return new SimplexTableau(coefficients, rhs, coefficientsMatrix, 0.0, MAX_ULPS);
    }
    
    private SimplexTableau createInfeasibleSimplexTableau() {
        double[] coefficients = new double[] {1.0, 2.0, 3.0};
        double[] rhs = new double[] {4.0, 5.0};
        double[][] coefficientsMatrix = new double[][] {{1.0, -1.0, 1.0}, {0.0, 1.0, -1.0}};
        
        return new SimplexTableau(coefficients, rhs, coefficientsMatrix, 1.0, MAX_ULPS);
    }
}