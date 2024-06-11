import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

  private MultiDirectional multiDirectional;

  @Before
  public void setup() {
    multiDirectional = new MultiDirectional();
  }

  @Test
  public void testIterateSimplex() {
    // Test the iterateSimplex method

    try {
      multiDirectional.iterateSimplex(null);
      fail("Expected Exception");
    } catch (FunctionEvaluationException e) {
      // Exception expected, do nothing
    } catch (OptimizationException e) {
      // Exception expected, do nothing
    } catch (IllegalArgumentException e) {
      // Exception expected, do nothing
    }

  }

  @Test
  public void testEvaluateNewSimplex() {
    // Test the evaluateNewSimplex method

    RealPointValuePair[] original = new RealPointValuePair[1];
    original[0] = new RealPointValuePair(new double[] {1.0}, 1.0, true);
    double coeff = 2.0;
    Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
      public int compare(RealPointValuePair o1, RealPointValuePair o2) {
        return 0;
      }
    };

    try {
      multiDirectional.evaluateNewSimplex(original, coeff, comparator);
      fail("Expected Exception");
    } catch (FunctionEvaluationException e) {
      // Exception expected, do nothing
    } catch (OptimizationException e) {
      // Exception expected, do nothing
    }

  }

  @Test
  public void testMultiDirectional() {
    // Test the default constructor

    assertEquals(2.0, multiDirectional.getKhi(), 0.0);
    assertEquals(0.5, multiDirectional.getGamma(), 0.0);

  }

  @Test
  public void testMultiDirectionalWithCoeff() {
    // Test the constructor with parameters

    multiDirectional = new MultiDirectional(3.0, 0.2);
    assertEquals(3.0, multiDirectional.getKhi(), 0.0);
    assertEquals(0.2, multiDirectional.getGamma(), 0.0);

  }

}