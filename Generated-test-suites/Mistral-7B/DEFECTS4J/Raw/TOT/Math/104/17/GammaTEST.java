import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test with positive values
 double result = Gamma.logGamma(1.0);
 assertEquals("logGamma(1.0) should be 0.0", 0.0, result, 1e-9);

 result = Gamma.logGamma(2.0);
 assertEquals("logGamma(2.0) should be 0.4054651083684933", 0.405465108368493, result, 1e-9);

 // Test with very large values
 result = Gamma.logGamma(100.0);
 assertTrue("logGamma(100.0) should be positive", result > 0.0);

 // Test with negative values
 result = Gamma.logGamma(-1.0);
 assertEquals("logGamma(-1.0) should be NaN", Double.NaN, result);

 // Test with zero and very small values
 result = Gamma.logGamma(0.0);
 assertEquals("logGamma(0.0) should be NaN", Double.NaN, result);
 result = Gamma.logGamma(1e-10);
 assertTrue("logGamma(1e-10) should be positive", result > 0.0);
 }

 @Test
 public void testRegularizedGammaP() {
 // Test with positive values
 double result = Gamma.regularizedGammaP(1.0, 1.0);
 assertEquals("P(1.0, 1.0) should be 0.5", 0.5, result, 1e-9);

 result = Gamma.regularizedGammaP(3.0, 1.5);
 assertEquals("P(3.0, 1.5) should be 0.423318684306453", 0.423318684306453, result, 1e-9);

 // Test with large values
 result = Gamma.regularizedGammaP(100.0, 100.0);
 assertEquals("P(100.0, 100.0) should be 1.0", 1.0, result, 1e-9);

 // Test with negative values
 result = Gamma.regularizedGammaP(-1.0, 1.0);
 assertEquals("P(-1.0, 1.0) should be NaN", Double.NaN, result);

 // Test with x < a
 result = Gamma.regularizedGammaP(1.0, 0.5);
 assertEquals("P(1.0, 0.5) should be approximately 0.5045782351410225", 0.5045782351410225, result, 1e-9);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPException() {
 // Test with invalid input
 double result = Gamma.regularizedGammaP(-1.0, 1.0);
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test with positive values
 double result = Gamma.regularizedGammaQ(1.0, 1.0);
 assertEquals("Q(1.0, 1.0) should be 0.5", 0.5, result, 1e-9);

 result = Gamma.regularizedGammaQ(3.0, 1.5);
 assertEquals("Q(3.0, 1.5) should be approximately 0.576681315959718", 0.576681315959718, result, 1e-9);

 // Test with large values
 result = Gamma.regularizedGammaQ(100.0, 100.0);
 assertEquals("Q(100.0, 100.0) should be 0.0", 0.0, result, 1e-9);

 // Test with negative values
 result = Gamma.regularizedGammaQ(-1.0, 1.0);
 assertEquals("Q(-1.0, 1.0) should be NaN", Double.NaN, result);

 // Test with x > a
 result = Gamma.regularizedGammaQ(1.0, 2.0);
 assertEquals("Q(1.0, 2.0) should be approximately 0.5045782351410225", 0.5045782351410225, result, 1e-9);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQException() {
 // Test with invalid input
 double result = Gamma.regularizedGammaQ(-1.0, 1.0);
 }
}