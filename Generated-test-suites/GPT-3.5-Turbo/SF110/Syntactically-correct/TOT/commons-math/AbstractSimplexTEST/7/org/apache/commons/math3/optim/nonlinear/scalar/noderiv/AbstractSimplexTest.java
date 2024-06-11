import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.*;

import java.util.Comparator;

import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testConstructor_NegativeDimension_ThrowDimensionMismatchException() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(-1);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // pass
        }
    }

    @Test
    public void testConstructor_ZeroSideLength_ThrowZeroException() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(5,0);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // pass
        }
    }

    @Test
    public void testConstructor_NullSteps_ThrowNullArgumentException() {
        try{
            AbstractSimplex simplex = new AbstractSimplex(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // pass
        }
    }

    @Test
    public void testConstructor_EqualVertices_ThrowZeroException() {
        double[] steps = {1.0, 0.0, 1.0, 1.0};
        try{
            AbstractSimplex simplex = new AbstractSimplex(steps);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // pass
        }
    }

    @Test
    public void testConstructor_EqualVertices_ThrowMathIllegalArgumentException() {
        double[][] referenceSimplex = {{0, 0}, {1, 1}, {0, 1}};
        try{
            AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testCreateHypercubeSteps_ValidInput_ReturnArrayWithSameValue() {
        int n = 5;
        double sideLength = 2;

        AbstractSimplex simplex = new AbstractSimplex(5, sideLength);
        double[] actualSteps = simplex.createHypercubeSteps(n, sideLength);

        double[] expectedSteps = {2, 2, 2, 2, 2};
        assertArrayEquals(expectedSteps, actualSteps, 0);
    }

    @Test
    public void testGetDimension_ValidInput_ReturnDimension() {
        int n = 5;
        AbstractSimplex simplex = new AbstractSimplex(5);

        int actualDimension = simplex.getDimension();

        assertEquals(n, actualDimension);
    }

    @Test
    public void testGetSize_ValidInput_ReturnSizeOfSimplexArray() {
        AbstractSimplex simplex = new AbstractSimplex(5);
        simplex.build(new double[]{1, 2, 3, 4, 5});

        int actualSize = simplex.getSize();

        assertEquals(6, actualSize);
    }

    @Test
    public void testIterate_ValidInput_SimplexArraySortedAccordingToComparator() {
        AbstractSimplex simplex = new AbstractSimplex(5);
        MultivariateFunction function = point -> point[0] + point[1];
        Comparator<PointValuePair> comparator = Comparator.comparingDouble(PointValuePair::getValue);

        simplex.iterate(function, comparator);
        PointValuePair[] actualSimplex = simplex.getPoints();

        double[][] expectedSimplex = {{1.0, 2.0, 3.0, 4.0, 5.0}, {2.0, 3.0, 4.0, 5.0, 6.0},
                {3.0, 4.0, 5.0, 6.0, 7.0}, {4.0, 5.0, 6.0, 7.0, 8.0}, {5.0, 6.0, 7.0, 8.0, 9.0}};
        for (int i = 0; i < expectedSimplex.length; i++) {
            assertArrayEquals(expectedSimplex[i], actualSimplex[i].getPointRef(), 0);
        }
    }

    @Test
    public void testBuild_ValidInput_SimplexArrayContainsExpectedPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1, 2, 3};

        simplex.build(startPoint);
        PointValuePair[] actualSimplex = simplex.getPoints();

        double[][] expectedSimplex = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}, {4.0, 5.0, 6.0}};
        for (int i = 0; i < expectedSimplex.length; i++) {
            assertArrayEquals(expectedSimplex[i], actualSimplex[i].getPointRef(), 0);
        }
    }

    @Test
    public void testEvaluate_ValidInput_EachPointInSimplexHasValueAssigned() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        MultivariateFunction function = point -> point[0] + point[1];
        Comparator<PointValuePair> comparator = Comparator.comparingDouble(PointValuePair::getValue);
        simplex.build(new double[]{1, 2, 3});

        simplex.evaluate(function, comparator);
        PointValuePair[] actualSimplex = simplex.getPoints();

        for (PointValuePair vertex : actualSimplex) {
            assertNotEquals(Double.NaN, vertex.getValue(), 0);
        }
    }

    @Test
    public void testReplaceWorstPoint_ValidInput_PointValuePairReplacedWithNewPointValuePair() {
        AbstractSimplex simplex = new AbstractSimplex(4);
        simplex.build(new double[]{1, 2, 3, 4});
        PointValuePair pointValuePair = new PointValuePair(new double[]{-1, -2, -3, -4}, -4.0);

        Comparator<PointValuePair> comparator = Comparator.comparingDouble(PointValuePair::getValue);
        simplex.replaceWorstPoint(pointValuePair, comparator);

        PointValuePair[] actualSimplex = simplex.getPoints();
        double[][] expectedSimplex = {{-1, -2, -3, -4}, {2, 3, 4, 5}, {3, 4, 5, 6}, {4, 5, 6, 7}};
        for (int i = 0; i < expectedSimplex.length; i++) {
            assertArrayEquals(expectedSimplex[i], actualSimplex[i].getPointRef(), 0);
        }
    }

    @Test
    public void testGetPoints_ValidInput_ReturnCopyOfSimplex() {
        AbstractSimplex simplex = new AbstractSimplex(4);
        simplex.build(new double[]{1, 2, 3, 4});

        PointValuePair[] actualPoints = simplex.getPoints();

        PointValuePair[] expectedPoints = simplex.getPoints();
        assertArrayEquals(expectedPoints, actualPoints);
        assertNotSame(expectedPoints, actualPoints);
    }

    @Test
    public void testGetPoint_ValidIndex_ReturnPointValuePairAtIndex() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});

        PointValuePair actualPoint = simplex.getPoint(2);

        double[] expectedPoint = {3, 4, 5};
        assertArrayEquals(expectedPoint, actualPoint.getPointRef(), 0);
    }

    @Test
    public void testGetPoint_OutOfRangeIndex_ThrowOutOfRangeException() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});

        try {
            PointValuePair actualPoint = simplex.getPoint(4);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // pass
        }
    }

    @Test
    public void testSetPoint_ValidIndexAndPoint_SetPointAtSpecifiedIndex() {
        AbstractSimplex simplex = new AbstractSimplex(4);
        simplex.build(new double[]{1, 2, 3, 4});

        PointValuePair pointValuePair = new PointValuePair(new double[]{5, 6, 7, 8}, 8.0);
        simplex.setPoint(0, pointValuePair);

        PointValuePair[] actualSimplex = simplex.getPoints();
        double[][] expectedSimplex = {{5, 6, 7, 8}, {2, 3, 4, 5}, {3, 4, 5, 6}, {4, 5, 6, 7}};
        for (int i = 0; i < expectedSimplex.length; i++) {
            assertArrayEquals(expectedSimplex[i], actualSimplex[i].getPointRef(), 0);
        }
    }

    @Test
    public void testSetPoint_OutOfRangeIndex_ThrowOutOfRangeException() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1, 2, 3});

        try {
            PointValuePair pointValuePair = new PointValuePair(new double[]{5, 6, 7, 8}, 8.0);
            simplex.setPoint(4, pointValuePair);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // pass
        }
    }

    @Test
    public void testSetPoints_ValidPoints_SetPointsInSimplex() {
        AbstractSimplex simplex = new AbstractSimplex(4);
        simplex.build(new double[]{1, 2, 3, 4});

        PointValuePair[] newPoints = {
                new PointValuePair(new double[]{5, 6, 7, 8}, 8.0),
                new PointValuePair(new double[]{6, 7, 8, 9}, 9.0),
                new PointValuePair(new double[]{7, 8, 9, 10}, 10.0),
                new PointValuePair(new double[]{8, 9, 10, 11}, 11.0),
                new PointValuePair(new double[]{9, 10, 11, 12}, 12.0)
        };
        simplex.setPoints(newPoints);

        PointValuePair[] actualPoints = simplex.getPoints();
        assertArrayEquals(newPoints, actualPoints);
    }

    @Test
    public void testSetPoints_DimensionMismatch_ThrowDimensionMismatchException() {
        AbstractSimplex simplex = new AbstractSimplex(4);
        simplex.build(new double[]{1, 2, 3, 4});

        PointValuePair[] newPoints = {
                new PointValuePair(new double[]{5, 6}, 8.0),
                new PointValuePair(new double[]{6, 7, 8, 9}, 9.0),
                new PointValuePair(new double[]{7, 8, 9, 10}, 10.0),
                new PointValuePair(new double[]{8, 9, 10, 11}, 11.0),
                new PointValuePair(new double[]{9, 10, 11, 12}, 12.0)
        };

        try {
            simplex.setPoints(newPoints);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // pass
        }
    }

    @Test
    public void testConstructHypercubeSteps_ValidInput_ReturnArrayWithSameValue() {
        int n = 5;
        double sideLength = 2;

        double[] actualSteps = AbstractSimplex.createHypercubeSteps(n, sideLength);

        double[] expectedSteps = {2, 2, 2, 2, 2};
        assertArrayEquals(expectedSteps, actualSteps, 0);
    }

}