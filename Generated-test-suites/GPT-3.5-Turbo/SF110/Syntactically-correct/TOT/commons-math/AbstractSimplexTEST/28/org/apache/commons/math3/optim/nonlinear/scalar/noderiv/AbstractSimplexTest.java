import org.junit.*;
import static org.junit.Assert.*;

public class AbstractSimplexTest {
    
    @Test
    public void testAbstractSimplexConstructor1_NullArgumentException() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(5);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Test passed
        }
    }
    
    @Test
    public void testAbstractSimplexConstructor1_ZeroException() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(0);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Test passed
        }
    }
    
    @Test
    public void testAbstractSimplexConstructor2_EqualVerticesException() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(new double[]{1, 2, 3, 2});
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Test passed
        }
    }
    
    @Test
    public void testAbstractSimplexConstructor2_DimensionMismatchException() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(new double[][]{{1, 2}, {3}, {1, 3}});
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Test passed
        }
    }
    
    @Test
    public void testAbstractSimplexConstructor2_NotStrictlyPositiveException() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(new double[][]{{1, 2}});
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Test passed
        }
    }
    
    @Test
    public void testAbstractSimplexConstructor3() {
        double[] steps = {1, 1, 1, 1};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(4, simplex.getDimension());
    }
    
    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testIterate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});
        simplex.iterate(new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        double[] expectedPoint = {1, 2, 3};
        assertEquals(expectedPoint, simplex.getPoint(0).getPoint());
    }
    
    @Test
    public void testBuild_DimensionMismatchException() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(3);
            simplex.build(new double[]{1, 2});
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Test passed
        }
    }
    
    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});
        simplex.evaluate(new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        double[] expectedPoint = {1, 2, 3};
        assertEquals(expectedPoint, simplex.getPoint(0).getPoint());
    }
    
    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});
        PointValuePair point = new PointValuePair(new double[]{1, 1, 1}, 6.0);
        simplex.replaceWorstPoint(point, new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        double[] expectedPoint = {1, 1, 1};
        assertEquals(expectedPoint, simplex.getPoint(0).getPoint());
    }
    
    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});
        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertEquals(4, points.length);
    }
    
    @Test
    public void testGetPoint_OutOfRangeException() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});
        try {
            PointValuePair point = simplex.getPoint(4);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }
    }
    
    @Test
    public void testSetPoint_OutOfRangeException() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});
        PointValuePair point = new PointValuePair(new double[]{1, 1, 1}, 6.0);
        try {
            simplex.setPoint(4, point);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }
    }
    
    @Test
    public void testSetPoints_DimensionMismatchException() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});
        PointValuePair[] points = {new PointValuePair(new double[]{1, 1, 1}, 6.0)};
        try {
            simplex.setPoints(points);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Test passed
        }
    }
    
    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 2);
        double[] expectedSteps = {2, 2, 2};
        assertArrayEquals(expectedSteps, steps);
    }

}