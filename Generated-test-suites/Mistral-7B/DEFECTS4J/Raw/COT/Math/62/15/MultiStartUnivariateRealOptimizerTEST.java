package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateFunction> {

    private UnivariateOptimizer<FUNC> optimizerMock;
    private RandomGenerator randomGeneratorMock;
    private MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer;
    private PointValuePair[] expectedOptima;
    private PointValuePair[] actualOptima;

    @Before
    public void setUp() {
        optimizerMock = mock(UnivariateOptimizer.class);
        randomGeneratorMock = mock(RandomGenerator.class);

        when(optimizerMock.optimize(Mockito.any(FUNC.class), Mockito.any(GoalType.class),
                Mockito.anyDouble(), Mockito.anyDouble())).thenCallRealMethod();

        when(randomGeneratorMock.nextDouble(Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(0.5);

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<FUNC>(optimizerMock, 3, randomGeneratorMock);
        expectedOptima = new PointValuePair[3];
    }

    @After
    public void tearDown() {
        multiStartOptimizer = null;
        expectedOptima = null;
        optimizerMock = null;
        randomGeneratorMock = null;
    }

    /**
     * Test basic functionality of optimize method with minimum goal type.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testMinimization() throws Exception {
        FUNC f = new PolynomialFunction(new double[][]{{-1.0, 2.0}, {0.0, 1.0}, {1.0, -2.0}}, 0);
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        expectedOptima[0] = new PointValuePair(-1.5, 2.25);
        expectedOptima[1] = new PointValuePair(1.5, 1.25);
        expectedOptima[2] = new PointValuePair(4.5, 6.75);
        when(optimizerMock.optimize(Mockito.any(FUNC.class), Mockito.eq(goal), Mockito.anyDouble(),
                Mockito.anyDouble())).thenReturn(expectedOptima[0]);
        when(optimizerMock.getMaxEvaluations()).thenReturn(MaxEval.INFINITE);

        actualOptima = multiStartOptimizer.optimize(f, goal, min, max);

        assertArrayEquals(expectedOptima, actualOptima);
    }

    /**
     * Test basic functionality of optimize method with maximum goal type.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testMaximization() throws Exception {
        FUNC f = new PolynomialFunction(new double[][]{{-1.0, 2.0}, {0.0, 1.0}, {1.0, -2.0}}, 0);
        GoalType goal = GoalType.MAXIMIZE;
        double min = -10.0;
        double max = 10.0;

        expectedOptima[0] = new PointValuePair(-1.5, -2.25);
        expectedOptima[1] = new PointValuePair(1.5, 1.25);
        expectedOptima[2] = new PointValuePair(4.5, 6.75);
        when(optimizerMock.optimize(Mockito.any(FUNC.class), Mockito.eq(goal), Mockito.anyDouble(),
                Mockito.anyDouble())).thenReturn(expectedOptima[0]);
        when(optimizerMock.getMaxEvaluations()).thenReturn(MaxEval.INFINITE);

        actualOptima = multiStartOptimizer.optimize(f, goal, min, max);

        assertArrayEquals(expectedOptima, actualOptima);
    }

    /**
     * Test convergence exception handling.
     *
     * @throws Exception if test fails
     */
    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws Exception {
        FUNC f = new PolynomialFunction(new double[][]{{-1.0, 2.0}, {0.0, 1.0}, {1.0, -2.0}}, 0);
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        when(optimizerMock.optimize(Mockito.any(FUNC.class), Mockito.any(GoalType.class),
                Mockito.anyDouble(), Mockito.anyDouble())).thenThrow(new ConvergenceException());

        multiStartOptimizer.optimize(f, goal, min, max);
    }

    /**
     * Test function evaluation exception handling.
     *
     * @throws Exception if test fails
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        FUNC f = new PolynomialFunction(new double[][]{{-1.0, 2.0}, {0.0, 1.0}, {1.0, -2.0}}, 0) {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Error message");
            }
        };
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        multiStartOptimizer.optimize(f, goal, min, max);
    }

    /**
     * Test max evaluations exceeded exception handling.
     *
     * @throws Exception if test fails
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testMaxEvalExceededException() throws Exception {
        FUNC f = new PolynomialFunction(new double[][]{{-1.0, 2.0}, {0.0, 1.0}, {1.0, -2.0}}, 0);
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        int maxEvaluations = 5;

        when(optimizerMock.optimize(Mockito.any(FUNC.class), Mockito.any(GoalType.class),
                Mockito.anyDouble(), Mockito.anyDouble())).thenCallRealMethod();
        when(optimizerMock.getMaxEvaluations()).thenReturn(maxEvaluations);

        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        multiStartOptimizer.optimize(f, goal, min, max);

        when(optimizerMock.optimize(Mockito.any(FUNC.class), Mockito.any(GoalType.class),
                Mockito.anyDouble(), Mockito.anyDouble())).thenThrow(new MaxEvalExceededException());

        multiStartOptimizer.optimize(f, goal, min, max);
    }

    /**
     * Test bounds exception handling.
     *
     * @throws Exception if test fails
     */
    @Test(expected = NoBracketException.class)
    public void testBoundsException() throws Exception {
        FUNC f = new PolynomialFunction(new double[][]{{-1.0, 2.0}, {0.0, 1.0}, {1.0, -2.0}}, 0);
        GoalType goal = GoalType.MINIMIZE;
        double min = -5.0;
        double max = -4.0;

        multiStartOptimizer.optimize(f, goal, min, max);
    }

}