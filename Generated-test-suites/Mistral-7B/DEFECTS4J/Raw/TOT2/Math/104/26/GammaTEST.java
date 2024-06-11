import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

 // Test case for logGamma method
 @Test
 public void testLogGamma() {
 double result = Gamma.logGamma(1.5);
 double expected = Math.log(Math.PI) + (1.5 * Math.log(1.5) - 0.5 * Math.log(2.0)) - 0.422790812324461;
 assertEquals(expected, result, 1e-6);
 }

 // Test case for logGamma with negative argument
 @Test(expected = MathException.class)
 public void testLogGammaNegativeArgument() {
 Gamma.logGamma(-1.0);
 }

 // Test case for logGamma with zero argument
 @Test(expected = MathException.class)
 public void testLogGammaZeroArgument() {
 Gamma.logGamma(0.0);
 }

 // Test case for logGamma with large argument
 @Test
 public void testLogGammaLargeArgument() {
 double result = Gamma.logGamma(100.0);
 double expected = Math.log(Math.PI) + (100.0 * Math.log(100.0) - 0.5 * Math.log(2.0 * 100.0)) - 145.77875825611;
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaP method
 @Test
 public void testRegularizedGammaP() {
 double result = Gamma.regularizedGammaP(1.5, 1.5);
 double expected = 0.267929524723968;
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaP with negative a
 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegativeA() {
 Gamma.regularizedGammaP(-1.0, 1.5);
 }

 // Test case for regularizedGammaP with negative x
 @Test(expected = MathException.class)
 public void testRegularizedGammaPNegativeX() {
 Gamma.regularizedGammaP(1.5, -1.5);
 }

 // Test case for regularizedGammaP with large a and x
 @Test
 public void testRegularizedGammaPLargeValues() {
 double result = Gamma.regularizedGammaP(50.0, 51.0);
 double expected = 2.62555132311956e-15;
 assertEquals(expected, result, 1e-15);
 }

 // Test case for regularizedGammaQ method
 @Test
 public void testRegularizedGammaQ() {
 double result = Gamma.regularizedGammaQ(1.5, 1.5);
 double expected = 0.732075475625273;
 assertEquals(expected, result, 1e-6);
 }

 // Test case for regularizedGammaQ with negative a
 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegativeA() {
 Gamma.regularizedGammaQ(-1.0, 1.5);
 }

 // Test case for regularizedGammaQ with negative x
 @Test(expected = MathException.class)
 public void testRegularizedGammaQNegativeX() {
 Gamma.regularizedGammaQ(1.5, -1.5);
 }

 // Test case for regularizedGammaQ with large a and x
 @Test
 public void testRegularizedGammaQLargeValues() {
 double result = Gamma.regularizedGammaQ(50.0, 51.0);
 double expected = 1.0 - 2.62555132311956e-15;
 assertEquals(expected, result, 1e-15);
 }
}