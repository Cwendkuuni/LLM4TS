import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test valid input
 double result = Gamma.logGamma(1.0);
 assertEquals(0.0, result, 1e-14);

 result = Gamma.logGamma(2.0);
 assertEquals(Math.log(Math.PI), result, 1e-14);

 result = Gamma.logGamma(3.0);
 assertEquals(1.0781169565941, result, 1e-14);

 // Test invalid input
 result = Gamma.logGamma(-1.0);
 assertFalse(Double.isFinite(result));

 result = Gamma.logGamma(0.0);
 assertFalse(Double.isFinite(result));
 }

 @Test
 public void testRegularizedGammaP() {
 // Test valid input
 double result = Gamma.regularizedGammaP(1.0, 1.0);
 assertEquals(1.0, result, 1e-14);

 result = Gamma.regularizedGammaP(0.5, 1.0);
 assertEquals(Math.sqrt(Math.PI) / 2.0, result, 1e-14);

 result = Gamma.regularizedGammaP(1.5, 1.0);
 assertEquals(Math.sqrt(Math.PI) / 2.0, result, 1e-14);

 result = Gamma.regularizedGammaP(2.0, 1.0);
 assertEquals(1.0, result, 1e-14);

 result = Gamma.regularizedGammaP(2.0, 2.0);
 assertEquals(1.0 / Math.sqrt(Math.PI), result, 1e-14);

 // Test exceptional input
 try {
 result = Gamma.regularizedGammaP(-1.0, 1.0);
 } catch (MathException e) {
 // Expected
 }
 assertFalse(Double.isFinite(result));

 try {
 result = Gamma.regularizedGammaP(0.0, 1.0);
 } catch (MathException e) {
 // Expected
 }
 assertFalse(Double.isFinite(result));

 try {
 result = Gamma.regularizedGammaP(1.0, -1.0);
 } catch (MathException e) {
 // Expected
 }
 assertFalse(Double.isFinite(result));

 try {
 result = Gamma.regularizedGammaP(1.0, 0.0);
 } catch (MathException e) {
 // Expected
 }
 assertFalse(Double.isFinite(result));
 }

 @Test
 public void testRegularizedGammaQ() {
 // Test valid input
 double result = Gamma.regularizedGammaQ(1.0, 1.0);
 assertEquals(0.0, result, 1e-14);

 result = Gamma.regularizedGammaQ(0.5, 1.0);
 assertEquals(1.0 - Math.sqrt(Math.PI) / 2.0, result, 1e-14);

 result = Gamma.regularizedGammaQ(1.5, 1.0);
 assertEquals(1.0 - Math.sqrt(Math.PI) / 2.0, result, 1e-14);

 result = Gamma.regularizedGammaQ(2.0, 1.0);
 assertEquals(1.0 - 1.0 / Math.sqrt(Math.PI), result, 1e-14);

 result = Gamma.regularizedGammaQ(2.0, 2.0);
 assertEquals(1.0 - 1.0 / Math.sqrt(Math.PI), result, 1e-14);

 // Test exceptional input
 try {
 result = Gamma.regularizedGammaQ(-1.0, 1.0);
 } catch (MathException e) {
 // Expected
 }
 assertFalse(Double.isFinite(result));

 try {
 result = Gamma.regularizedGammaQ(0.0, 1.0);
 } catch (MathException e) {
 // Expected
 }
 assertFalse(Double.isFinite(result));

 try {
 result = Gamma.regularizedGammaQ(1.0, -1.0);
 } catch (MathException e) {
 // Expected
 }
 assertFalse(Double.isFinite(result));

 try {
 result = Gamma.regularizedGammaQ(1.0, 0.0);
 } catch (MathException e) {
 // Expected
 }
 assertFalse(Double.isFinite(result));
 }
}