import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {
  
  @Test
  public void testGetPivotColumn() {
    SimplexSolver simplexSolver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();
    
    // Test with empty tableau
    assertNull(simplexSolver.getPivotColumn(tableau));
    
    // Test with tableau containing negative coefficients
    tableau.setEntry(0, 0, -5);
    tableau.setEntry(0, 1, -10);
    assertEquals(0, (int) simplexSolver.getPivotColumn(tableau));
  }
  
  @Test
  public void testGetPivotRow() {
    SimplexSolver simplexSolver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();
    
    // Test with empty tableau
    assertNull(simplexSolver.getPivotRow(0, tableau));
    
    // Test with non-negative ratio
    tableau.setEntry(0, 3, 5);
    tableau.setEntry(1, 3, 10);
    assertEquals(1, (int) simplexSolver.getPivotRow(3, tableau));
  }
  
  @Test
  public void testDoIteration() {
    SimplexSolver simplexSolver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();
    
    // Test with empty tableau
    assertThrows(OptimizationException.class, () -> simplexSolver.doIteration(tableau));
  }
  
  @Test
  public void testIsOptimal() {
    SimplexSolver simplexSolver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();
    
    // Test with empty tableau
    assertTrue(simplexSolver.isOptimal(tableau));
    
    // Test with non-optimal tableau
    tableau.setEntry(0, 1, -5);
    assertFalse(simplexSolver.isOptimal(tableau));
  }
  
  @Test
  public void testSolvePhase1() {
    SimplexSolver simplexSolver = new SimplexSolver();
    SimplexTableau tableau = new SimplexTableau();
    
    // Test with empty tableau
    assertThrows(OptimizationException.class, () -> simplexSolver.solvePhase1(tableau));
  }
  
  @Test
  public void testDoOptimize() {
    SimplexSolver simplexSolver = new SimplexSolver();
    
    // Test with all constraints satisfied
    double[] coefficients = {2, 3};
    LinearConstraintSet constraints = new LinearConstraintSet(coefficients, Relationship.LEQ, 10);
    RealPointValuePair result = simplexSolver.doOptimize(constraints);
    
    assertNotNull(result);
    assertArrayEquals(new double[]{2, 3}, result.getPoint(), 0.001);
    assertEquals(0, result.getValue(), 0.001);
  }
  
}