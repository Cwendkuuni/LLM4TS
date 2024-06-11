import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationTest;
import org.apache.commons.math3.optim.direct.MultiDirectional;
import org.apache.commons.math3.optim.function.FunctionWithGradient;
import org.apache.commons.math3.optim.initializer.AverageStartingPointInitializer;
import org.apache.commons.math3.optim.point.PointValuePairComparator;
import org.junit.Assert;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        Assert.assertEquals(2.0, optimizer.khi, 0.01);
        Assert.assertEquals(0.5, optimizer.gamma, 0.01);
    }

    @Test
    public void testMultiDirectionalCustomConstructor() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.6);
        Assert.assertEquals(3.0, optimizer.khi, 0.01);
        Assert.assertEquals(0.6, optimizer.gamma, 0.01);
    }

    @Test
    public void testIterateSimplexDefaultComparator() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();

        double[] x = {1.0, 2.0};
        FunctionWithGradient function = new FunctionWithGradient() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[]{2.0 * x[0], 2.0 * x[1]};
            }
        };

        RealPointValuePair[] simplex = new RealPointValuePair[3];
        simplex[0] = new RealPointValuePair(x, 5.0);
        simplex[1] = new RealPointValueValuePair(new double[]{1.5, 2.5}, 5.5);
        simplex[2] = new RealPointValuePair(new double[]{2.5, 1.5}, 6.5);

        PointValuePairComparator comparator = new PointValuePairComparator();
        optimizer.iterateSimplex(comparator, function, new AverageStartingPointInitializer(x), 100);

        PointValuePair best = optimizer.getBestPoint();
        Assert.assertEquals(1.0, best.getPoint()[0], 0.01);
        Assert.assertEquals(1.0, best.getValue(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexNullComparator() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.iterateSimplex(null, new FunctionWithGradient() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[0];
            }
        }, new AverageStartingPointInitializer(new double[2]), 100);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        FunctionWithGradient function = new FunctionWithGradient() {
            @Override
            public double value(double[] x) throws Exception {
                throw new FunctionEvaluationException("Function evaluation exception");
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[0];
            }
        };

        RealPointValuePair[] simplex = new RealPointValuePair[3];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 5.0);
        simplex[1] = new RealPointValuePair(new double[]{1.5, 2.5}, 5.5);
        simplex[2] = new RealPointValuePair(new double[]{2.5, 1.5}, 6.5);

        PointValuePairComparator comparator = new PointValuePairComparator();
        optimizer.iterateSimplex(comparator, function, new AverageStartingPointInitializer(new double[2]), 100);
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexOptimizationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        FunctionWithGradient function = new FunctionWithGradient() {
            @Override
            public double value(double[] x) {
                throw new OptimizationException("Optimization exception");
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[0];
            }
        };

        RealPointValuePair[] simplex = new RealPointValuePair[3];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 5.0);
        simplex[1] = new RealPointValuePair(new double[]{1.5, 2.5}, 5.5);
        simplex[2] = new RealPointValuePair(new double[]{2.5, 1.5}, 6.5);

        PointValuePairComparator comparator = new PointValuePairComparator();
        optimizer.iterateSimplex(comparator, function, new AverageStartingPointInitializer(new double[2]), 1);
    }

    @Test
    public void testEvaluateNewSimplexNullOriginal() {
        MultiDirectional optimizer = new MultiDirectional();
        Assert.assertNull(optimizer.evaluateNewSimplex((RealPointValuePair[]) null, 1.0, new PointValuePairComparator()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateNewSimplexNegativeCoef() {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 5.0);
        original[1] = new RealPointValuePair(new double[]{1.5, 2.5}, 5.5);
        original[2] = new RealPointValuePair(new double[]{2.5, 1.5}, 6.5);

        try {
            optimizer.evaluateNewSimplex(original, -1.0, new PointValuePairComparator());
        } catch (Exception e) {
            Assert.assertEquals("IllegalArgumentException", e.getClass().getName());
        }
    }

    @Test
    public void testEvaluateNewSimplexValid() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 5.0);
        original[1] = new RealPointValuePair(new double[]{1.5, 2.5}, 5.5);
        original[2] = new RealPointValuePair(new double[]{2.5, 1.5}, 6.5);

        RealPointValuePair best = optimizer.evaluateNewSimplex(original, 1.5, new PointValuePairComparator());
        Assert.assertNotNull(best);
        Assert.assertEquals(original[0].getPointRef().length, best.getPointRef().length);
    }

}