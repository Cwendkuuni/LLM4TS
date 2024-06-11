import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

   // Test logGamma method with positive value
   @Test
   public void testLogGammaPositive() {
      double result = Gamma.logGamma(5);
      assertEquals(result, 3.1780538303479458, 0.0000001);
   }

   // Test logGamma method with zero
   @Test
   public void testLogGammaZero() {
      double result = Gamma.logGamma(0);
      assertEquals(result, Double.NaN, 0);
   }

   // Test logGamma method with negative value
   @Test
   public void testLogGammaNegative() {
      double result = Gamma.logGamma(-5);
      assertEquals(result, Double.NaN, 0);
   }

   // Test regularizedGammaP method with positive values
   @Test
   public void testRegularizedGammaPPositive() throws MathException {
      double result = Gamma.regularizedGammaP(5, 2);
      assertEquals(result, 0.9774038585208, 0.0000001);
   }

   // Test regularizedGammaP method with zero
   @Test
   public void testRegularizedGammaPZero() throws MathException {
      double result = Gamma.regularizedGammaP(0, 2);
      assertEquals(result, 0.73575888234288, 0.0000001);
   }

   // Test regularizedGammaP method with negative values
   @Test
   public void testRegularizedGammaPNegative() throws MathException {
      double result = Gamma.regularizedGammaP(-5, 2);
      assertEquals(result, Double.NaN, 0);
   }

   // Test regularizedGammaQ method with positive values
   @Test
   public void testRegularizedGammaQPositive() throws MathException {
      double result = Gamma.regularizedGammaQ(5, 2);
      assertEquals(result, 0.0225961414792, 0.0000001);
   }

   // Test regularizedGammaQ method with zero
   @Test
   public void testRegularizedGammaQZero() throws MathException {
      double result = Gamma.regularizedGammaQ(0, 2);
      assertEquals(result, 0.26424111765712, 0.0000001);
   }

   // Test regularizedGammaQ method with negative values
   @Test
   public void testRegularizedGammaQNegative() throws MathException {
      double result = Gamma.regularizedGammaQ(-5, 2);
      assertEquals(result, Double.NaN, 0);
   }

}