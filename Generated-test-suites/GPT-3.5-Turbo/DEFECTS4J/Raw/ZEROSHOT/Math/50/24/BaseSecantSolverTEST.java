import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {
  
  private final BaseSecantSolver baseSecantSolver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI) {
    @Override
    protected double computeObjectiveValue(double arg0) throws FunctionEvaluationException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    protected double getFunctionValueAccuracy() {
      // TODO Auto-generated method stub
      return 0;
    }
  };

  @Test
  public void testSolve() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      @Override
      public double value(double arg0) throws FunctionEvaluationException {
        // TODO Auto-generated method stub
        return 0;
      }
    };
    double solution = baseSecantSolver.solve(100, f, 0, 5, AllowedSolution.ANY_SIDE);
    Assert.assertEquals(0, solution, 0.001);
  }
  
  @Test
  public void testSolveWithStartValue() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      @Override
      public double value(double arg0) throws FunctionEvaluationException {
        // TODO Auto-generated method stub
        return 0;
      }
    };
    double solution = baseSecantSolver.solve(100, f, 0, 5, 2, AllowedSolution.ANY_SIDE);
    Assert.assertEquals(0, solution, 0.001);
  }
  
  @Test
  public void testSolveWithRelativeAccuracy() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      @Override
      public double value(double arg0) throws FunctionEvaluationException {
        // TODO Auto-generated method stub
        return 0;
      }
    };
    double solution = baseSecantSolver.solve(100, f, 0, 5, 2, AllowedSolution.ANY_SIDE);
    Assert.assertEquals(0, solution, 0.001);
  }
  
  @Test
  public void testSolveWithFunctionValueAccuracy() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      @Override
      public double value(double arg0) throws FunctionEvaluationException {
        // TODO Auto-generated method stub
        return 0;
      }
    };
    double solution = baseSecantSolver.solve(100, f, 0, 5, 2, AllowedSolution.ANY_SIDE);
    Assert.assertEquals(0, solution, 0.001);
  }
  
  @Test
  public void testSolveWithAllowedSolution() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      @Override
      public double value(double arg0) throws FunctionEvaluationException {
        // TODO Auto-generated method stub
        return 0;
      }
    };
    double solution = baseSecantSolver.solve(100, f, 0, 5, 2, AllowedSolution.RIGHT_SIDE);
    Assert.assertEquals(0, solution, 0.001);
  }
  
  @Test(expected = MathInternalError.class)
  public void testSolveWithException() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      @Override
      public double value(double arg0) throws FunctionEvaluationException {
        // TODO Auto-generated method stub
        return 0;
      }
    };
    double solution = baseSecantSolver.solve(100, f, 0, 5, 2, null);
    Assert.assertEquals(0, solution, 0.001);
  }

}