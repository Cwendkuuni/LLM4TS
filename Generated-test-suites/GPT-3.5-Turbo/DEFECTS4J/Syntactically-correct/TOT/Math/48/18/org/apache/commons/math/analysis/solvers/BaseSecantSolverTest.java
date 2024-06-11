import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

  private BaseSecantSolver secantSolver;

  @Before
  public void setup() {
    secantSolver = new DummySecantSolver();
  }

  // Test computeObjectiveValue() method

  @Test
  public void testComputeObjectiveValue_withValidFunction_returnExpectedValue() {
    UnivariateFunction f = new TestFunction();
    double x = 2.0;
    double expectedValue = 1.0;
    
    double result = secantSolver.computeObjectiveValue(f, x);
    
    assertEquals(expectedValue, result, 0.0);
  }

  @Test(expected = NullPointerException.class)
  public void testComputeObjectiveValue_withNullFunction_throwNullPointerException() {
    UnivariateFunction f = null;
    double x = 2.0;
    
    secantSolver.computeObjectiveValue(f, x);
  }

  // Test verifyBracketing() method

  @Test(expected = ConvergenceException.class)
  public void testVerifyBracketing_withInvalidBracketing_throwConvergenceException() {
    double x0 = 1.0;
    double x1 = 1.0;
    
    secantSolver.verifyBracketing(x0, x1);
  }

  @Test
  public void testVerifyBracketing_withValidBracketing_noExceptionThrown() {
    double x0 = 1.0;
    double x1 = 2.0;
    
    secantSolver.verifyBracketing(x0, x1);
  }

  // Test solve() method

  @Test
  public void testSolve_withValidInput_returnExpectedRoot() {
    int maxEval = 100;
    UnivariateFunction f = new TestFunction();
    double min = 0.0;
    double max = 3.0;
    double startValue = 1.0;
    AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
    double expectedRoot = 1.0;
    
    double result = secantSolver.solve(maxEval, f, min, max, startValue, allowedSolution);
    
    assertEquals(expectedRoot, result, 0.0);
  }

  @Test(expected = NullPointerException.class)
  public void testSolve_withNullFunction_throwNullPointerException() {
    int maxEval = 100;
    UnivariateFunction f = null;
    double min = 0.0;
    double max = 3.0;
    double startValue = 1.0;
    AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
    
    secantSolver.solve(maxEval, f, min, max, startValue, allowedSolution);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSolve_withInvalidRange_throwIllegalArgumentException() {
    int maxEval = 100;
    UnivariateFunction f = new TestFunction();
    double min = 3.0;
    double max = 0.0;
    double startValue = 1.0;
    AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
    
    secantSolver.solve(maxEval, f, min, max, startValue, allowedSolution);
  }

  @Test(expected = ConvergenceException.class)
  public void testSolve_withInvalidStartValue_throwConvergenceException() {
    int maxEval = 100;
    UnivariateFunction f = new TestFunction();
    double min = 0.0;
    double max = 3.0;
    double startValue = 5.0;
    AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
    
    secantSolver.solve(maxEval, f, min, max, startValue, allowedSolution);
  }

  // Test doSolve() method

  @Test
  public void testDoSolve_withValidInput_returnExpectedRoot() {
    secantSolver.setAbsoluteAccuracy(1e-6);
    secantSolver.setRelativeAccuracy(1e-6);
    secantSolver.setFunctionValueAccuracy(1e-6);
    
    double expectedRoot = 1.0;
    
    double result = secantSolver.doSolve();
    
    assertEquals(expectedRoot, result, 1e-6);
  }

  @Test(expected = ConvergenceException.class)
  public void testDoSolve_withInvalidInput_throwConvergenceException() {
    secantSolver.setAbsoluteAccuracy(1e-10);
    secantSolver.setRelativeAccuracy(1e-10);
    secantSolver.setFunctionValueAccuracy(1e-10);
    
    secantSolver.doSolve();
  }

  // Test Helper Classes

  private class DummySecantSolver extends BaseSecantSolver {
  
    public DummySecantSolver() {
      super(1e-6, Method.REGULA_FALSI);
    }

    @Override
    protected double computeObjectiveValue(UnivariateFunction f, double x) {
      return f.value(x);
    }

    @Override
    protected void verifyBracketing(double x0, double x1) {
      if (x0 >= x1) {
        throw new ConvergenceException("Invalid bracketing");
      }
    }
  }

  private class TestFunction implements UnivariateFunction {
  
    @Override
    public double value(double x) {
      return x - 1.0;
    }
  }

}