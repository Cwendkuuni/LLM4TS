import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.analysis.MultivariateFunction;


public class AbstractSimplexTest {

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertNotNull(simplex.getSize());
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertEquals(4, points.length);
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };
        simplex.build(new double[]{1.0, 1.0, 1.0});
        simplex.evaluate(evaluationFunction, null);
        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertEquals(4, points.length);
        assertEquals(3.0, points[0].getValue(), 0.0001);
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        PointValuePair point = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 15.0);
        simplex.replaceWorstPoint(point, null);
        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertEquals(point, points[3]);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        PointValuePair point = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 15.0);
        simplex.setPoint(0, point);
        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertEquals(point, points[0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildWithIncorrectDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointWithInvalidIndex() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        simplex.getPoint(5);
    }

}