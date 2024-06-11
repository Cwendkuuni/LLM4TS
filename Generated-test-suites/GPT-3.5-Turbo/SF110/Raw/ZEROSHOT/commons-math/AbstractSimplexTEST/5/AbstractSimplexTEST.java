import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.exception.util.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.MultivariateFunction;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.analysis.MultivariateFunction;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        Assert.assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexWithSideLength() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3, 2.0);
        Assert.assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexWithSteps() {
        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplexImpl(steps);
        Assert.assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexWithReferenceSimplex() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        AbstractSimplex simplex = new AbstractSimplexImpl(referenceSimplex);
        Assert.assertEquals(2, simplex.getDimension());
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        Assert.assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        Assert.assertEquals(0, simplex.getSize());

        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        Assert.assertEquals(4, simplex.getSize());
    }

    @Test
    public void testIterate() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        MultivariateFunction p0 = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[1] * point[2];
            }
        };
        Comparator<PointValuePair> p1 = new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        simplex.iterate(p0, p1);
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        Assert.assertEquals(startPoint, points[0].getPoint());
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[1] * point[2];
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        simplex.build(new double[] {1.0, 2.0, 3.0});
        simplex.evaluate(evaluationFunction, comparator);
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        PointValuePair pointValuePair = new PointValuePair(new double[] {1.0, 2.0, 3.0}, 10.0);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        simplex.build(new double[] {1.0, 2.0, 3.0});
        simplex.replaceWorstPoint(pointValuePair, comparator);
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        simplex.build(new double[] {1.0, 2.0, 3.0});
        PointValuePair[] points = simplex.getPoints();
        Assert.assertEquals(4, points.length);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        simplex.build(new double[] {1.0, 2.0, 3.0});
        PointValuePair point = simplex.getPoint(0);
        Assert.assertEquals(1.0, point.getPoint()[0], 0.0);
        Assert.assertEquals(2.0, point.getPoint()[1], 0.0);
        Assert.assertEquals(3.0, point.getPoint()[2], 0.0);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        simplex.build(new double[] {1.0, 2.0, 3.0});
        PointValuePair point = new PointValuePair(new double[] {4.0, 5.0, 6.0}, 20.0);
        simplex.setPoint(0, point);
        Assert.assertEquals(4.0, simplex.getPoint(0).getPoint()[0], 0.0);
        Assert.assertEquals(5.0, simplex.getPoint(0).getPoint()[1], 0.0);
        Assert.assertEquals(6.0, simplex.getPoint(0).getPoint()[2], 0.0);
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        simplex.build(new double[] {1.0, 2.0, 3.0});
        PointValuePair[] points = simplex.getPoints();
        simplex.setPoints(points);
        Assert.assertEquals(points.length, simplex.getSize());
    }

    private class AbstractSimplexImpl extends AbstractSimplex {
        public AbstractSimplexImpl(int n) {
            super(n);
        }

        public AbstractSimplexImpl(int n, double sideLength) {
            super(n, sideLength);
        }

        public AbstractSimplexImpl(double[] steps) {
            super(steps);
        }

        public AbstractSimplexImpl(double[][] referenceSimplex) {
            super(referenceSimplex);
        }

        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // implementation here
        }
    }
}