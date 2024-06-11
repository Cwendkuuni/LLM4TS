import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testConstructor1() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void testConstructor2() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3, 2.0);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testConstructor3() {
        double[] steps = {2.0, 3.0, 4.0};
        AbstractSimplex simplex = new AbstractSimplexImpl(steps);
        assertEquals(3, simplex.getDimension());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructor_NullSteps() {
        double[] steps = null;
        AbstractSimplex simplex = new AbstractSimplexImpl(steps);
    }

    @Test(expected = ZeroException.class)
    public void testConstructor_ZeroSteps() {
        double[] steps = {};
        AbstractSimplex simplex = new AbstractSimplexImpl(steps);
    }

    @Test
    public void testConstructor4() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        AbstractSimplex simplex = new AbstractSimplexImpl(referenceSimplex);
        assertEquals(2, simplex.getDimension());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_EmptyReferenceSimplex() {
        double[][] referenceSimplex = {};
        AbstractSimplex simplex = new AbstractSimplexImpl(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_DimensionMismatch() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {2.0, 4.0}};
        AbstractSimplex simplex = new AbstractSimplexImpl(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructor_EqualVertices() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {1.0, 2.0, 3.0}, {3.0, 4.0, 5.0}};
        AbstractSimplex simplex = new AbstractSimplexImpl(referenceSimplex);
    }
    
    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplexImpl(5);
        assertEquals(5, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testIterate() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        simplex.iterate(new MultivariateFunctionImpl(), new ComparatorImpl());
        assertNotNull(simplex.getPoints());
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuild_DimensionMismatch() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        simplex.build(new double[]{1.0, 2.0});
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        simplex.evaluate(new MultivariateFunctionImpl(), new ComparatorImpl());
        assertNotNull(simplex.getPoints());
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        simplex.evaluate(new MultivariateFunctionImpl(), new ComparatorImpl());
        PointValuePair point = simplex.getPoint(0);
        simplex.replaceWorstPoint(point, new ComparatorImpl());
        assertNotNull(simplex.getPoints());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_OutOfRange() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        PointValuePair point = simplex.getPoint(5);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_OutOfRange() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        PointValuePair point = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0);
        simplex.setPoint(5, point);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPoints_DimensionMismatch() {
        AbstractSimplex simplex = new AbstractSimplexImpl(3);
        PointValuePair[] points = {new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0)};
        simplex.setPoints(points);
    }

    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(5, 2.0);
        assertEquals(5, steps.length);
        for (double step : steps) {
            assertEquals(2.0, step, 0.0);
        }
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
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {}
    }

    private class MultivariateFunctionImpl implements MultivariateFunction {
        public double value(double[] point) {
            return 0.0;
        }
    }

    private class ComparatorImpl implements Comparator<PointValuePair> {
        public int compare(PointValuePair p1, PointValuePair p2) {
            return 0;
        }
    }
}