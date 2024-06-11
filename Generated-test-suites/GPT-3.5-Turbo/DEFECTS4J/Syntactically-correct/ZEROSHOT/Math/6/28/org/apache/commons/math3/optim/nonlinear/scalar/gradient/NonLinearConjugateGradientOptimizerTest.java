import org.junit.Test;

import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testSetInitialStepNegativeValue() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testSetInitialStepZeroValue() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.0);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setConvergenceChecker(new SimpleValueChecker());
        optimizer.setMaxEvaluations(1000);

        GoalType goal = GoalType.MINIMIZE;
        double[] startPoint = {1.0, 1.0};
        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 0.0;

        PointValuePair result = optimizer.optimize(100, new NonLinearConjugateGradientOptimizer.ClosestPairFunction(), goal, startPoint);
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0);
        assertEquals(expectedValue, result.getValue(), 0.0);
    }

    private static class ClosestPairFunction implements DifferentiableMultivariateFunction {

        @Override
        public double value(double[] point) {
            double x = point[0];
            double y = point[1];
            return Math.pow(x, 2) + Math.pow(y, 2);
        }

        @Override
        public double[] gradient(double[] point) {
            double x = point[0];
            double y = point[1];
            double dx = 2 * x;
            double dy = 2 * y;
            return new double[]{dx, dy};
        }
    }
}