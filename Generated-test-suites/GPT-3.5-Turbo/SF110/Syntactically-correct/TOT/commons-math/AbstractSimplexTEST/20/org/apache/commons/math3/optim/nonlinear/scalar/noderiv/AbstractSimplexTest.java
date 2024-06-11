import org.junit.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import java.util.*;

public class AbstractSimplexTest {

    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        simplex = new AbstractSimplexImpl(3);
    }

    @After
    public void tearDown() {
        simplex = null;
    }

    @Test
    public void testGetDimension() {
        Assert.assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        simplex.build(new double[]{1, 2, 3});
        Assert.assertEquals(4, simplex.getSize());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildWithDifferentDimension() {
        simplex.build(new double[]{1, 2});
    }

    @Test
    public void testIterate() {
        simplex.build(new double[]{1, 2, 3});
        simplex.iterate(new MultivariateFunction() {
            public double value(double[] values) {
                return values[0] + values[1] + values[2];
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        PointValuePair[] points = simplex.getPoints();
        Assert.assertEquals(4, points.length);
        Assert.assertEquals(1, points[0].getPoint()[0], 0.0001);
        Assert.assertEquals(2, points[0].getPoint()[1], 0.0001);
        Assert.assertEquals(3, points[0].getPoint()[2], 0.0001);
    }

    @Test
    public void testEvaluate() {
        simplex.build(new double[]{1, 2, 3});
        simplex.evaluate(new MultivariateFunction() {
            public double value(double[] values) {
                return values[0] + values[1] + values[2];
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        PointValuePair[] points = simplex.getPoints();
        Assert.assertEquals(4, points.length);
        Assert.assertEquals(6, points[0].getValue(), 0.0001);
        Assert.assertEquals(1, points[0].getPoint()[0], 0.0001);
        Assert.assertEquals(2, points[0].getPoint()[1], 0.0001);
        Assert.assertEquals(3, points[0].getPoint()[2], 0.0001);
    }

    @Test
    public void testReplaceWorstPoint() {
        simplex.build(new double[]{1, 2, 3});
        simplex.evaluate(new MultivariateFunction() {
            public double value(double[] values) {
                return values[0] + values[1] + values[2];
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
        PointValuePair newPoint = new PointValuePair(new double[]{4, 5, 6}, 15);
        simplex.replaceWorstPoint(newPoint, new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        Assert.assertEquals(newPoint, simplex.getPoint(simplex.getSize() - 1));
        Assert.assertNotEquals(worstPoint, simplex.getPoint(simplex.getSize() - 1));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointWithInvalidIndex() {
        simplex.build(new double[]{1, 2, 3});
        simplex.getPoint(simplex.getSize());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointWithInvalidIndex() {
        simplex.build(new double[]{1, 2, 3});
        simplex.setPoint(simplex.getSize(), new PointValuePair(new double[]{4, 5, 6}, 15));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsWithDifferentDimension() {
        simplex.build(new double[]{1, 2, 3});
        simplex.setPoints(new PointValuePair[]{new PointValuePair(new double[]{4, 5}, 12)});
    }

    private class AbstractSimplexImpl extends AbstractSimplex {
        public AbstractSimplexImpl(int n) {
            super(n);
        }
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {}
    }

}