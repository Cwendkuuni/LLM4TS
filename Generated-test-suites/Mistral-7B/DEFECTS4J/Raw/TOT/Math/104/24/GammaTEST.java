import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test positive values
 assertEquals(0.0, Gamma.logGamma(0.0), 1e-12);
 assertEquals(0.0, Gamma.logGamma(1.0), 1e-12);
 assertEquals(1.202056506558796, Gamma.logGamma(2.0), 1e-12);
 assertEquals(1.3862943611198906, Gamma.logGamma(3.0), 1e-12);

 // Test negative values and NaN
 assertEquals(Double.NaN, Gamma.logGamma(-1.0), 1e-12);
 assertEquals(Double.NaN, Gamma.logGamma(0.5), 1e-12);
 }

 @Test
 public void testRegularizedGammaP() {
 // Test positive values
 assertEquals(0.0, Gamma.regularizedGammaP(0.0, 1.0), 1e-12);
 assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-12);
 assertEquals(0.563568572576461, Gamma.regularizedGammaP(1.5, 2.5), 1e-12);
 assertEquals(0.7853981633974483, Gamma.regularizedGammaP(2.0, 1.0), 1e-12);

 // Test negative values and NaN
 assertEquals(Double.NaN, Gamma.regularizedGammaP(-0.5, 1.0), 1e-12);
 assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, 0.0), 1e-12);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPFailure() {
 // Test failure condition
 assertEquals(1.0, Gamma.regularizedGammaP(1.0, 2.0), 1e-12);
 assertEquals(Double.NaN, Gamma.regularizedGammaP(0.5, 2.0), 1e-12);
 try {
 Gamma.regularizedGammaP(1.0, -2.0);
 } catch (MathException e) {
 // expected exception
 }
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test positive values
 assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 1.0), 1e-12);
 assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-12);
 assertEquals(0.436788045710871, Gamma.regularizedGammaQ(1.5, 2.5), 1e-12);
 assertEquals(0.2154434600751552, Gamma.regularizedGammaQ(2.0, 1.0), 1e-12);

 // Test negative values and NaN
 assertEquals(Double.NaN, Gamma.regularizedGammaQ(-0.5, 1.0), 1e-12);
 assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, 0.0), 1e-12);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQFailure() {
 // Test failure condition
 assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 2.0), 1e-12);
 assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.5, 2.0), 1e-12);
 try {
 Gamma.regularizedGammaQ(1.0, -2.0);
 } catch (MathException e) {
 // expected exception
 }
 }
}