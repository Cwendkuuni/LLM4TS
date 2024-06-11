package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.math.analysis.PolynomialFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.UnivariateRealFunction;
import org.apache.commons.math.optimization.UnivariateRealPointValuePair;
import org.apache.commons.math.random.RandomDataGenerator;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private RandomGenerator generator;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;
    private UnivariateRealFunction function;

    @Before
    public void setup() {
        optimizer = mock(BaseUnivariateRealOptimizer.class);
        generator = new RandomDataGenerator();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, generator);
        function = new PolynomialFunction(new double[]{-1d, 1d, 1d}, new double[]{0d, 0d, 1d});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithStartsLessThanOne() {
        new MultiStartUnivariateRealOptimizer<>(optimizer, 0, generator);
    }

    @Test
    public void testGetOptimaEmpty() throws ConvergenceException {
        when(optimizer.optimize(function, GoalType.MINIMIZE, 0d, 1d)).thenReturn(null);
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertNull(optima);
    }

    @Test
    public void testGetOptimaOneStart() throws ConvergenceException {
        when(optimizer.optimize(function, GoalType.MINIMIZE, 0d, 1d)).thenReturn(new UnivariateRealPointValuePair(0d, 0d));
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        UnivariateRealPointValuePair expected = new UnivariateRealPointValuePair(0d, 0d);
        assertArrayEquals(new UnivariateRealPointValuePair[]{expected}, optima);
    }

    @Test
    public void testGetOptimaMultipleStarts() throws ConvergenceException {
        when(optimizer.optimize(function, GoalType.MINIMIZE, 0d, 1d)).thenReturn(new UnivariateRealPointValuePair(0.1d, 0.1d));
        when(optimizer.optimize(function, GoalType.MINIMIZE, 0.5d, 1d)).thenReturn(new UnivariateRealPointValuePair(0.2d, 0.2d));
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        UnivariateRealPointValuePair[] expected = {new UnivariateRealPointValuePair(0.1d, 0.1d), new UnivariateRealPointValuePair(0.2d, 0.2d)};
        assertArrayEquals(expected, optima);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaNoOptimization() {
        multiStartOptimizer.getOptima();
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() throws ConvergenceException {
        when(optimizer.optimize(function, GoalType.MINIMIZE, 0d, 1d)).thenThrow(new ConvergenceException());
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0d, 1d);
    }

    @Test
    public void testOptimizeConvergence() throws ConvergenceException {
        when(optimizer.optimize(function, GoalType.MINIMIZE, 0d, 1d)).thenReturn(new UnivariateRealPointValuePair(0.1d, 0.1d));
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0d, 1d);
        UnivariateRealPointValuePair expected = new UnivariateRealPointValuePair(0.1d, 0.1d);
        assertArrayEquals(expected, new UnivariateRealPointValuePair[]{expected}, multiStartOptimizer.getOptima());
    }

    @Test
    public void testOptimizeMultipleConvergence() throws ConvergenceException {
        when(optimizer.optimize(function, GoalType.MINIMIZE, 0d, 1d)).thenReturn(new UnivariateRealPointValuePair(0.1d, 0.1d));
        when(optimizer.optimize(function, GoalType.MINIMIZE, 0.3d, 1d)).thenReturn(new UnivariateRealPointValuePair(0.2d, 0.2d));
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0d, 1d);
        UnivariateRealPointValuePair[] expected = {new UnivariateRealPointValuePair(0.1d, 0.1d), new UnivariateRealPointValuePair(0.2d, 0.2d)};
        assertArrayEquals(expected, new UnivariateRealPointValuePair[]{result}, multiStartOptimizer.getOptima());
    }
}