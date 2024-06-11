import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1.5);
        assertEquals(1.5, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testDoOptimize() {
        // Create optimizer
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);

        // Create test data
        double[] point = {1.0, 2.0, 3.0};
        double[] values = {4.0, 5.0, 6.0};
        DummyDifferentiableScalarFunction function = new DummyDifferentiableScalarFunction(point, values);

        // Set optimizer parameters
        optimizer.setMaxEvaluations(1000);
        optimizer.setStartPoint(point);
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Optimize
        PointValuePair result = optimizer.optimize(function);
        double[] optimizedPoint = result.getPoint();
        double[] expectedPoint = {0.0, 0.0, 0.0};

        // Verify result
        assertArrayEquals(expectedPoint, optimizedPoint, 0.01);
    }

    private class DummyDifferentiableScalarFunction implements DifferentiableScalarFunction {

        private double[] point;
        private double[] values;

        public DummyDifferentiableScalarFunction(double[] point, double[] values) {
            this.point = point;
            this.values = values;
        }

        @Override
        public double value(double[] point) {
            double result = 0.0;
            for (int i = 0; i < point.length; i++) {
                result += values[i] * point[i];
            }
            return result;
        }

        @Override
        public double[] gradient(double[] point) {
            double[] gradient = new double[point.length];
            for (int i = 0; i < point.length; i++) {
                gradient[i] = values[i];
            }
            return gradient;
        }
    }

}