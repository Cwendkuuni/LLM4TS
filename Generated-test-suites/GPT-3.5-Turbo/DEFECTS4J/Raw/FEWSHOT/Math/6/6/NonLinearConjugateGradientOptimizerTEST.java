package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
    @Test
    public void testDoOptimize() {
        // Test case 1
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.5);
        optimizer.setStartPoint(new double[]{1, 1});
        optimizer.setMaxEvaluations(1000);
        optimizer.setConvergenceChecker(new SimpleValueChecker(1e-6, 1e-6));
        PointValuePair result = optimizer.optimize(new ObjectiveFunction(new Rosenbrock()), GoalType.MINIMIZE, new InitialGuess(new double[]{0, 0}));
        assertEquals(1, result.getPointRef()[0], 1e-6);
        assertEquals(1, result.getPointRef()[1], 1e-6);
        assertEquals(0, result.getValue(), 1e-6);

        // Test case 2
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(0.5);
        optimizer.setStartPoint(new double[]{0, 0});
        optimizer.setMaxEvaluations(1000);
        optimizer.setConvergenceChecker(new SimpleValueChecker(1e-6, 1e-6));
        result = optimizer.optimize(new ObjectiveFunction(new Rosenbrock()), GoalType.MINIMIZE, new InitialGuess(new double[]{0, 0}));
        assertArrayEquals(new double[]{1, 1}, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);
    }

    private class Rosenbrock implements MultivariateFunction {
        public double value(double[] variables) {
            double x = variables[0];
            double y = variables[1];
            return Math.pow(1 - x, 2) + 100 * Math.pow(y - x * x, 2);
        }
    }
}