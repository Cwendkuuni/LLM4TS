package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractSimplexTest {
    @Test
    public void testAbstractSimplexConstructor() {
        AbstractSimplex simplex1 = new AbstractSimplex(3);
        assertEquals(3, simplex1.getDimension());

        AbstractSimplex simplex2 = new AbstractSimplex(5, 2.0);
        assertEquals(5, simplex2.getDimension());

        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex3 = new AbstractSimplex(steps);
        assertEquals(3, simplex3.getDimension());

        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        AbstractSimplex simplex4 = new AbstractSimplex(referenceSimplex);
        assertEquals(2, simplex4.getDimension());
    }

    @Test
    public void testIterate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        }, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
    }

    @Test
    public void testBuildAndEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(2);
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);

        simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[1];
            }
        }, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair point1 = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 10.0);
        PointValuePair point2 = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 20.0);
        PointValuePair point3 = new PointValuePair(new double[]{7.0, 8.0, 9.0}, 30.0);

        simplex.setPoints(new PointValuePair[]{point1, point2, point3});

        PointValuePair replacePoint = new PointValuePair(new double[]{10.0, 11.0, 12.0}, 40.0);
        simplex.replaceWorstPoint(replacePoint, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });

        assertSame(replacePoint, simplex.getPoint(2));
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(2);
        PointValuePair point1 = new PointValuePair(new double[]{1.0, 2.0}, 10.0);
        PointValuePair point2 = new PointValuePair(new double[]{3.0, 4.0}, 20.0);
        simplex.setPoints(new PointValuePair[]{point1, point2});

        PointValuePair[] points = simplex.getPoints();
        assertArrayEquals(new PointValuePair[]{point1, point2}, points);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(2);
        PointValuePair point1 = new PointValuePair(new double[]{1.0, 2.0}, 10.0);
        PointValuePair point2 = new PointValuePair(new double[]{3.0, 4.0}, 20.0);
        PointValuePair point3 = new PointValuePair(new double[]{5.0, 6.0}, 30.0);
        simplex.setPoints(new PointValuePair[]{point1, point2, point3});

        assertSame(point1, simplex.getPoint(0));
        assertSame(point2, simplex.getPoint(1));
        assertSame(point3, simplex.getPoint(2));
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(2);
        PointValuePair point1 = new PointValuePair(new double[]{1.0, 2.0}, 10.0);
        PointValuePair point2 = new PointValuePair(new double[]{3.0, 4.0}, 20.0);

        simplex.setPoint(0, point1);
        simplex.setPoint(1, point2);

        assertSame(point1, simplex.getPoint(0));
        assertSame(point2, simplex.getPoint(1));
    }
}