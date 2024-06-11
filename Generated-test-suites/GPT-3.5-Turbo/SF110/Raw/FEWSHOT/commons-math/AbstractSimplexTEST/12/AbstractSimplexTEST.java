package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;


public class AbstractSimplexTest {

    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        double[] steps = {1.0, 2.0, 3.0};
        simplex = new AbstractSimplexImpl(steps);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testEvaluate() {
        MultivariateFunction evaluationFunction = point -> point[0] * point[0] + point[1] * point[1] + point[2] * point[2];
        Comparator<PointValuePair> comparator = Comparator.comparingDouble(value -> value.getValue());
        simplex.evaluate(evaluationFunction, comparator);

        PointValuePair[] expectedPoints = {
                new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0),
                new PointValuePair(new double[]{1.0, 2.0, 3.0}, 14.0),
                new PointValuePair(new double[]{2.0, 4.0, 6.0}, 56.0),
                new PointValuePair(new double[]{3.0, 6.0, 9.0}, 135.0)
        };

        PointValuePair[] actualPoints = simplex.getPoints();

        assertNotNull(actualPoints);
        assertArrayEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testReplaceWorstPoint() {
        MultivariateFunction evaluationFunction = point -> point[0] * point[0] + point[1] * point[1] + point[2] * point[2];
        Comparator<PointValuePair> comparator = Comparator.comparingDouble(value -> value.getValue());
        simplex.evaluate(evaluationFunction, comparator);

        PointValuePair newPoint = new PointValuePair(new double[]{4.0, 8.0, 12.0}, 244.0);
        simplex.replaceWorstPoint(newPoint, comparator);

        PointValuePair[] expectedPoints = {
                new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0),
                new PointValuePair(new double[]{1.0, 2.0, 3.0}, 14.0),
                new PointValuePair(new double[]{2.0, 4.0, 6.0}, 56.0),
                newPoint
        };

        PointValuePair[] actualPoints = simplex.getPoints();

        assertNotNull(actualPoints);
        assertArrayEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testGetPoint() {
        PointValuePair expectedPoint = new PointValuePair(new double[]{1.0, 2.0, 3.0}, Double.NaN);
        PointValuePair actualPoint = simplex.getPoint(1);

        assertNotNull(actualPoint);
        assertEquals(expectedPoint, actualPoint);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetPointWithInvalidIndex() {
        simplex.getPoint(4);
    }

    @Test
    public void testSetPoint() {
        PointValuePair newPoint = new PointValuePair(new double[]{4.0, 8.0, 12.0}, 244.0);
        simplex.setPoint(1, newPoint);

        PointValuePair expectedPoint = newPoint;
        PointValuePair actualPoint = simplex.getPoint(1);

        assertNotNull(actualPoint);
        assertEquals(expectedPoint, actualPoint);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPointWithInvalidIndex() {
        PointValuePair newPoint = new PointValuePair(new double[]{4.0, 8.0, 12.0}, 244.0);
        simplex.setPoint(4, newPoint);
    }

    @Test
    public void testSetPoints() {
        PointValuePair[] newPoints = {
                new PointValuePair(new double[]{0.1, 0.2, 0.3}, 0.014),
                new PointValuePair(new double[]{0.2, 0.4, 0.6}, 0.056),
                new PointValuePair(new double[]{0.3, 0.6, 0.9}, 0.135),
                new PointValuePair(new double[]{0.4, 0.8, 1.2}, 0.244)
        };
        simplex.setPoints(newPoints);

        PointValuePair[] expectedPoints = newPoints;
        PointValuePair[] actualPoints = simplex.getPoints();

        assertNotNull(actualPoints);
        assertArrayEquals(expectedPoints, actualPoints);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsWithInvalidLength() {
        PointValuePair[] newPoints = {
                new PointValuePair(new double[]{0.1, 0.2, 0.3}, 0.014),
                new PointValuePair(new double[]{0.2, 0.4, 0.6}, 0.056),
                new PointValuePair(new double[]{0.3, 0.6, 0.9}, 0.135)
        };
        simplex.setPoints(newPoints);
    }

    private class AbstractSimplexImpl extends AbstractSimplex {

        public AbstractSimplexImpl(double[] steps) {
            super(steps);
        }

        public void iterate(final MultivariateFunction p0, final Comparator<PointValuePair> p1) {

        }
    }
}