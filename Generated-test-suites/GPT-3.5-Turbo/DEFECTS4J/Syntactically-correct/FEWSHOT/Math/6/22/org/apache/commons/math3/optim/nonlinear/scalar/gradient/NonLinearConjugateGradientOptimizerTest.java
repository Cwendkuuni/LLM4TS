package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setMaxEvaluations(100);
        optimizer.setInitialStep(0.1);

        double[] startPoint = {1.0, 2.0};
        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 0.0;

        PointValuePair result = optimizer.doOptimize(new MyObjectiveFunction(), startPoint);

        assertNotNull(result);
        assertEquals(expectedPoint.length, result.getPoint().length);
        assertEquals(expectedValue, result.getValue(), 1e-8);
        for (int i = 0; i < expectedPoint.length; i++) {
            assertEquals(expectedPoint[i], result.getPoint()[i], 1e-8);
        }
    }

    private static class MyObjectiveFunction implements DifferentiableMultivariateFunction {

        @Override
        public double value(double[] variables) {
            double x = variables[0];
            double y = variables[1];
            return x * x + y * y;
        }

        @Override
        public MultivariateVectorFunction gradient() {
            return new MultivariateVectorFunction() {
                @Override
                public double[] value(double[] variables) {
                    double x = variables[0];
                    double y = variables[1];
                    double[] gradient = new double[2];
                    gradient[0] = 2 * x;
                    gradient[1] = 2 * y;
                    return gradient;
                }
            };
        }
    }
}