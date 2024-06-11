import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testConstructorWithOneArgument() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testConstructorWithTwoArguments() {
        AbstractSimplex simplex = new AbstractSimplex(3, 2.0);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testConstructorWithDoubleArray() {
        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testConstructorWithDoubleArrayInvalidArguments() {
        double[] steps = {};
        try {
            AbstractSimplex simplex = new AbstractSimplex(steps);
        } catch (ZeroException e) {
            assertEquals("The array of steps must not be empty", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithDoubleArrayEqualVertices() {
        double[] steps = {1.0, 1.0};
        try {
            AbstractSimplex simplex = new AbstractSimplex(steps);
        } catch (ZeroException e) {
            assertEquals("The vertices of the simplex must not be equal", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithDoubleArrayReferenceSimplex() {
        double[][] referenceSimplex = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(2, simplex.getDimension());
    }

    @Test
    public void testConstructorWithDoubleArrayReferenceSimplexDimensionMismatch() {
        double[][] referenceSimplex = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0, 7.0}};
        try {
            AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        } catch (DimensionMismatchException e) {
            assertEquals("The vertices of the simplex must have the same dimension", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithDoubleArrayReferenceSimplexEqualVertices() {
        double[][] referenceSimplex = {{1.0, 2.0}, {1.0, 2.0}, {5.0, 6.0}};
        try {
            AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        } catch (MathIllegalArgumentException e) {
            assertEquals("The vertices of the simplex must not be equal: (0, 1)", e.getPattern());
        }
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testIterate() {
        // TODO: Implement test for iterate method
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        PointValuePair[] points = simplex.getPoints();
        for (int i = 0; i < points.length; i++) {
            double[] point = points[i].getPoint();
            assertArrayEquals(new double[]{1.0, 2.0, 3.0}, point, 0.001);
        }
    }

    @Test
    public void testEvaluate() {
        // TODO: Implement test for evaluate method
    }

    @Test
    public void testReplaceWorstPoint() {
        // TODO: Implement test for replaceWorstPoint method
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        PointValuePair point = simplex.getPoint(2);
        double[] expected = {1.0, 2.0, 3.0};
        assertArrayEquals(expected, point, 0.001);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        PointValuePair point = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0);
        simplex.setPoint(2, point);
        assertEquals(point, simplex.getPoint(2));
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        PointValuePair[] points = {new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0),
                new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0)};
        simplex.setPoints(points);
        assertArrayEquals(points, simplex.getPoints());
    }

    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(4, 2.0);
        double[] expected = {2.0, 2.0, 2.0, 2.0};
        assertArrayEquals(expected, steps, 0.001);
    }
}