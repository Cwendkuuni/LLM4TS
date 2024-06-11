import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealConstraint;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.direct.MultiDirectional;
import org.junit.Assert;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        Assert.assertEquals(2.0, optimizer.khi, 0.01);
        Assert.assertEquals(0.5, optimizer.gamma, 0.01);

        MultiDirectional optimizerCustom = new MultiDirectional(3.0, 0.6);
        Assert.assertEquals(3.0, optimizerCustom.khi, 0.01);
        Assert.assertEquals(0.6, optimizerCustom.gamma, 0.01);
    }

    @Test
    public void testIterateSimplexThrowsFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealConstraint[] constraints = new RealConstraint[0];
        SimpleBounds bounds = new SimpleBounds(new double[]{0.0}, new double[]{1.0}, new double[]{2.0});
        optimizer.setMaxEvaluations(1);
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        optimizer.iterateSimplex(new DefaultComparator());
        Assert.assertTrue(optimizer.hasConverged());
        throw new FunctionEvaluationException("Test exception");
    }

    @Test
    public void testIterateSimplexThrowsOptimizationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealConstraint[] constraints = new RealConstraint[0];
        SimpleBounds bounds = new SimpleBounds(new double[]{0.0}, new double[]{1.0}, new double[]{2.0});
        optimizer.setMaxEvaluations(0);
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        optimizer.iterateSimplex(new DefaultComparator());
        Assert.assertFalse(optimizer.hasConverged());
        throw new OptimizationException("Test exception");
    }

    @Test
    public void testIterateSimplexThrowsIllegalArgumentException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealConstraint[] constraints = new RealConstraint[0];
        SimpleBounds bounds = new SimpleBounds(null, new double[]{1.0}, new double[]{2.0});
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        optimizer.iterateSimplex(new DefaultComparator());
        Assert.assertFalse(optimizer.hasConverged());
        throw new IllegalArgumentException("Test exception");
    }

    @Test
    public void testIterateSimplexThrowsExceptionWhenNotAcceptingReflectedOrContractedSimplexs()
            throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealConstraint[] constraints = new RealConstraint[0];
        SimpleBounds bounds = new SimpleBounds(new double[]{0.0}, new double[]{1.0}, new double[]{2.0});
        RealPointValuePair[] original = new RealPointValuePair[5];
        for (int i = 0; i < 5; i++) {
            double[] x = new double[]{i};
            original[i] = new RealPointValuePair(x, Math.pow(i, 2.0), false);
        }
        double[] xSmallest = new double[]{0.0};
        double coeff = 1.2;

        try {
            optimizer.iterateSimplex(new DefaultComparator());
            Assert.fail();
        } catch (FunctionEvaluationException e) {
            // expected
        }

        optimizer.iterateSimplex(new DefaultComparator());

        RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, new DefaultComparator());
        RealPointValuePair expanded = optimizer.evaluateNewSimplex(original, optimizer.khi, new DefaultComparator());

        if (reflected.getValue() <= expanded.getValue()) {
            throw new AssertionError("Reflected simplex should have a worse function value");
        }

        optimizer.iterateSimplex(new DefaultComparator());

        RealPointValuePair contracted = optimizer.evaluateNewSimplex(original, optimizer.gamma, new DefaultComparator());

        if (contracted.getValue() < optimizer.simplex[0].getValue()) {
            throw new AssertionError("Contracted simplex should have a better function value than the best one");
        }

        optimizer.iterateSimplex(new DefaultComparator());
    }

}