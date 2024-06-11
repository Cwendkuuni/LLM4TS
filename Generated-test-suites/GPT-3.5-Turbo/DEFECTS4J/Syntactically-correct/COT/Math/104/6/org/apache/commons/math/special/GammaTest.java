import org.junit.Test;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.MathException;

public class GammaTest {

  @Test
  public void testLogGamma() {
    // Test cases with valid input
    double result1 = Gamma.logGamma(1);
    double result2 = Gamma.logGamma(5);
    double result3 = Gamma.logGamma(10);
    // Expected output: 0
    assert result1 == 0;
    // Expected output: 3.1780538303479458
    assert result2 == 3.1780538303479458;
    // Expected output: 12.801827480081469
    assert result3 == 12.801827480081469;

    // Test cases with invalid input
    double result4 = Gamma.logGamma(0);
    double result5 = Gamma.logGamma(-5);
    double result6 = Gamma.logGamma(Double.NaN);
    // Expected output: NaN
    assert Double.isNaN(result4);
    // Expected output: NaN
    assert Double.isNaN(result5);
    // Expected output: NaN
    assert Double.isNaN(result6);
  }

  @Test
  public void testRegularizedGammaP() throws MathException {
    // Test cases with valid input
    double result1 = Gamma.regularizedGammaP(1, 1);
    double result2 = Gamma.regularizedGammaP(5, 0.5);
    double result3 = Gamma.regularizedGammaP(10, 2);
    // Expected output: 0.6321205588285577
    assert result1 == 0.6321205588285577;
    // Expected output: 0.9681719425941694
    assert result2 == 0.9681719425941694;
    // Expected output: 0.999730838828904
    assert result3 == 0.999730838828904;

    // Test cases with invalid input
    double result4 = Gamma.regularizedGammaP(0, 1);
    double result5 = Gamma.regularizedGammaP(-5, 0.5);
    double result6 = Gamma.regularizedGammaP(Double.NaN, 1);
    double result7 = Gamma.regularizedGammaP(1, -2);
    // Expected output: NaN
    assert Double.isNaN(result4);
    // Expected output: NaN
    assert Double.isNaN(result5);
    // Expected output: NaN
    assert Double.isNaN(result6);
    // Expected output: NaN and MaxIterationsExceededException
    assert Double.isNaN(result7);
  }

  @Test
  public void testRegularizedGammaQ() throws MathException {
    // Test cases with valid input
    double result1 = Gamma.regularizedGammaQ(1, 1);
    double result2 = Gamma.regularizedGammaQ(5, 0.5);
    double result3 = Gamma.regularizedGammaQ(10, 2);
    // Expected output: 0.36787944117144233
    assert result1 == 0.36787944117144233;
    // Expected output: 0.031828057405830584
    assert result2 == 0.031828057405830584;
    // Expected output: 0.0002691611710960279
    assert result3 == 0.0002691611710960279;

    // Test cases with invalid input
    double result4 = Gamma.regularizedGammaQ(0, 1);
    double result5 = Gamma.regularizedGammaQ(-5, 0.5);
    double result6 = Gamma.regularizedGammaQ(Double.NaN, 1);
    double result7 = Gamma.regularizedGammaQ(1, -2);
    // Expected output: NaN
    assert Double.isNaN(result4);
    // Expected output: NaN
    assert Double.isNaN(result5);
    // Expected output: NaN
    assert Double.isNaN(result6);
    // Expected output: NaN and MaxIterationsExceededException
    assert Double.isNaN(result7);
  }
}