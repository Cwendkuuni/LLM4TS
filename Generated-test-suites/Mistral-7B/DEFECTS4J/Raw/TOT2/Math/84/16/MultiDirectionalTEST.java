import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MultiDirectionalTest {

    @Test
    public void testIterateSimplexWithDefaultCoefficients() throws Exception {
        // given
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[4];
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };

        // when
        optimizer.iterateSimplex(comparator);

        // then
        Assert.assertNotNull(optimizer.getBestX());
        Assert.assertNotNull(optimizer.getBestF());
        Assert.assertFalse(Double.isNaN(optimizer.getBestF()));
    }

    @Test
    public void testIterateSimplexWithCustomCoefficients() throws Exception {
        // given
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.8);
        RealPointValuePair[] simplex = new RealPointValuePointValuePair[4];
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };

        // when
        optimizer.iterateSimplex(comparator);

        // then
        Assert.assertNotNull(optimizer.getBestX());
        Assert.assertNotNull(optimizer.getBestF());
        Assert.assertFalse(Double.isNaN(optimizer.getBestF()));
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexThrowsFunctionEvaluationException() throws Exception {
        // given
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[4];
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                throw new FunctionEvaluationException("Test exception");
            }
        };

        // when
        optimizer.iterateSimplex(comparator);

        // then
        // test fails with expected FunctionEvaluationException
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexThrowsOptimizationException() throws Exception {
        // given
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[4];
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                throw new OptimizationException("Test exception");
            }
        };

        // when
        optimizer.iterateSimplex(comparator);

        // then
        // test fails with expected OptimizationException
    }

    @Test
    public void testEvaluateNewSimplex() throws Exception {
        // given
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[4];
        RealPointValuePair best = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false);
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };

        // when
        RealPointValuePair result = optimizer.evaluateNewSimplex(original, 1.5, comparator);

        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(best.getValue(), result.getValue(), 0.001);
    }

    @Test(expected = SimpleOptimizationException.class)
    public void testEvaluateNewSimplexThrowsSimpleOptimizationException() throws Exception {
        // given
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[4];
        RealPointValuePair best = new RealPointValuePair(new double[]{1.0, 1.0}, 2.0, false);
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                throw new SimpleOptimizationException("Test exception");
            }
        };

        // when
        optimizer.evaluateNewSimplex(original, 1.5, comparator);

        // then
        // test fails with expected SimpleOptimizationException
    }
}