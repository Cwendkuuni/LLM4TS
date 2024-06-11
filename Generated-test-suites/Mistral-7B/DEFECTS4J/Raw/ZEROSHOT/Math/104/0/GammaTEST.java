import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test valid inputs
 double result = Gamma.logGamma(1.5);
 double expected = Math.log(Math.PI) + 0.5 * Math.log(1.5) - 0.02239149326528532;
 assertEquals(expected, result, 1e-9);

 result = Gamma.logGamma(0.5);
 expected = Math.log(Math.PI) + 0.5 * Math.log(0.5) + 0.57721566490153286;
 assertEquals(expected, result, 1e-9);

 // Test invalid inputs
 result = Gamma.logGamma(-1);
 assertEquals(Double.NaN, result);
 result = Gamma.logGamma(0);
 assertEquals(Double.NaN, result);
 result = Gamma.logGamma(Integer.MAX_VALUE);
 assertEquals(Double.NaN, result);
 }

 @Test
 public void testRegularizedGammaP() throws MathException {
 // Test valid inputs
 double result = Gamma.regularizedGammaP(1.5, 1.5);
 double expected = Math.sqrt(Math.PI / 2.0);
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaP(0.5, 0.5);
 expected = 1.0 - Math.exp(-0.5) * Math.sqrt(Math.PI / 2.0);
 assertEquals(expected, result, 1e-9);

 // Test edge cases
 result = Gamma.regularizedGammaP(1.0, 1.0);
 assertEquals(1.0, result, 1e-9);

 result = Gamma.regularizedGammaP(1.0, 0.0);
 assertEquals(0.0, result, 1e-9);

 result = Gamma.regularizedGammaP(1.0, 2.0);
 assertEquals(1.0, result, 1e-9);

 result = Gamma.regularizedGammaP(1.0, Double.POSITIVE_INFINITY);
 assertEquals(1.0, result, 1e-9);

 // Test invalid inputs
 result = Gamma.regularizedGammaP(-1, 1.5);
 assertEquals(Double.NaN, result);
 result = Gamma.regularizedGammaP(0, 0.5);
 assertEquals(Double.NaN, result);
 result = Gamma.regularizedGammaP(Integer.MAX_VALUE, 1.5);
 assertEquals(Double.NaN, result);
 }

 @Test
 public void testRegularizedGammaQ() throws MathException {
 // Test valid inputs
 double result = Gamma.regularizedGammaQ(1.5, 1.5);
 double expected = 1.0 - Math.sqrt(Math.PI / 2.0);
 assertEquals(expected, result, 1e-9);

 result = Gamma.regularizedGammaQ(0.5, 0.5);
 expected = Math.exp(-0.5) * Math.sqrt(Math.PI / 2.0);
 assertEquals(expected, result, 1e-9);

 // Test edge cases
 result = Gamma.regularizedGammaQ(1.0, 1.0);
 assertEquals(0.0, result, 1e-9);

 result = Gamma.regularizedGammaQ(1.0, 0.0);
 assertEquals(1.0, result, 1e-9);

 result = Gamma.regularizedGammaQ(1.0, 2.0);
 assertEquals(0.0, result, 1e-9);

 result = Gamma.regularizedGammaQ(1.0, Double.POSITIVE_INFINITY);
 assertEquals(1.0, result, 1e-9);

 // Test invalid inputs
 result = Gamma.regularizedGammaQ(-1, 1.5);
 assertEquals(Double.NaN, result);
 result = Gamma.regularizedGammaQ(0, 0.5);
 assertEquals(Double.NaN, result);
 result = Gamma.regularizedGammaQ(Integer.MAX_VALUE, 1.5);
 assertEquals(Double.NaN, result);
 }
}