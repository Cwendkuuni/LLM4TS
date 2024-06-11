import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test with valid input
 double result = Gamma.logGamma(1.5);
 double expected = 0.405827942147462;
 assertEquals(expected, result, 1e-9);

 // Test with negative input
 result = Gamma.logGamma(-0.5);
 assertEquals(Double.NaN, result);

 // Test with zero input
 result = Gamma.logGamma(0.0);
 assertEquals(Double.NaN, result);
 }

 @Test
 public void testRegularizedGammaP() {
 // Test with valid input
 double result = Gamma.regularizedGammaP(1.5, 1.5);
 double expected = 0.888193111019158;
 assertEquals(expected, result, 1e-9);

 // Test with negative a or x
 result = Gamma.regularizedGammaP(-0.5, 1.5);
 assertEquals(Double.NaN, result);
 result = Gamma.regularizedGammaP(1.5, -0.5);
 assertEquals(Double.NaN, result);

 // Test with invalid input
 result = Gamma.regularizedGammaP(0.0, 1.5);
 assertEquals(Double.NaN, result);
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test with valid input
 double result = Gamma.regularizedGammaQ(1.5, 1.5);
 double expected = 0.111806888980842;
 assertEquals(expected, result, 1e-9);

 // Test with negative a or x
 result = Gamma.regularizedGammaQ(-0.5, 1.5);
 assertEquals(Double.NaN, result);
 result = Gamma.regularizedGammaQ(1.5, -0.5);
 assertEquals(Double.NaN, result);

 // Test with invalid input
 result = Gamma.regularizedGammaQ(0.0, 1.5);
 assertEquals(1.0, result, 1e-9);
 }
}