package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class AbstractSimplexTest {
    private AbstractSimplex simplex;
    private double[] startPoint;

    @Before
    public void setUp() {
        startPoint = new double[]{1.0, 2.0, 3.0};
        simplex = new AbstractSimplexImpl(startPoint);
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
    public void testBuild() {
        simplex.build(startPoint);

        PointValuePair[] expectedPoints = new PointValuePair[]{
            new PointValuePair(startPoint, Double.NaN),
            new PointValuePair(new double[]{2.0, 3.0, 4.0}, Double.NaN),
            new PointValuePair(new double[]{3.0, 4.0, 5.0}, Double.NaN),
            new PointValuePair(new double[]{4.0, 5.0, 6.0}, Double.NaN)
        };
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test
    public void testEvaluate() {
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };

        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        simplex.build(startPoint);
        simplex.evaluate(evaluationFunction, comparator);

        PointValuePair[] expectedPoints = new PointValuePair[]{
            new PointValuePair(new double[]{2.0, 3.0, 4.0}, 9.0, false),
            new PointValuePair(new double[]{3.0, 4.0, 5.0}, 12.0, false),
            new PointValuePair(new double[]{1.0, 2.0, 3.0}, 6.0, false),
            new PointValuePair(new double[]{4.0, 5.0, 6.0}, 15.0, false)
        };
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test
    public void testReplaceWorstPoint() {
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0, false);
        simplex.replaceWorstPoint(newPoint, new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });

        PointValuePair[] expectedPoints = new PointValuePair[]{
            new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0, false),
            new PointValuePair(new double[]{2.0, 3.0, 4.0}, Double.NaN),
            new PointValuePair(new double[]{3.0, 4.0, 5.0}, Double.NaN),
            new PointValuePair(new double[]{4.0, 5.0, 6.0}, Double.NaN)
        };
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test
    public void testGetPoints() {
        simplex.build(startPoint);

        PointValuePair[] expectedPoints = new PointValuePair[]{
            new PointValuePair(startPoint, Double.NaN),
            new PointValuePair(new double[]{2.0, 3.0, 4.0}, Double.NaN),
            new PointValuePair(new double[]{3.0, 4.0, 5.0}, Double.NaN),
            new PointValuePair(new double[]{4.0, 5.0, 6.0}, Double.NaN)
        };
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test
    public void testGetPoint() {
        simplex.build(startPoint);

        PointValuePair expectedPoint = new PointValuePair(startPoint, Double.NaN);
        assertEquals(expectedPoint, simplex.getPoint(0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRangeNegative() {
        simplex.build(startPoint);
        simplex.getPoint(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRangePositive() {
        simplex.build(startPoint);
        simplex.getPoint(10);
    }

    @Test
    public void testSetPoint() {
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0, false);
        simplex.setPoint(0, newPoint);

        assertEquals(newPoint, simplex.getPoint(0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRangeNegative() {
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0, false);
        simplex.setPoint(-1, newPoint);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRangePositive() {
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0, false);
        simplex.setPoint(10, newPoint);
    }

    @Test
    public void testSetPoints() {
        simplex.build(startPoint);

        PointValuePair[] newPoints = new PointValuePair[]{
            new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0, false),
            new PointValuePair(new double[]{1.0, 1.0, 1.0}, 1.0, false),
            new PointValuePair(new double[]{2.0, 2.0, 2.0}, 2.0, false),
            new PointValuePair(new double[]{3.0, 3.0, 3.0}, 3.0, false)
        };
        simplex.setPoints(newPoints);

        assertArrayEquals(newPoints, simplex.getPoints());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsDimensionMismatch() {
        simplex.build(startPoint);

        PointValuePair[] newPoints = new PointValuePair[]{
            new PointValuePair(new double[]{0.0, 0.0}, 0.0, false),
            new PointValuePair(new double[]{1.0, 1.0}, 1.0, false),
            new PointValuePair(new double[]{2.0, 2.0}, 2.0, false),
            new PointValuePair(new double[]{3.0, 3.0}, 3.0, false)
        };
        simplex.setPoints(newPoints);
    }

    private class AbstractSimplexImpl extends AbstractSimplex {
        public AbstractSimplexImpl(double[] steps) {
            super(steps);
        }

        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // Empty implementation for testing
        }
    }
}