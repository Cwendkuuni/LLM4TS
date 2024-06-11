import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {

  @Test
  public void testConstructor1() {
    BisectionSolver solver = new BisectionSolver();
    assertNotNull(solver);
  }

  @Test
  @Deprecated
  public void testConstructor2() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x;
      }
    };

    BisectionSolver solver = new BisectionSolver(f);
    assertNotNull(solver);
  }

  @Test
  @Deprecated
  public void testSolve1() {
    BisectionSolver solver = new BisectionSolver();
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };

    try {
      double result = solver.solve(f, 0, 2);
      assertEquals(2, result, 1E-6);
    } catch (MaxIterationsExceededException e) {
      fail(e.getMessage());
    } catch (FunctionEvaluationException e) {
      fail(e.getMessage());
    }
  }

  @Test
  @Deprecated
  public void testSolve2() {
    BisectionSolver solver = new BisectionSolver();
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };

    try {
      double result = solver.solve(f, 1, 3, 2);
      assertEquals(2, result, 1E-6);
    } catch (MaxIterationsExceededException e) {
      fail(e.getMessage());
    } catch (FunctionEvaluationException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testSolve3() {
    BisectionSolver solver = new BisectionSolver();
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };

    try {
      double result = solver.solve(f, 0, 2);
      assertEquals(2, result, 1E-6);
    } catch (MaxIterationsExceededException e) {
      fail(e.getMessage());
    } catch (FunctionEvaluationException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testSolve4() {
    BisectionSolver solver = new BisectionSolver();
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };

    try {
      double result = solver.solve(f, 1, 3);
      assertEquals(2, result, 1E-6);
    } catch (MaxIterationsExceededException e) {
      fail(e.getMessage());
    } catch (FunctionEvaluationException e) {
      fail(e.getMessage());
    }
  }

  @Test(expected=MaxIterationsExceededException.class)
  public void testSolve5() throws MaxIterationsExceededException, FunctionEvaluationException {
    BisectionSolver solver = new BisectionSolver();
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };

    double result = solver.solve(f, 0, 2, 50);
  }

  @Test(expected=MaxIterationsExceededException.class)
  public void testSolve6() throws MaxIterationsExceededException, FunctionEvaluationException {
    BisectionSolver solver = new BisectionSolver();
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };

    double result = solver.solve(f, 0, 2);
  }

  @Test
  public void testSolve7() {
    BisectionSolver solver = new BisectionSolver();
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };

    try {
      double result = solver.solve(f, -2, -1);
      assertEquals(-2, result, 1E-6);
    } catch (MaxIterationsExceededException e) {
      fail(e.getMessage());
    } catch (FunctionEvaluationException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testSolve8() {
    BisectionSolver solver = new BisectionSolver();
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };

    try {
      double result = solver.solve(f, -3, -1);
      assertEquals(-2, result, 1E-6);
    } catch (MaxIterationsExceededException e) {
      fail(e.getMessage());
    } catch (FunctionEvaluationException e) {
      fail(e.getMessage());
    }
  }

  @Test(expected=MaxIterationsExceededException.class)
  public void testSolve9() throws MaxIterationsExceededException, FunctionEvaluationException {
    BisectionSolver solver = new BisectionSolver();
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };

    double result = solver.solve(f, -2, -1, 200);
  }

  @Test(expected=MaxIterationsExceededException.class)
  public void testSolve10() throws MaxIterationsExceededException, FunctionEvaluationException {
    BisectionSolver solver = new BisectionSolver();
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };

    double result = solver.solve(f, -3, -1);
  }

}