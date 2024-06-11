package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSensitiveOptimizer;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction testFunction;
    private BaseUnivariateRealOptimizer<UnivariateFunction> optimizerMock;
    private MultiStartUnivariateRealOptimizer<UnivariateFunction> multiStartOptimizer;
    private RandomGenerator randomGeneratorMock;

    @Before
    public void setUp() throws Exception {
        testFunction = mock(UnivariateFunction.class);
        optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        randomGeneratorMock = mock(RandomGenerator.class);

        when(testFunction.getValue(Double.MIN_VALUE)).thenThrow(new MathIllegalArgumentException());
        when(testFunction.getValue(0.5)).thenReturn(1.2);
        when(testFunction.getValue(1.5)).thenReturn(1.1);
        when(testFunction.getValue(2.5)).thenReturn(0.8);
        when(testFunction.getValue(Double.MAX_VALUE)).thenThrow(new MathIllegalArgumentException());

        when(optimizerMock.optimize(testFunction, GoalType.MINIMIZE, new SimpleBounds(0.1, 3.5))).thenReturn(new PointValuePair(1.1, 1.5));
        when(optimizerMock.optimize(testFunction, GoalType.MAXIMIZE, new SimpleBounds(0.1, 3.5))).thenReturn(new PointValueValuePair(0.8, 2.5));

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, randomGeneratorMock);
    }

    @Test
    public void testGetOptimaWithMultipleStarts() {
        PointValuePair[] optima = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.1, 3.5);
        UnivariateRealPointValuePair[] multiStartOptima = multiStartOptimizer.getOptima();

        assertArrayEquals(new PointValuePair[] { new PointValuePair(1.1, 1.5), new PointValuePair(1.1, 1.5), new PointValuePair(1.1, 1.5) }, optima);
        assertArrayEquals(multiStartOptima, new UnivariateRealPointValuePair[] { new UnivariateRealPointValuePair(1.1, 1.5), null, null });
    }

    @Test
    public void testGetOptimaWithSingleStart() {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 1, randomGeneratorMock);
        PointValuePair[] optima = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.1, 3.5);

        UnivariateRealPointValuePair[] multiStartOptima = multiStartOptimizer.getOptima();

        assertArrayEquals(new PointValuePair[] { new PointValuePair(1.1, 1.5) }, optima);
        assertSame(multiStartOptima, new UnivariateRealPointValuePair[] { new UnivariateRealPointValuePair(1.1, 1.5) });
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaBeforeOptimization() {
        multiStartOptimizer.getOptima();
    }

    @Test
    public void testOptimizeWithMultipleStarts() {
        PointValuePair[] optima = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.1, 3.5);

        assertArrayEquals(new PointValuePair[] { new PointValuePair(1.1, 1.5), new PointValuePair(1.1, 1.5), new PointValuePair(1.1, 1.5) }, optima);
    }

    @Test
    public void testOptimizeWithSingleStart() {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 1, randomGeneratorMock);
        PointValuePair[] optima = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.1, 3.5);

        assertArrayEquals(new PointValuePair[] { new PointValuePair(1.1, 1.5) }, optima);
    }

    @Test
    public void testOptimizeWithGoalTypeSensitiveOptimizer() {
        GoalTypeSensitiveOptimizer optimizerMockGoalTypeSensitive = mock(GoalTypeSensitiveOptimizer.class);
        when(optimizerMockGoalTypeSensitive.optimize(testFunction, GoalType.MINIMIZE, new SimpleBounds(0.1, 3.5))).thenReturn(new PointValuePair(1.1, 1.5));
        when(optimizerMockGoalTypeSensitive.optimize(testFunction, GoalType.MAXIMIZE, new SimpleBounds(0.1, 3.5))).thenReturn(new PointValuePair(0.8, 2.5));

        MultiStartUnivariateRealOptimizer<GoalTypeSensitiveOptimizer> multiStartOptimizerGoalTypeSensitive = new MultiStartUnivariateRealOptimizer<>(optimizerMockGoalTypeSensitive, 3, randomGeneratorMock);

        PointValuePair[] optima = multiStartOptimizerGoalTypeSensitive.optimize(testFunction, GoalType.MINIMIZE, 0.1, 3.5);

        UnivariateRealPointValuePair[] multiStartOptima = multiStartOptimizerGoalTypeSensitive.getOptima();

        assertArrayEquals(new PointValuePair[] { new PointValuePair(1.1, 1.5), new PointValuePair(1.1, 1.5), new PointValuePair(1.1, 1.5) }, optima);
        assertArrayEquals(multiStartOptima, new UnivariateRealPointValuePair[] { new UnivariateRealPointValuePair(1.1, 1.5), null, null });
    }
}