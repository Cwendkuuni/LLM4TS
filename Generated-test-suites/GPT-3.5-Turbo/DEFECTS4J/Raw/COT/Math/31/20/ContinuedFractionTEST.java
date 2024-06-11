
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

   private ContinuedFraction continuedFraction = new ContinuedFraction() {
      protected double getA(int n, double x) {return n;}
      protected double getB(int n, double x) {return n;}
   };

   @Test
   public void testEvaluate_DefaultEpsilon() {
      double result = continuedFraction.evaluate(1.0);
      assertEquals(1.5, result, 0.000001);
   }

   @Test
   public void testEvaluate_CustomEpsilon() {
      double result = continuedFraction.evaluate(1.0, 0.0001);
      assertEquals(1.5, result, 0.0001);
   }

   @Test
   public void testEvaluate_CustomMaxIterations() {
      double result = continuedFraction.evaluate(1.0, 10);
      assertEquals(1.165254, result, 0.000001);
   }

   @Test
   public void testEvaluate_CustomEpsilonAndMaxIterations() {
      double result = continuedFraction.evaluate(1.0, 0.0001, 10);
      assertEquals(1.165254, result, 0.0001);
   }

}
