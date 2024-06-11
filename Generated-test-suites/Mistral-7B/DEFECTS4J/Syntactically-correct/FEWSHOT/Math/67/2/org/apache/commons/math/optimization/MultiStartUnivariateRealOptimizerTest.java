package org.apache.commons.cli.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction testFunction;

    @Before
    public void setUp() {
        testFunction = new UnivariateFunctionUnderTest();
        optimizer = new MultiStartUnivariateRealOptimizer(new NelderMeadSimulatedAnnealing(), 5, new java.util.Random());
    }

    @Test
    public void testGetFunctionValue() {
        assertNotNull(optimizer.getFunctionValue());
    }

    @Test
    public void testGetResult() {
        assertNull(optimizer.getResult());
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        assertNull(optimizer.getAbsoluteAccuracy());
    }

    @Test
    public void testGetIterationCount() {
        assertNotNull(optimizer.getIterationCount());
        optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        assertTrue(optimizer.getIterationCount() > 0);
    }

    @Test
    public void testGetMaxIterations() {
        optimizer.setMaxIterations(10);
        assertEquals(10, optimizer.getMaxIterations());
    }

    @Test
    public void testGetMaxEvaluations() {
        optimizer.setMaxEvaluations(20);
        assertEquals(20, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(0, optimizer.getEvaluations());
        optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        assertTrue(optimizer.getEvaluations() > 0);
    }

    @Test
    public void testGetRelativeAccuracy() {
        optimizer.setRelativeAccuracy(1e-5);
        assertEquals(1e-5, optimizer.getRelativeAccuracy(), 1e-8);
    }

    @Test
    public void testSetRelativeAccuracy() {
        optimizer.setRelativeAccuracy(1e-5);
        assertEquals(1e-5, optimizer.getRelativeAccuracy(), 1e-8);
        optimizer.setRelativeAccuracy(1e-6);
        assertEquals(1e-6, optimizer.getRelativeAccuracy(), 1e-8);
    }

    @Test
    public void testOptimizeMinimize() throws Exception {
        optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        assertArrayEquals(new double[]{0.2, 0.3, 0.4, 0.5, 0.6}, optima, 1e-8);
        assertArrayEquals(new double[]{0.1, 0.15, 0.18, 0.21, 0.23}, optimaValues, 1e-8);
    }

    @Test
    public void testOptimizeMaximize() throws Exception {
        optimizer.optimize(testFunction, GoalType.MAXIMIZE, 0.0, 1.0);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        assertArrayEquals(new double[]{0.8, 0.7, 0.6, 0.5, 0.4}, optima, 1e-8);
        assertArrayEquals(new double[]{0.9, 0.85, 0.82, 0.81, 0.8}, optimaValues, 1e-8);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptimaBeforeOptimize() throws Exception {
        optimizer.getOptima();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptimaValuesBeforeOptimize() throws Exception {
        optimizer.getOptimaValues();
    }

    @Test
    public void testGetOptimaAfterFailedOptimize() throws Exception {
        optimizer.setMaxIterations(1);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        assertArrayEquals(new double[]{Double.NaN}, optima, 1e-8);
        assertArrayEquals(new double[]{Double.NaN}, optimaValues, 1e-8);
    }

    private static class UnivariateFunctionUnderTest implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 2);
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }

        @Override
        public double deriveValue(double x) {
            return 2 * x;
        }

        @Override
        public int getNumberOfDerivatives() {
            return 1;
        }
    }
}