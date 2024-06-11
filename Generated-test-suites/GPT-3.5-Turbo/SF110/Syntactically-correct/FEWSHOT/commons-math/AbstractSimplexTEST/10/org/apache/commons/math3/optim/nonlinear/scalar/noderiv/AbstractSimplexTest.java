import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.junit.Test;

import java.util.Comparator;

public class AbstractSimplexTest {

    private class TestSimplex extends AbstractSimplex {

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
            // Implementation for testing purpose
        }
    }

    @Test
    public void testConstructorWithN() {
        int n = 5;
        TestSimplex simplex = new TestSimplex(n);

        assertEquals(n, simplex.getDimension());
        assertEquals(n+1, simplex.getSize());
        assertNotNull(simplex.getPoint(0));
    }

    @Test
    public void testConstructorWithNAndSideLength() {
        int n = 5;
        double sideLength = 2.0;
        TestSimplex simplex = new TestSimplex(n, sideLength);

        assertEquals(n, simplex.getDimension());
        assertEquals(n+1, simplex.getSize());
        assertNotNull(simplex.getPoint(0));
    }

    @Test
    public void testConstructorWithSteps() {
        double[] steps = {1.0, 2.0, 3.0, 4.0, 5.0};
        TestSimplex simplex = new TestSimplex(steps);

        assertEquals(steps.length, simplex.getDimension());
        assertEquals(steps.length + 1, simplex.getSize());
        assertNotNull(simplex.getPoint(0));
    }

    @Test
    public void testConstructorWithReferenceSimplex() {
        double[][] referenceSimplex = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        TestSimplex simplex = new TestSimplex(referenceSimplex);

        assertEquals(referenceSimplex.length - 1, simplex.getDimension());
        assertEquals(referenceSimplex.length, simplex.getSize());
        assertNotNull(simplex.getPoint(0));
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullSteps() {
        TestSimplex simplex = new TestSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testConstructorWithZeroSteps() {
        double[] steps = {};
        TestSimplex simplex = new TestSimplex(steps);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNoReferencePoint() {
        double[][] referenceSimplex = {};
        TestSimplex simplex = new TestSimplex(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithMismatchedDimensions() {
        double[][] referenceSimplex = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0},
                {7.0, 8.0, 9.0}
        };
        TestSimplex simplex = new TestSimplex(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildWithMismatchedDimensions() {
        double[] startPoint = {1.0, 2.0, 3.0};
        TestSimplex simplex = new TestSimplex(3);
        simplex.build(startPoint);
    }

    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 2.0, 3.0};
        TestSimplex simplex = new TestSimplex(3);
        simplex.build(startPoint);

        assertEquals(4, simplex.getSize());
        assertNotNull(simplex.getPoint(0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        TestSimplex simplex = new TestSimplex(3);
        simplex.getPoint(5);
    }

    @Test
    public void testSetPoints() {
        TestSimplex simplex1 = new TestSimplex(3);
        TestSimplex simplex2 = new TestSimplex(3);

        double[] startPoint = {1.0, 2.0, 3.0};
        simplex1.build(startPoint);

        PointValuePair[] points = {new PointValuePair(startPoint, 0.0)};
        simplex2.setPoints(points);

        assertEquals(1, simplex2.getSize());
        assertEquals(startPoint, simplex2.getPoint(0).getPoint());
    }
}