package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.PolynomialFunction;
import org.apache.commons.math.optim.GoalType;
import org.apache.commons.math.optim.MaxEval;
import org.apache.commons.math.optim.PointValuePair;
import org.apache.commons.math.optim.UnivariateOptimizer;
import org.apache.commons.math.random.RandomDataGenerator;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        // Test default constructor
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);
        assertNull(optimizer.getOptima());
        assertNull(optimizer.getOptimaValues());
        assertEquals(0, optimizer.getIterationCount());
        assertEquals(0, optimizer.getEvaluations());

        // Test constructor with null optimizer and valid parameters
        RandomGenerator generator = new RandomDataGenerator();
        optimizer = new MultiStartUnivariateRealOptimizer(null, 5, generator);
        assertNull(optimizer.getOptima());
        assertNull(optimizer.getOptimaValues());
        assertEquals(0, optimizer.getIterationCount());
        assertEquals(0, optimizer.getEvaluations());

        // Test constructor with valid optimizer and valid parameters
        UnivariateOptimizer underlyingOptimizer = new FibonacciSearch();
        optimizer = new MultiStartUnivariateRealOptimizer(underlyingOptimizer, 5, generator);
        assertNull(optimizer.getOptima());
        assertNull(optimizer.getOptimaValues());
        assertEquals(0, optimizer.getIterationCount());
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGettersAndSetters() {
        RandomGenerator generator = new RandomDataGenerator();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new FibonacciSearch(), 5, generator);

        optimizer.setMaxEvaluations(100);
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxIterationCount());
        assertEquals(100, optimizer.getMaxEvaluations());
        assertEquals(0, optimizer.getIterationCount());
        assertEquals(0, optimizer.getEvaluations());

        optimizer.setMaxIterationCount(20);
        assertEquals(20, optimizer.getMaxIterationCount());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(0, optimizer.getIterationCount());
        assertEquals(0, optimizer.getEvaluations());

        optimizer.resetIterationCount();
        optimizer.resetEvaluationCount();
        optimizer.optimize(new PolynomialFunction(2, 1), GoalType.MINIMIZE, 0, 1);

        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        assertEquals(5, optima.length);
        assertEquals(5, optimaValues.length);
        assertNull(optima);
        assertNull(optimaValues);

        optimizer.resetIterationCount();
        optimizer.resetEvaluationCount();
        optimizer.optimize(new PolynomialFunction(2, 1), GoalType.MINIMIZE, 0, 1);

        optima = optimizer.getOptima();
        optimaValues = optimizer.getOptimaValues();

        assertArrayEquals(new double[]{0.1275337320585536, 0.2361933625377373, 0.3250775516188413, 0.41376137859653375, 0.5}, optima, 0.0001);
        assertArrayEquals(new double[]{0.8862263279414064, 0.7268361778438662, 0.5833468744118239, 0.4494395359606356, 0.3215627525722542}, optimaValues, 0.0001);
    }

    @Test
    public void testMultiStartOptimization() {
        RandomGenerator generator = new RandomDataGenerator();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new FibonacciSearch(), 5, generator);

        PointValuePair result = optimizer.optimize(new PolynomialFunction(2, 1), GoalType.MINIMIZE, 0, 1);

        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        assertEquals(5, optima.length);
        assertEquals(5, optimaValues.length);
        assertArrayEquals(new double[]{0.1275337320585536, 0.2361933625377373, 0.3250775516188413, 0.41376137859653375, 0.5}, optima, 0.0001);
        assertArrayEquals(new double[]{0.8862263279414064, 0.7268361778438662, 0.5833468744118239, 0.4494395359606356, 0.3215627525722542}, optimaValues, 0.0001);
        assertEquals(result.getValue(), result.getPoint(), 0.0001);
    }

    @Test
    public void testMultiStartOptimizationConvergenceException() {
        RandomGenerator generator = new RandomDataGenerator();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new FibonacciSearch(), 5, generator);

        try {
            optimizer.optimize(new PolynomialFunction(2, 1), GoalType.MINIMIZE, 0, 1, 0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        assertTrue(ArrayUtils.isEqual(new double[5], optima));
        assertTrue(ArrayUtils.isEqual(new Double[5], optimaValues));
    }

    @Test
    public void testMultiStartOptimizationFunctionEvaluationException() {
        RandomGenerator generator = new RandomDataGenerator();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new FibonacciSearch(), 5, generator);

        try {
            optimizer.optimize(new PolynomialFunctionWithException(), GoalType.MINIMIZE, 0, 1);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        assertTrue(ArrayUtils.isEqual(new double[5], optima));
        assertTrue(ArrayUtils.isEqual(new Double[5], optimaValues));
    }
}

class PolynomialFunctionWithException extends PolynomialFunction {

    @Override
    public double value(double x) throws FunctionEvaluationException {
        if (x > 1) {
            throw new FunctionEvaluationException("Function evaluation is not allowed for x > 1");
        }
        return super.value(x);
    }
}