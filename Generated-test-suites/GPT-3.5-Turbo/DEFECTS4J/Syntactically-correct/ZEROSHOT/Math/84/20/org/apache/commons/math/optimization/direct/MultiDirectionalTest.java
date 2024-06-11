import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

  private MultiDirectional multiDirectional;

  @Before
  public void setUp() {
    multiDirectional = new MultiDirectional();
  }

  @Test
  public void testIterateSimplex() {
    try {
      multiDirectional.iterateSimplex(null);
      fail("Should have thrown an exception");
    } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException ex) {
      assert(ex.getClass().equals(IllegalArgumentException.class));
    }
  }

  @Test
  public void testEvaluateNewSimplex() {
    RealPointValuePair[] original = new RealPointValuePair[1];
    original[0] = new RealPointValuePair(new double[]{1.0, 2.0, 3.0}, Double.NaN, false);

    try {
      RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, 1.0, null);
      assertNotNull(result);
    } catch (FunctionEvaluationException | OptimizationException ex) {
      fail("Should not have thrown an exception");
    }
  }

}