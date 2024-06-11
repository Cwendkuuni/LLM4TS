import org.junit.*;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testConstructor1() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testConstructor2() {
        AbstractSimplex simplex = new AbstractSimplex(3, 2);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testConstructor3() {
        double[] steps = {2, 3, 4};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testConstructor4() {
        double[][] referenceSimplex = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(2, simplex.getDimension());
        assertEquals(3, simplex.getSize());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testConstructorNull() {
        AbstractSimplex simplex = new AbstractSimplex(null);
    }
    
    @Test(expected = ZeroException.class)
    public void testConstructorZero() {
        double[] steps = {};
        AbstractSimplex simplex = new AbstractSimplex(steps);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNotStrictlyPositive() {
        double[][] referenceSimplex = {};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDimensionMismatch() {
        double[][] referenceSimplex = {{1, 2}, {3, 4, 5}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
    }
    
    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorEqualVertices() {
        double[][] referenceSimplex = {{1, 2, 3}, {1, 2, 3}, {4, 5, 6}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testBuildDimensionMismatch() {
        double[] startPoint = {1, 2};
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(startPoint);
    }
    
    @Test
    public void testBuild() {
        double[] startPoint = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(startPoint);
        assertEquals(startPoint.length + 1, simplex.getSize());
        for (int i = 0; i < simplex.getSize(); i++) {
            PointValuePair point = simplex.getPoint(i);
            if (i == 0) {
                assertArrayEquals(startPoint, point.getPointRef(), 0);
            } else {
                for (int j = 0; j < simplex.getDimension(); j++) {
                    assertEquals(startPoint[j] + simplex.startConfiguration[i - 1][j], point.getPointRef()[j], 0);
                }
            }
        }
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testEvaluateDimensionMismatch() {
        double[] startPoint = {1, 2};
        AbstractSimplex simplex = new AbstractSimplex(2);
        simplex.evaluate(new MultivariateFunction(), new Comparator<PointValuePair>());
    }
    
    @Test
    public void testEvaluate() {
        double[] startPoint = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(startPoint);
        simplex.evaluate(new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        for (int i = 0; i < simplex.getSize(); i++) {
            PointValuePair point = simplex.getPoint(i);
            assertNotNull(point.getValue());
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.getPoint(5);
    }
    
    @Test
    public void testSetPoint() {
        double[] startPoint = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(startPoint);
        PointValuePair newPoint = new PointValuePair(new double[]{4, 5, 6}, Double.NaN);
        simplex.setPoint(1, newPoint);
        assertEquals(newPoint, simplex.getPoint(1));
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair point = new PointValuePair(new double[]{1, 2, 3}, Double.NaN);
        simplex.setPoint(5, point);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsDimensionMismatch() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] points = {new PointValuePair(new double[]{1, 2, 3}, Double.NaN), 
                                   new PointValuePair(new double[]{4, 5, 6}, Double.NaN)};
        simplex.setPoints(points);
    }
    
    @Test
    public void testGetPoints() {
        double[] startPoint = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(startPoint.length + 1, points.length);
        for (int i = 0; i < simplex.getSize(); i++) {
            assertArrayEquals(simplex.getPoint(i).getPointRef(), points[i].getPointRef(), 0);
        }
    }
    
    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 2);
        double[] expected = {2, 2, 2};
        assertArrayEquals(expected, steps, 0);
    }
}