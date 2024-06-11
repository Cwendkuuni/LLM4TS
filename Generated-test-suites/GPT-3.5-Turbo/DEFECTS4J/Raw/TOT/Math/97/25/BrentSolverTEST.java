import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;
  
  @Before
  public void setUp() {
    solver = new BrentSolver();
  }
  
  @Test
  public void testFindRoot_typicalCase() throws FunctionException {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (f2 < 0 && f1 >= 0) {
          return LOW;
        } else if (f1 < 0 && f2 >= 0) {
          return HIGH;
        } else {
          return UNCONVERGED;
        }
      }
    };
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    // Act
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    double root = solver.getRoot();
    
    // Assert
    assertTrue(rootFound);
    assertEquals(2.0, root, 0.001);
  }
  
  @Test
  public void testFindRoot_edgeCase() throws FunctionException {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (f2 < 0 && f1 >= 0) {
          return LOW;
        } else if (f1 < 0 && f2 >= 0) {
          return HIGH;
        } else {
          return UNCONVERGED;
        }
      }
    };
    int maxIter = 100;
    double x0 = -10.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);

    // Act
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    double root = solver.getRoot();
    
    // Assert
    assertTrue(rootFound);
    assertEquals(2.0, root, 0.001);
  }
  
  @Test
  public void testFindRoot_errorScenario() throws FunctionException {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (f2 < 0 && f1 >= 0) {
          return LOW;
        } else if (f1 < 0 && f2 >= 0) {
          return HIGH;
        } else {
          return UNCONVERGED;
        }
      }
    };
    int maxIter = 10;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    // Act
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    double root = solver.getRoot();
    
    // Assert
    assertFalse(rootFound);
    assertEquals(Double.NaN, root, 0.001);
  }
  
}