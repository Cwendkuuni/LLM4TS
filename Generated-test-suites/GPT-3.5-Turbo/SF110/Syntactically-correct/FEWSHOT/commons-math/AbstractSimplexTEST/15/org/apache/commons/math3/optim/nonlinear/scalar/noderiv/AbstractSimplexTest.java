import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.NullArgumentException;

public class AbstractSimplexTest {
    @Test
    public void testAbstractSimplex1() {
        AbstractSimplex simplex = new AbstractSimplex(3) {
            @Override
            public void iterate(final MultivariateFunction p0, final Comparator<PointValuePair> p1) {
                // Empty implementation for testing
            }
        };
        
        assertEquals(3, simplex.getDimension());
        assertEquals(0, simplex.getSize());

        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        assertEquals(4, simplex.getSize());

        PointValuePair[] points = simplex.getPoints();
        for (int i = 0; i < points.length; i++) {
            double[] point = points[i].getPointRef();
            for (int j = 0; j < simplex.getDimension(); j++) {
                assertEquals(startPoint[j], point[j], 0.0);
            }
        }

        PointValuePair point0 = simplex.getPoint(0);
        assertArrayEquals(startPoint, point0.getPointRef(), 0.0);

        PointValuePair pointOutOfRange = null;
        try {
            pointOutOfRange = simplex.getPoint(10);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            assertNull(pointOutOfRange);
        }

        PointValuePair newPoint = new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0);
        simplex.setPoint(1, newPoint);
        assertArrayEquals(newPoint.getPointRef(), simplex.getPoint(1).getPointRef(), 0.0);

        PointValuePair[] newPoints = {newPoint, newPoint, newPoint, newPoint};
        simplex.setPoints(newPoints);
        assertArrayEquals(newPoints, simplex.getPoints());
    }

    @Test
    public void testAbstractSimplex2() {
        double[] steps = {2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(steps) {
            @Override
            public void iterate(final MultivariateFunction p0, final Comparator<PointValuePair> p1) {
                // Empty implementation for testing
            }
        };
        
        assertEquals(2, simplex.getDimension());
        assertEquals(0, simplex.getSize());

        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
        
        assertEquals(3, simplex.getSize());

        PointValuePair[] points = simplex.getPoints();
        for (int i = 0; i < points.length; i++) {
            double[] point = points[i].getPointRef();
            for (int j = 0; j < simplex.getDimension(); j++) {
                assertEquals(startPoint[j] + steps[j], point[j], 0.0);
            }
        }

        PointValuePair point0 = simplex.getPoint(0);
        assertArrayEquals(startPoint, point0.getPointRef(), 0.0);

        PointValuePair pointOutOfRange = null;
        try {
            pointOutOfRange = simplex.getPoint(10);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            assertNull(pointOutOfRange);
        }

        PointValuePair newPoint = new PointValuePair(new double[]{0.0, 0.0}, 0.0);
        simplex.setPoint(1, newPoint);
        assertArrayEquals(newPoint.getPointRef(), simplex.getPoint(1).getPointRef(), 0.0);

        PointValuePair[] newPoints = {newPoint, newPoint, newPoint};
        simplex.setPoints(newPoints);
        assertArrayEquals(newPoints, simplex.getPoints());
    }

    @Test
    public void testAbstractSimplex3() {
        double[][] referenceSimplex = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex) {
            @Override
            public void iterate(final MultivariateFunction p0, final Comparator<PointValuePair> p1) {
                // Empty implementation for testing
            }
        };
        
        assertEquals(2, simplex.getDimension());
        assertEquals(0, simplex.getSize());

        double[] startPoint = {0.0, 0.0};
        simplex.build(startPoint);
        
        assertEquals(3, simplex.getSize());

        PointValuePair[] points = simplex.getPoints();
        for (int i = 0; i < points.length; i++) {
            double[] point = points[i].getPointRef();
            for (int j = 0; j < simplex.getDimension(); j++) {
                assertEquals(startPoint[j] + referenceSimplex[i][j], point[j], 0.0);
            }
        }

        PointValuePair point0 = simplex.getPoint(0);
        assertArrayEquals(startPoint, point0.getPointRef(), 0.0);

        PointValuePair pointOutOfRange = null;
        try {
            pointOutOfRange = simplex.getPoint(10);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            assertNull(pointOutOfRange);
        }

        PointValuePair newPoint = new PointValuePair(new double[]{0.0, 0.0}, 0.0);
        simplex.setPoint(1, newPoint);
        assertArrayEquals(newPoint.getPointRef(), simplex.getPoint(1).getPointRef(), 0.0);

        PointValuePair[] newPoints = {newPoint, newPoint, newPoint};
        simplex.setPoints(newPoints);
        assertArrayEquals(newPoints, simplex.getPoints());
    }
}