import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test logGamma with positive integer inputs
 assertEquals(0.0, Gamma.logGamma(1.0), 1e-14);
 assertEquals(1.20205995661811, Gamma.logGamma(2.0), 1e-14);
 assertEquals(1.70273177704625, Gamma.logGamma(3.0), 1e-14);
 // Test logGamma with negative integer inputs
 assertEquals(-0.40545959752357, Gamma.logGamma(-1.0), 1e-14);
 assertEquals(1.11022302368551, Gamma.logGamma(0.0), 1e-14);
 // Test logGamma with non-integer inputs
 assertEquals(0.34281264664446, Gamma.logGamma(1.5), 1e-14);
 // Test logGamma with extreme values
 assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(Double.POSITIVE_INFINITY), 0);
 assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(Double.NEGATIVE_INFINITY), 0);
 // Test logGamma with non-numeric inputs
 assertEquals(Double.NaN, Gamma.logGamma('a'), 0);
 assertEquals(Double.NaN, Gamma.logGamma(null), 0);
 }

 @Test
 public void testRegularizedGammaP() {
 // Test regularizedGammaP with positive integer a and x > a
 assertEquals(0.5, Gamma.regularizedGammaP(2.5, 2.5), 1e-14);
 // Test regularizedGammaP with negative integer a and x > |a|
 assertEquals(0.5, Gamma.regularizedGammaP(-2.5, 2.5), 1e-14);
 // Test regularizedGammaP with non-integer a and x > a
 assertEquals(0.66580936659409, Gamma.regularizedGammaP(1.5, 2.5), 1e-14);
 // Test regularizedGammaP with a = x
 assertEquals(1.0, Gamma.regularizedGammaP(2.5, 2.5), 1e-14);
 // Test regularizedGammaP with a < 0 and x < a
 assertEquals(Double.NaN, Gamma.regularizedGammaP(-2.5, 1.5), 0);
 // Test regularizedGammaP with a = 0
 assertEquals(1.0, Gamma.regularizedGammaP(0.5, 2.5), 1e-14);
 // Test regularizedGammaP with extreme a and x
 assertEquals(1.0, Gamma.regularizedGammaP(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), 0);
 assertEquals(0.0, Gamma.regularizedGammaP(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), 0);
 // Test regularizedGammaP with non-numeric inputs
 assertEquals(Double.NaN, Gamma.regularizedGammaP('a', 2.5), 0);
 assertEquals(Double.NaN, Gamma.regularizedGammaP(null, 2.5), 0);
 }

 @Test(expected = MathException.class)
 public void testRegularizedGammaP_MaxIterationsExceededException() {
 // Test regularizedGammaP with maxIterations set to a small number
 // to trigger MaxIterationsExceededException
 assertEquals(1.0, Gamma.regularizedGammaP(1.5, 1.5, 2, Integer.MAX_VALUE), 1e-14);
 assertEquals(Double.NaN, Gamma.regularizedGammaP(1.5, 1.5, 2, 2), 0);
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test regularizedGammaQ with positive integer a and x > a
 assertEquals(0.5, 1.0 - Gamma.regularizedGammaP(2.5, 2.5), 1e-14);
 // Test regularizedGammaQ with negative integer a and x > |a|
 assertEquals(0.5, 1.0 - Gamma.regularizedGammaP(-2.5, 2.5), 1e-14);
 // Test regularizedGammaQ with non-integer a and x > a
 assertEquals(0.33419360325243, 1.0 - Gamma.regularizedGammaP(1.5, 2.5), 1e-14);
 // Test regularizedGammaQ with a = x
 assertEquals(0.0, 1.0 - Gamma.regularizedGammaP(2.5, 2.5), 1e-14);
 // Test regularizedGammaQ with a < 0 and x < a
 assertEquals(Double.NaN, 1.0 - Gamma.regularizedGammaP(-2.5, 1.5), 0);
 // Test regularizedGammaQ with a = 0
 assertEquals(1.0, 1.0 - Gamma.regularizedGammaP(0.5, 2.5), 1e-14);
 // Test regularizedGammaQ with extreme a and x
 assertEquals(1.0, 1.0 - Gamma.regularizedGammaP(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), 0);
 assertEquals(0.0, 1.0 - Gamma.regularizedGammaP(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), 0);
 // Test regularizedGammaQ with non-numeric inputs
 assertEquals(Double.NaN, 1.0 - Gamma.regularizedGammaP('a', 2.5), 0);
 assertEquals(Double.NaN, 1.0 - Gamma.regularizedGammaP(null, 2.5), 0);
 }
}