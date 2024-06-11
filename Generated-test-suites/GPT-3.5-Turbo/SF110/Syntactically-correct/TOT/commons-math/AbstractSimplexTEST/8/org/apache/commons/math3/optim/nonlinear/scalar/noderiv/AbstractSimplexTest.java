import org.junit.*;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        int dimension = simplex.getDimension();
        assertEquals(3, dimension);
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});
        int size = simplex.getSize();
        assertEquals(4, size);
    }

    @Test
    public void testIterate() {
        AbstractSimplex simplex = new AbstractSimplex(3) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Test implementation
            }
        };
        // Testing code
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});

        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);

        PointValuePair firstPoint = points[0];
        assertArrayEquals(new double[]{1, 2, 3}, firstPoint.getPointRef(), 0.0001);

        PointValuePair secondPoint = points[1];
        assertArrayEquals(new double[]{2, 3, 4}, secondPoint.getPointRef(), 0.0001);

        PointValuePair lastPoint = points[3];
        assertArrayEquals(new double[]{4, 5, 6}, lastPoint.getPointRef(), 0.0001);
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});

        simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                // Test implementation
                return 0;
            }
        }, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                // Test implementation
                return 0;
            }
        });

        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);

        PointValuePair firstPoint = points[0];
        assertFalse(Double.isNaN(firstPoint.getValue()));

        PointValuePair secondPoint = points[1];
        assertFalse(Double.isNaN(secondPoint.getValue()));

        PointValuePair lastPoint = points[3];
        assertFalse(Double.isNaN(lastPoint.getValue()));
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});

        PointValuePair newPoint = new PointValuePair(new double[]{2, 3, 4}, 0);

        simplex.replaceWorstPoint(newPoint, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                // Test implementation
                return 0;
            }
        });

        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);

        PointValuePair firstPoint = points[0];
        assertArrayEquals(new double[]{1, 2, 3}, firstPoint.getPointRef(), 0.0001);

        PointValuePair replacedPoint = points[3];
        assertArrayEquals(new double[]{2, 3, 4}, replacedPoint.getPointRef(), 0.0001);
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});

        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);

        PointValuePair[] copiedPoints = simplex.getPoints();
        assertArrayEquals(points, copiedPoints);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});

        PointValuePair point = simplex.getPoint(2);

        assertArrayEquals(new double[]{3, 4, 5}, point.getPointRef(), 0.0001);
        assertFalse(Double.isNaN(point.getValue()));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_IndexOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});

        simplex.getPoint(5); // Throws OutOfRangeException
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});

        PointValuePair newPoint = new PointValuePair(new double[]{4, 5, 6}, 0);
        simplex.setPoint(2, newPoint);

        PointValuePair point = simplex.getPoint(2);
        assertArrayEquals(new double[]{4, 5, 6}, point.getPointRef(), 0.0001);
        assertFalse(Double.isNaN(point.getValue()));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_IndexOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});

        PointValuePair newPoint = new PointValuePair(new double[]{4, 5, 6}, 0);
        simplex.setPoint(4, newPoint); // Throws OutOfRangeException
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});

        PointValuePair[] newPoints = new PointValuePair[]{
                new PointValuePair(new double[]{4, 5, 6}, 0),
                new PointValuePair(new double[]{7, 8, 9}, 0),
                new PointValuePair(new double[]{10, 11, 12}, 0),
                new PointValuePair(new double[]{13, 14, 15}, 0)
        };

        simplex.setPoints(newPoints);

        PointValuePair[] points = simplex.getPoints();
        assertArrayEquals(newPoints, points);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPoints_DimensionMismatch() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});

        PointValuePair[] newPoints = new PointValuePair[]{
                new PointValuePair(new double[]{4, 5, 6}, 0),
                new PointValuePair(new double[]{7, 8, 9}, 0)
        };

        simplex.setPoints(newPoints); // Throws DimensionMismatchException
    }

    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 2);
        assertArrayEquals(new double[]{2, 2, 2}, steps, 0.0001);
    }

    @Test
    public void testCreateHypercubeSteps_ZeroLength() {
        double[] steps = AbstractSimplex.createHypercubeSteps(0, 2);
        assertArrayEquals(new double[]{}, steps, 0.0001);
    }

    @Test
    public void testCreateHypercubeSteps_NegativeSideLength() {
        double[] steps = AbstractSimplex.createHypercubeSteps(3, -2);
        assertArrayEquals(new double[]{-2, -2, -2}, steps, 0.0001);
    }

}