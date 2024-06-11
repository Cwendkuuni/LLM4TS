package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.UnivariateRealPointValuePair;
import org.apache.commons.math.optimization.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.random.RandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {

    @SuppressWarnings("unchecked")
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> getMockOptimizer() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mock(BaseUnivariateRealOptimizer.class);
        when(optimizer.optimize(Mockito.any(UnivariateRealFunction.class), Mockito.any(GoalType.class),
                                Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(new UnivariateRealPointValuePair(1.0, 1.0));
        return optimizer;
    }

    @Test
    public void testMultiStartWithMinimizer() throws Exception {
        final int starts = 3;
        final int maxEvaluations = 10;
        final RandomGenerator generator = new Random();

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = getMockOptimizer();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);

        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        UnivariateRealPointValuePair[] expectedOptima = new UnivariateRealPointValuePair[starts];
        for (int i = 0; i < starts; i++) {
            double[] startPoints = new double[]{min + (i == 0) ? 0.0 : generator.nextDouble() * (max - min),
                                              min + (i == 0) ? 1.0 : generator.nextDouble() * (max - min)};
            expectedOptima[i] = new UnivariateRealPointValuePair(startPoints[0], 1.0);
        }

        MultiStartUnivariateRealOptimizer.UnivariateRealPointValuePair[] resultOptima = multiStartOptimizer.optimize(function, goal, min, max);

        assertArrayEquals(expectedOptima, resultOptima);
    }

    @Test
    public void testMultiStartWithMaximizer() throws Exception {
        final int starts = 3;
        final int maxEvaluations = 10;
        final RandomGenerator generator = new Random();

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = getMockOptimizer();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);

        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 10.0;

        UnivariateRealPointValuePair[] expectedOptima = new UnivariateRealPointValuePair[starts];
        for (int i = 0; i < starts; i++) {
            double[] startPoints = new double[]{min + (i == 0) ? 0.0 : generator.nextDouble() * (max - min),
                                              min + (i == 0) ? 1.0 : generator.nextDouble() * (max - min)};
            expectedOptima[i] = new UnivariateRealPointValuePair(startPoints[0], 1.0);
        }

        MultiStartUnivariateRealOptimizer.UnivariateRealPointValuePair[] resultOptima = multiStartOptimizer.optimize(function, goal, min, max);

        assertArrayEquals(expectedOptima, resultOptima);
    }

    @Test(expected = ConvergenceException.class)
    public void testMultiStartWithoutConvergence() throws Exception {
        final int starts = 3;
        final int maxEvaluations = 10;
        final RandomGenerator generator = new Random();

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = getMockOptimizer();
        when(optimizer.optimize(Mockito.any(UnivariateRealFunction.class), Mockito.any(GoalType.class),
                                Mockito.anyDouble(), Mockito.anyDouble())).thenThrow(new ConvergenceException());

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);

        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        multiStartOptimizer.optimize(function, goal, min, max);
    }
}