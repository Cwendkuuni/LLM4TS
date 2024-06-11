package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;

public class AbstractSimplexTest {

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexConstructornull() {
        new AbstractSimplex(null, 1.0);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexConstructorZeroLength() {
        new AbstractSimplex(new double[] {});
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexConstructorZeroSteps() {
        new AbstractSimplex(new double[] {0.0});
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexConstructorZeroStepsEqualVerticies() {
        new AbstractSimplex(new double[] {1.0, 0.0});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexConstructorDimensionMismatch() {
        new AbstractSimplex(new double[][] {{1.0}});
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexConstructorSizeNotStrictlyPositive() {
        new AbstractSimplex(new double[][] {});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildDimensionMismatch() {
        AbstractSimplex simplex = createSimplex();
        simplex.build(new double[] {1.0, 2.0});
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = createSimplex();
        simplex.build(new double[] {1.0});
        PointValuePair[] points = simplex.getPoints();
        Assert.assertEquals(2, points.length);
        Assert.assertArrayEquals(new double[] {1.0}, points[0].getPoint(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        AbstractSimplex simplex = createSimplex();
        simplex.getPoint(2);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = createSimplex();
        simplex.build(new double[] {1.0});
        Assert.assertEquals(new PointValuePair(new double[] {1.0}, Double.NaN), simplex.getPoint(0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        AbstractSimplex simplex = createSimplex();
        simplex.setPoint(2, new PointValuePair(new double[] {1.0}, Double.NaN));
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = createSimplex();
        simplex.build(new double[] {1.0});
        simplex.setPoint(0, new PointValuePair(new double[] {2.0}, Double.NaN));
        Assert.assertEquals(new PointValuePair(new double[] {2.0}, Double.NaN), simplex.getPoint(0));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsDimensionMismatch() {
        AbstractSimplex simplex = createSimplex();
        simplex.build(new double[] {1.0});
        simplex.setPoints(new PointValuePair[] {
            new PointValuePair(new double[] {1.0}, Double.NaN),
            new PointValuePair(new double[] {2.0}, Double.NaN)
        });
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = createSimplex();
        simplex.build(new double[] {1.0});
        simplex.setPoints(new PointValuePair[] {
            new PointValuePair(new double[] {2.0}, Double.NaN),
            new PointValuePair(new double[] {3.0}, Double.NaN)
        });
        Assert.assertEquals(new PointValuePair(new double[] {2.0}, Double.NaN), simplex.getPoint(0));
        Assert.assertEquals(new PointValuePair(new double[] {3.0}, Double.NaN), simplex.getPoint(1));
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = createSimplex();
        Assert.assertEquals(1, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = createSimplex();
        Assert.assertEquals(3, simplex.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testIterateNullFunction() {
        AbstractSimplex simplex = createSimplex();
        simplex.iterate(null, null);
    }

    @Test(expected = OutOfRangeException.class)
    public void testIterateNullComparator() {
        AbstractSimplex simplex = createSimplex();
        simplex.iterate(new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        }, null);
    }

    @Test
    public void testEvaluateNaNValue() {
        AbstractSimplex simplex = createSimplex();
        simplex.build(new double[] {1.0});
        simplex.evaluate(new MultivariateFunction() {
            public double value(double[] point) {
                return Double.NaN;
            }
        }, null);
        Assert.assertTrue(Double.isNaN(simplex.getPoint(0).getValue()));
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = createSimplex();
        simplex.build(new double[] {1.0});
        simplex.evaluate(new MultivariateFunction() {
            public double value(double[] point) {
                return point[0];
            }
        }, null);
        Assert.assertEquals(1.0, simplex.getPoint(0).getValue(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testReplaceWorstPointOutOfRange() {
        AbstractSimplex simplex = createSimplex();
        simplex.replaceWorstPoint(new PointValuePair(new double[] {1.0}, Double.NaN), null);
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = createSimplex();
        simplex.build(new double[] {1.0});
        simplex.replaceWorstPoint(new PointValuePair(new double[] {2.0}, Double.NaN), null);
        Assert.assertEquals(new PointValuePair(new double[] {2.0}, Double.NaN), simplex.getPoint(2));
    }

    private AbstractSimplex createSimplex() {
        return new AbstractSimplex(new double[][] {
            {0.0}, 
            {1.0}, 
            {2.0}
        }) {
            public void iterate(final MultivariateFunction p0, final Comparator<PointValuePair> p1) {
                return;
            }
        };
    }

}