import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

  @Test
  public void testLogGamma() {
    // Test for a valid input
    double result = Gamma.logGamma(5);
    Assert.assertEquals(3.17805383, result, 0.000001);

    // Test for NaN input
    result = Gamma.logGamma(Double.NaN);
    Assert.assertTrue(Double.isNaN(result));
  }

  @Test
  public void testRegularizedGammaP() {
    // Test for a valid input
    try {
      double result = Gamma.regularizedGammaP(2, 3);
      Assert.assertEquals(0.59399415, result, 0.000001);
    } catch (MathException e) {
      Assert.fail("An unexpected MathException occured");
    }

    // Test for NaN input
    try {
      double result = Gamma.regularizedGammaP(Double.NaN, 3);
      Assert.assertTrue(Double.isNaN(result));
    } catch (MathException e) {
      Assert.fail("An unexpected MathException occured");
    } 

    // Test for negative a input
    try {
      double result = Gamma.regularizedGammaP(-2, 3);
      Assert.fail("An expected exception was not thrown");
    } catch (MathException e) {
      Assert.assertEquals("Maximum iterations (2147483647) exceeded", e.getMessage());
    }

    // Test for x < 0 input
    try {
      double result = Gamma.regularizedGammaP(2, -3);
      Assert.assertEquals(1.0, result, 0.000001);
    } catch (MathException e) {
      Assert.fail("An unexpected MathException occured");
    }
  }

  @Test
  public void testRegularizedGammaQ() {
    // Test for a valid input
    try {
      double result = Gamma.regularizedGammaQ(2, 3);
      Assert.assertEquals(0.40600585, result, 0.000001);
    } catch (MathException e) {
      Assert.fail("An unexpected MathException occured");
    }

    // Test for NaN input
    try {
      double result = Gamma.regularizedGammaQ(Double.NaN, 3);
      Assert.assertTrue(Double.isNaN(result));
    } catch (MathException e) {
      Assert.fail("An unexpected MathException occured");
    } 

    // Test for negative a input
    try {
      double result = Gamma.regularizedGammaQ(-2, 3);
      Assert.fail("An expected exception was not thrown");
    } catch (MathException e) {
      Assert.assertEquals("Maximum iterations (2147483647) exceeded", e.getMessage());
    }

    // Test for x < 0 input
    try {
      double result = Gamma.regularizedGammaQ(2, -3);
      Assert.assertEquals(0.0, result, 0.000001);
    } catch (MathException e) {
      Assert.fail("An unexpected MathException occured");
    }
  }

}