package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.initialGuess.RandomInitialGuesser;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.PolynomialObjectiveFunction;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.special.E;

import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartOptimizer() {
        // Test with a simple polynomial function
        UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 1.0);
        GoalType goal = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new DifferentialEvolution(new RandomInitialGuesser(new MersenneTwister()), 10),
                5,
                new RandomDataGenerator()
        );
        optimizer.setMaxEvaluations(20);
        PointValuePair best = optimizer.optimize(f, goal, min, max);
        assertEquals(E.EULER, best.getPoint(), 1e-5);
        assertEquals(-E.EULER, best.getValue(), 1e-5);
        assertArrayEquals(new UnivariateRealPointValuePair[]{best}, optimizer.getOptima());

        // Test with a function that has multiple local minima
        UnivariateRealFunction f2 = new PolynomialFunction(3, -5.0, 5.0);
        optimizer = new MultiStartUnivariateRealOptimizer<>(
                new DifferentialEvolution(new RandomInitialGuesser(new MersenneTwister()), 10),
                10,
                new RandomDataGenerator()
        );
        optimizer.setMaxEvaluations(50);
        best = optimizer.optimize(f2, goal, min, max);
        assertArrayNotEquals(new UnivariateRealPointValuePair[]{best}, optimizer.getOptima());
        PointValuePair[] optima = optimizer.getOptima();
        for (PointValuePair op : optima) {
            assertNotNull(op);
            if (op != best) {
                assertNotSame(best, op);
                assertEquals(-5.0 + Math.random() * 10.0, op.getPoint(), 1e-5);
                assertEquals(Math.abs(Math.E * Math.random()), op.getValue(), 1e-5);
            }
        }

        // Test with a function that does not converge
        UnivariateRealFunction f3 = new PolynomialFunction(1, 0.0, 0.0);
        optimizer.setMaxEvaluations(10);
        try {
            optimizer.optimize(f3, goal, min, max);
            fail(" optimization should have thrown a ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
            assertArrayEquals(new UnivariateRealPointValuePair[5], optimizer.getOptima());
        }
    }
}