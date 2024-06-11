package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class AbstractSimplexTest {
    private class AbstractSimplexImpl extends AbstractSimplex {
        public AbstractSimplexImpl(int n) {
            super(n);
        }
        
        public AbstractSimplexImpl(int n, double sideLength) {
            super(n, sideLength);
        }
        
        public AbstractSimplexImpl(double[] steps) {
            super(steps);
        }
        
        public AbstractSimplexImpl(double[][] referenceSimplex) {
            super(referenceSimplex);
        }
        
        public void iterate(final MultivariateFunction p0, final Comparator<PointValuePair> p1) {
            // implementation for testing purposes
        }
    }

    @Test
    public void testConstructor() {
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(3);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());

        simplex = new AbstractSimplexImpl(3, 2.0);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());

        double[] steps = {1.0, 2.0, 3.0};
        simplex = new AbstractSimplexImpl(steps);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());

        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        simplex = new AbstractSimplexImpl(referenceSimplex);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructor_NullSteps() {
        double[] steps = null;
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(steps);
    }

    @Test(expected = ZeroException.class)
    public void testConstructor_EmptySteps() {
        double[] steps = {};
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(steps);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_EmptyReferenceSimplex() {
        double[][] referenceSimplex = {};
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_DimensionMismatch() {
        double[][] referenceSimplex = {{1.0, 2.0}, {3.0, 4.0}};
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructor_EqualVertices() {
        double[][] referenceSimplex = {{1.0, 2.0}, {1.0, 2.0}, {3.0, 4.0}};
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(referenceSimplex);
    }

    @Test
    public void testGetDimension() {
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(3);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(3);
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testBuild() {
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(startPoint, points[0].getPointRef());
        assertEquals(4, points.length);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuild_DimensionMismatch() {
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(3);
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
    }

    @Test
    public void testEvaluate() {
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };

        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                if (p1.getValue() < p2.getValue()) {
                    return -1;
                }
                if (p1.getValue() > p2.getValue()) {
                    return 1;
                }
                return 0;
            }
        };

        simplex.evaluate(evaluationFunction, comparator);

        PointValuePair[] expectedPoints = {new PointValuePair(startPoint, 6.0, false),
                                           new PointValuePair(startPoint, 6.0, false),
                                           new PointValuePair(startPoint, 6.0, false),
                                           new PointValuePair(startPoint, 6.0, false)};
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[]{2.0, 3.0, 4.0}, 9.0, false);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                if (p1.getValue() < p2.getValue()) {
                    return -1;
                }
                if (p1.getValue() > p2.getValue()) {
                    return 1;
                }
                return 0;
            }
        };

        simplex.replaceWorstPoint(newPoint, comparator);

        PointValuePair[] expectedPoints = {new PointValuePair(new double[]{1.0, 2.0, 3.0}, 6.0, false),
                                           new PointValuePair(new double[]{1.0, 2.0, 3.0}, 6.0, false),
                                           new PointValuePair(new double[]{1.0, 2.0, 3.0}, 6.0, false),
                                           newPoint};
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_OutOfRange() {
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        simplex.getPoint(4);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair point = simplex.getPoint(2);

        assertEquals(startPoint, point.getPointRef());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_OutOfRange() {
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[]{2.0, 3.0, 4.0}, 9.0, false);

        simplex.setPoint(4, newPoint);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[]{2.0, 3.0, 4.0}, 9.0, false);

        simplex.setPoint(2, newPoint);

        PointValuePair[] expectedPoints = {new PointValuePair(new double[]{1.0, 2.0, 3.0}, 6.0, false),
                                           new PointValuePair(new double[]{1.0, 2.0, 3.0}, 6.0, false),
                                           newPoint,
                                           new PointValuePair(new double[]{1.0, 2.0, 3.0}, 6.0, false)};
        
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPoints_DimensionMismatch() {
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair[] points = {new PointValuePair(new double[]{1.0, 2.0, 3.0}, 6.0, false),
                                   new PointValuePair(new double[]{1.0, 2.0, 3.0}, 6.0, false),
                                   new PointValuePair(new double[]{1.0, 2.0, 3.0}, 6.0, false)};

        simplex.setPoints(points);
    }

    @Test
    public void testSetPoints() {
        AbstractSimplexImpl simplex = new AbstractSimplexImpl(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair[] points = {new PointValuePair(new double[]{2.0, 3.0, 4.0}, 9.0, false),
                                   new PointValuePair(new double[]{3.0, 4.0, 5.0}, 12.0, false),
                                   new PointValuePair(new double[]{4.0, 5.0, 6.0}, 15.0, false),
                                   new PointValuePair(new double[]{5.0, 6.0, 7.0}, 18.0, false)};

        simplex.setPoints(points);

        assertArrayEquals(points, simplex.getPoints());
    }
}