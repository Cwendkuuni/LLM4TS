package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.analysis.MultivariateFunction;

import java.util.Arrays;
import java.util.Comparator;

public class AbstractSimplexTest {
    @Test
    public void testAbstractSimplexConstructor() {
        AbstractSimplex simplex = new AbstractSimplex(3) {};
        assertEquals(3, simplex.getDimension());
        assertEquals(1, simplex.getSize());

        simplex = new AbstractSimplex(3, 2) {};
        assertEquals(3, simplex.getDimension());
        assertEquals(1, simplex.getSize());

        double[] steps = {1.0, 2.0, 3.0};
        simplex = new AbstractSimplex(steps) {};
        assertEquals(3, simplex.getDimension());
        assertEquals(1, simplex.getSize());

        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        simplex = new AbstractSimplex(referenceSimplex) {};
        assertEquals(2, simplex.getDimension());
        assertEquals(1, simplex.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexConstructorWithNullSteps() {
        new AbstractSimplex(null) {};
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexConstructorWithEmptySteps() {
        double[] steps = {};
        new AbstractSimplex(steps) {};
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexConstructorWithReferenceSimplexLessThanOnePoint() {
        double[][] referenceSimplex = {};
        new AbstractSimplex(referenceSimplex) {};
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexConstructorWithReferenceSimplexMismatchingDimensions() {
        double[][] referenceSimplex = {{1.0, 2.0}, {3.0, 4.0, 5.0}};
        new AbstractSimplex(referenceSimplex) {};
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexConstructorWithReferenceSimplexEqualVertices() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {1.0, 2.0, 3.0}};
        new AbstractSimplex(referenceSimplex) {};
    }

    @Test
    public void testAbstractSimplexGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(5) {};
        assertEquals(5, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(5) {};
        assertEquals(1, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexIterate() {
        AbstractSimplex simplex = new AbstractSimplex(5) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // do nothing
            }
        };
        simplex.iterate(null, null);
    }

    @Test
    public void testAbstractSimplexBuild() {
        AbstractSimplex simplex = new AbstractSimplex(5) {};
        double[] startPoint = {1.0, 2.0, 3.0, 4.0, 5.0};
        simplex.build(startPoint);

        PointValuePair[] expectedPoints = new PointValuePair[6];
        expectedPoints[0] = new PointValuePair(startPoint, Double.NaN);

        int dimension = simplex.getDimension();
        for (int i = 0; i < dimension; i++) {
            double[] vertexI = new double[dimension];
            for (int k = 0; k < dimension; k++) {
                vertexI[k] = startPoint[k];
            }
            vertexI[i] += 1.0;

            expectedPoints[i + 1] = new PointValuePair(vertexI, Double.NaN);
        }

        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexBuildWithMismatchingDimensions() {
        AbstractSimplex simplex = new AbstractSimplex(5) {};
        double[] startPoint = {1.0, 2.0, 3.0, 4.0};
        simplex.build(startPoint);
    }

    @Test
    public void testAbstractSimplexEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(5) {
            @Override
            public void evaluate(MultivariateFunction evaluationFunction, Comparator<PointValuePair> comparator) {
                // do nothing
            }
        };
        simplex.evaluate(null, null);
    }

    @Test
    public void testAbstractSimplexReplaceWorstPoint() {
        PointValuePair pointValuePair = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 1;
            }
        };

        AbstractSimplex simplex = new AbstractSimplex(5) {};
        simplex.replaceWorstPoint(pointValuePair, comparator);

        assertSame(pointValuePair, simplex.getPoint(4));
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexReplaceWorstPointWithInvalidIndex() {
        PointValuePair pointValuePair = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 1;
            }
        };

        AbstractSimplex simplex = new AbstractSimplex(5) {};
        simplex.replaceWorstPoint(pointValuePair, comparator);
        simplex.replaceWorstPoint(pointValuePair, comparator);
    }

    @Test
    public void testAbstractSimplexGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3) {};
        PointValuePair[] points = simplex.getPoints();

        assertEquals(1, points.length);
        assertTrue(Arrays.equals(new PointValuePair[1], points));
    }

    @Test
    public void testAbstractSimplexGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3) {};
        PointValuePair point = simplex.getPoint(0);

        assertNull(point);
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexGetPointWithInvalidIndex() {
        AbstractSimplex simplex = new AbstractSimplex(2) {};
        simplex.getPoint(2);
    }

    @Test
    public void testAbstractSimplexSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3) {};

        PointValuePair point = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        simplex.setPoint(0, point);

        assertSame(point, simplex.getPoint(0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexSetPointWithInvalidIndex() {
        AbstractSimplex simplex = new AbstractSimplex(2) {};

        PointValuePair point = new PointValuePair(new double[]{1.0, 2.0}, 0.0);
        simplex.setPoint(2, point);
    }

    @Test
    public void testAbstractSimplexSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3) {};

        PointValuePair[] points = {new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0)};

        simplex.setPoints(points);
        assertArrayEquals(points, simplex.getPoints());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexSetPointsWithMismatchingDimensions() {
        AbstractSimplex simplex = new AbstractSimplex(2) {};

        PointValuePair[] points = {new PointValuePair(new double[]{1.0, 2.0})};

        simplex.setPoints(points);
    }

    @Test
    public void testAbstractSimplexCreateHypercubeSteps() {
        double[] expectedSteps = {2.0, 2.0, 2.0};

        double[] steps = AbstractSimplex.createHypercubeSteps(3, 2);
        assertArrayEquals(expectedSteps, steps, 0.0);
    }
}