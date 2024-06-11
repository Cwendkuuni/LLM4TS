import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    
    // Test with a simple linear function
    ComputableFunction linearFunction = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return 2 * x - 4;
      }
    };
    
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (int) Math.signum(fLow * fHigh);
      }
    };
    
    BrentSolver brentSolver = new BrentSolver();
    boolean rootFound = brentSolver.findRoot(linearFunction, convergenceChecker, 100, 0, -4, 4, 4);
    
    Assert.assertTrue(rootFound);
    Assert.assertEquals(2.0, brentSolver.getRoot(), 0.0001);
  }
  
  @Test
  public void testFindRootEdgeCase() {
    
    // Test with a constant function
    ComputableFunction constantFunction = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return 2;
      }
    };
    
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (int) Math.signum(fLow * fHigh);
      }
    };
    
    BrentSolver brentSolver = new BrentSolver();
    boolean rootFound = brentSolver.findRoot(constantFunction, convergenceChecker, 100, 0, 2, 4, 2);
    
    Assert.assertFalse(rootFound);
    Assert.assertTrue(Double.isNaN(brentSolver.getRoot()));
  }
  
  @Test(expected = FunctionException.class)
  public void testFindRootErrorScenario() throws FunctionException {
    
    // Test with a function that throws an exception
    ComputableFunction errorFunction = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Error in function");
      }
    };
    
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (int) Math.signum(fLow * fHigh);
      }
    };
    
    BrentSolver brentSolver = new BrentSolver();
    brentSolver.findRoot(errorFunction, convergenceChecker, 100, 0, -4, 4, 4);
  }
}