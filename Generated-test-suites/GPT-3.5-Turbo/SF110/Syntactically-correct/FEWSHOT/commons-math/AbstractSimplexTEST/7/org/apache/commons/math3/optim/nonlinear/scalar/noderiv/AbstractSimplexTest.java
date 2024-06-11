package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.*;
import org.apache.commons.math3.util.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.util.Arrays;

public class AbstractSimplexTest {
    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        simplex = new DummySimplex(3);
    }

    @After
    public void tearDown() {
        simplex = null;
    }

    @Test
    public void testConstructor() {
        AbstractSimplex simplex = new DummySimplex(3);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());

        AbstractSimplex simplex2 = new DummySimplex(5, 2.0);
        assertEquals(5, simplex2.getDimension());
        assertEquals(6, simplex2.getSize());

        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex3 = new DummySimplex(steps);
        assertEquals(3, simplex3.getDimension());
        assertEquals(4, simplex3.getSize());

        double[][] referenceSimplex = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        AbstractSimplex simplex4 = new DummySimplex(referenceSimplex);
        assertEquals(2, simplex4.getDimension());
        assertEquals(3, simplex4.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorNullSteps() {
        AbstractSimplex simplex = new DummySimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testConstructorEmptySteps() {
        double[] steps = {};
        AbstractSimplex simplex = new DummySimplex(steps);
    }

    @Test(expected = ZeroException.class)
    public void testConstructorZeroSteps() {
        double[] steps = {0.0, 0.0, 0.0};
        AbstractSimplex simplex = new DummySimplex(steps);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidReferenceSimplex() {
        double[][] referenceSimplex = {};
        AbstractSimplex simplex = new DummySimplex(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDimensionMismatch() {
        double[][] referenceSimplex = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0}
        };
        AbstractSimplex simplex = new DummySimplex(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorEqualVertices() {
        double[][] referenceSimplex = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {1.0, 2.0, 3.0}
        };
        AbstractSimplex simplex = new DummySimplex(referenceSimplex);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        PointValuePair point = simplex.getPoint(4);
    }

    @Test
    public void testIterate() {
        DummyMultivariateFunction function = new DummyMultivariateFunction();
        DummyComparator comparator = new DummyComparator();
        simplex.iterate(function, comparator);
        assertTrue(function.isIterateCalled());
        assertTrue(comparator.isIterateCalled());
    }

    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        assertEquals(startPoint, simplex.getPoint(0).getPoint());
        double[] expected = {2.0, 3.0, 4.0};
        assertArrayEquals(expected, simplex.getPoint(1).getPoint(), 0.0);
        expected = new double[] {3.0, 4.0, 5.0};
        assertArrayEquals(expected, simplex.getPoint(2).getPoint(), 0.0);
        expected = new double[] {4.0, 5.0, 6.0};
        assertArrayEquals(expected, simplex.getPoint(3).getPoint(), 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildDimensionMismatch() {
        double[] startPoint = {1.0, 2.0, 3.0, 4.0};
        simplex.build(startPoint);
    }

    @Test
    public void testEvaluate() {
        DummyMultivariateFunction function = new DummyMultivariateFunction();
        DummyComparator comparator = new DummyComparator();
        simplex.build(new double[]{1.0, 2.0, 3.0});
        simplex.evaluate(function, comparator);

        assertTrue(function.isEvaluateCalled());
        assertTrue(comparator.isEvaluateCalled());
        assertTrue(comparator.isSortCalled());
    }

    @Test
    public void testReplaceWorstPoint() {
        PointValuePair pointValuePair = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 1.0);
        DummyComparator comparator = new DummyComparator();
        simplex.replaceWorstPoint(pointValuePair, comparator);

        assertEquals(pointValuePair, simplex.getPoint(0));
    }

    @Test
    public void testGetPoints() {
        simplex.build(new double[]{1.0, 2.0, 3.0});
        PointValuePair[] points = simplex.getPoints();

        assertEquals(4, points.length);
        assertEquals(new double[]{1.0, 2.0, 3.0}, points[0].getPoint());
        assertEquals(new double[]{2.0, 3.0, 4.0}, points[1].getPoint());
        assertEquals(new double[]{3.0, 4.0, 5.0}, points[2].getPoint());
        assertEquals(new double[]{4.0, 5.0, 6.0}, points[3].getPoint());
    }

    @Test
    public void testSetPoint() {
        PointValuePair pointValuePair = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 1.0);
        simplex.setPoint(0, pointValuePair);

        assertEquals(pointValuePair, simplex.getPoint(0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        PointValuePair pointValuePair = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 1.0);
        simplex.setPoint(4, pointValuePair);
    }

    @Test
    public void testSetPoints() {
        PointValuePair[] points = {
            new PointValuePair(new double[]{4.0, 5.0, 6.0}, 1.0),
            new PointValuePair(new double[]{7.0, 8.0, 9.0}, 2.0),
            new PointValuePair(new double[]{10.0, 11.0, 12.0}, 3.0),
            new PointValuePair(new double[]{13.0, 14.0, 15.0}, 4.0)
        };

        simplex.setPoints(points);
        assertSame(points, simplex.getPoints());
    }
}

class DummySimplex extends AbstractSimplex {
    public DummySimplex(int n) {
        super(n);
    }

    public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

    }
}

class DummyMultivariateFunction implements MultivariateFunction {
    private boolean iterateCalled;
    private boolean evaluateCalled;

    public double value(double[] doubles) {
        return 0;
    }

    public void setIterateCalled(boolean iterateCalled) {
        this.iterateCalled = iterateCalled;
    }

    public boolean isIterateCalled() {
        return iterateCalled;
    }

    public void setEvaluateCalled(boolean evaluateCalled) {
        this.evaluateCalled = evaluateCalled;
    }

    public boolean isEvaluateCalled() {
        return evaluateCalled;
    }
}

class DummyComparator implements Comparator<PointValuePair> {
    private boolean iterateCalled;
    private boolean evaluateCalled;
    private boolean sortCalled;

    public int compare(PointValuePair o1, PointValuePair o2) {
        return 0;
    }

    public void setIterateCalled(boolean iterateCalled) {
        this.iterateCalled = iterateCalled;
    }

    public boolean isIterateCalled() {
        return iterateCalled;
    }

    public void setEvaluateCalled(boolean evaluateCalled) {
        this.evaluateCalled = evaluateCalled;
    }

    public boolean isEvaluateCalled() {
        return evaluateCalled;
    }

    public void setSortCalled(boolean sortCalled) {
        this.sortCalled = sortCalled;
    }

    public boolean isSortCalled() {
        return sortCalled;
    }
}