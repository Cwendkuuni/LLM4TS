import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.util.Pair;

import java.util.Arrays;
import java.util.Comparator;

public class AbstractSimplexTest {

    @Test
    public void testConstructorWithSteps_NullArgumentException() {
        try {
            new AbstractSimplex(null);
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithSteps_ZeroException() {
        try {
            new AbstractSimplex(new double[0]);
            Assert.fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithSteps_EqualVerticesInSimplex() {
        try {
            new AbstractSimplex(new double[] { 1, 2, 1 });
            Assert.fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithReferenceSimplex_NotStrictlyPositiveException() {
        try {
            new AbstractSimplex(new double[0][0]);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithReferenceSimplex_DimensionMismatchException() {
        try {
            double[][] referenceSimplex = new double[3][2];
            referenceSimplex[0] = new double[] { 1, 2 };
            referenceSimplex[1] = new double[] { 1, 2, 3 };
            referenceSimplex[2] = new double[] { 1, 2 };
            new AbstractSimplex(referenceSimplex);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithReferenceSimplex_EqualVerticesInSimplex() {
        try {
            double[][] referenceSimplex = new double[3][2];
            referenceSimplex[0] = new double[] { 1, 2 };
            referenceSimplex[1] = new double[] { 1, 2 };
            referenceSimplex[2] = new double[] { 1, 2 };
            new AbstractSimplex(referenceSimplex);
            Assert.fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        Assert.assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        Assert.assertEquals(4, simplex.getSize());
    }

    @Test
    public void testIterate() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return 0;
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        };

        simplex.iterate(function, comparator);
        // TODO: add assertions for iterate method
    }

    @Test
    public void testBuild_DimensionMismatchException() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        double[] startPoint = new double[] { 1, 2 };
        try {
            simplex.build(startPoint);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        double[] startPoint = new double[] { 1, 2, 3 };
        simplex.build(startPoint);
        PointValuePair[] expectedPoints = new PointValuePair[4];
        expectedPoints[0] = new PointValuePair(startPoint, Double.NaN);
        expectedPoints[1] = new PointValuePair(new double[] { 2, 4, 6 }, Double.NaN);
        expectedPoints[2] = new PointValuePair(new double[] { 3, 6, 9 }, Double.NaN);
        expectedPoints[3] = new PointValuePair(new double[] { 4, 8, 12 }, Double.NaN);
        Assert.assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return 0;
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        };

        simplex.evaluate(function, comparator);
        // TODO: add assertions for evaluate method
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        PointValuePair point = new PointValuePair(new double[] { 4, 5, 6 }, Double.NaN);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        };

        simplex.replaceWorstPoint(point, comparator);
        // TODO: add assertions for replaceWorstPoint method
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        PointValuePair[] points = new PointValuePair[4];
        points[0] = new PointValuePair(new double[] { 1, 2, 3 }, Double.NaN);
        points[1] = new PointValuePair(new double[] { 2, 4, 6 }, Double.NaN);
        points[2] = new PointValuePair(new double[] { 3, 6, 9 }, Double.NaN);
        points[3] = new PointValuePair(new double[] { 4, 8, 12 }, Double.NaN);
        Assert.assertArrayEquals(points, simplex.getPoints());
    }

    @Test
    public void testGetPoint_OutOfRangeException() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        try {
            simplex.getPoint(4);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        PointValuePair expectedPoint = new PointValuePair(new double[] { 2, 4, 6 }, Double.NaN);
        Assert.assertEquals(expectedPoint, simplex.getPoint(1));
    }

    @Test
    public void testSetPoint_OutOfRangeException() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        PointValuePair point = new PointValuePair(new double[] { 2, 4, 6 }, Double.NaN);
        try {
            simplex.setPoint(4, point);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        PointValuePair point = new PointValuePair(new double[] { 2, 4, 6 }, Double.NaN);
        simplex.setPoint(1, point);
        PointValuePair[] expectedPoints = new PointValuePair[4];
        expectedPoints[0] = new PointValuePair(new double[] { 1, 2, 3 }, Double.NaN);
        expectedPoints[1] = point;
        expectedPoints[2] = new PointValuePair(new double[] { 3, 6, 9 }, Double.NaN);
        expectedPoints[3] = new PointValuePair(new double[] { 4, 8, 12 }, Double.NaN);
        Assert.assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test
    public void testSetPoints_DimensionMismatchException() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        PointValuePair[] points = new PointValuePair[3];
        try {
            simplex.setPoints(points);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] { 1, 2, 3 });
        PointValuePair[] points = new PointValuePair[4];
        points[0] = new PointValuePair(new double[] { 1, 2, 3 }, Double.NaN);
        points[1] = new PointValuePair(new double[] { 2, 4, 6 }, Double.NaN);
        points[2] = new PointValuePair(new double[] { 3, 6, 9 }, Double.NaN);
        points[3] = new PointValuePair(new double[] { 4, 8, 12 }, Double.NaN);
        simplex.setPoints(points);
        Assert.assertArrayEquals(points, simplex.getPoints());
    }

    @Test
    public void testCreateHypercubeSteps() {
        double[] expectedSteps = new double[] { 5, 5, 5, 5 };
        double[] steps = AbstractSimplex.createHypercubeSteps(4, 5);
        Assert.assertArrayEquals(expectedSteps, steps, 0);
    }

}