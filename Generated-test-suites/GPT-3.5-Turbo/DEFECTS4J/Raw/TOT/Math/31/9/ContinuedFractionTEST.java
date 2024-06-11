import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

   @Test
   public void testGetA() {
      // Test typical use case
      ContinuedFractionImpl fraction = new ContinuedFractionImpl();
      double result = fraction.getA(5, 3.14);
      assertEquals(6.14, result, 0.001);
      
      // Test edge case
      result = fraction.getA(0, 0);
      assertEquals(1.0, result, 0.001);
      
      // Test error scenario
      try {
          result = fraction.getA(-1, 2.0);
          fail("Expected IllegalArgumentException");
      } catch (IllegalArgumentException e) {
          assertEquals("Index must be non-negative: -1", e.getMessage());
      }
   }
   
   @Test
   public void testGetB() {
      // Test typical use case
      ContinuedFractionImpl fraction = new ContinuedFractionImpl();
      double result = fraction.getB(3, 1.5);
      assertEquals(4.5, result, 0.001);
      
      // Test edge case
      result = fraction.getB(0, 0);
      assertEquals(1.0, result, 0.001);
      
      // Test error scenario
      try {
          result = fraction.getB(-2, 2.5);
          fail("Expected IllegalArgumentException");
      } catch (IllegalArgumentException e) {
          assertEquals("Index must be non-negative: -2", e.getMessage());
      }
   }
   
   @Test
   public void testEvaluate() {
      // Test typical use case
      ContinuedFractionImpl fraction = new ContinuedFractionImpl();
      double result = fraction.evaluate(1.0);
      assertEquals(1.5, result, 0.001);
      
      // Test edge case
      result = fraction.evaluate(0);
      assertEquals(1.0, result, 0.001);
      
      // Test error scenario
      try {
          result = fraction.evaluate(-2);
          fail("Expected ConvergenceException");
      } catch (ConvergenceException e) {
          assertEquals("The algorithm failed to converge.", e.getMessage());
      }
   }
   
   @Test
   public void testEvaluateWithEpsilon() {
      // Test typical use case
      ContinuedFractionImpl fraction = new ContinuedFractionImpl();
      double result = fraction.evaluate(2.0, 0.001);
      assertEquals(2.5, result, 0.001);
      
      // Test edge case
      result = fraction.evaluate(0, 0.0);
      assertEquals(1.0, result, 0.001);
      
      // Test error scenario
      try {
          result = fraction.evaluate(-1, 0.01);
          fail("Expected ConvergenceException");
      } catch (ConvergenceException e) {
          assertEquals("The algorithm failed to converge.", e.getMessage());
      }
   }
   
   @Test
   public void testEvaluateWithMaxIterations() {
      // Test typical use case
      ContinuedFractionImpl fraction = new ContinuedFractionImpl();
      double result = fraction.evaluate(1.5, 500);
      assertEquals(2.0, result, 0.001);
      
      // Test edge case
      result = fraction.evaluate(0, 1000);
      assertEquals(1.0, result, 0.001);
      
      // Test error scenario
      try {
          result = fraction.evaluate(-1, 100);
          fail("Expected ConvergenceException");
      } catch (ConvergenceException e) {
          assertEquals("The algorithm failed to converge.", e.getMessage());
      }
   }
   
   @Test
   public void testEvaluateWithAllParams() {
      // Test typical use case
      ContinuedFractionImpl fraction = new ContinuedFractionImpl();
      double result = fraction.evaluate(3.0, 0.001, 1000);
      assertEquals(6.0, result, 0.001);
      
      // Test edge case
      result = fraction.evaluate(0, 0.0, 500);
      assertEquals(1.0, result, 0.001);
      
      // Test error scenario
      try {
          result = fraction.evaluate(-2, 0.01, 200);
          fail("Expected ConvergenceException");
      } catch (ConvergenceException e) {
          assertEquals("The algorithm failed to converge.", e.getMessage());
      }
   }
}