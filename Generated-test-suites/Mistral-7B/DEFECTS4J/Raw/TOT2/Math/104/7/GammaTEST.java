import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 // Test case 1: Test logGamma method with positive input
 @Test
 public void testLogGammaPositive() {
 double result = Gamma.logGamma(2.5);
 double expected = Math.log(1.7726597524565793e-02) + 1.0515426989563051;
 assertEquals(expected, result, 1e-9);
 }

 // Test case 2: Test logGamma method with negative input
 @Test(expected = MathException.class)
 public void testLogGammaNegative() {
 Gamma.logGamma(-1.5);
 }

 // Test case 3: Test logGamma method with zero input
 @Test(expected = MathException.class)
 public void testLogGammaZero() {
 Gamma.logGamma(0.0);
 }

 // Test case 4: Test regularizedGammaP method with valid input
 @Test
 public void testRegularizedGammaPValid() {
 double result = Gamma.regularizedGammaP(2.5, 1.5);
 double expected = 0.8881959819913374;
 assertEquals(expected, result, 1e-9);
 }

 // Test case 5: Test regularizedGammaP method with invalid input - negative a
 @Test(expected = MathException.class)
 public void testRegularizedGammaPInvalidNegativeA() {
 Gamma.regularizedGammaP(-2.5, 1.5);
 }

 // Test case 6: Test regularizedGammaP method with invalid input - negative x
 @Test(expected = MathException.class)
 public void testRegularizedGammaPInvalidNegativeX() {
 Gamma.regularizedGammaP(2.5, -1.5);
 }

 // Test case 7: Test regularizedGammaP method with maximum iterations exceeded
 @Test(expected = Gamma.MaxIterationsExceededException.class)
 public void testRegularizedGammaPMaxIterationsExceeded() {
 double result = Gamma.regularizedGammaP(0.01, 5.0, 1e-13, 100);
 }

 // Test case 8: Test regularizedGammaQ method with valid input
 @Test
 public void testRegularizedGammaQValid() {
 double result = Gamma.regularizedGammaQ(2.5, 1.5);
 double expected = 1.1118050182875357;
 assertEquals(expected, result, 1e-9);
 }

 // Test case 9: Test regularizedGammaQ method with invalid input - negative a
 @Test(expected = MathException.class)
 public void testRegularizedGammaQInvalidNegativeA() {
 Gamma.regularizedGammaQ(-2.5, 1.5);
 }

 // Test case 10: Test regularizedGammaQ method with invalid input - negative x
 @Test(expected = MathException.class)
 public void testRegularizedGammaQInvalidNegativeX() {
 Gamma.regularizedGammaQ(2.5, -1.5);
 }
}