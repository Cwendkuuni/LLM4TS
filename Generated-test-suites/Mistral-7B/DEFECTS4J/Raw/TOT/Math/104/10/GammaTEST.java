import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test positive values
 assertEquals(0.0, Gamma.logGamma(0.0), 1e-9);
 assertEquals(0.5, Gamma.logGamma(1.0), 1e-9);
 assertEquals(1.0, Gamma.logGamma(2.0), 1e-9);
 assertEquals(Math.log(3.0), Gamma.logGamma(3.0), 1e-9);

 // Test edge cases
 assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(Double.POSITIVE_INFINITY), 1e-9);
 assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(Double.NEGATIVE_INFINITY), 1e-9);
 assertEquals(Double.NaN, Gamma.logGamma(-0.5), 1e-9);
 assertEquals(Double.NaN, Gamma.logGamma(0.0), 1e-9);

 // Test error cases
 try {
 Gamma.logGamma(-1.5);
 } catch (MathException e) {
 // Expected MathException
 }
 }

 @Test
 public void testRegularizedGammaP() {
 // Test positive values
 assertEquals(0.0, Gamma.regularizedGammaP(0.5, 1.0), 1e-9);
 assertEquals(1.0, Gamma.regularizedGammaP(1.5, 1.0), 1e-9);
 assertEquals(Math.exp(-Math.PI) / Math.sqrt(2.0 * Math.PI), Gamma.regularizedGammaP(1.5, 2.0), 1e-9);

 // Test edge cases
 assertEquals(1.0, Gamma.regularizedGammaP(0.0, 0.0), 1e-9);
 assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-9);
 assertEquals(0.0, Gamma.regularizedGammaP(1.0, 2.0), 1e-9);

 // Test error cases
 try {
 Gamma.regularizedGammaP(-1.0, 1.0);
 } catch (MathException e) {
 // Expected MathException
 }
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test positive values
 assertEquals(1.0, Gamma.regularizedGammaQ(0.5, 1.0), 1e-9);
 assertEquals(0.0, Gamma.regularizedGammaQ(1.5, 1.0), 1e-9);
 assertEquals(1.0 - Math.exp(-Math.PI) / Math.sqrt(2.0 * Math.PI), Gamma.regularizedGammaQ(1.5, 2.0), 1e-9);

 // Test edge cases
 assertEquals(0.0, Gamma.regularizedGammaQ(0.0, 0.0), 1e-9);
 assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-9);
 assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 2.0), 1e-9);

 // Test error cases
 try {
 Gamma.regularizedGammaQ(-1.0, 1.0);
 } catch (MathException e) {
 // Expected MathException
 }
 }
}