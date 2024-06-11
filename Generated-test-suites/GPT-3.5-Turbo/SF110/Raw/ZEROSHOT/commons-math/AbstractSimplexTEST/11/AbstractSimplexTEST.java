import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        simplex = new AbstractSimplexImpl(3);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testBuild() {
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);

        PointValuePair[] points = simplex.getPoints();

        assertEquals(4, points.length);
        assertArrayEquals(startPoint, points[0].getPoint(), 0.0);

        for (int i = 1; i < points.length; i++) {
            double[] expectedPoint = new double[3];
            for (int j = 0; j < 3; j++) {
                expectedPoint[j] = startPoint[j] + (i-1);
            }
            assertArrayEquals(expectedPoint, points[i].getPoint(), 0.0);
        }
    }

    @Test
    public void testEvaluate() {
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);

        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };

        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        simplex.evaluate(evaluationFunction, comparator);

        PointValuePair[] points = simplex.getPoints();

        double[] expectedValues = {6, 7, 8, 9};
        for (int i = 0; i < points.length; i++) {
            assertEquals(expectedValues[i], points[i].getValue(), 0.0);
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);

        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };

        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        simplex.evaluate(evaluationFunction, comparator);

        PointValuePair newPoint = new PointValuePair(new double[]{4, 5, 6}, 15);
        simplex.replaceWorstPoint(newPoint, comparator);

        PointValuePair[] points = simplex.getPoints();
        assertEquals(newPoint, points[3]);
    }

    @Test
    public void testGetPoints() {
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);

        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertEquals(4, points.length);
    }

    @Test
    public void testGetPoint() {
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);

        PointValuePair point = simplex.getPoint(2);

        assertNotNull(point);
        assertArrayEquals(new double[]{2, 3, 4}, point.getPoint(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);

        PointValuePair point = simplex.getPoint(4);
    }

    @Test
    public void testSetPoint() {
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[]{4, 5, 6}, 15);
        simplex.setPoint(2, newPoint);

        PointValuePair point = simplex.getPoint(2);
        assertEquals(newPoint, point);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        double[] startPoint = {1, 2, 3};
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[]{4, 5, 6}, 15);
        simplex.setPoint(4, newPoint);
    }

    @Test
    public void testSetPoints() {
        PointValuePair[] points = new PointValuePair[]{
                new PointValuePair(new double[]{1,2,3}, 1),
                new PointValuePair(new double[]{4,5,6}, 2),
                new PointValuePair(new double[]{7,8,9}, 3),
                new PointValuePair(new double[]{10,11,12}, 4)
        };

        simplex.setPoints(points);

        PointValuePair[] simplexPoints = simplex.getPoints();

        assertArrayEquals(points, simplexPoints);
    }

    private class AbstractSimplexImpl extends AbstractSimplex {

        public AbstractSimplexImpl(int n) {
            super(n);
        }

        @Override
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

        }

        @Override
        public int getDimension() {
            return super.getDimension();
        }

        @Override
        public int getSize() {
            return super.getSize();
        }
    }
}