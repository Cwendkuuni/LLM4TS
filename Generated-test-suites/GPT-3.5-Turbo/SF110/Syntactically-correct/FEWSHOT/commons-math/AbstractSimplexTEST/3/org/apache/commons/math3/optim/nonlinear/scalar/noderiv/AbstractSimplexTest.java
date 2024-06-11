package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.util.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Pair;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class AbstractSimplexTest {

    @Test
    public void testConstructor_singleParameter() {
        int n = 4;
        AbstractSimplex simplex = new AbstractSimplexImpl(n);
        assertEquals(n, simplex.getDimension());
        assertEquals(n+1, simplex.getSize());
    }

    @Test
    public void testConstructor_doubleParameter() {
        int n = 4;
        double sideLength = 2.5;
        AbstractSimplex simplex = new AbstractSimplexImpl(n, sideLength);
        assertEquals(n, simplex.getDimension());
        assertEquals(n+1, simplex.getSize());
    }

    @Test
    public void testConstructor_doubleArrayParameter() {
        double[] steps = {2.0, 1.5, 2.5};
        AbstractSimplex simplex = new AbstractSimplexImpl(steps);
        assertEquals(steps.length, simplex.getDimension());
        assertEquals(steps.length+1, simplex.getSize());
    }

    @Test(expected=NullArgumentException.class)
    public void testConstructor_nullParameter() {
        double[] steps = null;
        AbstractSimplex simplex = new AbstractSimplexImpl(steps);
    }

    @Test(expected=ZeroException.class)
    public void testConstructor_emptyParameter() {
        double[] steps = {};
        AbstractSimplex simplex = new AbstractSimplexImpl(steps);
    }

    @Test
    public void testConstructor_doubleArray2DParameter() {
        double[][] referenceSimplex = {{2.0, 3.0, 4.0},
                                        {3.0, 4.0, 5.0},
                                        {4.0, 5.0, 6.0},
                                        {5.0, 6.0, 7.0}};
        AbstractSimplex simplex = new AbstractSimplexImpl(referenceSimplex);
        assertEquals(referenceSimplex.length-1, simplex.getDimension());
        assertEquals(referenceSimplex.length, simplex.getSize());
    }

    @Test
    public void testConstructor_doubleArray2DParameter_notStrictlyPositiveDimension() {
        double[][] referenceSimplex = new double[0][];
        boolean exceptionThrown = false;
        try {
            AbstractSimplex simplex = new AbstractSimplexImpl(referenceSimplex);
        } catch (NotStrictlyPositiveException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testConstructor_doubleArray2DParameter_dimensionMismatch() {
        double[][] referenceSimplex = {{2.0, 3.0, 4.0},
                                        {3.0, 4.0, 5.0},
                                        {4.0, 5.0, 6.0}};
        boolean exceptionThrown = false;
        try {
            AbstractSimplex simplex = new AbstractSimplexImpl(referenceSimplex);
        } catch (DimensionMismatchException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testIterate() {
        MultivariateFunction func = new MultivariateFunction() {
            public double value(double[] point) {
                return Arrays.stream(point).sum();
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        AbstractSimplex simplex = new AbstractSimplexImpl(2);
        simplex.build(new double[]{1.0, 2.5});
        simplex.iterate(func, comparator);

        PointValuePair[] expectedPoints = {
            new PointValuePair(new double[]{1.0, 2.5}, 0.0),
            new PointValuePair(new double[]{1.5, 3.0}, 0.0),
            new PointValuePair(new double[]{1.5, 3.0}, 0.5)
        };
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 2.5};
        AbstractSimplex simplex = new AbstractSimplexImpl(2);
        simplex.build(startPoint);

        PointValuePair[] expectedPoints = {
            new PointValuePair(startPoint, Double.NaN),
            new PointValuePair(new double[]{2.0, 4.0}, Double.NaN),
            new PointValuePair(new double[]{3.0, 5.5}, Double.NaN)
        };
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test(expected=DimensionMismatchException.class)
    public void testBuild_dimensionMismatch() {
        double[] startPoint = {1.0, 2.5, 3.0};
        AbstractSimplex simplex = new AbstractSimplexImpl(2);
        simplex.build(startPoint);
    }

    @Test
    public void testEvaluate() {
        MultivariateFunction func = new MultivariateFunction() {
            public double value(double[] point) {
                return Arrays.stream(point).sum();
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        AbstractSimplex simplex = new AbstractSimplexImpl(2);
        simplex.build(new double[]{1.0, 2.5});
        simplex.evaluate(func, comparator);

        PointValuePair[] expectedPoints = {
            new PointValuePair(new double[]{1.0, 2.5}, 3.5),
            new PointValuePair(new double[]{2.0, 5.0}, 7.0),
            new PointValuePair(new double[]{3.0, 7.5}, 10.5)
        };
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test
    public void testReplaceWorstPoint() {
        PointValuePair pointValuePair = new PointValuePair(new double[]{1.5, 3.0}, 0.5);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        AbstractSimplex simplex = new AbstractSimplexImpl(2);
        simplex.build(new double[]{1.0, 2.5});
        simplex.replaceWorstPoint(pointValuePair, comparator);

        PointValuePair[] expectedPoints = {
            new PointValuePair(new double[]{1.0, 2.5}, 0.0),
            new PointValuePair(new double[]{1.5, 3.0}, 0.5),
            new PointValuePair(new double[]{1.5, 3.0}, 0.5)
        };
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test(expected=OutOfRangeException.class)
    public void testGetPoint_outOfRange() {
        AbstractSimplex simplex = new AbstractSimplexImpl(2);
        simplex.getPoint(3);
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplexImpl(2);
        PointValuePair[] expectedPoints = {
            new PointValuePair(new double[]{1.0, 2.5}, 0.0),
            new PointValuePair(new double[]{2.0, 3.0}, 0.0),
            new PointValuePair(new double[]{3.0, 3.5}, 0.0)
        };
        simplex.setPoints(expectedPoints);

        PointValuePair[] actualPoints = simplex.getPoints();
        assertArrayEquals(expectedPoints, actualPoints);
    }

    @Test(expected=DimensionMismatchException.class)
    public void testSetPoints_dimensionMismatch() {
        AbstractSimplex simplex = new AbstractSimplexImpl(2);
        PointValuePair[] points = {
            new PointValuePair(new double[]{1.0, 2.5}, 0.0),
            new PointValuePair(new double[]{2.0, 3.0, 4.0}, 0.0),
            new PointValuePair(new double[]{3.0, 3.5}, 0.0)
        };
        simplex.setPoints(points);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplexImpl(2);
        simplex.build(new double[]{1.0, 2.5});
        PointValuePair newPoint = new PointValuePair(new double[]{2.0, 3.0}, 0.0);
        simplex.setPoint(1, newPoint);

        PointValuePair[] expectedPoints = {
            new PointValuePair(new double[]{1.0, 2.5}, 0.0),
            newPoint,
            new PointValuePair(new double[]{3.0, 3.5}, 0.0)
        };
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

}