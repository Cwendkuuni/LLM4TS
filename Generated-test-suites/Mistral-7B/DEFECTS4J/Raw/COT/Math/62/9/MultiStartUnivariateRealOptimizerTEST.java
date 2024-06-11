package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.special.E;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {
    private static final double ACCURACY = 1e-6;
    private static final int MAX_EVALUATIONS = 1000;

    private BaseUnivariateRealOptimizer<FUNC> optimizer;
    private MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer;
    private FUNC function;
    private GoalType goal;
    private double min;
    private double max;
    private double expectedMin;
    private double[] expectedStarts;

    @Parameterized.Parameters(name = "{index}: function={0}, goal={1}, bounds={2}, expectedMin={3}, expectedStarts={4}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                // Test function: f(x) = x^2
                {new PolynomialFunction(2), GoalType.MINIMIZE, new SimpleBounds(0, 10), 4.0, new double[]{2.0, 8.0, 3.5}},
                {new PolynomialFunction(2), GoalType.MAXIMIZE, new SimpleBounds(0, 10), 100.0, new double[]{1.0, 9.5, 5.5}},

                // Test function: f(x) = exp(x)
                {new PolynomialFunction(E.EXP), GoalType.MINIMIZE, new SimpleBounds(0, 10), 1.718281828, new double[]{1.0, 2.0, 3.0}},
                {new PolynomialFunction(E.EXP), GoalType.MAXIMIZE, new SimpleBounds(0, 10), 424.0, new double[]{8.0, 5.0, 7.3}},

                // Test function: f(x) = sin(x)
                {new PolynomialFunction(Math::sin), GoalType.MINIMIZE, new SimpleBounds(0, Math.PI), -1.0, new double[]{Math.PI / 2.0, Math.PI / 4.0, Math.PI * (3.0 / 2.0)}},
                {new PolynomialFunction(Math::sin), GoalType.MAXIMIZE, new SimpleBounds(0, Math.PI), 1.0, new double[]{Math.PI / 6.0, Math.PI * (2.0 / 3.0), Math.PI / 3.0}}
        });
    }

    @Parameterized.Constructor
    public MultiStartUnivariateRealOptimizerTest(BaseUnivariateRealOptimizer<FUNC> optimizer,
                                               FUNC function,
                                               GoalType goal,
                                               double min,
                                               double max,
                                               double expectedMin,
                                               double[] expectedStarts) {
        this.optimizer = optimizer;
        this.function = function;
        this.goal = goal;
        this.min = min;
        this.max = max;
        this.expectedMin = expectedMin;
        this.expectedStarts = expectedStarts;
        this.multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());
    }

    @Test
    public void testMultiStartOptimization() throws Exception {
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        multiStartOptimizer.setMaxEvaluations(MAX_EVALUATIONS * 3);

        PointValuePair result = multiStartOptimizer.optimize(function, goal, min, max);

        assertEquals(expectedMin, result.getPoint(), ACCURACY);

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertNotNull(optima);
        assertEquals(3, optima.length);
        Arrays.sort(optima, new Comparator<UnivariateRealPointValuePair>() {
            public int compare(UnivariateRealPointValuePair o1, UnivariateRealPointValuePair o2) {
                if (o1 == null) {
                    return (o2 == null) ? 0 : 1;
                } else if (o2 == null) {
                    return -1;
                }
                double v1 = o1.getValue();
                double v2 = o2.getValue();
                return (goal == GoalType.MINIMIZE) ?
                        Double.compare(v1, v2) : Double.compare(v2, v1);
            }
        });

        assertArrayEquals(expectedStarts, new double[]{optima[0].getPoint(), optima[1].getPoint(), optima[2].getPoint()});
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws Exception {
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        multiStartOptimizer.setMaxEvaluations(MAX_EVALUATIONS * 3);

        multiStartOptimizer.optimize(new PolynomialFunction(x -> x * x - 5 * x + 6), GoalType.MINIMIZE, -10, 10);
    }
}