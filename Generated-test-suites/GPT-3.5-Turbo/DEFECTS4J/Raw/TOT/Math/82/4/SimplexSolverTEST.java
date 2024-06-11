import org.junit.Assert;
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
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, -1.0);
    tableau.setEntry(0, 1, 2.0);
    tableau.setEntry(0, 2, -3.0);
    tableau.setEntry(0, 3, 4.0);
    
    Integer pivotColumn = solver.getPivotColumn(tableau);
    
    Assert.assertEquals(new Integer(2), pivotColumn);
  }
  
  @Test(expected = UnboundedSolutionException.class)
  public void testGetPivotRow_UnboundedSolution() {
    SimplexTableau tableau = new SimplexTableau();
    
    solver.getPivotRow(0, tableau);
  }
  
  @Test
  public void testGetPivotRow() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(1, 0, -1.0);
    tableau.setEntry(2, 0, 2.0);
    tableau.setEntry(3, 0, 1.0);
    
    Integer pivotRow = solver.getPivotRow(0, tableau);
    
    Assert.assertEquals(new Integer(1), pivotRow);
  }
  
  @Test
  public void testDoIteration() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 2.0);
    tableau.divideRow(0, 2.0);
    tableau.setEntry(1, 0, 1.0);
    tableau.subtractRow(1, 0, 1.0);
    
    solver.doIteration(tableau);
    
    double entry00 = tableau.getEntry(0, 0);
    double entry10 = tableau.getEntry(1, 0);
    
    Assert.assertEquals(1.0, entry00, 0.0001);
    Assert.assertEquals(0.0, entry10, 0.0001);
  }
  
  @Test
  public void testIsPhase1Solved_True() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, 2.0);
    tableau.setEntry(0, 2, 3.0);
    
    boolean isSolved = solver.isPhase1Solved(tableau);
    
    Assert.assertTrue(isSolved);
  }
  
  @Test
  public void testIsPhase1Solved_False() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, -2.0);
    tableau.setEntry(0, 2, 3.0);
    
    boolean isSolved = solver.isPhase1Solved(tableau);
    
    Assert.assertFalse(isSolved);
  }
  
  @Test
  public void testIsOptimal_True() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, 2.0);
    tableau.setEntry(0, 2, 3.0);
    
    boolean isOptimal = solver.isOptimal(tableau);
    
    Assert.assertTrue(isOptimal);
  }
  
  @Test
  public void testIsOptimal_False() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(0, 1, -2.0);
    tableau.setEntry(0, 2, 3.0);
    
    boolean isOptimal = solver.isOptimal(tableau);
    
    Assert.assertFalse(isOptimal);
  }
  
  @Test(expected = NoFeasibleSolutionException.class)
  public void testSolvePhase1_NoFeasibleSolution() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 1.0);
    
    solver.solvePhase1(tableau);
  }
  
  @Test
  public void testSolvePhase1() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 1.0);
    tableau.setEntry(1, 0, -1.0);
    tableau.setEntry(2, 0, 2.0);
    
    solver.solvePhase1(tableau);
    
    double entry01 = tableau.getEntry(0, 1);
    double entry02 = tableau.getEntry(0, 2);
    double entry10 = tableau.getEntry(1, 0);
    double entry11 = tableau.getEntry(1, 1);
    double entry12 = tableau.getEntry(1, 2);
    double entry20 = tableau.getEntry(2, 0);
    double entry21 = tableau.getEntry(2, 1);
    double entry22 = tableau.getEntry(2, 2);
    
    Assert.assertEquals(0.0, entry01, 0.0001);
    Assert.assertEquals(0.0, entry02, 0.0001);
    Assert.assertEquals(0.0, entry10, 0.0001);
    Assert.assertEquals(1.0, entry11, 0.0001);
    Assert.assertEquals(-2.0, entry12, 0.0001);
    Assert.assertEquals(0.0, entry20, 0.0001);
    Assert.assertEquals(0.0, entry21, 0.0001);
    Assert.assertEquals(1.0, entry22, 0.0001);
  }
  
  @Test
  public void testDoOptimize() {
    double[] f = {1.0, 2.0};
    double[][] constraints = {{1.0, 1.0}, {-1.0, 2.0}};
    GoalType goalType = GoalType.MAXIMIZE;
    boolean restrictToNonNegative = true;
    double epsilon = 0.0001;
    
    SimplexSolver solver = new SimplexSolver(epsilon);
    solver.setF(f);
    solver.setConstraints(constraints);
    solver.setGoalType(goalType);
    solver.setRestrictToNonNegative(restrictToNonNegative);
    
    RealPointValuePair solution = solver.doOptimize();
    
    Assert.assertArrayEquals(new double[]{1.6666, 0.6666}, solution.getPoint(), 0.0001);
    Assert.assertEquals(4.0, solution.getValue(), 0.0001);
  }
}