package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.math3.optim.DimensionMismatchException;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.optim.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

public class AbstractSimplexTest {

    private static final double EPSILON = 1e-10;

    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        simplex = new AbstractSimplex(3) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };
        simplex.build(new double[]{1, 2, 3});
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
        simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        }, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });

        PointValuePair[] points = simplex.getPoints();

        assertEquals(6.0, points[0].getValue(), EPSILON);
        assertEquals(1.0, points[0].getPointRef()[0], EPSILON);
        assertEquals(2.0, points[0].getPointRef()[1], EPSILON);
        assertEquals(3.0, points[0].getPointRef()[2], EPSILON);

        assertEquals(3.0, points[1].getValue(), EPSILON);
        assertEquals(1.0, points[1].getPointRef()[0], EPSILON);
        assertEquals(2.0, points[1].getPointRef()[1], EPSILON);
        assertEquals(4.0, points[1].getPointRef()[2], EPSILON);

        assertEquals(4.0, points[2].getValue(), EPSILON);
        assertEquals(1.0, points[2].getPointRef()[0], EPSILON);
        assertEquals(3.0, points[2].getPointRef()[1], EPSILON);
        assertEquals(4.0, points[2].getPointRef()[2], EPSILON);

        assertEquals(5.0, points[3].getValue(), EPSILON);
        assertEquals(2.0, points[3].getPointRef()[0], EPSILON);
        assertEquals(3.0, points[3].getPointRef()[1], EPSILON);
        assertEquals(4.0, points[3].getPointRef()[2], EPSILON);
    }

    @Test
    public void testReplaceWorstPoint() {
        PointValuePair point = new PointValuePair(new double[]{1, 1, 1}, 1.0);
        simplex.replaceWorstPoint(point, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });

        PointValuePair[] points = simplex.getPoints();

        assertEquals(1.0, points[3].getValue(), EPSILON);
        assertEquals(1.0, points[3].getPointRef()[0], EPSILON);
        assertEquals(1.0, points[3].getPointRef()[1], EPSILON);
        assertEquals(1.0, points[3].getPointRef()[2], EPSILON);
    }

    @Test
    public void testGetPoints() {
        PointValuePair[] points = simplex.getPoints();

        assertEquals(4, points.length);
        assertEquals(1.0, points[0].getPointRef()[0], EPSILON);
        assertEquals(2.0, points[0].getPointRef()[1], EPSILON);
        assertEquals(3.0, points[0].getPointRef()[2], EPSILON);

        assertEquals(1.0, points[1].getPointRef()[0], EPSILON);
        assertEquals(2.0, points[1].getPointRef()[1], EPSILON);
        assertEquals(4.0, points[1].getPointRef()[2], EPSILON);

        assertEquals(1.0, points[2].getPointRef()[0], EPSILON);
        assertEquals(3.0, points[2].getPointRef()[1], EPSILON);
        assertEquals(4.0, points[2].getPointRef()[2], EPSILON);

        assertEquals(2.0, points[3].getPointRef()[0], EPSILON);
        assertEquals(3.0, points[3].getPointRef()[1], EPSILON);
        assertEquals(4.0, points[3].getPointRef()[2], EPSILON);
    }

    @Test
    public void testGetPoint() {
        PointValuePair point1 = simplex.getPoint(0);
        PointValuePair point2 = simplex.getPoint(1);

        assertEquals(1.0, point1.getPointRef()[0], EPSILON);
        assertEquals(2.0, point1.getPointRef()[1], EPSILON);
        assertEquals(3.0, point1.getPointRef()[2], EPSILON);

        assertEquals(1.0, point2.getPointRef()[0], EPSILON);
        assertEquals(2.0, point2.getPointRef()[1], EPSILON);
        assertEquals(4.0, point2.getPointRef()[2], EPSILON);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsDimensionMismatch() {
        PointValuePair[] points = new PointValuePair[]{};
        simplex.setPoints(points);
    }

    @Test
    public void testSetPoints() {
        PointValuePair[] points = new PointValuePair[]{
                new PointValuePair(new double[]{4, 5, 6}, 7.0),
                new PointValuePair(new double[]{7, 8, 9}, 10.0),
                new PointValuePair(new double[]{10, 11, 12}, 13.0),
                new PointValuePair(new double[]{13, 14, 15}, 16.0)
        };

        simplex.setPoints(points);

        assertEquals(4, simplex.getPoints().length);
        assertEquals(4, simplex.getSize());

        assertNotEquals(points, simplex.getPoints()); // Check for reference copy

        // Check if values are updated
        for (int i = 0; i < simplex.getSize(); i++) {
            assertEquals(points[i], simplex.getPoints()[i]);
        }
    }
}