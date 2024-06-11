import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test case 1
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker();
    int maxIter1 = 100;
    double x01 = 0;
    double f01 = function1.valueAt(x01);
    double x11 = 5;
    double f11 = function1.valueAt(x11);
    assertEquals(true, solve.findRoot(function1, checker1, maxIter1, x01, f01, x11, f11));
    assertEquals(2.0, solve.getRoot(), 0.1);

    // Test case 2
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker();
    int maxIter2 = 100;
    double x02 = -1;
    double f02 = function2.valueAt(x02);
    double x12 = 1;
    double f12 = function2.valueAt(x12);
    assertEquals(true, solve.findRoot(function2, checker2, maxIter2, x02, f02, x12, f12));
    assertEquals(0.0, solve.getRoot(), 0.1);

    // Test case 3 (edge case)
    ComputableFunction function3 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x;
      }
    };
    ConvergenceChecker checker3 = new ConvergenceChecker();
    int maxIter3 = 100;
    double x03 = Double.MIN_VALUE;
    double f03 = function3.valueAt(x03);
    double x13 = Double.MAX_VALUE;
    double f13 = function3.valueAt(x13);
    assertEquals(true, solve.findRoot(function3, checker3, maxIter3, x03, f03, x13, f13));
    assertEquals(0.0, solve.getRoot(), 0.1);
  }

}