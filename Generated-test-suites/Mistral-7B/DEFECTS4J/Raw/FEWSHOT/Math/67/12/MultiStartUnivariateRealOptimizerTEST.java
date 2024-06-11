package org.apache.commons.cli.optimization; // assuming the test class is in the same package as MultiStartUnivariateRealOptimizer

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AmoebaInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() throws OptimizationException {
        UnivariateRealFunction function = new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0});
        GoalType goalType = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        int maxIterations = 100;
        int maxEvaluations = 1000;
        int starts = 10;
        RandomGenerator randomGenerator = new AmoebaInitializer();

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateRealOptimizer(function), starts, randomGenerator);
        optimizer.setMaxIterations(maxIterations);
        optimizer.setMaxEvaluations(maxEvaluations);

        double result = optimizer.optimize(function, goalType, min, max);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        assertEquals("Number of iterations should be less than maxIterations", maxIterations, optimizer.getIterationCount());
        assertEquals("Number of function evaluations should be less than maxEvaluations", maxEvaluations, optimizer.getEvaluations());
        assertNotEquals("Optima array should have at least one element", 0, optima.length);
        assertArrayEquals("Optima array should contain the expected values", new double[]{result}, optima);
        assertArrayEquals("OptimaValues array should contain the expected values", new double[]{function.value(result)}, optimaValues);
    }
}