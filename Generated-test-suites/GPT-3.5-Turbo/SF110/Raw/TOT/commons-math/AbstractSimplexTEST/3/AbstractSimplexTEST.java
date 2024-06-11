package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

public class AbstractSimplexTest {

    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        // Create an instance of AbstractSimplex for testing
        simplex = new AbstractSimplex(3) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {

            }
        };
    }

    @Test
    public void testGetDimension() {
        // Test the getDimension method
        Assert.assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        // Test the getSize method
        Assert.assertEquals(0, simplex.getSize());
    }

    @Test
    public void testBuild() {
        // Test the build method with valid input
        double[] startPoint = {1, 2, 3};
        Assert.assertDoesNotThrow(() -> simplex.build(startPoint));
        Assert.assertEquals(4, simplex.getSize());

        // Test the build method with invalid input
        double[] invalidStartPoint = {1, 2};
        Assert.assertThrows(DimensionMismatchException.class, () -> simplex.build(invalidStartPoint));
    }

    @Test
    public void testEvaluate() {
        // Test the evaluate method
        simplex.build(new double[]{1, 2, 3});
        MultivariateFunction evaluationFunction = point -> point[0] + point[1] + point[2];
        Comparator<PointValuePair> comparator = Comparator.comparingDouble(PointValuePair::getValue);
        simplex.evaluate(evaluationFunction, comparator);

        Assert.assertEquals(4, simplex.getSize());
        Assert.assertEquals(6.0, simplex.getPoint(0).getValue(), 0.0);
        Assert.assertEquals(1.0, simplex.getPoint(1).getValue(), 0.0);
        Assert.assertEquals(2.0, simplex.getPoint(2).getValue(), 0.0);
        Assert.assertEquals(3.0, simplex.getPoint(3).getValue(), 0.0);
    }

    @Test
    public void testReplaceWorstPoint() {
        // Test the replaceWorstPoint method
        simplex.build(new double[]{1, 2, 3});
        simplex.evaluate(point -> point[0] + point[1] + point[2], Comparator.comparingDouble(PointValuePair::getValue));
        PointValuePair newPoint = new PointValuePair(new double[]{4, 5, 6}, 15.0);
        simplex.replaceWorstPoint(newPoint, Comparator.comparingDouble(PointValuePair::getValue));

        Assert.assertEquals(4, simplex.getSize());
        Assert.assertEquals(1.0, simplex.getPoint(0).getValue(), 0.0);
        Assert.assertEquals(2.0, simplex.getPoint(1).getValue(), 0.0);
        Assert.assertEquals(3.0, simplex.getPoint(2).getValue(), 0.0);
        Assert.assertEquals(15.0, simplex.getPoint(3).getValue(), 0.0);
    }

    @Test
    public void testGetPoints() {
        // Test the getPoints method
        simplex.build(new double[]{1, 2, 3});
        PointValuePair[] points = simplex.getPoints();

        Assert.assertEquals(4, points.length);
        Assert.assertArrayEquals(new double[]{1, 2, 3}, points[0].getPoint(), 0.0);
    }

    @Test
    public void testGetPoint() {
        // Test the getPoint method with valid index
        simplex.build(new double[]{1, 2, 3});
        PointValuePair point = simplex.getPoint(0);
        Assert.assertArrayEquals(new double[]{1, 2, 3}, point.getPoint(), 0.0);

        // Test the getPoint method with invalid index
        int invalidIndex = -1;
        Assert.assertThrows(OutOfRangeException.class, () -> simplex.getPoint(invalidIndex));
    }

    @Test
    public void testSetPoint() {
        // Test the setPoint method with valid index and point
        simplex.build(new double[]{1, 2, 3});
        PointValuePair newPoint = new PointValuePair(new double[]{4, 5, 6}, 15.0);
        simplex.setPoint(0, newPoint);

        Assert.assertArrayEquals(new double[]{4, 5, 6}, simplex.getPoint(0).getPoint(), 0.0);

        // Test the setPoint method with invalid index
        int invalidIndex = -1;
        Assert.assertThrows(OutOfRangeException.class, () -> simplex.setPoint(invalidIndex, newPoint));
    }

    @Test
    public void testSetPoints() {
        // Test the setPoints method with valid points
        simplex.build(new double[]{1, 2, 3});
        PointValuePair[] newPoints = {
                new PointValuePair(new double[]{4, 5, 6}, 15.0),
                new PointValuePair(new double[]{7, 8, 9}, 24.0)
        };
        simplex.setPoints(newPoints);

        Assert.assertArrayEquals(newPoints, simplex.getPoints());

        // Test the setPoints method with invalid number of points
        PointValuePair[] invalidPoints = {
                new PointValuePair(new double[]{4, 5, 6}, 15.0),
                new PointValuePair(new double[]{7, 8}, 24.0)
        };
        Assert.assertThrows(DimensionMismatchException.class, () -> simplex.setPoints(invalidPoints));
    }

    @Test
    public void testCreateHypercubeSteps() {
        // Test the createHypercubeSteps method
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 2.0);
        Assert.assertArrayEquals(new double[]{2.0, 2.0, 2.0}, steps, 0.0);
    }
}