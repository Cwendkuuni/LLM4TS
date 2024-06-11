import org.junit.Test;
import org.junit.Assert;

public class ContinuedFractionTest {
  
  @Test
  public void testGetA() {
    // Test with n = 0
    ContinuedFraction cf = new ContinuedFraction() {
      protected double getA(int n, double x) {
        if (n == 0) {
          return 2.0;
        }
        return 0.0;
      }
      
      protected double getB(int n, double x) {
        return 0.0;
      }
    };
    
    Assert.assertEquals(2.0, cf.getA(0, 1.0), 0.0);
  }
  
  @Test
  public void testGetB() {
    // Test with n = 1
    ContinuedFraction cf = new ContinuedFraction() {
      protected double getA(int n, double x) {
        return 0.0;
      }
      
      protected double getB(int n, double x) {
        if (n == 1) {
          return 3.0;
        }
        return 0.0;
      }
    };
    
    Assert.assertEquals(3.0, cf.getB(1, 1.0), 0.0);
  }
  
  @Test
  public void testEvaluate() {
    // Test evaluation with default epsilon and maxIterations
    ContinuedFraction cf = new ContinuedFraction() {
      protected double getA(int n, double x) {
        return n + 1.0;
      }
      
      protected double getB(int n, double x) {
        return n + 2.0;
      }
    };
    
    Assert.assertEquals(0.571428571, cf.evaluate(1.0), 0.000001);
  }
  
  @Test
  public void testEvaluateWithEpsilon() {
    // Test evaluation with custom epsilon and default maxIterations
    ContinuedFraction cf = new ContinuedFraction() {
      protected double getA(int n, double x) {
        return n + 1.0;
      }
      
      protected double getB(int n, double x) {
        return n + 2.0;
      }
    };
    
    Assert.assertEquals(0.571428571, cf.evaluate(1.0, 0.000001), 0.000001);
  }
  
  @Test
  public void testEvaluateWithMaxIterations() {
    // Test evaluation with default epsilon and custom maxIterations
    ContinuedFraction cf = new ContinuedFraction() {
      protected double getA(int n, double x) {
        return n + 1.0;
      }
      
      protected double getB(int n, double x) {
        return n + 2.0;
      }
    };
    
    Assert.assertEquals(0.571428571, cf.evaluate(1.0, Integer.MAX_VALUE), 0.000001);
  }
  
  @Test
  public void testEvaluateWithEpsilonAndMaxIterations() {
    // Test evaluation with custom epsilon and maxIterations
    ContinuedFraction cf = new ContinuedFraction() {
      protected double getA(int n, double x) {
        return n + 1.0;
      }
      
      protected double getB(int n, double x) {
        return n + 2.0;
      }
    };
    
    Assert.assertEquals(0.571428571, cf.evaluate(1.0, 0.000001, Integer.MAX_VALUE), 0.000001);
  }
  
  @Test(expected = ConvergenceException.class)
  public void testEvaluateConvergenceException() {
    // Test convergence exception when algorithm fails to converge
    ContinuedFraction cf = new ContinuedFraction() {
      protected double getA(int n, double x) {
        return 1.0;
      }
      
      protected double getB(int n, double x) {
        return 1.0;
      }
    };
    
    cf.evaluate(1.0);
  }
  
  @Test(expected = MaxCountExceededException.class)
  public void testEvaluateMaxCountExceededException() {
    // Test max count exceeded exception when maximum number of convergents is reached
    ContinuedFraction cf = new ContinuedFraction() {
      protected double getA(int n, double x) {
        return n + 1.0;
      }
      
      protected double getB(int n, double x) {
        return n + 2.0;
      }
    };
    
    cf.evaluate(1.0, 0.000001, 100);
  }
  
}