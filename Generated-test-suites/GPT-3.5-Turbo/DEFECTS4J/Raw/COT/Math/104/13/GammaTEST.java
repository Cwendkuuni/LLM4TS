import static org.junit.Assert.assertEquals;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class GammaTest {

  // Test logGamma method
  @Test
  public void testLogGamma() {
    assertEquals(Math.log(Math.exp(3.0)), Gamma.logGamma(3.0), 0.0001);
    assertEquals(Double.NaN, Gamma.logGamma(-1.0), 0.0001);
    assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
  }

  // Test regularizedGammaP method
  @Test
  public void testRegularizedGammaP() throws MathException {
    assertEquals(0.999, Gamma.regularizedGammaP(2.0, 3.0), 0.001);
    assertEquals(1.0, Gamma.regularizedGammaP(2.0, 0.0), 0.001);
    assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 2.0), 0.001);
  }

  // Test regularizedGammaQ method
  @Test
  public void testRegularizedGammaQ() throws MathException {
    assertEquals(0.001, Gamma.regularizedGammaQ(2.0, 3.0), 0.001);
    assertEquals(0.0, Gamma.regularizedGammaQ(2.0, 0.0), 0.001);
    assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.0, 2.0), 0.001);
  }

}