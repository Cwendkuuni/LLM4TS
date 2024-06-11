package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.exception.util.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.OutOfRangeException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

public class AbstractSimplexTest {
    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplex_NullSteps() {
        new TestSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_ZeroSteps() {
        new TestSimplex(new double[]{});
    }

    @Test
    public void testAbstractSimplex_StartConfiguration() {
        double[] steps = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new TestSimplex(steps);
        double[][] startConfiguration = simplex.getStartConfiguration();
        assertEquals(3, startConfiguration.length);
        assertEquals(3, startConfiguration[0].length);
        assertEquals(2, startConfiguration[1].length);
        assertEquals(1, startConfiguration[2].length);
    }

    @Test
    public void testAbstractSimplex_ReferenceSimplex() {
        double[][] referenceSimplex = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        AbstractSimplex simplex = new TestSimplex(referenceSimplex);
        double[][] startConfiguration = simplex.getStartConfiguration();
        assertEquals(3, startConfiguration.length);
        assertEquals(3, startConfiguration[0].length);
        assertEquals(2, startConfiguration[1].length);
        assertEquals(1, startConfiguration[2].length);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplex_ReferenceSimplex_ZeroLength() {
        double[][] referenceSimplex = new double[][]{};
        new TestSimplex(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuild_WrongDimension() {
        double[] steps = new double[]{1.0};
        AbstractSimplex simplex = new TestSimplex(steps);
        double[] startPoint = new double[]{1.0, 2.0};
        simplex.build(startPoint);
    }

    @Test
    public void testBuild() {
        double[] steps = new double[]{1.0};
        AbstractSimplex simplex = new TestSimplex(steps);
        double[] startPoint = new double[]{1.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(2, points.length);
        assertEquals(startPoint, points[0].getPoint(), 0.0);
    }

    @Test
    public void testEvaluate() {
        double[] steps = new double[]{1.0};
        AbstractSimplex simplex = new TestSimplex(steps);
        double[] startPoint = new double[]{1.0};
        simplex.build(startPoint);
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0];
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                double v1 = p1.getValue();
                double v2 = p2.getValue();
                return Double.compare(v1, v2);
            }
        };
        simplex.evaluate(evaluationFunction, comparator);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(2, points.length);
        assertEquals(1.0, points[0].getPoint()[0], 0.0);
        assertEquals(1.0, points[0].getValue(), 0.0);
        assertEquals(2.0, points[1].getPoint()[0], 0.0); 
        assertEquals(4.0, points[1].getValue(), 0.0);
    }

    @Test
    public void testReplaceWorstPoint() {
        double[] steps = new double[]{1.0};
        AbstractSimplex simplex = new TestSimplex(steps);
        double[] startPoint = new double[]{1.0};
        simplex.build(startPoint);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                double v1 = p1.getValue();
                double v2 = p2.getValue();
                return Double.compare(v1, v2);
            }
        };
        int dimension = simplex.getDimension();
        PointValuePair[] points = simplex.getPoints();
        PointValuePair worstPoint = points[dimension];
        worstPoint = new PointValuePair(new double[]{0.5}, 0.0);
        simplex.replaceWorstPoint(worstPoint, comparator);
        points = simplex.getPoints();
        assertEquals(worstPoint, points[dimension]);
    }

    @Test
    public void testGetPoint() {
        double[] steps = new double[]{1.0};
        AbstractSimplex simplex = new TestSimplex(steps);
        double[] startPoint = new double[]{1.0};
        simplex.build(startPoint);
        PointValuePair point = simplex.getPoint(0);
        assertEquals(startPoint, point.getPoint(), 0.0);
        point = simplex.getPoint(1);
        assertTrue(point.getPoint()[0] > startPoint[0]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_IndexOutOfRange() {
        double[] steps = new double[]{1.0};
        AbstractSimplex simplex = new TestSimplex(steps);
        double[] startPoint = new double[]{1.0};
        simplex.build(startPoint);
        simplex.getPoint(2);
    }

    @Test
    public void testSetPoint() {
        double[] steps = new double[]{1.0};
        AbstractSimplex simplex = new TestSimplex(steps);
        double[] startPoint = new double[]{1.0};
        simplex.build(startPoint);
        int index = 1;
        PointValuePair point = simplex.getPoint(index);
        simplex.setPoint(index, new PointValuePair(new double[]{0.5}, 0.0));
        PointValuePair updatedPoint = simplex.getPoint(index);
        assertFalse(point == updatedPoint);
        assertEquals(new double[]{0.5}, updatedPoint.getPoint(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_IndexOutOfRange() {
        double[] steps = new double[]{1.0};
        AbstractSimplex simplex = new TestSimplex(steps);
        double[] startPoint = new double[]{1.0};
        simplex.build(startPoint);
        simplex.setPoint(2, new PointValuePair(new double[]{0.5}, 0.0));
    }
}

class TestSimplex extends AbstractSimplex {
    public TestSimplex(double[] steps) {
        super(steps);
    }

    public TestSimplex(double[][] referenceSimplex) {
        super(referenceSimplex);
    }

    public double[][] getStartConfiguration() {
        return startConfiguration;
    }

    public void iterate(MultivariateFunction evaluationFunction, Comparator<PointValuePair> comparator) {
        // Do nothing for testing purposes
    }
}