import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test logGamma with positive values
 assertEquals(0.0, Gamma.logGamma(0.0), 1e-15);
 assertEquals(0.0, Gamma.logGamma(1.0), 1e-15);
 assertEquals(1.0, Gamma.logGamma(2.0), 1e-15);
 // Test logGamma with negative values
 assertEquals(Math.log(Math.PI), Gamma.logGamma(0.5), 1e-15);
 // Test logGamma with non-integer values
 assertEquals(-0.2307280234445175, Gamma.logGamma(1.2), 1e-15);
 // Test logGamma with large values
 assertEquals(1.178097989277247, Gamma.logGamma(10.0), 1e-15);
 // Test logGamma with small values
 assertEquals(-11.588805630202236, Gamma.logGamma(0.5), 1e-15);
 }

 @Test(expected = MathException.class)
 public void testLogGammaNaN() {
 assertEquals(Double.NaN, Gamma.logGamma(-1.0), 1e-15);
 assertEquals(Double.NaN, Gamma.logGamma(1.0 / 0.0), 1e-15);
 }

 @Test(expected = MathException.class)
 public void testLogGammaNegative() {
 assertEquals(Double.NaN, Gamma.logGamma(-1.5), 1e-15);
 }

 @Test
 public void testRegularizedGammaP() {
 // Test regularizedGammaP with positive values
 assertEquals(0.5, Gamma.regularizedGammaP(1.5, 1.0), 1e-15);
 assertEquals(0.1713193331237235, Gamma.regularizedGammaP(0.5, 1.0), 1e-15);
 // Test regularizedGammaP with non-integer values
 assertEquals(0.254426766373262, Gamma.regularizedGammaP(1.6, 1.0), 1e-15);
 // Test regularizedGammaP with large values
 assertEquals(0.9999999999999999, Gamma.regularizedGammaP(10.0, 9.0), 1e-15);
 // Test regularizedGammaP with small values
 assertEquals(0.741457229975926, Gamma.regularizedGammaP(0.75, 0.1), 1e-15);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPNaN() {
 assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 1.0), 1e-15);
 assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0 / 0.0, 1.0), 1e-15);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegative() {
 assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.5, 1.0), 1e-15);
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test regularizedGammaQ with positive values
 assertEquals(0.4558855846126514, Gamma.regularizedGammaQ(1.5, 1.0), 1e-15);
 assertEquals(0.8221030526666843, Gamma.regularizedGammaQ(0.5, 1.0), 1e-15);
 // Test regularizedGammaQ with non-integer values
 assertEquals(0.7455812254314565, Gamma.regularizedGammaQ(1.6, 1.0), 1e-15);
 // Test regularizedGammaQ with large values
 assertEquals(0.0, Gamma.regularizedGammaQ(10.0, 9.0), 1e-15);
 // Test regularizedGammaQ with small values
 assertEquals(0.2585487874135482, Gamma.regularizedGammaQ(0.75, 0.1), 1e-15);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQNaN() {
 assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.0, 1.0), 1e-15);
 assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0 / 0.0, 1.0), 1e-15);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegative() {
 assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.5, 1.0), 1e-15);
 }
}