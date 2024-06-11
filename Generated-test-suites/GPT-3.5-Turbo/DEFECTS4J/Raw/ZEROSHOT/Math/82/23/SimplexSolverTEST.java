import org.junit.Test;
import org.junit.Assert;

public class SimplexSolverTest {
  
  @Test
  public void testGetPivotColumn() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 1);
    tableau.setEntry(0, 1, -1);
    tableau.setEntry(0, 2, 2);
    
    SimplexSolver solver = new SimplexSolver();
    Integer pivotCol = solver.getPivotColumn(tableau);
    
    Assert.assertEquals(Integer.valueOf(1), pivotCol);
  }
  
  @Test
  public void testGetPivotRow() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 2);
    tableau.setEntry(0, 1, -1);
    tableau.setEntry(1, 0, 1);
    tableau.setEntry(1, 1, 1);
    
    SimplexSolver solver = new SimplexSolver();
    Integer pivotRow = solver.getPivotRow(1, tableau);
    
    Assert.assertEquals(Integer.valueOf(1), pivotRow);
  }
  
  @Test
  public void testDoIteration() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 1);
    tableau.setEntry(0, 1, -1);
    tableau.setEntry(1, 0, 1);
    tableau.setEntry(1, 1, 1);
    
    SimplexSolver solver = new SimplexSolver();
    solver.doIteration(tableau);
    
    Assert.assertEquals(0, tableau.getEntry(0, 0), 0.0001);
    Assert.assertEquals(0, tableau.getEntry(0, 1), 0.0001);
    Assert.assertEquals(1, tableau.getEntry(1, 0), 0.0001);
    Assert.assertEquals(0, tableau.getEntry(1, 1), 0.0001);
  }
  
  @Test
  public void testIsPhase1Solved() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 0);
    tableau.setEntry(0, 1, 0);
    tableau.setEntry(1, 0, 1);
    tableau.setEntry(1, 1, 1);
    
    SimplexSolver solver = new SimplexSolver();
    boolean phase1Solved = solver.isPhase1Solved(tableau);
    
    Assert.assertTrue(phase1Solved);
  }
  
  @Test
  public void testIsOptimal() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 0);
    tableau.setEntry(0, 1, 0);
    tableau.setEntry(1, 0, 1);
    tableau.setEntry(1, 1, 1);
    
    SimplexSolver solver = new SimplexSolver();
    boolean optimal = solver.isOptimal(tableau);
    
    Assert.assertTrue(optimal);
  }
  
  @Test
  public void testSolvePhase1() {
    SimplexTableau tableau = new SimplexTableau();
    tableau.setEntry(0, 0, 0);
    tableau.setEntry(0, 1, 0);
    tableau.setEntry(1, 0, 1);
    tableau.setEntry(1, 1, 1);
    
    SimplexSolver solver = new SimplexSolver();
    try {
      solver.solvePhase1(tableau);
    } catch (OptimizationException e) {
      Assert.fail("Exception thrown: " + e.getMessage());
    }
    
    Assert.assertEquals(0, tableau.getEntry(0, 0), 0.0001);
    Assert.assertEquals(0, tableau.getEntry(0, 1), 0.0001);
    Assert.assertEquals(1, tableau.getEntry(1, 0), 0.0001);
    Assert.assertEquals(0, tableau.getEntry(1, 1), 0.0001);
  }
  
  @Test
  public void testDoOptimize() {
    SimplexSolver solver = new SimplexSolver();
    try {
      RealPointValuePair solution = solver.doOptimize();
      double[] point = solution.getPoint();
      Assert.assertEquals(0, point[0], 0.0001);
      Assert.assertEquals(0, point[1], 0.0001);
      Assert.assertEquals(0, point[2], 0.0001);
    } catch (OptimizationException e) {
      Assert.fail("Exception thrown: " + e.getMessage());
    }
  }

}