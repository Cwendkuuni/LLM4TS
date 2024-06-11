package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math.analysis.SincFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.apache.commons.math.random.RandomDataGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    private MultiStartUnivariateRealOptimizer<SincFunction> optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer<SincFunction>(new BrentOptimizer(),
                                                                      5,
                                                                      new JDKRandomGenerator());
        optimizer.setMaxEvaluations(100);
    }

    @Test
    public void testOptimize() {
        SincFunction function = new SincFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5;
        double max = 5;
        double startValue = 0;

        UnivariateRealPointValuePair result = null;
        try {
            result = optimizer.optimize(function, goalType, min, max, startValue);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected=ConvergenceException.class)
    public void testOptimizeWithConvergenceException() {
        SincFunction function = new SincFunction();
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -10;
        double max = 10;
        double startValue = 0;

        optimizer.optimize(function, goalType, min, max, startValue);
    }

    @Test(expected=FunctionEvaluationException.class)
    public void testOptimizeWithFunctionEvaluationException() {
        SincFunction function = new SincFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -5;
        double max = 5;
        double startValue = 0;

        optimizer.optimize(function, goalType, min, max, startValue);
    }

    @Test
    public void testGetOptima() {
        assertNull(optimizer.getOptima());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(200);
        assertEquals(200, optimizer.getMaxEvaluations());
    }

}