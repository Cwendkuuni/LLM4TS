import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionTestUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  @Test
  public void testConstructor() {
    new BrentSolver();
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithNullFunction() {
    new BrentSolver().findRoot(null, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    }, 10, 0.0, 1.0, 1.0, 1.0);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithNullChecker() {
    new BrentSolver().findRoot(new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return 0.0;
      }
    }, null, 10, 0.0, 1.0, 1.0, 1.0);
  }

  @Test
  public void testFindRootSimpleExample() {
    ComputableFunction function = FunctionTestUtils.simpleFunction();
    BrentSolver solver = new BrentSolver();
    double root = 2.0;
    double x0 = 1.0;
    double x1 = 3.0;

    Assert.assertTrue(solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    }, 10, x0, function.valueAt(x0), x1, function.valueAt(x1), root));

    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootDivergentFunction() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.log(Math.abs(x - 1.0));
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;

    Assert.assertFalse(solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    }, 10, x0, function.valueAt(x0), x1, function.valueAt(x1), 0.0));

    Assert.assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  @Test
  public void testFindRootFunctionWithMultipleRoots() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 2.0, 2.0) * (x - 3.0);
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;

    Assert.assertTrue(solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    }, 10, x0, function.valueAt(x0), x1, function.valueAt(x1), 1.5));

    Assert.assertEquals(1.5, solver.getRoot(), epsilon);

    solver.reset();

    Assert.assertTrue(solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.HIGH;
      }
    }, 10, x0, function.valueAt(x0), x1, function.valueAt(x1), 2.5));

    Assert.assertEquals(2.5, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootFunctionWithIntervalContainingNoRoots() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI * x);
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = -1.0;
    double x1 = 1.0;

    Assert.assertFalse(solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    }, 10, x0, function.valueAt(x0), x1, function.valueAt(x1), 0.0));

    Assert.assertEquals(0.0, solver.getRoot(), 0.0);
  }

  @Test
  public void testFindRootFunctionWithIntervalContainingSingleRoot() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1.0, 2.0) * (x - 2.0);
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 0.5;
    double x1 = 2.5;

    Assert.assertTrue(solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    }, 10, x0, function.valueAt(x0), x1, function.valueAt(x1), 1.0));

    Assert.assertEquals(1.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootFunctionWithIntervalContainingRootsAtBounds() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1.0, 2.0) * (x - 2.0);
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 1.5;

    Assert.assertTrue(solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    }, 10, x0, function.valueAt(x0), x1, function.valueAt(x1), 1.0));

    Assert.assertEquals(1.0, solver.getRoot(), epsilon);

    solver.reset();

    Assert.assertTrue(solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.HIGH;
      }
    }, 10, x0, function.valueAt(x0), x1, function.valueAt(x1), 1.5));

    Assert.assertEquals(1.5, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootFunctionWithIntervalContainingRootsInMiddle() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1.0, 2.0) * (x - 2.0);
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;

    Assert.assertTrue(solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    }, 10, x0, function.valueAt(x0), x1, function.valueAt(x1), 1.5));

    Assert.assertEquals(1.5, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootFunctionWithIntervalContainingRootsFarAway() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1.0, 2.0) * (x - 2.0);
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = -10.0;
    double x1 = 10.0;

    Assert.assertTrue(solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    }, 10, x0, function.valueAt(x0), x1, function.valueAt(x1), 0.5));

    Assert.assertEquals(0.5, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootFunctionWithIntervalContainingRootsNearBoundary() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1.0, 2.0) * (x - 2.0);
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = -1.0;
    double x1 = 1.0;

    Assert.assertTrue(solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    }, 10, x0, function.valueAt(x0), x1, function.valueAt(x1), 0.0));

    Assert.assertEquals(0.0, solver.getRoot(), 0.0);

    solver.reset();

    Assert.assertTrue(solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.HIGH;
      }
    }, 10, x0, function.valueAt(x0), x1, function.valueAt(x1), 1.0));

    Assert.assertEquals(1.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootFunctionWithLargeInterval() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - 1.0, 2.0) * (x - 2.0);
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = -1000.0;
    double x1 = 1000.0;

    Assert.assertTrue(solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    }, 10, x0, function.valueAt(x0), x1, function.valueAt(x1), 0.5));

    Assert.assertEquals(0.5, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootFunctionWithFunctionThrowingException() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Test exception");
      }
    };
    BrentSolver solver = new BrentSolver();

    try {
      solver.findRoot(function, new ConvergenceChecker() {
        @Override
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
          return ConvergenceChecker.LOW;
        }
      }, 10, 0.0, 0.0, 1.0, 1.0, 0.0);
      Assert.fail();
    } catch (FunctionException e) {
      Assert.assertEquals("Test exception", e.getMessage());
    }

    Assert.assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

}