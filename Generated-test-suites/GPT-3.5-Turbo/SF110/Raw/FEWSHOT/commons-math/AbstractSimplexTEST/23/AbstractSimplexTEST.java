package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {
    @Test
    public void testConstructor1() {
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };

        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testConstructor2() {
        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(steps) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };

        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testConstructor3() {
        double[][] referenceSimplex = {
            {1.0, 2.0, 3.0},
            {2.0, 3.0, 4.0},
            {3.0, 4.0, 5.0}
        };

        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };

        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorNullSteps() {
        double[] steps = null;
        AbstractSimplex simplex = new AbstractSimplex(steps) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };
    }

    @Test(expected = ZeroException.class)
    public void testConstructorEmptySteps() {
        double[] steps = {};
        AbstractSimplex simplex = new AbstractSimplex(steps) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        double[][] referenceSimplex = {};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorMismatchedDimensions() {
        double[][] referenceSimplex = {
            {1.0, 2.0, 3.0},
            {2.0, 3.0}
        };

        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorEqualVertices() {
        double[][] referenceSimplex = {
            {1.0, 2.0, 3.0},
            {1.0, 2.0, 3.0},
            {3.0, 4.0, 5.0}
        };

        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };
    }

    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };

        simplex.build(startPoint);

        assertEquals(4, simplex.getSize());
        assertArrayEquals(startPoint, simplex.getPoint(0).getPointRef(), 0.0001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildMismatchedDimensions() {
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(2) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };

        simplex.build(startPoint);
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };

        simplex.build(new double[]{1.0, 2.0, 3.0});

        MultivariateFunction evaluationFunction = new MultivariateFunction() {

            public double value(double[] point) {
                return point[0] * 2 + point[1] * 3 + point[2] * 4;
            }
        };

        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        simplex.evaluate(evaluationFunction, comparator);

        for (int i = 0; i < simplex.getSize(); i++) {
            assertFalse(Double.isNaN(simplex.getPoint(i).getValue()));
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };

        PointValuePair worstPoint = new PointValuePair(new double[]{2.0, 3.0, 4.0}, 10.0);

        simplex.build(new double[]{1.0, 2.0, 3.0});

        simplex.replaceWorstPoint(worstPoint, new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });

        assertArrayEquals(worstPoint.getPointRef(), simplex.getPoint(2).getPointRef(), 0.0001);
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };

        simplex.build(new double[]{1.0, 2.0, 3.0});

        PointValuePair[] points = simplex.getPoints();

        assertArrayEquals(simplex.getPoints(), points);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };

        simplex.getPoint(5);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };

        simplex.build(new double[]{1.0, 2.0, 3.0});

        PointValuePair newPoint = new PointValuePair(new double[]{2.0, 3.0, 4.0}, 5.0);
        simplex.setPoint(1, newPoint);

        assertArrayEquals(newPoint.getPointRef(), simplex.getPoint(1).getPointRef(), 0.0001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };

        simplex.setPoint(5, new PointValuePair(new double[]{1.0, 2.0, 3.0}, 4.0));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsMismatchedDimensions() {
        AbstractSimplex simplex1 = new AbstractSimplex(3) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };

        simplex1.build(new double[]{1.0, 2.0, 3.0});

        PointValuePair[] points = {
            new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0),
            new PointValuePair(new double[]{2.0, 3.0, 4.0}, 2.0),
            new PointValuePair(new double[]{3.0, 4.0, 5.0}, 3.0),
            new PointValuePair(new double[]{4.0, 5.0, 6.0}, 4.0),
        };

        AbstractSimplex simplex2 = new AbstractSimplex(4) {
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };

        simplex2.setPoints(points);
    }
}