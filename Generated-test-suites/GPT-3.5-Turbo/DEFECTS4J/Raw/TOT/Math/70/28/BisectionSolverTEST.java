import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BisectionSolverTest {

  //Test cases for the constructor with argument
  @Test
  public void testConstructorWithArgument() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return Math.sin(x);
      }
    };
    
    BisectionSolver solver = new BisectionSolver(f);
    
    assertEquals(f, solver.getFunction());
    assertEquals(100, solver.getMaximalIterationCount());
    assertEquals(1E-6, solver.getAbsoluteAccuracy(), 0.0);
  }

  //Test cases for the constructor without argument
  @Test
  public void testConstructorWithoutArgument() {
    BisectionSolver solver = new BisectionSolver();
    
    assertNull(solver.getFunction());
    assertEquals(100, solver.getMaximalIterationCount());
    assertEquals(1E-6, solver.getAbsoluteAccuracy(), 0.0);
  }

  //Test cases for solve(double, double, double)
  @Test
  public void testSolveWithInitialGuess() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x * x - 4;
      }
    };
    
    BisectionSolver solver = new BisectionSolver(f);
    
    double result = solver.solve(0, 3, 1);
    
    assertEquals(2, result, 1E-6);
  }

  @Test
  public void testSolveNoInitialGuess() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x * x - 9;
      }
    };
    
    BisectionSolver solver = new BisectionSolver(f);
    
    double result = solver.solve(0, 5);
    
    assertEquals(3, result, 1E-6);
  }

  //Test cases for solve(UnivariateRealFunction, double, double, double)
  @Test
  public void testSolveWithInitialGuess2() throws MaxIterationsExceededException, FunctionEvaluationException {
    double min = 0.0;
    double max = 3.0;
    double initial = 1.0;
    
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x * x - 4;
      }
    };
    
    BisectionSolver solver = new BisectionSolver(f);
    
    double result = solver.solve(f, min, max, initial);
    
    assertEquals(2, result, 1E-6);
  }

  //Test cases for solve(UnivariateRealFunction, double, double)
  @Test
  public void testSolveNoInitialGuess2() throws MaxIterationsExceededException, FunctionEvaluationException {
    double min = 0.0;
    double max = 5.0;
    
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x * x - 9;
      }
    };
    
    BisectionSolver solver = new BisectionSolver(f);
    
    double result = solver.solve(f, min, max);
    
    assertEquals(3, result, 1E-6);
  }

  //Edge cases
  @Test
  public void testSolveWithEqualMinMax() throws MaxIterationsExceededException, FunctionEvaluationException {
    double min = 2.0;
    double max = 2.0;
    
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x * x - 4;
      }
    };
    
    BisectionSolver solver = new BisectionSolver(f);
    
    double result = solver.solve(f, min, max);
    
    assertEquals(2, result, 1E-6);
  }

  //Error scenarios
  @Test(expected = MaxIterationsExceededException.class)
  public void testSolveWithMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
    double min = 0.0;
    double max = 3.0;
    
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x * x - 4;
      }
    };
    
    BisectionSolver solver = new BisectionSolver(f);
    
    double result = solver.solve(f, min, max, 1000);
  }

  @Test(expected = FunctionEvaluationException.class)
  public void testSolveWithFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
    double min = 0.0;
    double max = 3.0;
    
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException(x);
      }
    };
    
    BisectionSolver solver = new BisectionSolver(f);
    
    double result = solver.solve(f, min, max);
  }
}