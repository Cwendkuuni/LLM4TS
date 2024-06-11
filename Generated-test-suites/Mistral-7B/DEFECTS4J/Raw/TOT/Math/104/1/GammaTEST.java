import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test positive values
 double result = Gamma.logGamma(1.0);
 assertEquals(0.0, result, 1e-9);

 result = Gamma.logGamma(2.0);
 assertEquals(0.405820323393892, result, 1e-9);

 result = Gamma.logGamma(3.0);
 assertEquals(0.785398163397448, result, 1e-9);

 // Test zero
 result = Gamma.logGamma(0.0);
 assertEquals(Double.NaN, result);

 // Test negative values
 result = Gamma.logGamma(-1.0);
 assertEquals(Double.NaN, result);
 }

 @Test
 public void testRegularizedGammaP() throws MathException {
 // Test positive values
 double result = Gamma.regularizedGammaP(1.0, 1.0);
 assertEquals(0.5, result, 1e-9);

 result = Gamma.regularizedGammaP(1.2, 1.0);
 assertEquals(0.615422947134135, result, 1e-9);

 result = Gamma.regularizedGammaP(2.0, 2.0);
 assertEquals(0.6366197723671875, result, 1e-9);

 // Test zero
 result = Gamma.regularizedGammaP(1.0, 0.0);
 assertEquals(1.0, result);

 result = Gamma.regularizedGammaP(2.0, 0.0);
 assertEquals(1.0, result);

 // Test negative values
 result = Gamma.regularizedGammaP(-1.0, 1.0);
 assertEquals(Double.NaN, result);

 result = Gamma.regularizedGammaP(1.0, -1.0);
 assertEquals(Double.NaN, result);
 }

 @Test
 public void testRegularizedGammaQ() throws MathException {
 // Test positive values
 double result = Gamma.regularizedGammaQ(1.0, 1.0);
 assertEquals(0.5, result, 1e-9);

 result = Gamma.regularizedGammaQ(1.2, 1.0);
 assertEquals(0.384577052865865, result, 1e-9);

 result = Gamma.regularizedGammaQ(2.0, 2.0);
 assertEquals(0.363400756412506, result, 1e-9);

 // Test zero
 result = Gamma.regularizedGammaQ(1.0, 0.0);
 assertEquals(0.0, result, 1e-9);

 result = Gamma.regularizedGammaQ(2.0, 0.0);
 assertEquals(0.0, result, 1e-9);

 // Test negative values
 result = Gamma.regularizedGammaQ(-1.0, 1.0);
 assertEquals(Double.NaN, result);

 result = Gamma.regularizedGammaQ(1.0, -1.0);
 assertEquals(Double.NaN, result);
 }
}