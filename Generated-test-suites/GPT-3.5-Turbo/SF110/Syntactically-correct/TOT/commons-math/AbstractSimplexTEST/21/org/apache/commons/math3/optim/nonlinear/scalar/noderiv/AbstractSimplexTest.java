import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.analysis.MultivariateFunction;
import java.util.Comparator;

public class AbstractSimplexTest {

    // Test case for the constructor AbstractSimplex(int n)
    @Test
    public void testConstructor_int_N() {
        AbstractSimplex simplex = new AbstractSimplex(3);

        assertNotNull(simplex);
        assertEquals(3, simplex.getDimension());
    }

    // Test case for the constructor AbstractSimplex(int n, double sideLength)
    @Test
    public void testConstructor_int_N_double_SideLength() {
        AbstractSimplex simplex = new AbstractSimplex(3, 2.0);

        assertNotNull(simplex);
        assertEquals(3, simplex.getDimension());
    }

    // Test case for the constructor AbstractSimplex(double[] steps)
    @Test
    public void testConstructor_double_Steps() {
        double[] steps = { 1.0, 2.0, 3.0 };
        AbstractSimplex simplex = new AbstractSimplex(steps);

        assertNotNull(simplex);
        assertEquals(3, simplex.getDimension());
    }

    // Test case for the constructor AbstractSimplex(double[][] referenceSimplex)
    @Test
    public void testConstructor_double_ReferenceSimplex() {
        double[][] referenceSimplex = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);

        assertNotNull(simplex);
        assertEquals(2, simplex.getDimension());
    }

    // Test case for getDimension()
    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);

        assertEquals(3, simplex.getDimension());
    }

    // Test case for getSize()
    @Test
    public void testGetSize() {
        double[][] referenceSimplex = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);

        assertEquals(3, simplex.getSize());
    }

    // Test case for iterate(MultivariateFunction, Comparator<PointValuePair>)
    @Test
    public void testIterate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };

        // Comparator to order PointValuePair objects in descending order based on value
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p2.getValue(), p1.getValue());
            }
        };

        simplex.iterate(function, comparator);
        PointValuePair[] points = simplex.getPoints();

        assertNotNull(points);
        assertEquals(4, points.length);
    }

    // Test case for build(double[] startPoint)
    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = { 1.0, 2.0, 3.0 };
        simplex.build(startPoint);

        PointValuePair[] points = simplex.getPoints();

        assertNotNull(points);
        assertEquals(4, points.length);
    }

    // Test case for evaluate(MultivariateFunction, Comparator<PointValuePair>)
    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };

        // Comparator to order PointValuePair objects in descending order based on value
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p2.getValue(), p1.getValue());
            }
        };

        simplex.build(new double[] { 1.0, 2.0, 3.0 });
        simplex.evaluate(function, comparator);

        PointValuePair[] points = simplex.getPoints();

        assertNotNull(points);
        assertEquals(4, points.length);
    }

    // Test case for replaceWorstPoint(PointValuePair, Comparator<PointValuePair>)
    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair pv1 = new PointValuePair(new double[] { 1.0, 2.0, 3.0 }, 6.0);
        PointValuePair pv2 = new PointValuePair(new double[] { 4.0, 5.0, 6.0 }, 15.0);
        simplex.setPoints(new PointValuePair[] { pv1, pv2, pv2, pv2 });

        PointValuePair newPoint = new PointValuePair(new double[] { 7.0, 8.0, 9.0 }, 24.0);

        // Comparator to order PointValuePair objects in ascending order based on value
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        simplex.replaceWorstPoint(newPoint, comparator);

        PointValuePair[] points = simplex.getPoints();

        assertNotNull(points);
        assertEquals(4, points.length);
    }

    // Test case for getPoints()
    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair pv1 = new PointValuePair(new double[] { 1.0, 2.0, 3.0 }, 6.0);
        PointValuePair pv2 = new PointValuePair(new double[] { 4.0, 5.0, 6.0 }, 15.0);
        simplex.setPoints(new PointValuePair[] { pv1, pv2, pv2, pv2 });

        PointValuePair[] points = simplex.getPoints();

        assertNotNull(points);
        assertEquals(4, points.length);
    }

    // Test case for getPoint(int index)
    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair pv1 = new PointValuePair(new double[] { 1.0, 2.0, 3.0 }, 6.0);
        PointValuePair pv2 = new PointValuePair(new double[] { 4.0, 5.0, 6.0 }, 15.0);
        simplex.setPoints(new PointValuePair[] { pv1, pv2, pv2, pv2 });

        PointValuePair point = simplex.getPoint(2);

        assertNotNull(point);
        assertEquals(pv2, point);
    }

    // Test case for setPoint(int index, PointValuePair point)
    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair pv1 = new PointValuePair(new double[] { 1.0, 2.0, 3.0 }, 6.0);
        PointValuePair pv2 = new PointValuePair(new double[] { 4.0, 5.0, 6.0 }, 15.0);
        simplex.setPoints(new PointValuePair[] { pv1, pv2, pv2, pv2 });

        PointValuePair newPoint = new PointValuePair(new double[] { 7.0, 8.0, 9.0 }, 24.0);
        simplex.setPoint(1, newPoint);

        PointValuePair[] points = simplex.getPoints();

        assertNotNull(points);
        assertEquals(4, points.length);
        assertEquals(newPoint, points[1]);
    }

    // Test case for setPoints(PointValuePair[] points)
    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair pv1 = new PointValuePair(new double[] { 1.0, 2.0, 3.0 }, 6.0);
        PointValuePair pv2 = new PointValuePair(new double[] { 4.0, 5.0, 6.0 }, 15.0);
        simplex.setPoints(new PointValuePair[] { pv1, pv2, pv2, pv2 });

        PointValuePair[] newPoints = { pv2, pv2, pv2, pv2 };
        simplex.setPoints(newPoints);

        PointValuePair[] points = simplex.getPoints();

        assertNotNull(points);
        assertEquals(4, points.length);
        assertArrayEquals(newPoints, points);
    }

    // Test case for createHypercubeSteps(int n, double sideLength)
    @Test
    public void testCreateHypercubeSteps() {
        double[] expectedSteps = { 2.0, 2.0, 2.0 };
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 2.0);

        assertNotNull(steps);
        assertEquals(3, steps.length);
        assertArrayEquals(expectedSteps, steps, 0.0);
    }
}