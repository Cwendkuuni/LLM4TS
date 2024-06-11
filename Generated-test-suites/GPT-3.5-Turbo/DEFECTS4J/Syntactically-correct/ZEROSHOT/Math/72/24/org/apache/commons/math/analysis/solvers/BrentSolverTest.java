import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test case 1: Root found in the given interval
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 2.0;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLower, double fLower, double xHigher, double fHigher) {
        if (Math.abs(fLower) < 1e-10) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigher) < 1e-10) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    int maxIter1 = 100;
    double x0_1 = 1.0;
    double f0_1 = -1.0;
    double x1_1 = 3.0;
    double f1_1 = 1.0;

    BrentSolver solver1 = new BrentSolver();
    boolean result1 = solver1.findRoot(function1, checker1, maxIter1, x0_1, f0_1, x1_1, f1_1);

    org.junit.Assert.assertTrue(result1);
    org.junit.Assert.assertEquals(2.0, solver1.getRoot(), 1e-10);


    // Test case 2: Root not found in the given interval
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4.0;
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLower, double fLower, double xHigher, double fHigher) {
        if (Math.abs(fLower) < 1e-10) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigher) < 1e-10) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    int maxIter2 = 100;
    double x0_2 = 1.0;
    double f0_2 = -3.0;
    double x1_2 = 3.0;
    double f1_2 = 5.0;

    BrentSolver solver2 = new BrentSolver();
    boolean result2 = solver2.findRoot(function2, checker2, maxIter2, x0_2, f0_2, x1_2, f1_2);

    org.junit.Assert.assertFalse(result2);


    // Test case 3: Root found at the lower bound
    ComputableFunction function3 = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 1.0;
      }
    };
    ConvergenceChecker checker3 = new ConvergenceChecker() {
      public int converged(double xLower, double fLower, double xHigher, double fHigher) {
        if (Math.abs(fLower) < 1e-10) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigher) < 1e-10) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    int maxIter3 = 100;
    double x0_3 = 1.0;
    double f0_3 = 0.0;
    double x1_3 = 3.0;
    double f1_3 = 2.0;

    BrentSolver solver3 = new BrentSolver();
    boolean result3 = solver3.findRoot(function3, checker3, maxIter3, x0_3, f0_3, x1_3, f1_3);

    org.junit.Assert.assertTrue(result3);
    org.junit.Assert.assertEquals(1.0, solver3.getRoot(), 1e-10);


    // Test case 4: Root found at the higher bound
    ComputableFunction function4 = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 3.0;
      }
    };
    ConvergenceChecker checker4 = new ConvergenceChecker() {
      public int converged(double xLower, double fLower, double xHigher, double fHigher) {
        if (Math.abs(fLower) < 1e-10) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigher) < 1e-10) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    int maxIter4 = 100;
    double x0_4 = 1.0;
    double f0_4 = -2.0;
    double x1_4 = 3.0;
    double f1_4 = 0.0;

    BrentSolver solver4 = new BrentSolver();
    boolean result4 = solver4.findRoot(function4, checker4, maxIter4, x0_4, f0_4, x1_4, f1_4);

    org.junit.Assert.assertTrue(result4);
    org.junit.Assert.assertEquals(3.0, solver4.getRoot(), 1e-10);
  }
}