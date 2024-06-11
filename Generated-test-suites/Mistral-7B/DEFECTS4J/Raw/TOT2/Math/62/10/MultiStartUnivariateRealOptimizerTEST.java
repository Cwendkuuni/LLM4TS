package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeInterval;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValue;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.RealVectorValue;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Fibonacci1D;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;
    private UnivariateRealFunction function;
    private RandomGenerator randomGenerator;

    @Before
    public void setUp() {
        optimizer = mock(BaseUnivariateRealOptimizer.class);
        when(optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
            .thenCallRealMethod();
        randomGenerator = new RandomDataGenerator();
        function = new Fibonacci1D();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, randomGenerator);
    }

    @Test
    public void testOptimizeMinimize() throws Exception {
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        assertEquals(0.0, result.getPoint().getValue(), 1e-6);
        assertEquals(-1.6102538658793233d, result.getValue(), 1e-6);

        PointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(3, optima.length);
        assertEquals(0.0, optima[0].getPoint().getValue(), 1e-6);
        assertEquals(-1.6102538658793233d, optima[0].getValue(), 1e-6);
        assertNull(optima[1]);
        assertNull(optima[2]);
    }

    @Test
    public void testOptimizeMaximize() throws Exception {
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);
        assertEquals(5.527236612021055d, result.getPoint().getValue(), 1e-6);
        assertEquals(1.0d, result.getValue(), 1e-6);

        PointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(3, optima.length);
        assertNull(optima[0]);
        assertNull(optima[1]);
        assertEquals(5.527236612021055d, optima[2].getPoint().getValue(), 1e-6);
        assertEquals(1.0d, optima[2].getValue(), 1e-6);
    }

    @Test
    public void testMaxEvaluations() throws Exception {
        when(optimizer.setMaxEvaluations(anyInt()))
            .thenThrow(new MaxEvaluationsExceededException());

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
            fail("Expected MaxEvaluationsExceededException");
        } catch (MaxEvaluationsExceededException e) {
            // expected
        }

        PointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(0, optima.length);
    }

    @Test
    public void testNullFunction() throws Exception {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, randomGenerator);
        multiStartOptimizer.optimize(null, GoalType.MINIMIZE, 0.0, 10.0);
        PointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(0, optima.length);
    }

    @Test
    public void testNullBounds() throws Exception {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, null, null);
        PointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(0, optima.length);
        assertNull(result);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidBounds() throws Exception {
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, new SimpleBounds(-1.0, 0.0), new SimpleBounds(1.0, 0.0));
        PointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(0, optima.length);
        assertNull(result);
    }

    @Test
    public void testConvergenceException() throws Exception {
        when(optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0))
            .thenThrow(new ConvergenceException(LocalizedFormats.NO_CONVERGENCE_REACHED, null));

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        PointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(0, optima.length);
    }

    @Test(expected = NoRootsException.class)
    public void testNoRootsException() throws Exception {
        when(optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0))
            .thenThrow(new NoRootsException(LocalizedFormats.NO_ROOTS_FOUND));

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
            fail("Expected NoRootsException");
        } catch (NoRootsException e) {
            // expected
        }

        PointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(0, optima.length);
    }

    @Test
    public void testEmptyOptima() throws Exception {
        GoalType goal = GoalType.MAXIMIZE;
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, goal, 0.0, 10.0);
        PointValuePair[] optima = multiStartOptimizer.getOptima();

        // Multi-start optimization should never return empty result when a single start is performed
        assertFalse(Arrays.stream(optima).allMatch(p -> p == null));
        assertEquals(1, optima.length);
        if (goal == GoalType.MAXIMIZE) {
            assertEquals(5.527236612021055d, optima[0].getPoint().getValue(), 1e-6);
            assertEquals(1.0d, optima[0].getValue(), 1e-6);
        } else {
            assertEquals(0.0, optima[0].getPoint().getValue(), 1e-6);
            assertEquals(-1.6102538658793233d, optima[0].getValue(), 1e-6);
        }
    }

    @Test
    public void testFindMultipleLocalMinima() throws Exception {
        UnivariateRealFunction function = new Fibonacci1D();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 10, randomGenerator);
        GoalType goal = GoalType.MINIMIZE;
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, goal, 0.0, 10.0);
        PointValuePair[] optima = multiStartOptimizer.getOptima();

        // Multi-start optimization should find multiple local minima
        assertFalse(Arrays.stream(optima).allMatch(p -> p == null));
        assertTrue(Arrays.stream(optima).filter(p -> p != null).mapToDouble(p -> p.getValue()).distinct().count() > 1);
    }

    @Test
    public void testFindMultipleGlobalMinima() throws Exception {
        UnivariateRealFunction function = new Fibonacci1D();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 100, randomGenerator);
        GoalType goal = GoalType.MINIMIZE;
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, goal, 0.0, 10.0);
        PointValuePair[] optima = multiStartOptimizer.getOptima();

        // Multi-start optimization should find multiple global minima
        assertFalse(Arrays.stream(optima).allMatch(p -> p == null));
        assertTrue(Arrays.stream(optima).filter(p -> p != null).mapToDouble(p -> p.getValue()).distinct().count() > 1);
    }
}