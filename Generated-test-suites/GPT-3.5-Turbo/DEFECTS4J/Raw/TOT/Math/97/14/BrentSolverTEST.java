package org.spaceroots.mantissa.roots;

import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot_withValidInput_shouldReturnTrue() {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // Always return ConvergenceChecker.HIGH to stop the iterations
        return ConvergenceChecker.HIGH;
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = -4;
    double x1 = 5;
    double f1 = 21;
    BrentSolver brentSolver = new BrentSolver();

    // Act
    boolean isRootFound = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    double root = brentSolver.getRoot();

    // Assert
    assertTrue(isRootFound);
    assertEquals(2.0, root, 0.0001);
  }

  @Test
  public void testFindRoot_withInvalidIntervalAndMaxIter_shouldReturnFalse() {
    // Arrange
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // Always return ConvergenceChecker.HIGH to stop the iterations
        return ConvergenceChecker.HIGH;
      }
    };
    int maxIter = 2;  // Set a small number of iterations
    double x0 = 2;    // Invalid lower bound
    double f0 = 0;
    double x1 = 5;
    double f1 = 21;
    BrentSolver brentSolver = new BrentSolver();

    // Act
    boolean isRootFound = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    double root = brentSolver.getRoot();

    // Assert
    assertFalse(isRootFound);
    assertTrue(Double.isNaN(root));
  }
}