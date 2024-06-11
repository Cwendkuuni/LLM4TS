import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test valid input
 double result = Gamma.logGamma(1.5);
 assertEquals(0.4058250432233433, result, 1e-9);

 // Test invalid input
 result = Gamma.logGamma(-0.5);
 assertNaN(result);
 // Test edge cases
 result = Gamma.logGamma(0.0);
 assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);
 result = Gamma.logGamma(1.0);
 assertEquals(0.0, result, 1e-15);
 }

 @Test(expected = MathException.class)
 public void testLogGammaException() {
 // Test logGamma with complex values
 Gamma.logGamma(Math.PI);
 }

 @Test
 public void testRegularizedGammaP() {
 // Test valid input
 double result = Gamma.regularizedGammaP(2.5, 1.5);
 assertEquals(1.1286639057819907, result, 1e-9);

 // Test invalid input
 result = Gamma.regularizedGammaP(-1.5, 1.5);
 assertNaN(result);
 result = Gamma.regularizedGammaP(1.5, 0.0);
 assertEquals(Double.NaN, result, 0.0);
 result = Gamma.regularizedGammaP(1.5, 1.5);
 assertEquals(1.0, result, 1e-15);

 // Test edge cases
 result = Gamma.regularizedGammaP(0.5, 0.5);
 assertEquals(0.5989422804014327, result, 1e-9);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaPException() {
 // Test regularizedGammaP with complex values
 Gamma.regularizedGammaP(1.5, Math.PI);
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test valid input
 double result = Gamma.regularizedGammaQ(2.5, 1.5);
 assertEquals(0.8710576295985673, result, 1e-9);

 // Test invalid input
 result = Gamma.regularizedGammaQ(-1.5, 1.5);
 assertNaN(result);
 result = Gamma.regularizedGammaQ(1.5, 0.0);
 assertEquals(Double.NaN, result, 0.0);
 result = Gamma.regularizedGammaQ(1.5, 1.5);
 assertEquals(1.0, result, 1e-15);

 // Test edge cases
 result = Gamma.regularizedGammaQ(0.5, 0.5);
 assertEquals(0.4010576710878913, result, 1e-9);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaQException() {
 // Test regularizedGammaQ with complex values
 Gamma.regularizedGammaQ(1.5, Math.PI);
 }
}