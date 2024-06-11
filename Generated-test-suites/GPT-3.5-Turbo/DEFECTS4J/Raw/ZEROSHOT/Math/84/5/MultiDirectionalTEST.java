import org.junit.Assert;
import org.junit.Test;

public class MultiDirectionalTest {

  @Test
  public void testMultiDirectional() {
    MultiDirectional multiDirectional = new MultiDirectional();
    Assert.assertNotNull(multiDirectional);
  }

  @Test
  public void testMultiDirectionalWithCoefficients() {
    double khi = 2.0;
    double gamma = 0.5;
    MultiDirectional multiDirectional = new MultiDirectional(khi, gamma);
    Assert.assertNotNull(multiDirectional);
  }

  @Test
  public void testIterateSimplex() {
    MultiDirectional multiDirectional = new MultiDirectional();
    try {
      multiDirectional.iterateSimplex(null);
    } catch (Exception e) {
      Assert.fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testEvaluateNewSimplex() {
    MultiDirectional multiDirectional = new MultiDirectional();
    RealPointValuePair[] original = { new RealPointValuePair(new double[] { 1.0, 2.0 }, 0.0),
        new RealPointValuePair(new double[] { 3.0, 4.0 }, 0.0),
        new RealPointValuePair(new double[] { 5.0, 6.0 }, 0.0) };
    double coeff = 2.0;
    try {
      multiDirectional.evaluateNewSimplex(original, coeff, null);
    } catch (Exception e) {
      Assert.fail("Exception thrown: " + e.getMessage());
    }
  }
}