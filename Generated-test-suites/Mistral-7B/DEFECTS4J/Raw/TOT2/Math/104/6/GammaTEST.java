import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGamma() {
 double result = Gamma.logGamma(1.0);
 double expected = Math.log(Math.Gamma(1.0));
 assertEquals(expected, result, 1e-9);
 }

 @Test
 public void testLogGammaNegativeArgument() {
 try {
 Gamma.logGamma(-1.0);
 fail("Expected MathException");
 } catch (MathException e) {
 // expected
 }
 }

 @Test
 public void testLogGammaNaN() {
 try {
 Gamma.logGamma(Double.NaN);
 fail("Expected MathException");
 } catch (MathException e) {
 // expected
 }
 }

 @Test
 public void testRegularizedGammaP() {
 double result = Gamma.regularizedGammaP(1.5, 1.0);
 double expected = Math.Gamma(1.5) / Math.sqrt(Math.PI) / Math.exp(1.0);
 assertEquals(expected, result, 1e-9);
 }

 @Test
 public void testRegularizedGammaPNegativeArgument() {
 try {
 Gamma.regularizedGammaP(-1.0, 1.0);
 fail("Expected MathException");
 } catch (MathException e) {
 // expected
 }
 }

 @Test
 public void testRegularizedGammaPNonPositiveArgument() {
 try {
 Gamma.regularizedGammaP(0.0, 1.0);
 fail("Expected MathException");
 } catch (MathException e) {
 // expected
 }
 }

 @Test
 public void testRegularizedGammaPZeroValue() {
 double result = Gamma.regularizedGammaP(0.5, 0.0);
 double expected = 0.5;
 assertEquals(expected, result, 1e-9);
 }

 @Test
 public void testRegularizedGammaPMaxIterationsExceeded() {
 try {
 Gamma.regularizedGammaP(1.5, 1.0, 0, 1);
 fail("Expected MaxIterationsExceededException");
 } catch (MaxIterationsExceededException e) {
 // expected
 }
 }

 @Test
 public void testRegularizedGammaQ() {
 double result = Gamma.regularizedGammaQ(1.5, 1.0);
 double expected = 1.0 - Math.Gamma(1.5) / Math.sqrt(Math.PI) / Math.exp(1.0);
 assertEquals(expected, result, 1e-9);
 }

 @Test
 public void testRegularizedGammaQNegativeArgument() {
 try {
 Gamma.regularizedGammaQ(-1.0, 1.0);
 fail("Expected MathException");
 } catch (MathException e) {
 // expected
 }
 }

 @Test
 public void testRegularizedGammaQNonPositiveArgument() {
 try {
 Gamma.regularizedGammaQ(0.0, 1.0);
 fail("Expected MathException");
 } catch (MathException e) {
 // expected
 }
 }

 @Test
 public void testRegularizedGammaQZeroValue() {
 double result = Gamma.regularizedGammaQ(0.5, 0.0);
 double expected = 1.0 - 0.5;
 assertEquals(expected, result, 1e-9);
 }

 @Test
 public void testRegularizedGammaQMaxIterationsExceeded() {
 try {
 Gamma.regularizedGammaQ(1.5, 1.0, 0, 1);
 fail("Expected MaxIterationsExceededException");
 } catch (MaxIterationsExceededException e) {
 // expected
 }
 }
}