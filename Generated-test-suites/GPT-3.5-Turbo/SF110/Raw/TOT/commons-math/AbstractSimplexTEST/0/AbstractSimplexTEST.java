import org.junit.Test;
import org.junit.Before;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.*;
import org.apache.commons.math3.optim.*;

import static org.junit.Assert.*;

public class AbstractSimplexTest {

    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        simplex = new AbstractSimplexStub();
    }

    private class AbstractSimplexStub extends AbstractSimplex {
        public AbstractSimplexStub() {
            super(2);
        }

        @Override
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // Do nothing
        }
    }

    // Testing constructor with dimension parameter
    @Test
    public void testConstructor1() {
        AbstractSimplex simplexFourDimension = new AbstractSimplexStub();
        assertEquals(2, simplexFourDimension.getDimension());
        assertEquals(2, simplexFourDimension.getSize());
    }

    // Testing constructor with dimension and sideLength parameters
    @Test
    public void testConstructor2() {
        double sideLength = 2.0;
        AbstractSimplex simplex = new AbstractSimplexStub(3, sideLength);
        assertEquals(3, simplexFourDimension.getDimension());
        assertEquals(3, simplex.getSize());
    }

    // Testing constructor with steps parameter
    @Test
    public void testConstructor3() {
        double[] steps = {1.0, 2.0};
        AbstractSimplex simplex = new AbstractSimplexStub(steps);
        assertEquals(2, simplex.getDimension());
        assertEquals(2, simplex.getSize());
    }

    // Testing constructor with referenceSimplex parameter
    @Test
    public void testConstructor4() {
        double[][] referenceSimplex = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        AbstractSimplex simplex = new AbstractSimplexStub(referenceSimplex);
        assertEquals(2, simplex.getDimension());
        assertEquals(3, simplex.getSize());
    }

    // Testing getDimension method
    @Test
    public void testGetDimension() {
        assertEquals(2, simplex.getDimension());
    }

    // Testing getSize method
    @Test
    public void testGetSize() {
        assertEquals(2, simplex.getSize());
    }

    // Testing build method
    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);

        PointValuePair[] points = simplex.getPoints();
        assertEquals(2, points.length);

        double[] expectedStartPoint = {1.0, 2.0};

        assertArrayEquals(expectedStartPoint, points[0].getPoint(), 0.0001);
        assertTrue(Double.isNaN(points[0].getValue()));

        double[] expectedVertexPoint = {2.0, 4.0};
        assertArrayEquals(expectedVertexPoint, points[1].getPoint(), 0.0001);
        assertTrue(Double.isNaN(points[1].getValue()));
    }

    // Testing evaluate method
    @Test
    public void testEvaluate() {
        MultivariateFunction evaluationFunction = new MultivariateFunctionStub();
        Comparator<PointValuePair> comparator = new PointValuePairComparatorStub();

        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);

        simplex.evaluate(evaluationFunction, comparator);

        PointValuePair[] points = simplex.getPoints();

        double[] expectedStartPoint = {1.0, 2.0};
        assertEquals(3.0, points[0].getValue(), 0.0001);
        assertArrayEquals(expectedStartPoint, points[0].getPoint(), 0.0001);

        double[] expectedVertexPoint = {2.0, 4.0};
        assertEquals(4.0, points[1].getValue(), 0.0001);
        assertArrayEquals(expectedVertexPoint, points[1].getPoint(), 0.0001);
    }

    // Testing replaceWorstPoint method
    @Test
    public void testReplaceWorstPoint() {
        PointValuePair pointValuePair = new PointValuePair(new double[]{3.0, 5.0}, 5.0);

        Comparator<PointValuePair> comparator = new PointValuePairComparatorStub();

        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);

        simplex.replaceWorstPoint(pointValuePair, comparator);

        PointValuePair[] points = simplex.getPoints();

        double[] expectedStartPoint = {1.0, 2.0};
        assertEquals(2.0, points[0].getValue(), 0.0001);
        assertArrayEquals(expectedStartPoint, points[0].getPoint(), 0.0001);

        double[] expectedVertexPoint = {3.0, 5.0};
        assertEquals(5.0, points[1].getValue(), 0.0001);
        assertArrayEquals(expectedVertexPoint, points[1].getPoint(), 0.0001);
    }

    // Testing getPoints method
    @Test
    public void testGetPoints() {
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);

        PointValuePair[] points = simplex.getPoints();

        double[] expectedStartPoint = {1.0, 2.0};
        assertArrayEquals(expectedStartPoint, points[0].getPoint(), 0.0001);
        assertTrue(Double.isNaN(points[0].getValue()));

        double[] expectedVertexPoint = {2.0, 4.0};
        assertArrayEquals(expectedVertexPoint, points[1].getPoint(), 0.0001);
        assertTrue(Double.isNaN(points[1].getValue()));
    }

    // Testing getPoint method
    @Test
    public void testGetPoint() {
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);

        PointValuePair point = simplex.getPoint(0);

        double[] expectedStartPoint = {1.0, 2.0};
        assertArrayEquals(expectedStartPoint, point.getPoint(), 0.0001);
        assertTrue(Double.isNaN(point.getValue()));
    }

    // Testing setPoint method
    @Test
    public void testSetPoint() {
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);

        PointValuePair newPoint = new PointValuePair(new double[]{3.0, 4.0}, 4.0);
        simplex.setPoint(0, newPoint);

        PointValuePair point = simplex.getPoint(0);

        double[] expectedNewPoint = {3.0, 4.0};
        assertArrayEquals(expectedNewPoint, point.getPoint(), 0.0001);
        assertEquals(4.0, point.getValue(), 0.0001);
    }

    // Testing setPoints method
    @Test
    public void testSetPoints() {
        PointValuePair[] newPoints = {
            new PointValuePair(new double[]{1.0, 2.0}, 2.0),
            new PointValuePair(new double[]{3.0, 4.0}, 4.0)
        };
        simplex.setPoints(newPoints);

        PointValuePair[] points = simplex.getPoints();

        double[] expectedPoint1 = {1.0, 2.0};
        assertArrayEquals(expectedPoint1, points[0].getPoint(), 0.0001);
        assertEquals(2.0, points[0].getValue(), 0.0001);

        double[] expectedPoint2 = {3.0, 4.0};
        assertArrayEquals(expectedPoint2, points[1].getPoint(), 0.0001);
        assertEquals(4.0, points[1].getValue(), 0.0001);
    }

    // Testing createHypercubeSteps method
    @Test
    public void testCreateHypercubeSteps() {
        int n = 3;
        double sideLength = 2.0;

        double[] expectedSteps = {2.0, 2.0, 2.0};
        double[] actualSteps = AbstractSimplex.createHypercubeSteps(n, sideLength);

        assertArrayEquals(expectedSteps, actualSteps, 0.0001);
    }
}

class MultivariateFunctionStub implements MultivariateFunction {

    @Override
    public double value(double[] point) {
        return point[0] + point[1];
    }
}

class PointValuePairComparatorStub implements Comparator<PointValuePair> {

    @Override
    public int compare(PointValuePair o1, PointValuePair o2) {
        return Double.compare(o1.getValue(), o2.getValue());
    }
}