import org.junit.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;

public class AbstractSimplexTest {

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplex_NullArguments() {
        AbstractSimplex as = new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_ZeroArguments() {
        AbstractSimplex as = new AbstractSimplex(new double[]{});
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_EqualVertices() {
        AbstractSimplex as = new AbstractSimplex(new double[]{0.0, 0.0});
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplex_NotPositiveVertices() {
        AbstractSimplex as = new AbstractSimplex(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_DimensionMismatch() {
        AbstractSimplex as = new AbstractSimplex(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0}});
    }

    @Test
    public void testAbstractSimplex_CorrectArguments() {
        AbstractSimplex as1 = new AbstractSimplex(3);
        AbstractSimplex as2 = new AbstractSimplex(3, 2.0);
        AbstractSimplex as3 = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        AbstractSimplex as4 = new AbstractSimplex(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}});
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex as = new AbstractSimplex(3);
        Assert.assertEquals(3, as.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex as = new AbstractSimplex(3);
        Assert.assertEquals(4, as.getSize());
    }

    @Test
    public void testBuild() {
        AbstractSimplex as = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        as.build(startPoint);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuild_DimensionMismatch() {
        AbstractSimplex as = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0, 4.0};
        as.build(startPoint);
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex as = new AbstractSimplex(3);
        as.evaluate(new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair pv1, PointValuePair pv2) {
                return Double.compare(pv1.getValue(), pv2.getValue());
            }
        });
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex as = new AbstractSimplex(3);
        PointValuePair pv = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        as.replaceWorstPoint(pv, new Comparator<PointValuePair>() {
            public int compare(PointValuePair pv1, PointValuePair pv2) {
                return Double.compare(pv1.getValue(), pv2.getValue());
            }
        });
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex as = new AbstractSimplex(3);
        PointValuePair[] points = as.getPoints();
        Assert.assertEquals(4, points.length);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_OutOfRange() {
        AbstractSimplex as = new AbstractSimplex(3);
        PointValuePair pv = as.getPoint(10);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex as = new AbstractSimplex(3);
        PointValuePair pv = as.getPoint(2);
        Assert.assertNotNull(pv);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_OutOfRange() {
        AbstractSimplex as = new AbstractSimplex(3);
        PointValuePair pv = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        as.setPoint(10, pv);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex as = new AbstractSimplex(3);
        PointValuePair pv = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        as.setPoint(2, pv);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPoints_DimensionMismatch() {
        AbstractSimplex as = new AbstractSimplex(3);
        PointValuePair[] points = new PointValuePair[] {new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0)};
        as.setPoints(points);
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex as = new AbstractSimplex(3);
        PointValuePair[] points = new PointValuePair[] {new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0), new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0)};
        as.setPoints(points);
    }

    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 2.0);
        Assert.assertArrayEquals(new double[]{2.0, 2.0, 2.0}, steps, 0.0);
    }
}