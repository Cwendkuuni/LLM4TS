import static org.junit.Assert.*;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

  @Test
  public void testDefaultConstructor() {
    BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
    assertNotNull(solver);
    assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0);
    assertEquals(5, solver.getMaximalOrder());
  }
  
  @Test
  public void testConstructorWithAccuracy() {
    BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
    assertNotNull(solver);
    assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0);
    assertEquals(3, solver.getMaximalOrder());
  }
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testConstructorWithInvalidMaximalOrder() {
    new BracketingNthOrderBrentSolver(1e-8, 1);
  }
  
  @Test
  public void testConstructorWithAllParameters() {
    BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1e-10, 1e-12, 4);
    assertNotNull(solver);
    assertEquals(1e-8, solver.getRelativeAccuracy(), 0);
    assertEquals(1e-10, solver.getAbsoluteAccuracy(), 0);
    assertEquals(1e-12, solver.getFunctionValueAccuracy(), 0);
    assertEquals(4, solver.getMaximalOrder());
  }

  @Test(expected = NumberIsTooSmallException.class)
  public void testConstructorWithAllParametersAndInvalidMaximalOrder() {
    new BracketingNthOrderBrentSolver(-1e-8, 1e-10, 1e-12, 1);
  }

  @Test
  public void testSolving() {
    UnivariateFunction f = new UnivariateFunction() {
      public double value(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
    double root = solver.solve(100, f, -10, 10);
    assertEquals(2, root, 0);
  }

}