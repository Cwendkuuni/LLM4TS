import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

  private class TestContinuedFraction extends ContinuedFraction {
    protected double getA(int n, double x) {
      switch (n) {
        case 0: return 1.0;
        case 1: return 2.0;
        case 2: return 3.0;
        default: return 0.0;
      }
    }

    protected double getB(int n, double x) {
      switch (n) {
        case 0: return 4.0;
        case 1: return 5.0;
        case 2: return 6.0;
        default: return 0.0;
      }
    }
  }

  private ContinuedFraction fraction;

  @Test
  public void testEvaluate() {
    fraction = new TestContinuedFraction();
    assertEquals(15.0, fraction.evaluate(1.0), 0.0001);
    assertEquals(36.0, fraction.evaluate(2.0), 0.0001);
    assertEquals(69.0, fraction.evaluate(3.0), 0.0001);
  }

  @Test
  public void testEvaluateWithEpsilon() {
    fraction = new TestContinuedFraction();
    assertEquals(15.0, fraction.evaluate(1.0, 1e-9), 0.0001);
    assertEquals(36.0, fraction.evaluate(2.0, 1e-9), 0.0001);
    assertEquals(69.0, fraction.evaluate(3.0, 1e-9), 0.0001);
  }

  @Test
  public void testEvaluateWithMaxIterations() {
    fraction = new TestContinuedFraction();
    assertEquals(15.0, fraction.evaluate(1.0, 1e-9, 100), 0.0001);
    assertEquals(36.0, fraction.evaluate(2.0, 1e-9, 100), 0.0001);
    assertEquals(69.0, fraction.evaluate(3.0, 1e-9, 100), 0.0001);
  }

  @Test
  public void testEvaluateWithMaxIterationsAndEpsilon() {
    fraction = new TestContinuedFraction();
    assertEquals(15.0, fraction.evaluate(1.0, 1e-9, 100), 0.0001);
    assertEquals(36.0, fraction.evaluate(2.0, 1e-9, 100), 0.0001);
    assertEquals(69.0, fraction.evaluate(3.0, 1e-9, 100), 0.0001);
  }

}