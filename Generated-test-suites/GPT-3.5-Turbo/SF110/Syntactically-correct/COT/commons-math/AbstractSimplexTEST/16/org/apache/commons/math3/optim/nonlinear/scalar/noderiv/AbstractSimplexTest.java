import org.junit.Test;
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
        AbstractSimplex simplex = new AbstractSimplex(3, 2.0);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testConstructor3() {
        double[] steps = {2.0, 3.0, 4.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testConstructor4() {
        double[][] referenceSimplex = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(2, simplex.getDimension());
        assertEquals(3, simplex.getSize());
    }

    @Test (expected = DimensionMismatchException.class)
    public void testConstructor4DimensionMismatch() {
        double[][] referenceSimplex = {{1.0, 2.0}, {3.0, 4.0, 5.0}, {6.0, 7.0, 8.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
    }

    @Test (expected = MathIllegalArgumentException.class)
    public void testConstructor4EqualVertices() {
        double[][] referenceSimplex = {{1.0, 2.0}, {1.0, 2.0}, {3.0, 4.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        assertEquals(4, simplex.getSize());
    }

    @Test (expected = DimensionMismatchException.class)
    public void testBuildDimensionMismatch() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return 2.0 * point[0] + 3.0 * point[1] + 4.0 * point[2];
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        simplex.evaluate(evaluationFunction, comparator);
        assertEquals(4, simplex.getSize());
        assertFalse(Double.isNaN(simplex.getPoint(0).getValue()));
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return 2.0 * point[0] + 3.0 * point[1] + 4.0 * point[2];
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        simplex.evaluate(evaluationFunction, comparator);
        PointValuePair pointValuePair = simplex.getPoint(3);
        simplex.replaceWorstPoint(pointValuePair, comparator);
        assertEquals(pointValuePair, simplex.getPoint(simplex.getSize() - 1));
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(simplex.getSize(), points.length);
        for (int i = 0; i < simplex.getSize(); i++) {
            assertEquals(simplex.getPoint(i), points[i]);
        }
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        for (int i = 0; i < simplex.getSize(); i++) {
            assertEquals(i, simplex.getPoint(i).getPointRef()[0], 0.1);
            assertEquals(i, simplex.getPoint(i).getPointRef()[1], 0.1);
            assertEquals(i, simplex.getPoint(i).getPointRef()[2], 0.1);
        }
    }

    @Test (expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        simplex.getPoint(4);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair point = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0);
        simplex.setPoint(0, point);
        assertEquals(point, simplex.getPoint(0));
    }

    @Test (expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair point = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0);
        simplex.setPoint(4, point);
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair[] points = new PointValuePair[]{new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0)};
        simplex.setPoints(points);
        assertEquals(points.length, simplex.getSize());
        assertEquals(points[0], simplex.getPoint(0));
    }

    @Test (expected = DimensionMismatchException.class)
    public void testSetPointsDimensionMismatch() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair[] points = new PointValuePair[]{new PointValuePair(new double[]{4.0, 5.0}, 0.0)};
        simplex.setPoints(points);
    }

    private static double[] createHypercubeSteps(final int n, final double sideLength) {
        final double[] steps = new double[n];
        for (int i = 0; i < n; ++i) {
            steps[i] = sideLength;
        }
        return steps;
    }

    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = createHypercubeSteps(3, 2.0);
        assertEquals(3, steps.length);
        for (int i = 0; i < steps.length; i++) {
            assertEquals(2.0, steps[i], 0.1);
        }
    }

}