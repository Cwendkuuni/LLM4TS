import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AbstractSimplexTest {
    
    private AbstractSimplex simplex;
    
    @Before
    public void setUp() {
        simplex = new AbstractSimplexMock();
    }
    
    @Test
    public void testGetDimension() {
        int dimension = simplex.getDimension();
        assertEquals(2, dimension);
    }
    
    @Test
    public void testGetSize() {
        int size = simplex.getSize();
        assertEquals(3, size);
    }
    
    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 2.0};
        
        simplex.build(startPoint);
        
        PointValuePair[] points = simplex.getPoints();
        
        assertEquals(startPoint[0], points[0].getPoint()[0], 0.001);
        assertEquals(startPoint[1], points[0].getPoint()[1], 0.001);
    }
    
    @Test
    public void testEvaluate() {
        MultivariateFunction function = new MultivariateFunctionMock();
        
        simplex.evaluate(function, new ComparatorMock());
        
        PointValuePair[] points = simplex.getPoints();
        
        assertTrue(points[0].getValue() < points[1].getValue());
        assertTrue(points[1].getValue() < points[2].getValue());
    }
    
    @Test
    public void testReplaceWorstPoint() {
        PointValuePair pointValuePair = new PointValuePair(new double[]{3.0, 4.0}, 0.0);
        Comparator<PointValuePair> comparator = new ComparatorMock();
        
        simplex.replaceWorstPoint(pointValuePair, comparator);
        
        PointValuePair[] points = simplex.getPoints();
        
        assertEquals(pointValuePair, points[2]);
    }
    
    @Test
    public void testGetPoints() {
        PointValuePair[] points = simplex.getPoints();
        
        assertEquals(3, points.length);
    }
    
    @Test
    public void testGetPoint() {
        PointValuePair point = simplex.getPoint(1);
        
        assertNotNull(point);
        assertEquals(1.0, point.getPoint()[0], 0.001);
        assertEquals(2.0, point.getPoint()[1], 0.001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_OutOfRange() {
        simplex.getPoint(3);
    }
    
    @Test
    public void testSetPoint() {
        PointValuePair point = new PointValuePair(new double[]{3.0, 4.0}, 0.0);
        
        simplex.setPoint(1, point);
        
        assertEquals(point, simplex.getPoint(1));
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_OutOfRange() {
        PointValuePair point = new PointValuePair(new double[]{3.0, 4.0}, 0.0);
        
        simplex.setPoint(4, point);
    }
    
    @Test
    public void testSetPoints() {
        PointValuePair[] points = {new PointValuePair(new double[]{3.0, 4.0}, 0.0),
                                   new PointValuePair(new double[]{5.0, 6.0}, 0.0),
                                   new PointValuePair(new double[]{7.0, 8.0}, 0.0)};
        
        simplex.setPoints(points);
        
        assertArrayEquals(points, simplex.getPoints());
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testSetPoints_DimensionMismatch() {
        PointValuePair[] points = {new PointValuePair(new double[]{3.0, 4.0}, 0.0),
                                   new PointValuePair(new double[]{5.0, 6.0}, 0.0)};
        
        simplex.setPoints(points);
    }
    
    // Additional test cases for edge cases and exception handling
    
    // Test constructor: AbstractSimplex(int n)
    // n = 0
    @Test(expected = ZeroException.class)
    public void testConstructorN() {
        AbstractSimplex zeroN = new AbstractSimplexMock(0);
    }
    
    // n < 0
    @Test(expected = NegativeArraySizeException.class)
    public void testConstructorN_Negative() {
        AbstractSimplex negativeN = new AbstractSimplexMock(-1);
    }
    
    // Test constructor: AbstractSimplex(int n, double sideLength)
    // n = 0, sideLength = 1.0
    @Test(expected = ZeroException.class)
    public void testConstructorNAndSideLength() {
        AbstractSimplex zeroNSideLength = new AbstractSimplexMock(0, 1.0);
    }
    
    // n < 0, sideLength = 1.0
    @Test(expected = NegativeArraySizeException.class)
    public void testConstructorNAndSideLength_NegativeN() {
        AbstractSimplex negativeNSideLength = new AbstractSimplexMock(-1, 1.0);
    }
    
    // n = 0, sideLength < 0
    @Test(expected = ZeroException.class)
    public void testConstructorNAndSideLength_NegativeSideLength() {
        AbstractSimplex zeroNNegativeSideLength = new AbstractSimplexMock(0, -1.0);
    }
    
    // n < 0, sideLength < 0
    @Test(expected = NegativeArraySizeException.class)
    public void testConstructorNAndSideLength_NegativeNAndSideLength() {
        AbstractSimplex negativeNNegativeSideLength = new AbstractSimplexMock(-1, -1.0);
    }
    
    // Test constructor: AbstractSimplex(double[] steps)
    // steps = null
    @Test(expected = NullArgumentException.class)
    public void testConstructorSteps_Null() {
        AbstractSimplex nullSteps = new AbstractSimplexMock(null);
    }
    
    // steps = []
    @Test(expected = ZeroException.class)
    public void testConstructorSteps_Empty() {
        AbstractSimplex emptySteps = new AbstractSimplexMock(new double[]{});
    }
    
    // Test constructor: AbstractSimplex(double[][] referenceSimplex)
    // referenceSimplex = []
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorReferenceSimplex() {
        AbstractSimplex emptyReferenceSimplex = new AbstractSimplexMock(new double[][]{});
    }
    
    
    private class AbstractSimplexMock extends AbstractSimplex {
        
        public AbstractSimplexMock() {
            super(2);
        }
        
        public AbstractSimplexMock(int n) {
            super(n);
        }
        
        public AbstractSimplexMock(int n, double sideLength) {
            super(n, sideLength);
        }
        
        public AbstractSimplexMock(double[] steps) {
            super(steps);
        }
        
        public AbstractSimplexMock(double[][] referenceSimplex) {
            super(referenceSimplex);
        }
        
        @Override
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // Not implemented for testing purposes
        }
    }
    
    private class MultivariateFunctionMock implements MultivariateFunction {
        
        @Override
        public double value(double[] point) {
            return point[0] * point[0] + point[1] * point[1];
        }
    }
    
    private class ComparatorMock implements Comparator<PointValuePair> {
        
        @Override
        public int compare(PointValuePair p1, PointValuePair p2) {
            return Double.compare(p1.getValue(), p2.getValue());
        }
    }
}