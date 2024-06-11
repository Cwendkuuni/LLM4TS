import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

 // Test for logGamma method
 @Test
 public void testLogGamma() {
 double result = Gamma.logGamma(1.0);
 Assert.assertEquals(0.0, result, 1e-6); // Trivial test case

 double result1 = Gamma.logGamma(0.5);
 Assert.assertEquals(-0.577215664901532861, result1, 1e-6); // Edge case

 double result2 = Gamma.logGamma(-1.0);
 Assert.assertNotNull(result2); // Error scenario
 Assert.assertTrue(Double.isNaN(result2));
 }

 // Test for regularizedGammaP method
 @Test
 public void testRegularizedGammaP() {
 double result = 0.5 * Gamma.regularizedGammaP(1.5, 2.5);
 Assert.assertEquals(0.5 * 0.918283525585367120, result, 1e-6); // Trivial test case

 double result1 = Gamma.regularizedGammaP(0.5, 1.0);
 Assert.assertEquals(0.5, result1, 1e-6); // Edge case

 double result2 = Gamma.regularizedGammaP(-1.0, 2.0);
 Assert.assertNotNull(result2); // Error scenario
 Assert.assertTrue(Double.isNaN(result2));
 }

 // Test for regularizedGammaQ method
 @Test
 public void testRegularizedGammaQ() {
 double result = 1.0 - Gamma.regularizedGammaP(1.5, 2.5);
 Assert.assertEquals(0.497284314414632880, result, 1e-6); // Trivial test case

 double result1 = Gamma.regularizedGammaQ(0.5, 1.0);
 Assert.assertEquals(0.5, result1, 1e-6); // Edge case

 double result2 = Gamma.regularizedGammaQ(-1.0, 2.0);
 Assert.assertNotNull(result2); // Error scenario
 Assert.assertTrue(Double.isNaN(result2));
 }
}