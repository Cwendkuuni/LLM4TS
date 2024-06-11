import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  private BrentSolver brentSolver;
  private double[] xValues;
  private double[] fValues;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
    xValues = new double[] {1.0, 2.0, 3.0, 4.0};
    fValues = new double[] {1.0, 4.0, 9.0, 16.0};   
  }

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker(); // Stubbed ConvergenceChecker class

    boolean result = brentSolver.findRoot(function, checker, 100, xValues[0], fValues[0], xValues[3], fValues[3]);
    
    Assert.assertTrue(result);
    Assert.assertEquals(0.0, brentSolver.getRoot(), 1e-5);
  }

  // Additional test methods can be added here

}