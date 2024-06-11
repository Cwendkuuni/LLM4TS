import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot_WithPositiveInputs_ReturnsTrueAndRoot(){
    // Setup
    BrentSolver solver = new BrentSolver();
    
    double x0 = -1.0;
    double f0 = f(x0);
    double x1 = 1.0;
    double f1 = f(x1);

    // Action
    boolean result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return f(x);
      }
    }, new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2){
        return 0;
      }
    }, 100, x0, f0, x1, f1);

    // Verify
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 0.000001);
  }

  @Test
  public void testFindRoot_WithNegativeInputs_ReturnsTrueAndRoot(){
    // Setup
    BrentSolver solver = new BrentSolver();
    
    double x0 = -1.0;
    double f0 = f(x0);
    double x1 = -2.0;
    double f1 = f(x1);

    // Action
    boolean result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return f(x);
      }
    }, new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2){
        return 0;
      }
    }, 100, x0, f0, x1, f1);

    // Verify
    assertTrue(result);
    assertEquals(-1.618034, solver.getRoot(), 0.000001);
  }

  @Test
  public void testFindRoot_WithZeroInputs_ReturnsTrueAndRoot(){
    // Setup
    BrentSolver solver = new BrentSolver();
    
    double x0 = -0.5;
    double f0 = f(x0);
    double x1 = 0.5;
    double f1 = f(x1);

    // Action
    boolean result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return f(x);
      }
    }, new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2){
        return 0;
      }
    }, 100, x0, f0, x1, f1);

    // Verify
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 0.000001);
  }

  @Test
  public void testFindRoot_WithInvalidInterval_ReturnsFalseAndNaN(){
    // Setup
    BrentSolver solver = new BrentSolver();
    
    double x0 = 1.0;
    double f0 = f(x0);
    double x1 = 0.0;
    double f1 = f(x1);

    // Action
    boolean result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return f(x);
      }
    }, new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2){
        return 0;
      }
    }, 100, x0, f0, x1, f1);

    // Verify
    assertFalse(result);
    assertTrue(Double.isNaN(solver.getRoot()));
  }

  private double f(double x){
    return x * x - 4;
  }

}