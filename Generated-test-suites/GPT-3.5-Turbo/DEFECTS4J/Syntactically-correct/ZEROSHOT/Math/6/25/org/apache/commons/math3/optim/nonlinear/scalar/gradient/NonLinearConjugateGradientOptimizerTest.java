import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0);
        assertEquals(1.0, optimizer.getInitialStep(), 0);
    }

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1.0);
        optimizer.setMaxEvaluations(1000);
        double[] startPoint = {0, 0};
        double[] endPoint = {1, 2};
        TestFunction testFunction = new TestFunction();
        optimizer.optimize(new MaxEval(1000), testFunction, GoalType.MINIMIZE, startPoint);
        double[] result = optimizer.getOptimum().getPoint();
        assertEquals(endPoint[0], result[0], 0.001);
        assertEquals(endPoint[1], result[1], 0.001);
    }
    
    private class TestFunction implements DifferentiableMultivariateFunction {

        @Override
        public double[] value(double[] point) {
            double x = point[0];
            double y = point[1];
            double[] values = new double[2];
            values[0] = Math.pow(x - 1, 2) + Math.pow(y - 2, 2);
            values[1] = Math.pow(x + 1, 2) + Math.pow(y - 2, 2);
            return values;
        }

        @Override
        public MultivariateMatrixFunction jacobian() {
            return new MultivariateMatrixFunction() {
                @Override
                public double[][] value(double[] point) throws IllegalArgumentException {
                    double x = point[0];
                    double y = point[1];
                    double[][] jacobian = new double[2][2];
                    jacobian[0][0] = 2 * (x - 1);
                    jacobian[0][1] = 2 * (y - 2);
                    jacobian[1][0] = 2 * (x + 1);
                    jacobian[1][1] = 2 * (y - 2);
                    return jacobian;
                }
            };
        }

    }
}