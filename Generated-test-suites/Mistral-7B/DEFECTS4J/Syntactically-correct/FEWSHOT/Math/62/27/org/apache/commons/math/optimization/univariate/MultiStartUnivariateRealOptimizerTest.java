package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.goal.GoalType;
import org.apache.commons.math3.optim.nonlinear.multistart.MultiStartUnivariateOptimizer;
import org.apache.commons.math3.random.MersenneTwister;

import java.util.Arrays;

public class MultiStartUnivariateRealOptimizerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testMinimize() {
        UnivariateRealFunction function = new PolynomialFunctions.Cubic(1.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());

        multiStartOptimizer.setMaxEvaluations(100);

        PointValuePair bestOptimum = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
        assertNotNull(bestOptimum);
        assertEquals(-1.0, bestOptimum.getPoint(), 1e-6);
        assertEquals(-1.0, bestOptimum.getValue(), 1e-6);

        PointValuePair[] allOptima = multiStartOptimizer.getOptima();
        assertNotNull(allOptima);
        assertEquals(3, allOptima.length);
        assertNull(allOptima[1]);
        assertNull(allOptima[2]);
        assertArrayEquals(new Double[]{ -1.0, -1.0, -1.0 }, Arrays.stream(allOptima).mapToDouble(p -> p == null ? Double.NaN : p.getPoint()).toArray(), 1e-6);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMaximize() {
        UnivariateRealFunction function = new PolynomialFunctions.Quartic(1.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());

        multiStartOptimizer.setMaxEvaluations(100);

        PointValuePair bestOptimum = multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0);
        assertNotNull(bestOptimum);
        assertEquals(1.0, bestOptimum.getPoint(), 1e-6);
        assertEquals(1.0, bestOptimum.getValue(), 1e-6);

        PointValuePair[] allOptima = multiStartOptimizer.getOptima();
        assertNotNull(allOptima);
        assertEquals(3, allOptima.length);
        assertNull(allOptima[1]);
        assertNull(allOptima[2]);
        assertArrayEquals(new Double[]{ 1.0, 1.0, 1.0 }, Arrays.stream(allOptima).mapToDouble(p -> p == null ? Double.NaN : p.getPoint()).toArray(), 1e-6);
    }

    @Test
    public void testNullFunction() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());

        try {
            multiStartOptimizer.optimize(null, GoalType.MINIMIZE, 0.0, 0.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            multiStartOptimizer.getOptima();
            fail();
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testNullStarts() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 0, new MersenneTwister());

        try {
            multiStartOptimizer.optimize(new PolynomialFunctions.Sine(), GoalType.MINIMIZE, 0.0, 2.0 * Math.PI);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            multiStartOptimizer.getOptima();
            fail();
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testNullGenerator() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, null);

        try {
            multiStartOptimizer.optimize(new PolynomialFunctions.Sine(), GoalType.MINIMIZE, 0.0, 2.0 * Math.PI);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            multiStartOptimizer.getOptima();
            fail();
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testMaxEvaluationsLessThanOptimizer() {
        UnivariateRealFunction function = new PolynomialFunctions.Sine();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());

        multiStartOptimizer.setMaxEvaluations(2);

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 2.0 * Math.PI);
            fail();
        } catch (ConvergenceException e) {
            // Expected
        }

        try {
            multiStartOptimizer.getOptima();
            fail();
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testNullBound1() {
        UnivariateRealFunction function = new PolynomialFunctions.Sine();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, null, 2.0 * Math.PI);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            multiStartOptimizer.getOptima();
            fail();
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testNullBound2() {
        UnivariateRealFunction function = new PolynomialFunctions.Sine();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            multiStartOptimizer.getOptima();
            fail();
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testZeroBound1() {
        UnivariateRealFunction function = new PolynomialFunctions.Sine();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 0.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            multiStartOptimizer.getOptima();
            fail();
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testZeroBound2() {
        UnivariateRealFunction function = new PolynomialFunctions.Sine();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 0.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            multiStartOptimizer.getOptima();
            fail();
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testOverlappingBounds() {
        UnivariateRealFunction function = new PolynomialFunctions.Sine();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 1.0, 1.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            multiStartOptimizer.getOptima();
            fail();
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testOutOfRangeStartValue() {
        UnivariateRealFunction function = new PolynomialFunctions.Sine();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 2.0 * Math.PI, 0.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            multiStartOptimizer.getOptima();
            fail();
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testNegativeStartValue() {
        UnivariateRealFunction function = new PolynomialFunctions.Sine();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -2.0 * Math.PI, 0.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            multiStartOptimizer.getOptima();
            fail();
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testNonConvergence() {
        UnivariateRealFunction function = new PolynomialFunctions.Rastrigin();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());

        multiStartOptimizer.setMaxEvaluations(100);

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
            fail();
        } catch (ConvergenceException e) {
            // Expected
        }

        try {
            multiStartOptimizer.getOptima();
            fail();
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }
}