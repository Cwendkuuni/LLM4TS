import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex_constructor() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertNotNull(simplex);
    }

    @Test
    public void testAbstractSimplex_constructorWithSideLength() {
        AbstractSimplex simplex = new AbstractSimplex(3, 2.0);
        assertNotNull(simplex);
    }

    @Test
    public void testAbstractSimplex_constructorWithSteps() {
        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertNotNull(simplex);
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplex_constructorWithNullSteps() {
        double[] steps = null;
        AbstractSimplex simplex = new AbstractSimplex(steps);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_constructorWithEmptySteps() {
        double[] steps = {};
        AbstractSimplex simplex = new AbstractSimplex(steps);
    }

    @Test
    public void testAbstractSimplex_constructorWithReferenceSimplex() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertNotNull(simplex);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplex_constructorWithEmptyReferenceSimplex() {
        double[][] referenceSimplex = {};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_constructorWithInconsistentReferenceSimplex() {
        double[][] referenceSimplex = {{1.0, 2.0}, {3.0, 4.0, 5.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplex_constructorWithEqualVertices() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {1.0, 2.0, 3.0}, {7.0, 8.0, 9.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testAbstractSimplex_getDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        int dimension = simplex.getDimension();
        assertEquals(3, dimension);
    }

    @Test
    public void testAbstractSimplex_getSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        int size = simplex.getSize();
        assertEquals(4, size); // The simplex has 3 dimensions, so size is 4
    }

    @Test
    public void testAbstractSimplex_iterate() {
        // TODO: Implement test case for iterate() method
    }

    @Test
    public void testAbstractSimplex_build() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertEquals(4, points.length);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_buildWithInconsistentStartPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
    }

    @Test
    public void testAbstractSimplex_evaluate() {
        // TODO: Implement test case for evaluate() method
    }

    @Test
    public void testAbstractSimplex_replaceWorstPoint() {
        // TODO: Implement test case for replaceWorstPoint() method
    }

    @Test
    public void testAbstractSimplex_getPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertEquals(4, points.length);
    }

    @Test
    public void testAbstractSimplex_getPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair point = simplex.getPoint(0);
        assertNotNull(point);
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplex_getPointWithInvalidIndex() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair point = simplex.getPoint(4);
    }

    @Test
    public void testAbstractSimplex_setPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair point = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0);
        simplex.setPoint(0, point);
        PointValuePair retrievedPoint = simplex.getPoint(0);
        assertNotNull(retrievedPoint);
        assertEquals(point, retrievedPoint);
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplex_setPointWithInvalidIndex() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair point = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0);
        simplex.setPoint(4, point);
    }

    @Test
    public void testAbstractSimplex_setPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] points = new PointValuePair[]{new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0)};
        simplex.setPoints(points);
        PointValuePair[] retrievedPoints = simplex.getPoints();
        assertNotNull(retrievedPoints);
        assertEquals(points, retrievedPoints);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_setPointsWithInconsistentDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] points = new PointValuePair[]{new PointValuePair(new double[]{1.0, 2.0}, 1.0)};
        simplex.setPoints(points);
    }

    @Test
    public void testAbstractSimplex_createHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 2.0);
        assertNotNull(steps);
        assertEquals(3, steps.length);
        for (double step : steps) {
            assertEquals(2.0, step, 0.0);
        }
    }

}