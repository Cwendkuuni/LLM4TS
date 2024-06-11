import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {
  
  @Test
  public void testDefaultConstructor() {
    BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
    assertNotNull(solver);
    assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
    assertEquals(5, solver.getMaximalOrder());
  }
  
  @Test
  public void testConstructorWithAbsoluteAccuracy() {
    BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 8);
    assertNotNull(solver);
    assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
    assertEquals(8, solver.getMaximalOrder());
  }
  
  @Test
  public void testConstructorWithRelativeAccuracy() {
    BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1e-8, 1e-10, 10);
    assertNotNull(solver);
    assertEquals(1e-6, solver.getRelativeAccuracy(), 0.0);
    assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
    assertEquals(1e-10, solver.getFunctionValueAccuracy(), 0.0);
    assertEquals(10, solver.getMaximalOrder());
  }
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testConstructorWithInvalidMaximalOrder() {
    new BracketingNthOrderBrentSolver(1e-6, 1);
  }

  // Add more test cases for other methods in the class
  
}