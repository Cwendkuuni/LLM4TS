package org.apache.commons.cli.optimization; // Change the package name if necessary

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.MaxEval;
import org.apache.commons.math.optimization.SimpleGoalType;
import org.apache.commons.math.optimization.SimpleUnivariateRealFunction;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testGettersAndSetters() {
        UnivariateRealFunction f = new SimpleUnivariateRealFunction((x) -> Math.pow(Math.sin(x), 2));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new SimpleUnivariateRealOptimizer(f), 2, new MaxEval());

        assertEquals(2, optimizer.getStarts());
        assertEquals(null, optimizer.getOptima());
        assertEquals(null, optimizer.getOptimaValues());

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(20);
        assertEquals(10, optimizer.getMaxIterations());
        assertEquals(20, optimizer.getMaxEvaluations());

        optimizer.resetAbsoluteAccuracy();
        optimizer.resetMaximalIterationCount();
        optimizer.resetRelativeAccuracy();

        optimizer.setAbsoluteAccuracy(0.001);
        optimizer.setMaxIterations(5);
        optimizer.setMaxEvaluations(15);
        optimizer.setRelativeAccuracy(0.01);
    }

    @Test
    public void testOptimizeFunctionNoStarts() {
        UnivariateRealFunction f = new SimpleUnivariateRealFunction((x) -> Math.pow(Math.sin(x), 2));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new SimpleUnivariateRealOptimizer(f), 0, new MaxEval());

        try {
            optimizer.optimize(f, SimpleGoalType.MINIMIZE, -10.0, 10.0);
            fail("Should have thrown an exception.");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("number of starts must be greater than zero"));
        }
    }

    @Test
    public void testOptimizeFunctionSingleStart() {
        UnivariateRealFunction f = new SimpleUnivariateRealFunction((x) -> Math.pow(Math.sin(x), 2));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new SimpleUnivariateRealOptimizer(f), 1, new MaxEval());

        double x = optimizer.optimize(f, SimpleGoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(-3.141592653589793, x, 0.0001);
        assertArrayEquals(new double[]{ -3.141592653589793 }, optimizer.getOptima(), 0.0001);
        assertArrayEquals(new double[]{ Math.pow(-3.141592653589793, 2) }, optimizer.getOptimaValues(), 0.0001);
    }

    @Test
    public void testOptimizeFunctionMultipleStarts() {
        UnivariateRealFunction f = new SimpleUnivariateRealFunction((x) -> Math.pow(Math.sin(x), 2));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new SimpleUnivariateRealOptimizer(f), 3, new MaxEval());

        double[] optima = optimizer.optimize(f, SimpleGoalType.MINIMIZE, -10.0, 10.0);
        double[] expectedOptima = new double[]{ -3.141592653589793, -2.094595102364176, 1.210225931117647 };
        assertArrayEquals(expectedOptima, optima, 0.0001);

        double[] optimaValues = optimizer.getOptimaValues();
        double[] expectedOptimaValues = new double[]{ Math.pow(-3.141592653589793, 2), Math.pow(-2.094595102364176, 2), Math.pow(1.210225931117647, 2) };
        assertArrayEquals(expectedOptimaValues, optimaValues, 0.0001);
    }

    @Test
    public void testOptimizeFunctionConvergenceException() {
        UnivariateRealFunction f = (x) -> { throw new ConvergenceException("Test ConvergenceException"); };
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new SimpleUnivariateRealOptimizer(f), 3, new MaxEval());

        try {
            optimizer.optimize(f, SimpleGoalType.MINIMIZE, -10.0, 10.0);
            fail("Should have thrown a ConvergenceException.");
        } catch (ConvergenceException e) {
            // pass
        }

        assertArrayEquals(new double[3], optimizer.getOptima(), 0.0);
        assertArrayEquals(new double[3], optimizer.getOptimaValues(), 0.0);
    }

    @Test
    public void testOptimizeFunctionFunctionEvaluationException() {
        UnivariateRealFunction f = (x) -> { throw new FunctionEvaluationException("Test FunctionEvaluationException"); };
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new SimpleUnivariateRealOptimizer(f), 3, new MaxEval());

        try {
            optimizer.optimize(f, SimpleGoalType.MINIMIZE, -10.0, 10.0);
            fail("Should have thrown a FunctionEvaluationException.");
        } catch (FunctionEvaluationException e) {
            // pass
        }

        assertNull(optimizer.getOptima());
        assertNull(optimizer.getOptimaValues());
    }
}