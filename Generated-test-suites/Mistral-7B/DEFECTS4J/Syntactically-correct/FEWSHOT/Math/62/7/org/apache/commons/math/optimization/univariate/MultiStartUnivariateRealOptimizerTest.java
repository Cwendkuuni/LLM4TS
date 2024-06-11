package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSorter;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSorter.SortedPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.function.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.goaltype.FMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.goaltype.FMinimizer.MinimizerType;
import org.apache.commons.math3.optim.nonlinear.scalar.goaltype.FMaximizer;
import org.apache.commons.math3.optim.nonlinear.scalar.goaltype.FMaximizer.MaximizerType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer.BrentMethod;
import org.apache.commons.math3.random.RandomValuesGenerator;

import org.apache.commons.cli.MultiStartUnivariateRealOptimizer;
import org.apache.commons.cli.MultiStartUnivariateRealOptimizer.BaseUnivariateRealOptimizer;

/**
 * Test cases for {@link MultiStartUnivariateRealOptimizer} class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    @SuppressWarnings("unchecked")
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> getTestOptimizer() {
        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mock(BrentOptimizer.class);
        when(optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenReturn(new UnivariateRealPointValuePair(0.0, 0.0));
        when(optimizer.getMaxEvaluations()).thenReturn(MaxEval.UNLIMITED);
        when(optimizer.getConvergenceChecker()).thenReturn(mock(ConvergenceChecker.class));
        when(optimizer.getGoalType()).thenReturn(GoalType.MINIMIZE);
        return optimizer;
    }

    @Test
    public void testMinimize() {
        // Test with a mock optimizer.
        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = getTestOptimizer();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomValuesGenerator());

        // Test with a linear function.
        final UnivariateRealFunction function = new LinearFunction(1.0, 2.0, 3.0);
        final GoalType goal = GoalType.MINIMIZE;
        final double min = Double.NEGATIVE_INFINITY;
        final double max = Double.POSITIVE_INFINITY;

        final UnivariateRealPointValuePair[] expectedOptima = new UnivariateRealPointValuePair[] {
                new UnivariateRealPointValuePair(-1.0, 1.0),
                new UnivariateRealPointValuePair(1.0, 2.0),
                new UnivariateRealPointValuePair(0.0, 0.0)
        };

        final UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, goal, min, max);
        assertArrayEquals(expectedOptima, multiStartOptimizer.getOptima());
        assertEquals(3 * MaxEval.UNLIMITED, multiStartOptimizer.getEvaluations());
        assertEquals(result, expectedOptima[2]);

        // Test with a non-linear function.
        final UnivariateRealFunction nonlinearFunction = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x * x) + 2;
            }
        };

        final UnivariateRealPointValuePair[] nonlinearExpectedOptima = new UnivariateRealPointValuePair[] {
                new UnivariateRealPointValuePair(-3.14159265359, -3.14159265359),
                new UnivariateRealPointValuePair(0.785398163397, -0.695579447091),
                new UnivariateRealPointValuePair(1.81143103388, 2.126191275097)
        };

        final UnivariateRealPointValuePair nonlinearResult = multiStartOptimizer.optimize(nonlinearFunction, goal, min, max);
        assertArrayEquals(nonlinearExpectedOptima, multiStartOptimizer.getOptima());
        assertEquals(3 * MaxEval.UNLIMITED, multiStartOptimizer.getEvaluations());
        assertEquals(nonlinearResult, nonlinearExpectedOptima[2]);
    }

    @Test
    public void testMaximize() {
        // Test with a mock optimizer.
        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = getTestOptimizer();
        when(optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenReturn(new UnivariateRealPointValuePair(0.0, 0.0));
        when(optimizer.getMaxEvaluations()).thenReturn(MaxEval.UNLIMITED);
        when(optimizer.getConvergenceChecker()).thenReturn(mock(ConvergenceChecker.class));
        when(optimizer.getGoalType()).thenReturn(GoalType.MAXIMIZE);

        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomValuesGenerator());

        // Test with a linear function.
        final UnivariateRealFunction function = new LinearFunction(-1.0, 2.0, 3.0);
        final GoalType goal = GoalType.MAXIMIZE;
        final double min = Double.NEGATIVE_INFINITY;
        final double max = Double.POSITIVE_INFINITY;

        final UnivariateRealPointValuePair[] expectedOptima = new UnivariateRealPointValuePair[] {
                new UnivariateRealPointValuePair(1.0, 1.0),
                new UnivariateRealPointValuePair(2.0, 2.0),
                new UnivariateRealPointValuePair(0.0, 0.0)
        };

        final UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, goal, min, max);
        assertArrayEquals(expectedOptima, multiStartOptimizer.getOptima());
        assertEquals(3 * MaxEval.UNLIMITED, multiStartOptimizer.getEvaluations());
        assertEquals(result, expectedOptima[2]);

        // Test with a non-linear function.
        final UnivariateRealFunction nonlinearFunction = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) * Math.exp(-x * x) + 2;
            }
        };

        final UnivariateRealPointValuePair[] nonlinearExpectedOptima = new UnivariateRealPointValuePair[] {
                new UnivariateRealPointValuePair(3.14159265359, 3.14159265359),
                new UnivariateRealPointValuePair(0.785398163397, 0.695579447091),
                new UnivariateRealPointValuePair(-1.81143103388, 2.126191275097)
        };

        final UnivariateRealPointValuePair nonlinearResult = multiStartOptimizer.optimize(nonlinearFunction, goal, min, max);
        assertArrayEquals(nonlinearExpectedOptima, multiStartOptimizer.getOptima());
        assertEquals(3 * MaxEval.UNLIMITED, multiStartOptimizer.getEvaluations());
        assertEquals(nonlinearResult, nonlinearExpectedOptima[2]);
    }

    @Test
    public void testNoConvergence() {
        // Test with a mock optimizer that always throws a ConvergenceException.
        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mock(BrentOptimizer.class);
        when(optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenThrow(new ConvergenceException(LocalizedFormats.NO_CONVERGENCE, null));

        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomValuesGenerator());

        final UnivariateRealFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };

        final GoalType goal = GoalType.MINIMIZE;
        final double min = Double.NEGATIVE_INFINITY;
        final double max = Double.POSITIVE_INFINITY;

        try {
            multiStartOptimizer.optimize(function, goal, min, max);
            fail("Expected a ConvergenceException to be thrown.");
        } catch (ConvergenceException expected) {
            final UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
            assertEquals(Arrays.asList(null), Arrays.asList(optima));
        }
    }

    @Test
    public void testSortOptima() {
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(getTestOptimizer(), 3, new RandomValuesGenerator());

        final UnivariateRealFunction function = new LinearFunction(1.0, 2.0, 3.0);
        final GoalType goal = GoalType.MINIMIZE;
        final double min = Double.NEGATIVE_INFINITY;
        final double max = Double.POSITIVE_INFINITY;

        multiStartOptimizer.optimize(function, goal, min, max);

        final UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        final GoalTypeSorter sorter = new GoalTypeSorter(goal, new FMinimizer(MinimizerType.NORMAL_LINE_SEARCH));

        final SortedPointValuePair[] sortedOptima = sorter.sort(optima);
        final UnivariateRealPointValuePair lastOptimum = sortedOptima[sortedOptima.length - 1].getPoint();

        assertArrayEquals(new UnivariateRealPointValuePair[] {
                new UnivariateRealPointValuePair(-1.0, 1.0),
                new UnivariateRealPointValuePair(1.0, 2.0),
                new UnivariateRealPointValuePair(0.0, 0.0)
        }, optima);

        assertEquals(0.0, lastOptimum.getValue(), 1e-6);
    }

    @Test
    public void testSortOptimaMaximize() {
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(getTestOptimizer(), 3, new RandomValuesGenerator());

        final UnivariateRealFunction function = new LinearFunction(-1.0, 2.0, 3.0);
        final GoalType goal = GoalType.MAXIMIZE;
        final double min = Double.NEGATIVE_INFINITY;
        final double max = Double.POSITIVE_INFINITY;

        multiStartOptimizer.optimize(function, goal, min, max);

        final UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        final GoalTypeSorter sorter = new GoalTypeSorter(goal, new FMaximizer(MaximizerType.SECANT));

        final SortedPointValuePair[] sortedOptima = sorter.sort(optima);
        final UnivariateRealPointValuePair lastOptimum = sortedOptima[sortedOptima.length - 1].getPoint();

        assertArrayEquals(new UnivariateRealPointValuePair[] {
                new UnivariateRealPointValuePair(1.0, 1.0),
                new UnivariateRealPointValuePair(2.0, 2.0),
                new UnivariateRealPointValuePair(0.0, 0.0)
        }, optima);

        assertEquals(0.0, lastOptimum.getValue(), 1e-6);
    }

    @Test
    public void testSortOptimaNoConvergence() {
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(mock(BrentOptimizer.class), 3, new RandomValuesGenerator());

        final UnivariateRealFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };

        final GoalType goal = GoalType.MINIMIZE;
        final double min = Double.NEGATIVE_INFINITY;
        final double max = Double.POSITIVE_INFINITY;

        multiStartOptimizer.optimize(function, goal, min, max);

        final UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        final GoalTypeSorter sorter = new GoalTypeSorter(goal, new FMinimizer(MinimizerType.NORMAL_LINE_SEARCH));

        final SortedPointValuePair[] sortedOptima = sorter.sort(optima);
        final UnivariateRealPointValuePair lastOptimum = sortedOptima[sortedOptima.length - 1];

        assertArrayEquals(new UnivariateRealPointValuePair[0], optima);
        assertNull(lastOptimum);
    }
}