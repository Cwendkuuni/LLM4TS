package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

public class AbstractSimplexTest {
    private AbstractSimplex simplex;
    private MultivariateFunction evaluationFunction;
    private Comparator<PointValuePair> comparator;

    @Before
    public void setUp() {
        int n = 3;
        double sideLength = 1.0;
        simplex = new AbstractSimplexImplementation(n, sideLength);
        evaluationFunction = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] + point[1] + point[2];
            }
        };
        comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
    }

    @Test
    public void testConstructor() {
        int n = 3;
        double sideLength = 1.0;

        // Test constructor AbstractSimplex(int n)
        AbstractSimplex s1 = new AbstractSimplexImplementation(n);
        assertEquals(n, s1.getDimension());

        // Test constructor AbstractSimplex(int n, double sideLength)
        AbstractSimplex s2 = new AbstractSimplexImplementation(n, sideLength);
        assertEquals(n, s2.getDimension());

        // Test constructor AbstractSimplex(double[] steps)
        double[] steps = {sideLength, sideLength, sideLength};
        AbstractSimplex s3 = new AbstractSimplexImplementation(steps);
        assertEquals(n, s3.getDimension());

        // Test constructor AbstractSimplex(double[][] referenceSimplex)
        double[][] referenceSimplex = {{0.0, 0.0, 0.0}, {1.0, 1.0, 1.0}, {2.0, 2.0, 2.0}};
        AbstractSimplex s4 = new AbstractSimplexImplementation(referenceSimplex);
        assertEquals(n, s4.getDimension());
    }

    @Test
    public void testGetDimension() {
        int expected = 3;
        assertEquals(expected, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        int expected = 4;
        assertEquals(expected, simplex.getSize());
    }

    @Test
    public void testIterate() {
        // Not testable, as it is an abstract method
    }

    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair[] expectedPoints = new PointValuePair[4];
        expectedPoints[0] = new PointValuePair(startPoint, Double.NaN);
        expectedPoints[1] = new PointValuePair(new double[] {2.0, 2.0, 3.0}, Double.NaN);
        expectedPoints[2] = new PointValuePair(new double[] {1.0, 3.0, 3.0}, Double.NaN);
        expectedPoints[3] = new PointValuePair(new double[] {1.0, 2.0, 4.0}, Double.NaN);

        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildDimensionMismatch() {
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
    }

    @Test
    public void testEvaluate() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        simplex.evaluate(evaluationFunction, comparator);

        double[] values = {6.0, 6.0, 6.0, 6.0};
        for (int i = 0; i < simplex.getSize(); i++) {
            assertEquals(values[i], simplex.getPoint(i).getValue(), 0.0);
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[] {0.0, 0.0, 0.0}, 0.0);

        simplex.replaceWorstPoint(newPoint, comparator);

        assertEquals(newPoint, simplex.getPoint(3));
    }

    @Test
    public void testGetPoints() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair[] expected = new PointValuePair[4];
        expected[0] = new PointValuePair(startPoint, Double.NaN);
        expected[1] = new PointValuePair(new double[] {2.0, 2.0, 3.0}, Double.NaN);
        expected[2] = new PointValuePair(new double[] {1.0, 3.0, 3.0}, Double.NaN);
        expected[3] = new PointValuePair(new double[] {1.0, 2.0, 4.0}, Double.NaN);

        assertArrayEquals(expected, simplex.getPoints());
    }

    @Test
    public void testGetPoint() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair expected = new PointValuePair(startPoint, Double.NaN);

        assertEquals(expected, simplex.getPoint(0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        simplex.getPoint(4);
    }

    @Test
    public void testSetPoint() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[] {0.0, 0.0, 0.0}, 0.0);
        simplex.setPoint(1, newPoint);

        assertEquals(newPoint, simplex.getPoint(1));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[] {0.0, 0.0, 0.0}, 0.0);
        simplex.setPoint(4, newPoint);
    }

    @Test
    public void testSetPoints() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair[] points = new PointValuePair[4];
        points[0] = new PointValuePair(new double[] {0.0, 0.0, 0.0}, 0.0);
        points[1] = new PointValuePair(new double[] {1.0, 1.0, 1.0}, 1.0);
        points[2] = new PointValuePair(new double[] {2.0, 2.0, 2.0}, 2.0);
        points[3] = new PointValuePair(new double[] {3.0, 3.0, 3.0}, 3.0);

        simplex.setPoints(points);

        assertArrayEquals(points, simplex.getPoints());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsDimensionMismatch() {
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair[] points = new PointValuePair[5];
        points[0] = new PointValuePair(new double[] {0.0, 0.0, 0.0}, 0.0);
        points[1] = new PointValuePair(new double[] {1.0, 1.0, 1.0}, 1.0);
        points[2] = new PointValuePair(new double[] {2.0, 2.0, 2.0}, 2.0);
        points[3] = new PointValuePair(new double[] {3.0, 3.0}, 3.0);
        points[4] = new PointValuePair(new double[] {4.0, 4.0, 4.0}, 4.0);

        simplex.setPoints(points);
    }
}

class AbstractSimplexImplementation extends AbstractSimplex {
    public AbstractSimplexImplementation(int n) {
        super(n);
    }

    public AbstractSimplexImplementation(int n, double sideLength) {
        super(n, sideLength);
    }

    public AbstractSimplexImplementation(double[] steps) {
        super(steps);
    }

    public AbstractSimplexImplementation(double[][] referenceSimplex) {
        super(referenceSimplex);
    }

    @Override
    public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

    }
}