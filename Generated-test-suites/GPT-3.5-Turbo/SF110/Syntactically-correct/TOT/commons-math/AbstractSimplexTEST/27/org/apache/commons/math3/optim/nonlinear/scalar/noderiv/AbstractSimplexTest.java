import org.junit.*;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testConstructorWithZeroArgument() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(0);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testConstructorWithNullArgument() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testConstructorWithPositiveArgument() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void testConstructorWithNegativeArgument() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(-1, 1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetDimension() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void testGetSize() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testBuildWithIncorrectDimension() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        double[] startPoint = {1, 2, 3, 4};
        try {
            simplex.build(startPoint);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testBuild() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testEvaluate() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        simplex.evaluate(function, comparator);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        assertEquals(6.0, points[0].getValue(), 0.0001);
        
    }
    
    @Test
    public void testReplaceWorstPoint() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);
        PointValuePair p1 = new PointValuePair(new double[]{1, 2, 3}, 10.0);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        simplex.replaceWorstPoint(p1, comparator);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        assertEquals(10.0, points[3].getValue(), 0.0001);
    }
    
    @Test
    public void testGetPoints() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
    }
    
    @Test
    public void testGetPointWithInvalidIndex() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        try {
            simplex.getPoint(4);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetPoint() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);
        PointValuePair point = simplex.getPoint(1);
        assertArrayEquals(new double[]{2, 4, 6}, point.getPoint(), 0.0001);
    }
    
    @Test
    public void testSetPointWithInvalidIndex() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        PointValuePair point = new PointValuePair(new double[]{1, 2, 3}, 10.0);
        try {
            simplex.setPoint(4, point);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSetPoint() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);
        PointValuePair point = new PointValuePair(new double[]{1, 2, 3}, 10.0);
        simplex.setPoint(0, point);
        assertArrayEquals(new double[]{1, 2, 3}, simplex.getPoint(0).getPoint(), 0.0001);
    }
    
    @Test
    public void testSetPointsWithInvalidDimension() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        PointValuePair[] points = {new PointValuePair(new double[]{1, 2, 3}, 10.0)};
        try {
            simplex.setPoints(points);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSetPoints() {
        double[] steps = {1, 2, 3};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);
        PointValuePair[] points = {new PointValuePair(new double[]{1, 2, 3}, 10.0), new PointValuePair(new double[]{2, 4, 6}, 20.0)};
        simplex.setPoints(points);
        assertArrayEquals(new double[]{1, 2, 3}, simplex.getPoint(0).getPoint(), 0.0001);
        assertArrayEquals(new double[]{2, 4, 6}, simplex.getPoint(1).getPoint(), 0.0001);
    }
    
    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 2.5);
        assertArrayEquals(new double[]{2.5, 2.5, 2.5}, steps, 0.0001);
    }
}