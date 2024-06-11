import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BFGS;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test MultiStartUnivariateRealOptimizer class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testConstructor() {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        // checks if the constructor is called correctly
        Assert.assertEquals(optimizer, multiStartOptimizer.getOptimizer());
        Assert.assertEquals(3, multiStartOptimizer.getStarts());
        Assert.assertNull(multiStartOptimizer.getOptima());
        Assert.assertNull(multiStartOptimizer.getOptimaValues());
        Assert.assertEquals(Integer.MAX_VALUE, multiStartOptimizer.getMaxIterations());
        Assert.assertEquals(Integer.MAX_VALUE, multiStartOptimizer.getMaxEvaluations());
        Assert.assertEquals(0, multiStartOptimizer.getTotalIterations());
        Assert.assertEquals(0, multiStartOptimizer.getTotalEvaluations());
    }

    @Test
    public void testGetFunctionValue() {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        double result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0)[1];
        Assert.assertEquals(result, multiStartOptimizer.getFunctionValue(), 0.001);
    }

    @Test
    public void testGetResult() {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        double[] optima = multiStartOptimizer.getOptima();
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        double result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0)[0];
        Assert.assertEquals(result, optima[0], 0.001);
        Assert.assertEquals(function.value(result), optimaValues[0], 0.001);
    }

    @Test
    public void testGetTotalIterations() {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        int iterations = multiStartOptimizer.getTotalIterations();
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        Assert.assertEquals(iterations + optimizer.getIterationCount(), multiStartOptimizer.getTotalIterations());
    }

    @Test
    public void testGetTotalEvaluations() {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        int evaluations = multiStartOptimizer.getTotalEvaluations();
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        Assert.assertEquals(evaluations + optimizer.getEvaluations(), multiStartOptimizer.getTotalEvaluations());
    }

    @Test
    public void testSetMaxIterations() {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        multiStartOptimizer.setMaxIterations(5);
        Assert.assertEquals(5, multiStartOptimizer.getMaxIterations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIterationsNegative() {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        multiStartOptimizer.setMaxIterations(-1);
    }

    @Test
    public void testSetMaxEvaluations() {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        multiStartOptimizer.setMaxEvaluations(5);
        Assert.assertEquals(5, multiStartOptimizer.getMaxEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluationsNegative() {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        multiStartOptimizer.setMaxEvaluations(-1);
    }

    @Test
    public void testGetOptima() throws IllegalStateException {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        double[] optima = multiStartOptimizer.getOptima();
        double[] expectedOptima = {0.2, 0.4, 0.6};
        Assert.assertArrayEquals(expectedOptima, optima, 0.001);
    }

    @Test
    public void testGetOptimaValues() throws IllegalStateException {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        double[] expectedOptimaValues = {0.1, 0.3, 0.5};
        Assert.assertArrayEquals(expectedOptimaValues, optimaValues, 0.001);
    }

    @Test
    public void testMultipleCallsToOptimize() {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        double[] firstOptima = new double[3];
        double[] firstOptimaValues = new double[3];
        double[] secondOptima = new double[3];
        double[] secondOptimaValues = new double[3];
        for (int i = 0; i < 2; i++) {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            firstOptima = multiStartOptimizer.getOptima();
            firstOptimaValues = multiStartOptimizer.getOptimaValues();
            Arrays.sort(firstOptima);
            Arrays.sort(firstOptimaValues);
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            secondOptima = multiStartOptimizer.getOptima();
            secondOptimaValues = multiStartOptimizer.getOptimaValues();
            Arrays.sort(secondOptima);
            Arrays.sort(secondOptimaValues);
            Assert.assertArrayEquals(firstOptima, secondOptima);
            Assert.assertArrayEquals(firstOptimaValues, secondOptimaValues);
        }
    }

    @Test
    public void testConvergenceException() {
        UnivariateRealFunction function = new ConvergenceExceptionFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            Assert.fail("An exception should have been thrown");
        } catch (ConvergenceException e) {
            // expected result
        }
        Assert.assertArrayEquals(new double[0], multiStartOptimizer.getOptima(), 0.0);
        Assert.assertArrayEquals(new double[0], multiStartOptimizer.getOptimaValues(), 0.0);
    }

    @Test
    public void testThrowsIllegalStateExceptionIfNoOptimizationHasBeenPerformed() {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MyRandomGenerator());
        try {
            multiStartOptimizer.getOptima();
            Assert.fail("An exception should have been thrown");
        } catch (IllegalStateException e) {
            // expected result
        }
        try {
            multiStartOptimizer.getOptimaValues();
            Assert.fail("An exception should have been thrown");
        } catch (IllegalStateException e) {
            // expected result
        }
    }

    @Test
    public void testThrowsIllegalArgumentExceptionIfLessThanOneStarts() {
        UnivariateRealFunction function = new SimpleFunction();
        UnivariateRealOptimizer optimizer = new BFGS();
        try {
            new MultiStartUnivariateRealOptimizer(optimizer, 0, new MyRandomGenerator());
            Assert.fail("An exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected result
        }
    }

    private static class SimpleFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 2.0);
        }
    }

    private static class ConvergenceExceptionFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            throw new ConvergenceException();
        }

        @Override
        public double value(double x, int maxEvaluations) throws FunctionEvaluationException {
            throw new FunctionEvaluationException();
        }
    }

    private static class MyRandomGenerator implements RandomGenerator {

        @Override
        public double nextGaussian() {
            return 0.0;
        }

        @Override
        public int nextInt(int n) {
            return 0;
        }

        @Override
        public long nextLong() {
            return 0;
        }
    }
}