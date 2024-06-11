package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.convergence.AbstractConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.IterationCounter;
import org.apache.commons.math3.optim.nonlinear.convergence.StoppingCondition;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeCriterion;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValue;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairFunction;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    private MultiStartUnivariateRealOptimizer<FUNC> optimizer;
    private UnivariateOptimizer<FUNC> underlyingOptimizer;
    private ObjectiveFunction<Double> function;
    private double min;
    private double max;
    private MersenneTwister generator;

    @Before
    public void setup() {
        FunctionC criterion = new FunctionC();
        GoalTypeValue goalTypeValue = new GoalTypeValue(SimpleGoalType.MINIMIZE);
        GoalType goal = new GoalTypeCriterion(goalTypeValue, criterion);
        AbstractConvergenceChecker<PointValuePair<Double>> convergenceChecker = new IterationCounter(100);
        StoppingCondition stoppingCondition = new IterationCounter(1000);
        underlyingOptimizer = new FminBrent(goal, convergenceChecker, stoppingCondition);
        optimizer = new MultiStartUnivariateRealOptimizer<>(underlyingOptimizer, 10, new MersenneTwister(System.currentTimeMillis()));
        function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, false);
        min = -5.0;
        max = 5.0;
        generator = new MersenneTwister(System.currentTimeMillis());
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(optimizer);
    }

    /**
     * Test setConvergenceChecker method.
     */
    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<PointValuePair<Double>> newChecker = new AbstractConvergenceChecker<PointValuePair<Double>>() {
            @Override
            public boolean test(PointValuePair<Double> pointValuePair, int iteration) {
                return pointValuePair != null;
            }
        };
        optimizer.setConvergenceChecker(newChecker);
        assertSame(newChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test getConvergenceChecker method.
     */
    @Test
    public void testGetConvergenceChecker() {
        assertSame(underlyingOptimizer.getConvergenceChecker(), optimizer.getConvergenceChecker());
    }

    /**
     * Test getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    /**
     * Test getEvaluations method.
     */
    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, min, max);
        assertEquals(evaluations + 10, optimizer.getEvaluations());
    }

    /**
     * Test setMaxEvaluations method.
     */
    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(5);
        assertEquals(5, optimizer.getMaxEvaluations());
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, min, max);
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, SimpleGoalType.MINIMIZE, min, max));
    }

    /**
     * Test getOptima method.
     */
    @Test
    public void testGetOptima() {
        PointValuePair<Double>[] expectedOptima = new PointValuePair[10];
        for (int i = 0; i < 10; i++) {
            expectedOptima[i] = new PointValuePair<>(i * 0.1 + 0.5, function.value(i * 0.1 + 0.5));
        }
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, min, max);
        PointValuePair<UnivariateRealPointValuePair>[] actualOptima = optimizer.getOptima();
        for (int i = 0; i < 10; i++) {
            assertNotNull(actualOptima[i]);
            assertEquals(expectedOptima[i].getFirst(), actualOptima[i].getValue().getX(), 0.001);
            assertEquals(expectedOptima[i].getSecond(), actualOptima[i].getValue().getFvalue(), 0.001);
        }
    }

    /**
     * Test optimize method with minimum goal type when function has a unique minimum.
     */
    @Test
    public void testOptimizeMin() {
        PointValuePair<Double> expectedOptimum = new PointValuePair<>(0.5, function.value(0.5));
        PointValuePair<UnivariateRealPointValuePair> actualOptimum = optimizer.optimize(function, SimpleGoalType.MINIMIZE, min, max);
        assertEquals(expectedOptimum.getFirst(), actualOptimum.getValue().getX(), 0.001);
        assertEquals(expectedOptimum.getSecond(), actualOptimum.getValue().getFvalue(), 0.001);
    }

    /**
     * Test optimize method with minimum goal type when function has multiple minima.
     */
    @Test
    public void testOptimizeMinMultiple() {
        PointValuePair<Double>[] expectedOptima = new PointValuePair[10];
        for (int i = 0; i < 10; i++) {
            expectedOptima[i] = new PointValuePair<>(i * 0.1 + 0.5, function.value(i * 0.1 + 0.5));
        }
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, min, max);
        PointValuePair<UnivariateRealPointValuePair>[] actualOptima = optimizer.getOptima();
        for (int i = 0; i < 10; i++) {
            assertNotNull(actualOptima[i]);
            assertEquals(expectedOptima[i].getFirst(), actualOptima[i].getValue().getX(), 0.001);
            assertEquals(expectedOptima[i].getSecond(), actualOptima[i].getValue().getFvalue(), 0.001);
        }
    }

    /**
     * Test optimize method with maximum goal type when function has a unique maximum.
     */
    @Test
    public void testOptimizeMax() {
        PointValuePair<Double> expectedOptimum = new PointValuePair<>(4.5, function.value(4.5));
        PointValuePair<UnivariateRealPointValuePair> actualOptimum = optimizer.optimize(function, SimpleGoalType.MAXIMIZE, min, max);
        assertEquals(expectedOptimum.getFirst(), actualOptimum.getValue().getX(), 0.001);
        assertEquals(expectedOptimum.getSecond(), actualOptimum.getValue().getFvalue(), 0.001);
    }

    /**
     * Test optimize method with maximum goal type when function has multiple maxima.
     */
    @Test
    public void testOptimizeMaxMultiple() {
        PointValuePair<Double>[] expectedOptima = new PointValuePair[10];
        for (int i = 0; i < 10; i++) {
            expectedOptima[i] = new PointValuePair<>(5.0 - i * 0.1, function.value(5.0 - i * 0.1));
        }
        optimizer.optimize(function, SimpleGoalType.MAXIMIZE, min, max);
        PointValuePair<UnivariateRealPointValuePair>[] actualOptima = optimizer.getOptima();
        for (int i = 0; i < 10; i++) {
            assertNotNull(actualOptima[i]);
            assertEquals(expectedOptima[i].getFirst(), actualOptima[i].getValue().getX(), 0.001);
            assertEquals(expectedOptima[i].getSecond(), actualOptima[i].getValue().getFvalue(), 0.001);
        }
    }

    /**
     * Test optimize method with invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidGoalType() {
        optimizer.optimize(function, null, min, max);
    }

    /**
     * Test optimize method with invalid bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidBounds() {
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, null, null);
    }

    /**
     * Test optimize method with invalid starting value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidStartingValue() {
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, -1.0, max);
    }

    /**
     * Test optimize method with no convergence.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() {
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, min, max);
    }

    /**
     * Test optimize method with no convergence and multiple starts.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergenceMultipleStarts() {
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, min, max, 0.0);
    }

    /**
     * Test optimize method when all starts converge to the same point.
     */
    @Test
    public void testOptimizeAllStartsSamePoint() {
        optimizer.setMaxEvaluations(1000);
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, min, max, 0.0);
        PointValuePair<UnivariateRealPointValuePair>[] actualOptima = optimizer.getOptima();
        for (int i = 1; i < actualOptima.length; i++) {
            assertNull(actualOptima[i]);
        }
        PointValuePair<Double> expectedOptimum = new PointValuePair<>(0.5, function.value(0.5));
        assertEquals(expectedOptimum.getFirst(), actualOptima[0].getValue().getX(), 0.001);
        assertEquals(expectedOptimum.getSecond(), actualOptima[0].getValue().getFvalue(), 0.001);
    }

    /**
     * Test optimize method when some starts converge to different points.
     */
    @Test
    public void testOptimizeDifferentStarts() {
        optimizer.setMaxEvaluations(1000);
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, min, max, 0.0);
        PointValuePair<UnivariateRealPointValuePair>[] actualOptima = optimizer.getOptima();
        for (int i = 0; i < 10; i++) {
            assertNotNull(actualOptima[i]);
            PointValuePair<Double> expectedOptimaI = new PointValuePair<>(i * 0.1 + 0.5, function.value(i * 0.1 + 0.5));
            assertEquals(expectedOptimaI.getFirst(), actualOptima[i].getValue().getX(), 0.001);
            assertEquals(expectedOptimaI.getSecond(), actualOptima[i].getValue().getFvalue(), 0.001);
        }
    }

    /**
     * Test optimize method when some starts do not converge.
     */
    @Test
    public void testOptimizeNoConvergenceStarts() {
        optimizer.setMaxEvaluations(10);
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, min, max, 0.0);
        PointValuePair<UnivariateRealPointValuePair>[] actualOptima = optimizer.getOptima();
        for (int i = 1; i < actualOptima.length; i++) {
            assertNull(actualOptima[i]);
        }
        PointValuePair<Double> expectedOptimum = new PointValuePair<>(0.5, function.value(0.5));
        assertEquals(expectedOptimum.getFirst(), actualOptima[0].getValue().getX(), 0.001);
        assertEquals(expectedOptimum.getSecond(), actualOptima[0].getValue().getFvalue(), 0.001);
    }

    /**
     * Test optimize method when some starts converge to the same point and some do not converge.
     */
    @Test
    public void testOptimizeMixedConvergence() {
        optimizer.setMaxEvaluations(1000);
        optimizer.optimize(function, SimpleGoalType.MINIMIZE, min, max, 0.0);
        PointValuePair<UnivariateRealPointValuePair>[] actualOptima = optimizer.getOptima();
        for (int i = 1; i < actualOptima.length && actualOptima[i] == null; i++) {
            // Skip null elements, which correspond to starts that did not converge
        }
        if (i < actualOptima.length) {
            PointValuePair<Double> expectedOptimaI = new PointValuePair<>(i * 0.1 + 0.5, function.value(i * 0.1 + 0.5));
            assertEquals(expectedOptimaI.getFirst(), actualOptima[i].getValue().getX(), 0.001);
            assertEquals(expectedOptimaI.getSecond(), actualOptima[i].getValue().getFvalue(), 0.001);
        } else {
            fail("No valid optima found");
        }
    }
}

class FunctionC implements ObjectiveFunction<Double> {

    @Override
    public Double value(Double x) {
        return Math.pow(x, 2) + Math.sin(Math.PI * x);
    }
}