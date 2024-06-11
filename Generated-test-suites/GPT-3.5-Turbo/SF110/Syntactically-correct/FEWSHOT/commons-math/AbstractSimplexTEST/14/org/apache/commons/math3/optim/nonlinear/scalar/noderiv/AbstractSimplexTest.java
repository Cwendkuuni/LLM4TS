
package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.junit.Before;
import org.junit.Test;

public class AbstractSimplexTest {

    private AbstractSimplex simplex;

    @Before
    public void setup() {
        simplex = new AbstractSimplexImpl(3);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildThrowsExceptionWithIncorrectStartPointDimension() {
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
    }

    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);

        assertEquals(startPoint[0], points[0].getPointRef()[0], 0.0);
        assertEquals(startPoint[1], points[0].getPointRef()[1], 0.0);
        assertEquals(startPoint[2], points[0].getPointRef()[2], 0.0);

        assertEquals(startPoint[0] + AbstractSimplexImpl.SIDE_LENGTH, points[1].getPointRef()[0], 0.0);
        assertEquals(startPoint[1], points[1].getPointRef()[1], 0.0);
        assertEquals(startPoint[2], points[1].getPointRef()[2], 0.0);

        assertEquals(startPoint[0], points[2].getPointRef()[0], 0.0);
        assertEquals(startPoint[1] + AbstractSimplexImpl.SIDE_LENGTH, points[2].getPointRef()[1], 0.0);
        assertEquals(startPoint[2], points[2].getPointRef()[2], 0.0);

        assertEquals(startPoint[0], points[3].getPointRef()[0], 0.0);
        assertEquals(startPoint[1], points[3].getPointRef()[1], 0.0);
        assertEquals(startPoint[2] + AbstractSimplexImpl.SIDE_LENGTH, points[3].getPointRef()[2], 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointThrowsExceptionWithNegativeIndex() {
        simplex.getPoint(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointThrowsExceptionWithIndexGreaterThanSize() {
        simplex.getPoint(4);
    }

    @Test
    public void testGetPoint() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair point = simplex.getPoint(2);
        assertEquals(startPoint[0], point.getPointRef()[0], 0.0);
        assertEquals(startPoint[1] + AbstractSimplexImpl.SIDE_LENGTH, point.getPointRef()[1], 0.0);
        assertEquals(startPoint[2], point.getPointRef()[2], 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointThrowsExceptionWithNegativeIndex() {
        simplex.setPoint(-1, new PointValuePair(null, 0.0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointThrowsExceptionWithIndexGreaterThanSize() {
        simplex.setPoint(4, new PointValuePair(null, 0.0));
    }

    @Test
    public void testSetPoint() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0);
        simplex.setPoint(2, newPoint);

        PointValuePair point = simplex.getPoint(2);
        assertEquals(0.0, point.getPointRef()[0], 0.0);
        assertEquals(0.0, point.getPointRef()[1], 0.0);
        assertEquals(0.0, point.getPointRef()[2], 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsThrowsExceptionWithIncorrectPointsDimension() {
        PointValuePair[] points = {new PointValuePair(new double[]{1.0, 2.0}, 0.0)};
        simplex.setPoints(points);
    }

    @Test
    public void testSetPoints() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair newPoint1 = new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0);
        PointValuePair newPoint2 = new PointValuePair(new double[]{0.0, 1.0, 0.0}, 0.0);
        PointValuePair[] points = {newPoint1, newPoint2};

        simplex.setPoints(points);

        PointValuePair[] simplexPoints = simplex.getPoints();
        assertEquals(2, simplexPoints.length);

        assertEquals(0.0, simplexPoints[0].getPointRef()[0], 0.0);
        assertEquals(0.0, simplexPoints[0].getPointRef()[1], 0.0);
        assertEquals(0.0, simplexPoints[0].getPointRef()[2], 0.0);

        assertEquals(0.0, simplexPoints[1].getPointRef()[0], 0.0);
        assertEquals(1.0, simplexPoints[1].getPointRef()[1], 0.0);
        assertEquals(0.0, simplexPoints[1].getPointRef()[2], 0.0);
    }

    private static class AbstractSimplexImpl extends AbstractSimplex {

        private static final double SIDE_LENGTH = 2.0;

        public AbstractSimplexImpl(int n) {
            super(n);
        }

        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // Do nothing
        }
    }
}
