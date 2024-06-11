package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.exception.util.NullArgumentException;
import org.apache.commons.math3.analysis.MultivariateFunction;
import java.util.Comparator;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Pair;
import java.util.Arrays;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        assertEquals(5, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexWithSideLength() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5, 2.0);
        assertEquals(5, simplex.getDimension());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexWithNullSteps() {
        AbstractSimplex simplex = new AbstractSimplexImpl(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexWithEmptySteps() {
        AbstractSimplex simplex = new AbstractSimplexImpl(new double[]{});
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexWithZeroSteps() {
        AbstractSimplex simplex = new AbstractSimplexImpl(new double[]{0.0, 0.0, 0.0});
    }

    @Test
    public void testAbstractSimplexWithReferenceSimplex() {
        double[][] referenceSimplex = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        AbstractSimplex simplex = new AbstractSimplexImpl(referenceSimplex);
        assertEquals(2, simplex.getDimension());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexWithEmptyReferenceSimplex() {
        double[][] referenceSimplex = {};
        AbstractSimplex simplex = new AbstractSimplexImpl(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexWithInvalidReferenceSimplex() {
        double[][] referenceSimplex = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0}
        };
        AbstractSimplex simplex = new AbstractSimplexImpl(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexWithEqualVertices() {
        double[][] referenceSimplex = {
            {1.0, 2.0, 3.0},
            {1.0, 2.0, 3.0}
        };
        AbstractSimplex simplex = new AbstractSimplexImpl(referenceSimplex);
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        assertEquals(5, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        assertEquals(6, simplex.getSize());
    }

    @Test
    public void testIterate() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 1.0;
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        };
        simplex.iterate(function, comparator);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildWithInvalidStartPoint() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        double[] startPoint = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(6, points.length);
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 1.0;
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        };
        simplex.evaluate(function, comparator);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointWithInvalidIndex() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        simplex.getPoint(6);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        PointValuePair point = simplex.getPoint(0);
        assertNotNull(point);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointWithInvalidIndex() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        PointValuePair point = new PointValuePair(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, 1.0);
        simplex.setPoint(6, point);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        PointValuePair point = new PointValuePair(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, 1.0);
        simplex.setPoint(0, point);
        PointValuePair result = simplex.getPoint(0);
        assertEquals(point, result);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsWithInvalidLength() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        PointValuePair[] points = {
            new PointValuePair(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, 1.0),
            new PointValuePair(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, 1.0)
        };
        simplex.setPoints(points);
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        PointValuePair[] points = {
            new PointValuePair(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, 1.0),
            new PointValuePair(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, 1.0),
            new PointValuePair(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, 1.0),
            new PointValuePair(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, 1.0),
            new PointValuePair(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, 1.0),
            new PointValuePair(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, 1.0)
        };
        simplex.setPoints(points);
        PointValuePair[] result = simplex.getPoints();
        assertArrayEquals(points, result);
    }
}

class AbstractSimplexImpl extends AbstractSimplex {
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

    public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
        System.out.println("Iterating");
    }
}