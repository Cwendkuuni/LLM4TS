
import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testConstructor() {
        AbstractSimplex simplex = new AbstractSimplex(3);

        assertNotNull(simplex);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};

        simplex.build(startPoint);

        assertNotNull(simplex.getPoint(0));
        assertNotNull(simplex.getPoint(1));
        assertNotNull(simplex.getPoint(2));
        assertNotNull(simplex.getPoint(3));
        assertEquals(startPoint, simplex.getPoint(0).getPointRef());
        assertEquals(startPoint[0] + 1.0, simplex.getPoint(1).getPointRef()[0], 0.0001);
        assertEquals(startPoint[1] + 1.0, simplex.getPoint(1).getPointRef()[1], 0.0001);
        assertEquals(startPoint[2] + 1.0, simplex.getPoint(1).getPointRef()[2], 0.0001);
        assertEquals(startPoint[0] + 2.0, simplex.getPoint(2).getPointRef()[0], 0.0001);
        assertEquals(startPoint[1] + 2.0, simplex.getPoint(2).getPointRef()[1], 0.0001);
        assertEquals(startPoint[2] + 2.0, simplex.getPoint(2).getPointRef()[2], 0.0001);
        assertEquals(startPoint[0] + 3.0, simplex.getPoint(3).getPointRef()[0], 0.0001);
        assertEquals(startPoint[1] + 3.0, simplex.getPoint(3).getPointRef()[1], 0.0001);
        assertEquals(startPoint[2] + 3.0, simplex.getPoint(3).getPointRef()[2], 0.0001);
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};

        simplex.build(startPoint);
        simplex.evaluate(evaluationFunction, comparator);

        assertNotNull(simplex.getPoint(0));
        assertNotNull(simplex.getPoint(1));
        assertNotNull(simplex.getPoint(2));
        assertNotNull(simplex.getPoint(3));
        assertFalse(Double.isNaN(simplex.getPoint(0).getValue()));
        assertFalse(Double.isNaN(simplex.getPoint(1).getValue()));
        assertFalse(Double.isNaN(simplex.getPoint(2).getValue()));
        assertFalse(Double.isNaN(simplex.getPoint(3).getValue()));
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};

        simplex.build(startPoint);
        simplex.evaluate(evaluationFunction, comparator);
        PointValuePair point = new PointValuePair(new double[] {4.0, 5.0, 6.0}, 1.0);
        simplex.replaceWorstPoint(point, comparator);

        assertNotNull(simplex.getPoint(0));
        assertNotNull(simplex.getPoint(1));
        assertNotNull(simplex.getPoint(2));
        assertNotNull(simplex.getPoint(3));
        assertEquals(point, simplex.getPoint(3));
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};

        simplex.build(startPoint);
        simplex.evaluate(evaluationFunction, comparator);

        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertEquals(4, points.length);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};

        simplex.build(startPoint);
        simplex.evaluate(evaluationFunction, comparator);

        PointValuePair point = simplex.getPoint(2);
        assertNotNull(point);
        assertEquals(startPoint[0] + 2.0, point.getPointRef()[0], 0.0001);
        assertEquals(startPoint[1] + 2.0, point.getPointRef()[1], 0.0001);
        assertEquals(startPoint[2] + 2.0, point.getPointRef()[2], 0.0001);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};

        simplex.build(startPoint);
        simplex.evaluate(evaluationFunction, comparator);

        PointValuePair point = new PointValuePair(new double[] {4.0, 5.0, 6.0}, 1.0);
        simplex.setPoint(2, point);

        assertNotNull(simplex.getPoint(0));
        assertNotNull(simplex.getPoint(1));
        assertNotNull(simplex.getPoint(2));
        assertNotNull(simplex.getPoint(3));
        assertEquals(point, simplex.getPoint(2));
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        PointValuePair[] points = new PointValuePair[4];

        simplex.build(startPoint);
        simplex.evaluate(evaluationFunction, comparator);
        for (int i = 0; i < 4; i++) {
            double[] p = new double[3];
            Arrays.fill(p, i + 1.0);
            points[i] = new PointValuePair(p, i);
        }
        simplex.setPoints(points);

        assertNotNull(simplex.getPoint(0));
        assertNotNull(simplex.getPoint(1));
        assertNotNull(simplex.getPoint(2));
        assertNotNull(simplex.getPoint(3));
        assertEquals(points[0], simplex.getPoint(0));
        assertEquals(points[1], simplex.getPoint(1));
        assertEquals(points[2], simplex.getPoint(2));
        assertEquals(points[3], simplex.getPoint(3));
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test(expected = NullArgumentException.class)
    public void testConstructor_nullSteps() {
        AbstractSimplex simplex = new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testConstructor_emptySteps() {
        AbstractSimplex simplex = new AbstractSimplex(new double[0]);
    }

    @Test(expected = ZeroException.class)
    public void testConstructor_equalVerticesInSimplex() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0, 1.0});
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_referenceSimplexNeedOnePoint() {
        AbstractSimplex simplex = new AbstractSimplex(new double[0][0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuild_dimensionMismatch() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0};

        simplex.build(startPoint);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEvaluate_dimensionMismatch() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};

        simplex.build(startPoint);
        simplex.evaluate(evaluationFunction, comparator);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_indexOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};

        simplex.build(startPoint);
        simplex.evaluate(evaluationFunction, comparator);

        PointValuePair point = simplex.getPoint(4);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_indexOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};

        simplex.build(startPoint);
        simplex.evaluate(evaluationFunction, comparator);

        PointValuePair point = new PointValuePair(new double[] {4.0, 5.0, 6.0}, 1.0);
        simplex.setPoint(4, point);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPoints_dimensionMismatch() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        PointValuePair[] points = new PointValuePair[2];

        simplex.build(startPoint);
        simplex.evaluate(evaluationFunction, comparator);
        for (int i = 0; i < 2; i++) {
            double[] p = new double[2];
            Arrays.fill(p, i + 1.0);
            points[i] = new PointValuePair(p, i);
        }
        simplex.setPoints(points);
    }
}
