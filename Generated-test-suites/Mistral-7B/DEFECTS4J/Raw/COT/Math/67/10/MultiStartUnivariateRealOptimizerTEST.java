package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.RandomValuesGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction function;
    private UnivariateRealOptimizer wrappedOptimizer;
    private RandomValuesGenerator generator;

    @Before
    public void setUp() {
        function = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0, 1.0});
        wrappedOptimizer = neworg.apache.commons.math3.optim.nonlinear.scalar.NelderMeadSimplex();
        optimizer = new MultiStartUnivariateRealOptimizer(wrappedOptimizer, 5, generator);
        generator = new RandomValuesGenerator();
    }

    @Test
    public void testGetFunctionValue() throws Exception {
        assertEquals(function.value(0.0), optimizer.getFunctionValue(), 1e-12);
    }

    @Test
    public void testGetResult() throws Exception {
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(result.getPoint(), optimizer.getResult(), 1e-12);
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        assertEquals(optimizer.getAbsoluteAccuracy(), wrappedOptimizer.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetIterationCount() {
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(optimizer.getIterationCount(), wrappedOptimizer.getIterationCount());
    }

    @Test
    public void testGetMaxIterations() {
        optimizer.setMaxIterations(10);
        assertEquals(optimizer.getMaxIterations(), wrappedOptimizer.getMaxIterations());
    }

    @Test
    public void testGetMaxEvaluations() {
        optimizer.setMaxEvaluations(10);
        assertEquals(optimizer.getMaxEvaluations(), wrappedOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(optimizer.getEvaluations(), wrappedOptimizer.getEvaluations());
    }

    @Test
    public void testGetRelativeAccuracy() {
        optimizer.setRelativeAccuracy(1e-10);
        assertEquals(optimizer.getRelativeAccuracy(), wrappedOptimizer.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testResetAbsoluteAccuracy() {
        optimizer.resetAbsoluteAccuracy();
        wrappedOptimizer.resetAbsoluteAccuracy();
        assertEquals(optimizer.getAbsoluteAccuracy(), wrappedOptimizer.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testResetMaxIterations() {
        optimizer.resetMaxIterations();
        wrappedOptimizer.resetMaxIterations();
        assertEquals(optimizer.getMaxIterations(), wrappedOptimizer.getMaxIterations());
    }

    @Test
    public void testResetRelativeAccuracy() {
        optimizer.resetRelativeAccuracy();
        wrappedOptimizer.resetRelativeAccuracy();
        assertEquals(optimizer.getRelativeAccuracy(), wrappedOptimizer.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testSetAbsoluteAccuracy() {
        optimizer.setAbsoluteAccuracy(1e-8);
        wrappedOptimizer.setAbsoluteAccuracy(1e-8);
        assertEquals(optimizer.getAbsoluteAccuracy(), wrappedOptimizer.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(10);
        wrappedOptimizer.setMaxIterations(10);
        assertEquals(optimizer.getMaxIterations(), wrappedOptimizer.getMaxIterations());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptimaBeforeOptimize() throws Exception {
        optimizer.getOptima();
    }

    @Test
    public void testGetOptimaAfterOptimize() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        double[] result = optimizer.getOptima();
        assertEquals(result.length, 5);
        for (double d : result) {
            assertTrue(Double.isNaN(d) || function.value(d) >= 0);
        }
    }

    @Test
    public void testGetOptimaValuesAfterOptimize() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        double[] result = optimizer.getOptimaValues();
        assertEquals(result.length, 5);
        for (double d : result) {
            assertEquals(function.value(optimizer.getResult()), d, 1e-12);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptimaValuesBeforeOptimize() throws Exception {
        optimizer.getOptimaValues();
    }

    @Test
    public void testOptimizeMinWithinBounds() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, new SimpleBounds(-5.0, 5.0));
        assertEquals(function.value(optimizer.getResult()), 0.0, 1e-12);
    }

    @Test
    public void testOptimizeMinOutsideBounds() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, new SimpleBounds(-5.0, 2.0));
        double result = optimizer.getResult();
        assertTrue(result > 2.0);
        assertEquals(function.value(result), 0.0, 1e-12);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeMinNoConvergence() throws Exception {
        optimizer.optimize(function, GoalType.MINIMIZE, new SimpleBounds(-5.0, 5.0), new MaxEval(1));
    }

    @Test
    public void testOptimizeMaxWithinBounds() throws Exception {
        optimizer.optimize(new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0, 1.0}), GoalType.MAXIMIZE, new SimpleBounds(-5.0, 5.0));
        assertEquals(function.value(optimizer.getResult()), 0.0, 1e-12);
    }

    @Test
    public void testOptimizeMaxOutsideBounds() throws Exception {
        optimizer.optimize(new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0, 1.0}), GoalType.MAXIMIZE, new SimpleBounds(-5.0, 2.0));
        double result = optimizer.getResult();
        assertTrue(result < 2.0);
        assertEquals(function.value(result), 0.0, 1e-12);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeMaxNoConvergence() throws Exception {
        optimizer.optimize(new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0, 1.0}), GoalType.MAXIMIZE, new SimpleBounds(-5.0, 5.0), new MaxEval(1));
    }

}