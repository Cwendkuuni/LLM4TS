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
  public void testDefaultConstructor() {
    assertEquals(2.0, multiDirectional.khi, 0.0001);
    assertEquals(0.5, multiDirectional.gamma, 0.0001);
  }

  @Test
  public void testParameterizedConstructor() {
    double khi = 1.5;
    double gamma = 0.8;
    MultiDirectional md = new MultiDirectional(khi, gamma);
    assertEquals(khi, md.khi, 0.0001);
    assertEquals(gamma, md.gamma, 0.0001);
  }

  @Test
  public void testIterateSimplex_WithBestReflection() {
    // Test when the reflected point is better than the best original point
    // Simplex: (1, 2, 3, 4), (2, 3, 4, 5), (3, 4, 5, 6), (4, 5, 6, 7)
    // Reflection: (5, 6, 7, 8)
    // Expected: Simplex is updated with the reflected point
    multiDirectional.simplex = new RealPointValuePair[5];
    multiDirectional.simplex[0] = new RealPointValuePair(new double[]{1, 2, 3, 4}, 0.0);
    multiDirectional.simplex[1] = new RealPointValuePair(new double[]{2, 3, 4, 5}, 0.0);
    multiDirectional.simplex[2] = new RealPointValuePair(new double[]{3, 4, 5, 6}, 0.0);
    multiDirectional.simplex[3] = new RealPointValuePair(new double[]{4, 5, 6, 7}, 0.0);
    multiDirectional.simplex[4] = new RealPointValuePair(new double[]{5, 6, 7, 8}, 0.0);
    Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
      public int compare(RealPointValuePair p1, RealPointValuePair p2) {
        return Double.compare(p1.getValue(), p2.getValue());
      }
    };
    try {
      multiDirectional.iterateSimplex(comparator);
      RealPointValuePair[] simplex = multiDirectional.simplex;
      assertEquals(5, simplex.length);
      assertArrayEquals(new double[]{5, 6, 7, 8}, simplex[0].getPoint(), 0.0001);
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testIterateSimplex_WithBetterExpandedPoint() {
    // Test when the reflected point is worse than the best original point, 
    // but the expanded point is better than the reflected point
    // Simplex: (1, 2, 3, 4), (2, 3, 4, 5), (3, 4, 5, 6), (4, 5, 6, 7)
    // Reflection: (5, 6, 7, 8)
    // Expansion: (9, 10, 11, 12)
    // Expected: Simplex is updated with the expanded point
    multiDirectional.simplex = new RealPointValuePair[5];
    multiDirectional.simplex[0] = new RealPointValuePair(new double[]{1, 2, 3, 4}, 0.0);
    multiDirectional.simplex[1] = new RealPointValuePair(new double[]{2, 3, 4, 5}, 0.0);
    multiDirectional.simplex[2] = new RealPointValuePair(new double[]{3, 4, 5, 6}, 0.0);
    multiDirectional.simplex[3] = new RealPointValuePair(new double[]{4, 5, 6, 7}, 0.0);
    multiDirectional.simplex[4] = new RealPointValuePair(new double[]{5, 6, 7, 8}, 0.0);
    Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
      public int compare(RealPointValuePair p1, RealPointValuePair p2) {
        return Double.compare(p1.getValue(), p2.getValue());
      }
    };
    try {
      multiDirectional.iterateSimplex(comparator);
      RealPointValuePair[] simplex = multiDirectional.simplex;
      assertEquals(5, simplex.length);
      assertArrayEquals(new double[]{9, 10, 11, 12}, simplex[0].getPoint(), 0.0001);
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testIterateSimplex_WithWorseReflectedAndContractedPoints() {
    // Test when the reflected point is worse than the best original point, 
    // and the contracted point is also worse than the best original point
    // Simplex: (1, 2, 3, 4), (2, 3, 4, 5), (3, 4, 5, 6)
    // Reflection: (5, 6, 7, 8)
    // Contraction: (2, 3, 4, 5)
    // Expected: No changes in the simplex
    multiDirectional.simplex = new RealPointValuePair[4];
    multiDirectional.simplex[0] = new RealPointValuePair(new double[]{1, 2, 3, 4}, 0.0);
    multiDirectional.simplex[1] = new RealPointValuePair(new double[]{2, 3, 4, 5}, 0.0);
    multiDirectional.simplex[2] = new RealPointValuePair(new double[]{3, 4, 5, 6}, 0.0);
    multiDirectional.simplex[3] = new RealPointValuePair(new double[]{5, 6, 7, 8}, 0.0);
    Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
      public int compare(RealPointValuePair p1, RealPointValuePair p2) {
        return Double.compare(p1.getValue(), p2.getValue());
      }
    };
    try {
      multiDirectional.iterateSimplex(comparator);
      RealPointValuePair[] simplex = multiDirectional.simplex;
      assertEquals(4, simplex.length);
      assertArrayEquals(new double[]{1, 2, 3, 4}, simplex[0].getPoint(), 0.0001);
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
 }

  @Test
  public void testEvaluateNewSimplex() {
    // Test the evaluateNewSimplex method
    // Original Simplex: (1, 2, 3, 4), (2, 3, 4, 5), (3, 4, 5, 6)
    // Linear coefficient: 0.5
    // Expected: Transformed Simplex: (0.5, 1, 1.5, 2), (1.0, 1.5, 2.0, 2.5), (1.5, 2.0, 2.5, 3.0)
    RealPointValuePair[] original = new RealPointValuePair[4];
    original[0] = new RealPointValuePair(new double[]{1, 2, 3, 4}, 0.0);
    original[1] = new RealPointValuePair(new double[]{2, 3, 4, 5}, 0.0);
    original[2] = new RealPointValuePair(new double[]{3, 4, 5, 6}, 0.0);
    original[3] = new RealPointValuePair(new double[]{4, 5, 6, 7}, 0.0);
    try {
      RealPointValuePair transformedPoint = multiDirectional.evaluateNewSimplex(original, 0.5, null);
      double[][] expected = new double[][]{{0.5, 1, 1.5, 2}, {1.0, 1.5, 2.0, 2.5}, {1.5, 2.0, 2.5, 3.0}};
      assertArrayEquals(expected[0], transformedPoint.getPoint(), 0.0001);
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

}