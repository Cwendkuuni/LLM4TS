import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;

public class AbstractSimplexTest {

    private AbstractSimplex simplex;

    @Before
    public void setUp() throws Exception {
        simplex = new TestSimplex();
    }

    @Test
    public void testConstructor1() {
        AbstractSimplex simplex = new TestSimplex(5);
        assertEquals(5, simplex.getDimension());
        assertEquals(6, simplex.getSize());
    }

    @Test
    public void testConstructor2() {
        AbstractSimplex simplex = new TestSimplex(5, 2.0);
        assertEquals(5, simplex.getDimension());
        assertEquals(6, simplex.getSize());
    }

    @Test
    public void testConstructor3() {
        double[] steps = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        AbstractSimplex simplex = new TestSimplex(steps);
        assertEquals(5, simplex.getDimension());
        assertEquals(6, simplex.getSize());
    }

    @Test
    public void testConstructor4() {
        double[][] referenceSimplex = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        AbstractSimplex simplex = new TestSimplex(referenceSimplex);
        assertEquals(2, simplex.getDimension());
        assertEquals(3, simplex.getSize());
    }

    @Test
    public void testGetDimension() {
        assertEquals(2, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals(3, simplex.getSize());
    }

    @Test
    public void testIterate() {
        // implementation specific tests
    }

    @Test
    public void testBuild() {
        double[] startPoint = new double[]{1.0, 2.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(3, points.length);
        assertEquals(startPoint, points[0].getPoint());
    }

    @Test
    public void testEvaluate() {
        // implementation specific tests
    }

    @Test
    public void testReplaceWorstPoint() {
        // implementation specific tests
    }

    @Test
    public void testGetPoints() {
        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertEquals(3, points.length);
    }

    @Test
    public void testGetPoint() {
        PointValuePair point = simplex.getPoint(0);
        assertNotNull(point);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        PointValuePair point = simplex.getPoint(3);
    }

    @Test
    public void testSetPoint() {
        PointValuePair point = new PointValuePair(new double[]{3.0, 2.0}, 1.0);
        simplex.setPoint(0, point);
        assertEquals(point, simplex.getPoint(0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        PointValuePair point = new PointValuePair(new double[]{3.0, 2.0}, 1.0);
        simplex.setPoint(3, point);
    }

    @Test
    public void testSetPoints() {
        PointValuePair[] points = new PointValuePair[]{new PointValuePair(new double[]{1.0, 2.0}, 1.0), new PointValuePair(new double[]{3.0, 4.0}, 2.0), new PointValuePair(new double[]{5.0, 6.0}, 3.0)};
        simplex.setPoints(points);
        assertArrayEquals(points, simplex.getPoints());
    }

    private class TestSimplex extends AbstractSimplex {

        public TestSimplex() {
            super(2);
        }

        public TestSimplex(int n) {
            super(n);
        }

        public TestSimplex(int n, double sideLength) {
            super(n, sideLength);
        }

        public TestSimplex(double[] steps) {
            super(steps);
        }

        public TestSimplex(double[][] referenceSimplex) {
            super(referenceSimplex);
        }

        @Override
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // implementation specific
        }

    }
}