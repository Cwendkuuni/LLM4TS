package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplex_NullSteps_ExceptionThrown() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_EmptySteps_ExceptionThrown() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{});
    }

    @Test
    public void testAbstractSimplex_Steps() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, abstractSimplex.getDimension());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplex_ReferenceSimplex_InvalidSize_ExceptionThrown() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_ReferenceSimplex_InvalidDimension_ExceptionThrown() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0}});
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplex_ReferenceSimplex_EqualVertices_ExceptionThrown() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[][]{{1.0, 2.0}, {1.0, 2.0}});
    }

    @Test
    public void testAbstractSimplex_ReferenceSimplex_ValidInput() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[][]{{0.0, 0.0}, {1.0, 2.0}});
        assertEquals(1, abstractSimplex.getDimension());
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{1.0, 2.0});
        assertEquals(2, abstractSimplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{1.0, 2.0});
        assertEquals(3, abstractSimplex.getSize());
    }

    @Test
    public void testBuild_DimensionMismatchException() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        double[] startPoint = {1.0, 2.0};
        try {
            abstractSimplex.build(startPoint);
            fail("Expected DimensionMismatchException to be thrown");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testBuild_ValidInput() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{1.0, 2.0});
        double[] startPoint = {1.0, 2.0};
        abstractSimplex.build(startPoint);
        assertEquals(startPoint, abstractSimplex.getPoint(0).getPointRef());
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{1.0, 2.0});
        Comparator<PointValuePair> comparator = null;
        abstractSimplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return 0;
            }
        }, comparator);
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{1.0, 2.0});
        Comparator<PointValuePair> comparator = null;
        PointValuePair pointValuePair = new PointValuePair(new double[]{1.0, 2.0}, 0);
        abstractSimplex.replaceWorstPoint(pointValuePair, comparator);
        assertEquals(pointValuePair, abstractSimplex.getPoint(0));
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{1.0, 2.0});
        PointValuePair[] points = {new PointValuePair(new double[]{1.0, 2.0}, 0)};
        abstractSimplex.setPoints(points);
        assertArrayEquals(points, abstractSimplex.getPoints());
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{1.0, 2.0});
        PointValuePair pointValuePair = new PointValuePair(new double[]{1.0, 2.0}, 0);
        abstractSimplex.setPoint(0, pointValuePair);
        assertEquals(pointValuePair, abstractSimplex.getPoint(0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_IndexOutOfRange_ExceptionThrown() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{1.0, 2.0});
        abstractSimplex.getPoint(3);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_IndexOutOfRange_ExceptionThrown() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{1.0, 2.0});
        PointValuePair pointValuePair = new PointValuePair(new double[]{1.0, 2.0}, 0);
        abstractSimplex.setPoint(3, pointValuePair);
    }

    @Test
    public void testSetPoints_DimensionMismatchException() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{1.0, 2.0});
        PointValuePair[] points = {new PointValuePair(new double[]{1.0, 2.0}, 0)};
        try {
            abstractSimplex.setPoints(new PointValuePair[]{});
            fail("Expected DimensionMismatchException to be thrown");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

}