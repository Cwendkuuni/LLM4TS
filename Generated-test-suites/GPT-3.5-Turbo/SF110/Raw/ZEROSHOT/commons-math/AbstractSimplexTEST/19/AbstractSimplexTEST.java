import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AbstractSimplexTest {
    
    private class TestAbstractSimplex extends AbstractSimplex {
        
        public TestAbstractSimplex(int n) {
            super(n);
        }
        
        public TestAbstractSimplex(int n, double sideLength) {
            super(n, sideLength);
        }
        
        public TestAbstractSimplex(double[] steps) {
            super(steps);
        }
        
        public TestAbstractSimplex(double[][] referenceSimplex) {
            super(referenceSimplex);
        }
        
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // implementation for testing
        }
    }
    
    private TestAbstractSimplex simplex;
    
    @Before
    public void setUp() {
        simplex = new TestAbstractSimplex(3);
    }
    
    @Test
    public void testGetDimension() {
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void testGetSize() {
        assertEquals(0, simplex.getSize());
        
        simplex.build(new double[] {1, 2, 3});
        
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testBuild() {
        simplex.build(new double[] {1, 2, 3});
        
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        assertArrayEquals(new double[] {1, 2, 3}, points[0].getPoint(), 0);
        assertArrayEquals(new double[] {2, 3, 4}, points[1].getPoint(), 0);
        assertArrayEquals(new double[] {3, 4, 5}, points[2].getPoint(), 0);
        assertArrayEquals(new double[] {4, 5, 6}, points[3].getPoint(), 0);
    }
    
    @Test
    public void testEvaluate() {
        simplex.build(new double[] {1, 2, 3});
        
        simplex.evaluate(new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        assertEquals(6, points[0].getValue(), 0);
        assertEquals(9, points[1].getValue(), 0);
        assertEquals(12, points[2].getValue(), 0);
        assertEquals(15, points[3].getValue(), 0);
        
        assertEquals(6, points[0].getPoint()[0], 0);
        assertEquals(9, points[0].getPoint()[1], 0);
        assertEquals(12, points[0].getPoint()[2], 0);
    }
    
    @Test
    public void testReplaceWorstPoint() {
        simplex.build(new double[] {1, 2, 3});
        
        simplex.evaluate(new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        
        PointValuePair point = simplex.getPoint(3);
        
        simplex.replaceWorstPoint(new PointValuePair(new double[] {5, 6, 7}, 18), new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        assertEquals(6, points[0].getValue(), 0);
        assertEquals(9, points[1].getValue(), 0);
        assertEquals(12, points[2].getValue(), 0);
        assertEquals(18, points[3].getValue(), 0);
        
        assertEquals(5, points[3].getPoint()[0], 0);
        assertEquals(6, points[3].getPoint()[1], 0);
        assertEquals(7, points[3].getPoint()[2], 0);
    }
    
    @Test
    public void testGetPoints() {
        simplex.build(new double[] {1, 2, 3});
        
        PointValuePair[] points = simplex.getPoints();
        
        assertNotSame(points, simplex.getPoints());
        assertArrayEquals(points, simplex.getPoints());
    }
    
    @Test
    public void testGetPoint() {
        simplex.build(new double[] {1, 2, 3});
        
        PointValuePair point = simplex.getPoint(2);
        
        assertSame(point, simplex.getPoint(2));
        
        assertArrayEquals(new double[] {3, 4, 5}, point.getPoint(), 0);
    }
    
    @Test
    public void testSetPoint() {
        simplex.build(new double[] {1, 2, 3});
        
        simplex.setPoint(2, new PointValuePair(new double[] {5, 6, 7}, 18));
        
        PointValuePair point = simplex.getPoint(2);
        
        assertNotSame(point, simplex.getPoint(2));
        
        assertArrayEquals(new double[] {5, 6, 7}, point.getPoint(), 0);
    }
    
    @Test
    public void testSetPoints() {
        simplex.build(new double[] {1, 2, 3});
        
        simplex.setPoints(new PointValuePair[] {
            new PointValuePair(new double[] {4, 5, 6}, 15),
            new PointValuePair(new double[] {7, 8, 9}, 24),
            new PointValuePair(new double[] {10, 11, 12}, 33),
            new PointValuePair(new double[] {13, 14, 15}, 42)
        });
        
        PointValuePair[] points = simplex.getPoints();
        
        assertEquals(4, points.length);
        assertEquals(15, points[0].getValue(), 0);
        assertEquals(24, points[1].getValue(), 0);
        assertEquals(33, points[2].getValue(), 0);
        assertEquals(42, points[3].getValue(), 0);
        
        assertArrayEquals(new double[] {4, 5, 6}, points[0].getPoint(), 0);
        assertArrayEquals(new double[] {7, 8, 9}, points[1].getPoint(), 0);
        assertArrayEquals(new double[] {10, 11, 12}, points[2].getPoint(), 0);
        assertArrayEquals(new double[] {13, 14, 15}, points[3].getPoint(), 0);
    }
    
    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 2);
        
        assertArrayEquals(new double[] {2, 2, 2}, steps, 0);
    }
}