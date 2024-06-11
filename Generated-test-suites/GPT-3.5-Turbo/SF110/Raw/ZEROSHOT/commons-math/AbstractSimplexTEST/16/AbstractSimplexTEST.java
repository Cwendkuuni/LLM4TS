import org.junit.*;
import static org.junit.Assert.*;

public class AbstractSimplexTest {
  @Test
  public void testGetDimension() {
    AbstractSimplex simplex = new AbstractSimplexImpl(3);
    assertEquals(3, simplex.getDimension());
  }

  @Test
  public void testGetSize() {
    AbstractSimplex simplex = new AbstractSimplexImpl(3);
    simplex.build(new double[]{1.0, 2.0, 3.0});
    assertEquals(4, simplex.getSize());
  }

  @Test
  public void testIterate() {
    AbstractSimplex simplex = new AbstractSimplexImpl(3);
    simplex.iterate(new MultivariateFunctionImpl(), new ComparatorImpl());
    // Check if the iteration is successful
  }

  @Test
  public void testBuild() {
    AbstractSimplex simplex = new AbstractSimplexImpl(3);
    simplex.build(new double[]{1.0, 2.0, 3.0});
    PointValuePair[] points = simplex.getPoints();
    assertEquals(4, points.length);
    assertEquals(1.0, points[0].getPointRef()[0], 0.0);
    assertEquals(2.0, points[0].getPointRef()[1], 0.0);
    assertEquals(3.0, points[0].getPointRef()[2], 0.0);
    // Check if the build is successful
  }

  @Test
  public void testEvaluate() {
    AbstractSimplex simplex = new AbstractSimplexImpl(3);
    simplex.build(new double[]{1.0, 2.0, 3.0});
    simplex.evaluate(new MultivariateFunctionImpl(), new ComparatorImpl());
    PointValuePair[] points = simplex.getPoints();
    assertEquals(4, points.length);
    // Check if the evaluation is successful
  }

  @Test
  public void testReplaceWorstPoint() {
    AbstractSimplex simplex = new AbstractSimplexImpl(3);
    simplex.build(new double[]{1.0, 2.0, 3.0});
    PointValuePair worstPoint = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0);
    simplex.replaceWorstPoint(worstPoint, new ComparatorImpl());
    PointValuePair[] points = simplex.getPoints();
    assertEquals(4.0, points[3].getPointRef()[0], 0.0);
    assertEquals(5.0, points[3].getPointRef()[1], 0.0);
    assertEquals(6.0, points[3].getPointRef()[2], 0.0);
  }

  @Test
  public void testGetPoints() {
    AbstractSimplex simplex = new AbstractSimplexImpl(3);
    simplex.build(new double[]{1.0, 2.0, 3.0});
    PointValuePair[] points = simplex.getPoints();
    assertNotSame(simplex.getPoints(), points);
  }

  @Test
  public void testGetPoint() {
    AbstractSimplex simplex = new AbstractSimplexImpl(3);
    simplex.build(new double[]{1.0, 2.0, 3.0});
    PointValuePair point = simplex.getPoint(1);
    assertEquals(2.0, point.getPointRef()[0], 0.0);
    assertEquals(3.0, point.getPointRef()[1], 0.0);
    assertEquals(4.0, point.getPointRef()[2], 0.0);
  }

  @Test(expected=OutOfRangeException.class)
  public void testGetPointOutOfRange() {
    AbstractSimplex simplex = new AbstractSimplexImpl(3);
    simplex.build(new double[]{1.0, 2.0, 3.0});
    PointValuePair point = simplex.getPoint(4);
    // Expecting OutOfRangeException
  }

  @Test
  public void testSetPoint() {
    AbstractSimplex simplex = new AbstractSimplexImpl(3);
    simplex.build(new double[]{1.0, 2.0, 3.0});
    PointValuePair point = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0);
    simplex.setPoint(1, point);
    PointValuePair[] points = simplex.getPoints();
    assertEquals(4.0, points[1].getPointRef()[0], 0.0);
    assertEquals(5.0, points[1].getPointRef()[1], 0.0);
    assertEquals(6.0, points[1].getPointRef()[2], 0.0);
  }

  @Test(expected=OutOfRangeException.class)
  public void testSetPointOutOfRange() {
    AbstractSimplex simplex = new AbstractSimplexImpl(3);
    simplex.build(new double[]{1.0, 2.0, 3.0});
    PointValuePair point = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0);
    simplex.setPoint(4, point);
    // Expecting OutOfRangeException
  }

  @Test
  public void testSetPoints() {
    AbstractSimplex simplex = new AbstractSimplexImpl(3);
    simplex.build(new double[]{1.0, 2.0, 3.0});
    PointValuePair[] points = new PointValuePair[]{
      new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0),
      new PointValuePair(new double[]{7.0, 8.0, 9.0}, 0.0),
      new PointValuePair(new double[]{10.0, 11.0, 12.0}, 0.0),
      new PointValuePair(new double[]{13.0, 14.0, 15.0}, 0.0)
    };
    simplex.setPoints(points);
    assertSame(points, simplex.getPoints());
  }

  static class AbstractSimplexImpl extends AbstractSimplex {
    AbstractSimplexImpl(int n) {
      super(n);
    }

    public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
      // Implementation not needed for testing
    }
  }

  static class MultivariateFunctionImpl implements MultivariateFunction {
    public double value(double[] point) {
      // Implementation not needed for testing
      return 0.0;
    }
  }

  static class ComparatorImpl implements Comparator<PointValuePair> {
    public int compare(PointValuePair p1, PointValuePair p2) {
      // Implementation not needed for testing
      return 0;
    }
  }
}