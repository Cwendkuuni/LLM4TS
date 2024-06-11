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
    public void testConstructorWithInt() {
        AbstractSimplex simplex = new AbstractSimplexTest(5);
        assertEquals(5, simplex.getDimension());
        assertEquals(6, simplex.getSize());
    }

    @Test
    public void testConstructorWithIntAndLength() {
        AbstractSimplex simplex = new AbstractSimplexTest(5, 2.0);
        assertEquals(5, simplex.getDimension());
        assertEquals(6, simplex.getSize());
    }

    @Test
    public void testConstructorWithSteps() {
        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplexTest(steps);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testConstructorWithReferenceSimplex() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        AbstractSimplex simplex = new AbstractSimplexTest(referenceSimplex);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorNullSteps() {
        double[] steps = null;
        AbstractSimplex simplex = new AbstractSimplexTest(steps);
    }

    @Test(expected = ZeroException.class)
    public void testConstructorEmptySteps() {
        double[] steps = {};
        AbstractSimplex simplex = new AbstractSimplexTest(steps);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorEmptyReferenceSimplex() {
        double[][] referenceSimplex = {};
        AbstractSimplex simplex = new AbstractSimplexTest(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorInvalidReferenceSimplex() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0, 7.0}, {8.0, 9.0}};
        AbstractSimplex simplex = new AbstractSimplexTest(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorEqualVertices() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        AbstractSimplex simplex = new AbstractSimplexTest(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildInvalidStartPoint() {
        AbstractSimplex simplex = new AbstractSimplexTest(3);
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplexTest(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        assertEquals(4, simplex.getSize());
        PointValuePair[] points = simplex.getPoints();
        for (int i = 0; i < points.length; i++) {
            double[] point = points[i].getPointRef();
            assertEquals(startPoint[i], point[i], 0.0);
        }
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplexTest(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

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

        PointValuePair[] points = simplex.getPoints();
        assertEquals(6, points[0].getValue(), 0.0);
        assertEquals(7, points[1].getValue(), 0.0);
        assertEquals(8, points[2].getValue(), 0.0);
        assertEquals(9, points[3].getValue(), 0.0);
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplexTest(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

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

        PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
        worstPoint = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 10.0);

        simplex.replaceWorstPoint(worstPoint, comparator);

        PointValuePair[] points = simplex.getPoints();
        assertEquals(10, points[3].getValue(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplexTest(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair point = simplex.getPoint(simplex.getSize());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplexTest(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair point = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 10.0);
        simplex.setPoint(simplex.getSize(), point);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsInvalidSize() {
        AbstractSimplex simplex = new AbstractSimplexTest(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair[] points = new PointValuePair[]{new PointValuePair(new double[]{4.0, 5.0, 6.0}, 10.0)};
        simplex.setPoints(points);
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplexTest(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair[] points = simplex.getPoints();
        assertEquals(startPoint[0], points[0].getPointRef()[0], 0.0);
        assertEquals(startPoint[1], points[0].getPointRef()[1], 0.0);
        assertEquals(startPoint[2], points[0].getPointRef()[2], 0.0);
    }

    private class AbstractSimplexTest extends AbstractSimplex {

        public AbstractSimplexTest(int n) {
            super(n);
        }

        public AbstractSimplexTest(int n, double sideLength) {
            super(n, sideLength);
        }

        public AbstractSimplexTest(double[] steps) {
            super(steps);
        }

        public AbstractSimplexTest(double[][] referenceSimplex) {
            super(referenceSimplex);
        }

        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // Implementation here
        }
    }
}