

import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {
    
    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new TestSimplex(3);
        
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new TestSimplex(3);
        
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testBuild() {
        AbstractSimplex simplex = new TestSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        
        simplex.build(startPoint);
        
        PointValuePair[] points = simplex.getPoints();
        assertEquals(startPoint, points[0].getPointRef());
        assertEquals(1.0, points[0].getValue(), 0.0001);
        
        assertEquals(startPoint[0] + 1.0, points[1].getPointRef()[0], 0.0001);
        assertEquals(startPoint[1] + 1.0, points[1].getPointRef()[1], 0.0001);
        assertEquals(startPoint[2] + 1.0, points[1].getPointRef()[2], 0.0001);
        assertEquals(Double.NaN, points[1].getValue(), 0.0001);
    }
    
    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new TestSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        
        simplex.build(startPoint);
        
        simplex.evaluate(new TestFunction(), new TestComparator());
        
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4.0, points[0].getValue(), 0.0001);
        assertEquals(3.0, points[1].getValue(), 0.0001);
        assertEquals(2.0, points[2].getValue(), 0.0001);
        assertEquals(1.0, points[3].getValue(), 0.0001);
    }
    
    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new TestSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        
        simplex.build(startPoint);
        
        simplex.evaluate(new TestFunction(), new TestComparator());
        
        PointValuePair newPoint = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 5.0);
        simplex.replaceWorstPoint(newPoint, new TestComparator());
        
        PointValuePair[] points = simplex.getPoints();
        assertEquals(5.0, points[0].getValue(), 0.0001);
        assertEquals(4.0, points[1].getValue(), 0.0001);
        assertEquals(2.0, points[2].getValue(), 0.0001);
        assertEquals(1.0, points[3].getValue(), 0.0001);
    }
    
    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new TestSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        
        simplex.build(startPoint);
        
        PointValuePair point = simplex.getPoint(2);
        
        assertEquals(startPoint[0] + 2.0, point.getPointRef()[0], 0.0001);
        assertEquals(Double.NaN, point.getValue(), 0.0001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        AbstractSimplex simplex = new TestSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        
        simplex.build(startPoint);
        
        simplex.getPoint(4);
    }
    
    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new TestSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        
        simplex.build(startPoint);
        
        PointValuePair newPoint = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 5.0);
        simplex.setPoint(2, newPoint);
        
        PointValuePair point = simplex.getPoint(2);
        
        assertEquals(4.0, point.getPointRef()[0], 0.0001);
        assertEquals(5.0, point.getValue(), 0.0001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        AbstractSimplex simplex = new TestSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        
        simplex.build(startPoint);
        
        PointValuePair newPoint = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 5.0);
        simplex.setPoint(4, newPoint);
    }
    
    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new TestSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        
        simplex.build(startPoint);
        
        PointValuePair[] newPoints = {
            new PointValuePair(new double[]{7.0, 8.0, 9.0}, 9.0),
            new PointValuePair(new double[]{10.0, 11.0, 12.0}, 12.0),
            new PointValuePair(new double[]{13.0, 14.0, 15.0}, 15.0),
            new PointValuePair(new double[]{16.0, 17.0, 18.0}, 18.0)
        };
        simplex.setPoints(newPoints);
        
        PointValuePair[] points = simplex.getPoints();
        assertEquals(7.0, points[0].getPointRef()[0], 0.0001);
        assertEquals(9.0, points[0].getValue(), 0.0001);
        assertEquals(10.0, points[1].getPointRef()[0], 0.0001);
        assertEquals(12.0, points[1].getValue(), 0.0001);
        assertEquals(13.0, points[2].getPointRef()[0], 0.0001);
        assertEquals(15.0, points[2].getValue(), 0.0001);
        assertEquals(16.0, points[3].getPointRef()[0], 0.0001);
        assertEquals(18.0, points[3].getValue(), 0.0001);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsDimensionMismatch() {
        AbstractSimplex simplex = new TestSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        
        simplex.build(startPoint);
        
        PointValuePair[] newPoints = {
            new PointValuePair(new double[]{7.0, 8.0}, 9.0),
            new PointValuePair(new double[]{10.0, 11.0}, 12.0),
            new PointValuePair(new double[]{13.0, 14.0}, 15.0),
            new PointValuePair(new double[]{16.0, 17.0}, 18.0)
        };
        simplex.setPoints(newPoints);
    }
    
    private static class TestSimplex extends AbstractSimplex {
        
        public TestSimplex(int n) {
            super(n);
        }
        
        public void iterate(final MultivariateFunction p0, final Comparator<PointValuePair> p1) {
            // implementation not required for this test
        }
    }
    
    private static class TestFunction implements MultivariateFunction {
        
        public double value(double[] point) {
            return point[0] + point[1] + point[2];
        }
    }
    
    private static class TestComparator implements Comparator<PointValuePair> {
        
        public int compare(PointValuePair p1, PointValuePair p2) {
            return (int) (p2.getValue() - p1.getValue());
        }
    }
}
