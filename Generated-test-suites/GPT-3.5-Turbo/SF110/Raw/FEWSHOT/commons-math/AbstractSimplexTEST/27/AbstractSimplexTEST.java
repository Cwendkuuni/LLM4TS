
package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class AbstractSimplexTest {
    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        simplex = new AbstractSimplexMock(3);
    }

    @Test
    public void testGetDimension() {
        int dimension = simplex.getDimension();

        assertEquals(3, dimension);
    }

    @Test
    public void testGetSize() {
        int size = simplex.getSize();

        assertEquals(4, size);
    }

    @Test
    public void testBuild() {
        double[] startPoint = { 1.0, 2.0, 3.0 };

        simplex.build(startPoint);

        assertArrayEquals(startPoint, simplex.getPoint(0).getPointRef(), 0.0);
        assertNotEquals(Double.NaN, simplex.getPoint(0).getValue());
    }

    @Test
    public void testEvaluate() {
        AbstractSimplexMock comparator = new AbstractSimplexMock();
        for (int i = 0; i < simplex.getSize(); i++) {
            simplex.setPoint(i, new PointValuePair(new double[] { i + 1, i + 2, i + 3 }, Double.NaN));
        }

        simplex.evaluate((double[] point) -> point[0] * point[1], comparator);

        assertEquals(6.0, simplex.getPoint(0).getValue(), 0.0);
        assertEquals(8.0, simplex.getPoint(1).getValue(), 0.0);
        assertEquals(10.0, simplex.getPoint(2).getValue(), 0.0);
        assertEquals(12.0, simplex.getPoint(3).getValue(), 0.0);

        assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, simplex.getPoint(0).getPointRef(), 0.0);
        assertArrayEquals(new double[] { 2.0, 3.0, 4.0 }, simplex.getPoint(1).getPointRef(), 0.0);
        assertArrayEquals(new double[] { 3.0, 4.0, 5.0 }, simplex.getPoint(2).getPointRef(), 0.0);
        assertArrayEquals(new double[] { 4.0, 5.0, 6.0 }, simplex.getPoint(3).getPointRef(), 0.0);
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplexMock comparator = new AbstractSimplexMock();
        simplex.setPoint(0, new PointValuePair(new double[] { 1.0, 2.0, 3.0 }, 6.0));
        simplex.setPoint(1, new PointValuePair(new double[] { 2.0, 3.0, 4.0 }, 8.0));
        simplex.setPoint(2, new PointValuePair(new double[] { 3.0, 4.0, 5.0 }, 10.0));
        simplex.setPoint(3, new PointValuePair(new double[] { 4.0, 5.0, 6.0 }, 12.0));

        PointValuePair newPoint = new PointValuePair(new double[] { 0.0, 1.0, 2.0 }, 4.0);
        simplex.replaceWorstPoint(newPoint, comparator);

        assertEquals(newPoint, simplex.getPoint(0));
        assertNotEquals(newPoint, simplex.getPoint(1));
        assertNotEquals(newPoint, simplex.getPoint(2));
        assertNotEquals(newPoint, simplex.getPoint(3));
    }

    @Test
    public void testGetPoints() {
        PointValuePair[] points = { new PointValuePair(new double[] { 1.0, 2.0, 3.0 }, Double.NaN),
                new PointValuePair(new double[] { 2.0, 3.0, 4.0 }, Double.NaN),
                new PointValuePair(new double[] { 3.0, 4.0, 5.0 }, Double.NaN),
                new PointValuePair(new double[] { 4.0, 5.0, 6.0 }, Double.NaN) };

        simplex.setPoints(points);
        PointValuePair[] result = simplex.getPoints();

        assertArrayEquals(points, result);
    }

    @Test
    public void testGetPoint() {
        PointValuePair point = new PointValuePair(new double[] { 1.0, 2.0, 3.0 }, Double.NaN);
        simplex.setPoint(0, point);

        PointValuePair result = simplex.getPoint(0);

        assertEquals(point, result);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        simplex.getPoint(4);
    }

    @Test
    public void testSetPoint() {
        PointValuePair point = new PointValuePair(new double[] { 1.0, 2.0, 3.0 }, Double.NaN);
        simplex.setPoint(0, point);

        PointValuePair result = simplex.getPoint(0);

        assertEquals(point, result);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        simplex.setPoint(4, new PointValuePair(new double[] { 0.0, 1.0, 2.0 }, 4.0));
    }

    @Test
    public void testSetPoints() {
        PointValuePair[] points = { new PointValuePair(new double[] { 1.0, 2.0, 3.0 }, Double.NaN),
                new PointValuePair(new double[] { 2.0, 3.0, 4.0 }, Double.NaN),
                new PointValuePair(new double[] { 3.0, 4.0, 5.0 }, Double.NaN),
                new PointValuePair(new double[] { 4.0, 5.0, 6.0 }, Double.NaN) };

        simplex.setPoints(points);
        PointValuePair[] result = simplex.getPoints();

        assertArrayEquals(points, result);
    }
}

class AbstractSimplexMock extends AbstractSimplex {
    public AbstractSimplexMock() {
        super(3);
    }

    public AbstractSimplexMock(int n) {
        super(n);
    }

    @Override
    public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
    }
}
