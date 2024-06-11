package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;
    private UnivariateFunction function;
    private double[] startPoint;
    private double[] expectedOptimum;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                        {new SimpleFunction(), new double[]{1.0}, new double[]{0.0}},
                        {new SimpleFunction(), new double[]{2.0}, new double[]{1.0}},
                        {new SinFunction(), new double[]{0.0}, new double[]{-1.0}}
                });
    }

    public NonLinearConjugateGradientOptimizerTest(UnivariateFunction function, double[] startPoint, double[] expectedOptimum) {
        this.function = function;
        this.startPoint = startPoint;
        this.expectedOptimum = expectedOptimum;
    }

    @Test
    public void testOptimize() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
        optimizer.setMaxEvaluations(1000);
        optimizer.optimize(function, startPoint);
        double[] result = optimizer.getPoint();
        assertEquals(expectedOptimum.length, result.length);
        assertArrayEquals(expectedOptimum, result, 0.001);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testDivergence() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
        optimizer.setMaxEvaluations(10);
        optimizer.optimize(function, startPoint);
    }

    @Test
    public void testSetInitialStep() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
        optimizer.setInitialStep(2.0);
        optimizer.optimize(function, startPoint);
        double[] result = optimizer.getPoint();
        assertEquals(expectedOptimum.length, result.length);
        assertArrayEquals(expectedOptimum, result, 0.001);
    }

    private static class SimpleFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public double[] valuePartials(double x) {
            return new double[]{2 * x};
        }
    }

    private static class SinFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sin(x);
        }

        @Override
        public double[] valuePartials(double x) {
            return new double[]{Math.cos(x)};
        }
    }
}