import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;

public class ContinuedFractionTest {
  
  @Test
  public void testEvaluateDefaultEpsilon() {
    ContinuedFraction cf = new ContinuedFraction() {
      @Override
      protected double getA(int n, double x) {
        return 1.0;
      }
      
      @Override
      protected double getB(int n, double x) {
        return 1.0;
      }
    };
    
    double result = cf.evaluate(1.0);
    assertEquals(1.0, result, 0.0);
  }
  
  @Test
  public void testEvaluateCustomEpsilon() {
    ContinuedFraction cf = new ContinuedFraction() {
      @Override
      protected double getA(int n, double x) {
        return 1.0;
      }
      
      @Override
      protected double getB(int n, double x) {
        return 1.0;
      }
    };
    
    double result = cf.evaluate(1.0, 0.0001);
    assertEquals(1.0, result, 0.0001);
  }
  
  @Test
  public void testEvaluateCustomIterations() {
    ContinuedFraction cf = new ContinuedFraction() {
      @Override
      protected double getA(int n, double x) {
        return 1.0;
      }
      
      @Override
      protected double getB(int n, double x) {
        return 1.0;
      }
    };
    
    double result = cf.evaluate(1.0, 10);
    assertEquals(1.0, result, 0.0);
  }
  
  @Test(expected = ConvergenceException.class)
  public void testEvaluateConvergenceException() {
    ContinuedFraction cf = new ContinuedFraction() {
      @Override
      protected double getA(int n, double x) {
        return 1.0;
      }
      
      @Override
      protected double getB(int n, double x) {
        return 0.0; // Causes convergence exception
      }
    };
    
    cf.evaluate(1.0);
  }
  
  @Test(expected = MaxCountExceededException.class)
  public void testEvaluateMaxCountExceededException() {
    ContinuedFraction cf = new ContinuedFraction() {
      @Override
      protected double getA(int n, double x) {
        return 1.0;
      }
      
      @Override
      protected double getB(int n, double x) {
        return 1.0;
      }
    };
    
    cf.evaluate(1.0, 5); // Max iterations set to 5
  }
}