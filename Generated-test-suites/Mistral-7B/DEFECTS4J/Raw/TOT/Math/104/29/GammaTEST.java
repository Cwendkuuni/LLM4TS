import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Positive integer test
 assertEquals(0.405822717069011, Gamma.logGamma(1.0), 1e-8);
 // Positive non-integer test
 assertEquals(1.8313278463374603, Gamma.logGamma(2.5), 1e-8);
 // Zero test
 assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(0.0), 1e-8);
 // Negative test
 try {
 Gamma.logGamma(-1.0);
 fail("Exception expected: MathException");
 } catch (MathException e) {
 // pass
 }
 // NaN test
 try {
 Gamma.logGamma(Double.NaN);
 fail("Exception expected: MathException");
 } catch (MathException e) {
 // pass
 }
 }

 @Test
 public void testRegularizedGammaP() {
 // Positive integer test
 assertEquals(0.888186810233612, Gamma.regularizedGammaP(1.5, 2.0), 1e-8);
 // Positive non-integer test
 assertEquals(0.447346246372592, Gamma.regularizedGammaP(0.5, 1.0), 1e-8);
 // Zero test
 assertEquals(1.0, Gamma.regularizedGammaP(1.0, 0.0), 1e-8);
 // Negative test
 try {
 Gamma.regularizedGammaP(-1.0, 2.0);
 fail("Exception expected: MathException");
 } catch (MathException e) {
 // pass
 }
 // Non-positive value test
 try {
 Gamma.regularizedGammaP(1.0, -2.0);
 fail("Exception expected: MathException");
 } catch (MathException e) {
 // pass
 }
 // NaN test
 try {
 Gamma.regularizedGammaP(Double.NaN, 2.0);
 fail("Exception expected: MathException");
 } catch (MathException e) {
 // pass
 }
 }

 @Test
 public void testRegularizedGammaQ() {
 // Positive integer test
 assertEquals(0.111822263652714, 1.0 - Gamma.regularizedGammaP(1.5, 2.0), 1e-8);
 // Positive non-integer test
 assertEquals(0.552653736347208, 1.0 - Gamma.regularizedGammaP(0.5, 1.0), 1e-8);
 // Zero test
 assertEquals(1.0, 1.0 - Gamma.regularizedGammaP(1.0, 0.0), 1e-8);
 // Negative test
 try {
 Gamma.regularizedGammaQ(-1.0, 2.0);
 fail("Exception expected: MathException");
 } catch (MathException e) {
 // pass
 }
 // Non-positive value test
 try {
 Gamma.regularizedGammaQ(1.0, -2.0);
 fail("Exception expected: MathException");
 } catch (MathException e) {
 // pass
 }
 // NaN test
 try {
 Gamma.regularizedGammaQ(Double.NaN, 2.0);
 fail("Exception expected: MathException");
 } catch (MathException e) {
 // pass
 }
 }
}