package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractSimplexTest {

    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        simplex = new AbstractSimplexTestImpl(3);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        simplex.build(new double[] {1.0, 2.0, 3.0});
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testIterate() {
        MultivariateFunction p0 = point -> point[0] + point[1] + point[2];
        Comparator<PointValuePair> p1 = Comparator.comparingDouble(PointValuePair::getValue);

        simplex.build(new double[] {0.0, 0.0, 0.0});
        simplex.iterate(p0, p1);

        //Assert something
    }

    @Test
    public void testEvaluate() {
        MultivariateFunction evaluationFunction = point -> point[0] * point[1] * point[2];
        Comparator<PointValuePair> comparator = Comparator.comparingDouble(PointValuePair::getValue);

        simplex.build(new double[] {1.0, 2.0, 3.0});
        simplex.evaluate(evaluationFunction, comparator);

        //Assert something
    }

    @Test
    public void testReplaceWorstPoint() {
        PointValuePair pointValuePair = new PointValuePair(new double[] {1.0, 2.0, 3.0}, 10.0);
        Comparator<PointValuePair> comparator = Comparator.comparingDouble(PointValuePair::getValue);

        simplex.replaceWorstPoint(pointValuePair, comparator);

        //Assert something
    }

    @Test
    public void testGetPoints() {
        simplex.build(new double[] {1.0, 2.0, 3.0});
        PointValuePair[] points = simplex.getPoints();

        //Assert something
    }

    @Test
    public void testGetPoint() {
        simplex.build(new double[] {1.0, 2.0, 3.0});
        PointValuePair point = simplex.getPoint(2);

        //Assert something
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_IndexOutOfBounds() {
        simplex.build(new double[] {1.0, 2.0, 3.0});
        PointValuePair point = simplex.getPoint(5);
    }

    @Test
    public void testSetPoint() {
        simplex.build(new double[] {1.0, 2.0, 3.0});
        PointValuePair point = new PointValuePair(new double[] {4.0, 5.0, 6.0}, 15.0);
        simplex.setPoint(1, point);

        //Assert something
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_IndexOutOfBounds() {
        simplex.build(new double[] {1.0, 2.0, 3.0});
        PointValuePair point = new PointValuePair(new double[] {4.0, 5.0, 6.0}, 15.0);
        simplex.setPoint(5, point);
    }

    @Test
    public void testSetPoints() {
        PointValuePair[] points = new PointValuePair[4];
        points[0] = new PointValuePair(new double[] {1.0, 2.0, 3.0}, 10.0);
        points[1] = new PointValuePair(new double[] {4.0, 5.0, 6.0}, 15.0);
        points[2] = new PointValuePair(new double[] {7.0, 8.0, 9.0}, 20.0);
        points[3] = new PointValuePair(new double[] {10.0, 11.0, 12.0}, 25.0);

        simplex.setPoints(points);

        //Assert something
    }

    private class AbstractSimplexTestImpl extends AbstractSimplex {

        protected AbstractSimplexTestImpl(int n) {
            super(n);
        }

        @Override
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            //Implement method for test
        }
    }
}