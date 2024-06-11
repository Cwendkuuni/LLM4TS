import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

  // Test solve(int, UnivariateRealFunction, double, double, AllowedSolution)
  @Test
  public void testSolve1() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    double solution = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    Assert.assertEquals(2, solution, 1e-6);
  }
  
  // Test solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)
  @Test
  public void testSolve2() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    double solution = solver.solve(100, f, 1, 3, 1.5, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    Assert.assertEquals(2, solution, 1e-6);
  }
  
  // Test solve(int, UnivariateRealFunction, double, double, AllowedSolution)
  @Test
  public void testSolve3() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    double solution = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    Assert.assertEquals(2, solution, 1e-6);
  }
  
  // Test solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)
  @Test
  public void testSolve4() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    double solution = solver.solve(100, f, 1, 3, 1.5, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    Assert.assertEquals(2, solution, 1e-6);
  }
  
  // Test solve(int, UnivariateRealFunction, double, double, AllowedSolution)
  @Test
  public void testSolve5() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    double solution = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    Assert.assertEquals(2, solution, 1e-6);
  }
  
  // Test solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)
  @Test
  public void testSolve6() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    double solution = solver.solve(100, f, 1, 3, 1.5, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    Assert.assertEquals(2, solution, 1e-6);
  }
  
  // Test computeObjectiveValue(double)
  @Test
  public void testComputeObjectiveValue1() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    double value = solver.computeObjectiveValue(2);
    Assert.assertEquals(0, value, 1e-6);
  }
  
  // Test computeObjectiveValue(double)
  @Test
  public void testComputeObjectiveValue2() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
    double value = solver.computeObjectiveValue(2);
    Assert.assertEquals(0, value, 1e-6);
  }
  
  // Test computeObjectiveValue(double)
  @Test
  public void testComputeObjectiveValue3() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
    double value = solver.computeObjectiveValue(2);
    Assert.assertEquals(0, value, 1e-6);
  }
  
  // Test verifyBracketing(double, double)
  @Test(expected = MathInternalError.class)
  public void testVerifyBracketing() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    solver.verifyBracketing(1, 4);
  }
  
  // Test getFunctionValueAccuracy()
  @Test
  public void testGetFunctionValueAccuracy() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    double accuracy = solver.getFunctionValueAccuracy();
    Assert.assertEquals(1e-6, accuracy, 1e-6);
  }
  
  // Test getAbsoluteAccuracy()
  @Test
  public void testGetAbsoluteAccuracy() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    double accuracy = solver.getAbsoluteAccuracy();
    Assert.assertEquals(1e-6, accuracy, 1e-6);
  }
  
  // Test getRelativeAccuracy()
  @Test
  public void testGetRelativeAccuracy() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    double accuracy = solver.getRelativeAccuracy();
    Assert.assertEquals(1e-6, accuracy, 1e-6);
  }
  
  // Test getMin()
  @Test
  public void testGetMin() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    double min = solver.getMin();
    Assert.assertEquals(Double.NEGATIVE_INFINITY, min, 1e-6);
  }
  
  // Test getMax()
  @Test
  public void testGetMax() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    double max = solver.getMax();
    Assert.assertEquals(Double.POSITIVE_INFINITY, max, 1e-6);
  }
  
  // Test getAllowedSolution()
  @Test
  public void testGetAllowedSolution() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    BaseSecantSolver.AllowedSolution solution = solver.getAllowedSolution();
    Assert.assertEquals(BaseSecantSolver.AllowedSolution.ANY_SIDE, solution);
  } 

}