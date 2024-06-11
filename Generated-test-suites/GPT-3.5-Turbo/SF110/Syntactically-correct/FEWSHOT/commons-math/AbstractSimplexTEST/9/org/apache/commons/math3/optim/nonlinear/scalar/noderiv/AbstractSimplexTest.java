package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.util.NullArgumentException;
import org.apache.commons.math3.exception.util.OutOfRangeException;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class AbstractSimplexTest {
    private static class TestSimplex extends AbstractSimplex {
        public TestSimplex(int n) {
            super(n);
        }

        public TestSimplex(int n, double sideLength) {
            super(n, sideLength);
        }

        public TestSimplex(double[] steps) {
            super(steps);
        }

        public TestSimplex(double[][] referenceSimplex) {
            super(referenceSimplex);
        }

        @Override
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // TODO Auto-generated method stub
            
        }
    }

    private AbstractSimplex simplex1;
    private AbstractSimplex simplex2;
    private AbstractSimplex simplex3;
    private AbstractSimplex simplex4;

    private final double[] steps = {1, 2, 3, 4};

    @Before
    public void setUp() {
        simplex1 = new TestSimplex(3);
        simplex2 = new TestSimplex(3, 2);
        simplex3 = new TestSimplex(steps);
        simplex4 = new TestSimplex(new double[][]{{1, 2}, {3, 4}});
    }

    @Test
    public void testConstructor1() {
        assertEquals(3, simplex1.getDimension());
    }

    @Test
    public void testConstructor2() {
        assertEquals(3, simplex2.getDimension());
    }

    @Test
    public void testConstructor3() {
        assertEquals(4, simplex3.getDimension());
    }

    @Test
    public void testConstructor4() {
        assertEquals(1, simplex4.getDimension());
    }

    @Test
    public void testCreateHypercubeSteps() {
        double[] expectedSteps = {2, 2, 2};
        double[] actualSteps = TestSimplex.createHypercubeSteps(3, 2);
        assertTrue(Arrays.equals(expectedSteps, actualSteps));
    }

    @Test
    public void testCreateHypercubeStepsWithNZero() {
        assertThrows(NotStrictlyPositiveException.class,
                () -> TestSimplex.createHypercubeSteps(0, 2));
    }

    @Test
    public void testCreateHypercubeStepsWithSideLengthZero() {
        assertThrows(ZeroException.class,
                () -> TestSimplex.createHypercubeSteps(3, 0));
    }

    @Test
    public void testCreateHypercubeStepsWithNegativeSideLength() {
        double[] expectedSteps = {-2, -2, -2};
        double[] actualSteps = TestSimplex.createHypercubeSteps(3, -2);
        assertTrue(Arrays.equals(expectedSteps, actualSteps));
    }

    @Test
    public void testBuildWithInvalidDimension() {
        assertThrows(DimensionMismatchException.class,
                () -> simplex1.build(new double[]{1, 2}));
    }

    @Test
    public void testBuild() {
        double[] startPoint = {1, 2, 3};
        simplex1.build(startPoint);
        assertEquals(startPoint[2], simplex1.getPoint(0).getPoint()[2], 0);
    }

    @Test
    public void testEvaluate() {
        double[] startPoint = {1, 2, 3};
        simplex1.build(startPoint);
        simplex1.evaluate(p -> 1, (p1, p2) -> 1);
        PointValuePair[] points = simplex1.getPoints();
        assertEquals(1, points[0].getValue(), 0);
    }

    @Test
    public void testEvaluateWithNanValue() {
        double[] startPoint = {1, 2, 3};
        simplex1.build(startPoint);
        simplex1.evaluate(p -> Double.NaN, (p1, p2) -> 1);
        PointValuePair[] points = simplex1.getPoints();
        assertTrue(Double.isNaN(points[0].getValue()));
    }

    @Test
    public void testReplaceWorstPoint() {
        simplex1.build(new double[]{1, 2, 3});
        simplex1.setPoint(1, new PointValuePair(new double[]{4, 5, 6}, 10));
        simplex1.replaceWorstPoint(new PointValuePair(new double[]{7, 8, 9}, 20),
                (p1, p2) -> p1.getValue() < p2.getValue() ? -1 : 1);
        assertEquals(20, simplex1.getPoint(2).getValue(), 0);
    }

    @Test
    public void testReplaceWorstPointWithInvalidIndex() {
        assertThrows(OutOfRangeException.class,
                () -> simplex1.replaceWorstPoint(new PointValuePair(new double[]{7, 8, 9}, 20),
                        (p1, p2) -> p1.getValue() < p2.getValue() ? -1 : 1));
    }

    @Test
    public void testGetPoints() {
        double[] startPoint = {1, 2, 3};
        simplex1.build(startPoint);
        PointValuePair[] points = simplex1.getPoints();
        assertTrue(Arrays.equals(startPoint, points[0].getPoint()));
    }

    @Test
    public void testGetPointsImmutable() {
        double[] startPoint = {1, 2, 3};
        simplex1.build(startPoint);
        PointValuePair[] points = simplex1.getPoints();
        points[0] = new PointValuePair(new double[]{4, 5, 6}, 10);
        assertTrue(Arrays.equals(startPoint, simplex1.getPoint(0).getPoint()));
    }

    @Test
    public void testGetPoint() {
        double[] startPoint = {1, 2, 3};
        simplex1.build(startPoint);
        assertEquals(startPoint[2], simplex1.getPoint(0).getPoint()[2], 0);
    }

    @Test
    public void testGetPointWithInvalidIndex() {
        assertThrows(OutOfRangeException.class,
                () -> simplex1.getPoint(1));
    }

    @Test
    public void testSetPoint() {
        double[] startPoint = {1, 2, 3};
        simplex1.build(startPoint);
        simplex1.setPoint(0, new PointValuePair(new double[]{4, 5, 6}, 10));
        assertEquals(10, simplex1.getPoint(0).getValue(), 0);
    }

    @Test
    public void testSetPointWithInvalidIndex() {
        assertThrows(OutOfRangeException.class,
                () -> simplex1.setPoint(1, new PointValuePair(new double[]{4, 5, 6}, 10)));
    }

    @Test
    public void testSetPoints() {
        double[] startPoint = {1, 2, 3};
        simplex1.build(startPoint);
        PointValuePair[] newPoints = {new PointValuePair(new double[]{4, 5, 6}, 10)};
        simplex1.setPoints(newPoints);
        assertEquals(10, simplex1.getPoint(0).getValue(), 0);
    }

    @Test
    public void testSetPointsWithInvalidLength() {
        double[] startPoint = {1, 2, 3};
        simplex1.build(startPoint);
        PointValuePair[] newPoints = {new PointValuePair(new double[]{4, 5, 6}, 10),
                new PointValuePair(new double[]{7, 8, 9}, 20),
                new PointValuePair(new double[]{10, 11, 12}, 30)};
        assertThrows(DimensionMismatchException.class,
                () -> simplex1.setPoints(newPoints));
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, simplex1.getDimension());
    }

    @Test
    public void testGetSize() {
        double[] startPoint = {1, 2, 3};
        simplex1.build(startPoint);
        assertEquals(4, simplex1.getSize());
    }

    @Test
    public void testIterate() {
        double[] startPoint = {1, 2, 3};
        simplex1.build(startPoint);
        simplex1.iterate(p -> 1, (p1, p2) -> 1);
        // Need to add assertions to verify the iteration logic.
        // Cannot test this method as it is.
    }
}