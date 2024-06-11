import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {
  
  @Test
  public void testLogGamma() {
    assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0);
    assertEquals(Double.NaN, Gamma.logGamma(0.0), 0);
    assertEquals(0.5723649429247, Gamma.logGamma(1.0), 1e-6);
    assertEquals(0.0, Gamma.logGamma(2.0), 0);
    assertEquals(0.6931471805599453, Gamma.logGamma(3.0), 1e-6);
    assertEquals(1.791759469228055, Gamma.logGamma(4.0), 1e-6);
    assertEquals(62223.83799923374, Gamma.logGamma(100.0), 1e-6);
    assertEquals(148863.53600464562, Gamma.logGamma(200.0), 1e-6);
  }
  
  @Test
  public void testRegularizedGammaP() throws MathException {
    assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 0.0), 0);
    assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, Double.NaN), 0);
    assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, 0.0), 0);
    assertEquals(0.0, Gamma.regularizedGammaP(0.0, Double.POSITIVE_INFINITY), 0);
    assertEquals(0.0, Gamma.regularizedGammaP(Double.NEGATIVE_INFINITY, 0.0), 0);
    assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, Double.POSITIVE_INFINITY), 0);
    assertEquals(0.0, Gamma.regularizedGammaP(Double.POSITIVE_INFINITY, 0.0), 0);
    assertEquals(0.0, Gamma.regularizedGammaP(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), 0);
  }
  
  @Test
  public void testRegularizedGammaQ() throws MathException {
    assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 0.0), 0);
    assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, Double.NaN), 0);
    assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, 0.0), 0);
    assertEquals(1.0, Gamma.regularizedGammaQ(0.0, Double.POSITIVE_INFINITY), 0);
    assertEquals(1.0, Gamma.regularizedGammaQ(Double.NEGATIVE_INFINITY, 0.0), 0);
    assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, Double.POSITIVE_INFINITY), 0);
    assertEquals(1.0, Gamma.regularizedGammaQ(Double.POSITIVE_INFINITY, 0.0), 0);
    assertEquals(1.0, Gamma.regularizedGammaQ(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), 0);
  }
}