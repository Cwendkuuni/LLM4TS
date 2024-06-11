import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGamma() {
 double result = Gamma.logGamma(0.5);
 double expected = -0.5 * Math.log(2.0 * Math.PI) + 0.5 * Math.log(0.5) - Math.log(Math.PI) + 0.26551223566882832713;
 assertEquals("Incorrect log gamma value for 0.5", expected, result, 1e-6);
 }

 @Test
 public void testLogGammaNaN() {
 double result = Gamma.logGamma(-1.0);
 assertEquals("logGamma should return NaN for negative argument", Double.NaN, result);
 }

 @Test
 public void testLogGammaZero() {
 double result = Gamma.logGamma(0.0);
 assertEquals("logGamma should return NaN for zero argument", Double.NaN, result);
 }

 @Test
 public void testRegularizedGammaP() {
 double result = Gamma.regularizedGammaP(1.0, 1.0);
 double expected = 0.78886578943858159424;
 assertEquals("Incorrect regularized gamma value for P(1, 1)", expected, result, 1e-6);
 }

 @Test
 public void testRegularizedGammaPInvalidArguments() {
 try {
 Gamma.regularizedGammaP(-2.0, 1.5);
 } catch (MathException e) {
 // Expected exception
 }
 }

 @Test
 public void testRegularizedGammaPNaN() {
 double result = Gamma.regularizedGammaP(1.5, Double.NaN);
 assertEquals("regularizedGammaP should return NaN for complex argument", Double.NaN, result);
 }

 @Test
 public void testRegularizedGammaPZero() {
 double result = Gamma.regularizedGammaP(1.5, 0.0);
 assertEquals("regularizedGammaP should return 1 for zero argument", 1.0, result, 1e-6);
 }

 @Test
 public void testRegularizedGammaPInfinity() {
 double result = Gamma.regularizedGammaP(1.5, Double.POSITIVE_INFINITY);
 assertEquals("regularizedGammaP should return 1 for positive infinity", 1.0, result, 1e-6);
 }

 @Test
 public void testRegularizedGammaQ() {
 double result = Gamma.regularizedGammaQ(1.0, 1.0);
 double expected = 0.21198956283561728358;
 assertEquals("Incorrect regularized gamma value for Q(1, 1)", expected, result, 1e-6);
 }

 @Test
 public void testRegularizedGammaQInvalidArguments() {
 try {
 Gamma.regularizedGammaQ(-2.0, 1.5);
 } catch (MathException e) {
 // Expected exception
 }
 }

 @Test
 public void testRegularizedGammaQNaN() {
 double result = Gamma.regularizedGammaQ(1.5, Double.NaN);
 assertEquals("regularizedGammaQ should return NaN for complex argument", Double.NaN, result);
 }

 @Test
 public void testRegularizedGammaQZero() {
 double result = Gamma.regularizedGammaQ(1.5, 0.0);
 assertEquals("regularizedGammaQ should return 1 - regularizedGammaP(1.5, 0.0)", 1.0 - 0.78886578943858159424, result, 1e-6);
 }

 @Test
 public void testRegularizedGammaQInfinity() {
 double result = Gamma.regularizedGammaQ(1.5, Double.POSITIVE_INFINITY);
 assertEquals("regularizedGammaQ should return 0 for positive infinity", 0.0, result, 1e-6);
 }
}