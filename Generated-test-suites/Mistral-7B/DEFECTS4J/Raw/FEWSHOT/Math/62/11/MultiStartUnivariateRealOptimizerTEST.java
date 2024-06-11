package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleLineSearch;
import org.apache.commons.math3.optim.nonlinear.convergence.StoppingCondition;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.LinearFitMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundingMethod;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleLineSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleXpressions;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

import org.apache.commons.math.optimization.univariate.GoalType;
import org.apache.commons.math.optimization.univariate.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;

public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimization with an simple polynomial function.
     */
    @Test
    public void testOptimizationPolynomialFunction() {
        UnivariateRealFunction function = new PolynomialFunction(-1.0, 2.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new LinearFitMinimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        double min = -5.0;
        double max = 5.0;
        GoalType goal = GoalType.MINIMIZE;

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.optimize(function, goal, min, max);
        assertTrue(Arrays.stream(optima).anyMatch(pair -> pair != null));
        assertArrayEquals(new double[]{1.0, function.value(1.0)}, new double[]{optima[0].getPoint()[0], optima[0].getValue()});

        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new UnivariateRealPointValuePair[]{new UnivariateRealPointValuePair(1.0, function.value(1.0))}, optima);
    }

    /**
     * Test optimization with a complex objective function.
     */
    @Test
    public void testOptimizationComplexFunction() {
        FunctionEvaluationException fe = null;
        try {
            ObjectiveFunction<Double> function = new ObjectiveFunction<Double>() {
                @Override
                public Double value(Double x) throws FunctionEvaluationException {
                    double y = Math.exp(-Math.pow(x, 2.0) / 2.0);
                    return y * Erf.erf(x / Math.sqrt(2.0));
                }

                @Override
                public Double value(Double x, Double[] gradient) throws FunctionEvaluationException {
                    double y = Math.exp(-Math.pow(x, 2.0) / 2.0);
                    double erf = Erf.erf(x / Math.sqrt(2.0));
                    double term1 = y * 2.0 * (1.0 / (Math.sqrt(2.0) * Math.PI)) * Erf.erfc(x / Math.sqrt(2.0));
                    double term2 = y * -Math.exp(-Math.pow(x, 2.0) / 2.0) / (Math.sqrt(2.0) * Math.sqrt(Math.PI));

                    if (gradient != null) {
                        gradient[0] = term1 - term2;
                    }
                    return y * Erf.erf(x / Math.sqrt(2.0));
                }
            };
            BaseUnivariateRealOptimizer<ObjectiveFunction<Double>> optimizer = new SimpleBoundingMethod(new SimpleLineSearch(new SimpleXpressions(), new StoppingCondition() {}));
            MultiStartUnivariateRealOptimizer<ObjectiveFunction<Double>> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

            double min = -5.0;
            double max = 5.0;
            GoalType goal = GoalType.MINIMIZE;

            UnivariateRealPointValuePair optimum = multiStartOptimizer.optimize(function, goal, min, max);
            assertTrue(Arrays.stream(multiStartOptimizer.getOptima()).anyMatch(pair -> pair != null));
            assertArrayEquals(new double[]{0.0, function.value(0.0)}, new double[]{optimum.getPoint()[0], optimum.getValue()});

        } catch (FunctionEvaluationException e) {
            fe = e;
        }

        assertTrue(fe != null);
    }

    /**
     * Test optimization with an initial guess close to the solution.
     */
    @Test
    public void testOptimizationCloseToSolution() {
        UnivariateRealFunction function = new PolynomialFunction(-1.0, 2.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new LinearFitMinimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        double min = -5.0;
        double max = 5.0;
        GoalType goal = GoalType.MINIMIZE;
        double initialGuess = 1.1;

        UnivariateRealPointValuePair optima = multiStartOptimizer.optimize(function, goal, min, max, initialGuess);
        assertTrue(Arrays.stream(optima).anyMatch(pair -> pair != null));
        assertArrayEquals(new double[]{initialGuess, function.value(initialGuess)}, new double[]{optima.getPoint()[0], optima.getValue()});

        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new UnivariateRealPointValuePair[]{new UnivariateRealPointValuePair(initialGuess, function.value(initialGuess))}, optima);
    }

    /**
     * Test optimization with an initial guess far from the solution.
     */
    @Test
    public void testOptimizationFarFromSolution() {
        UnivariateRealFunction function = new PolynomialFunction(-1.0, 2.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new LinearFitMinimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        double min = -5.0;
        double max = 5.0;
        GoalType goal = GoalType.MINIMIZE;
        double initialGuess = 10.0;

        UnivariateRealPointValuePair optima = multiStartOptimizer.optimize(function, goal, min, max, initialGuess);
        assertNull(optima);

        UnivariateRealPointValuePair[] optimaArray = multiStartOptimizer.getOptima();
        assertTrue(Arrays.stream(optimaArray).allMatch(pair -> pair == null));
    }
}