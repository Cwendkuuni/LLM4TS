package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.ConvergenceCheckerException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private RealFunction testFunction;
    private RealPointValuePair[] simplex;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        testFunction = new TestFunction();
        simplex = new RealPointValuePair[6];
    }

    @Test
    public void testDefaultConstructor() {
        MultiDirectional md = new MultiDirectional();
        assertEquals(2.0, md.getExpansionCoefficient(), 0.01);
        assertEquals(0.5, md.getContractionCoefficient(), 0.01);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional md = new MultiDirectional(3.0, 0.8);
        assertEquals(3.0, md.getExpansionCoefficient(), 0.01);
        assertEquals(0.8, md.getContractionCoefficient(), 0.01);
    }

    @Test
    public void testIterateSimplexWithSimpleFunction() throws FunctionEvaluationException,
            OptimizationException, ConvergenceCheckerException, MaxEvaluationsExceededException {
        simplex[0] = new RealPointValuePair(new double[]{-1.0, -1.0}, 2.0, false);
        simplex[1] = new RealPointValuePair(new double[]{0.0, -1.0}, 1.0, false);
        simplex[2] = new RealPointValuePair(new double[]{1.0, -1.0}, 0.0, false);
        simplex[3] = new RealPointValuePair(new double[]{-1.0, 0.0}, 1.0, false);
        simplex[4] = new RealPointValuePair(new double[]{0.0, 0.0}, 0.0, false);
        simplex[5] = new RealPointValuePair(new double[]{1.0, 0.0}, 0.5, false);

        RealPointValuePair bestPoint = optimizer.optimize(testFunction, simplex, new RealConvergenceChecker(1e-6));
        assertEquals(-1.0, bestPoint.getPointRef()[0], 0.01);
        assertEquals(-1.0, bestPoint.getPointRef()[1], 0.01);
        assertEquals(0.0, testFunction.value(bestPoint.getPointRef()), 0.01);
    }

    @Test
    public void testIterateSimplexWithComplexFunction() throws FunctionEvaluationException,
            OptimizationException, ConvergenceCheckerException, MaxEvaluationsExceededException {
        simplex[0] = new RealPointValuePair(new double[]{-1.0, -1.0}, Math.E, false);
        simplex[1] = new RealPointValuePair(new double[]{0.0, -1.0}, 2.0, false);
        simplex[2] = new RealPointValuePair(new double[]{1.0, -1.0}, Math.E * Math.E, false);
        simplex[3] = new RealPointValuePair(new double[]{-1.0, 0.0}, Math.SQRT2, false);
        simplex[4] = new RealPointValuePair(new double[]{0.0, 0.0}, 0.0, false);
        simplex[5] = new RealPointValuePair(new double[]{1.0, 0.0}, Math.PI, false);

        RealPointValuePair bestPoint = optimizer.optimize(testFunction, simplex, new RealConvergenceChecker(1e-6));
        assertEquals(-1.0, bestPoint.getPointRef()[0], 0.01);
        assertEquals(-1.0, bestPoint.getPointRef()[1], 0.01);
        assertEquals(Math.PI, testFunction.value(bestPoint.getPointRef()), 0.01);
    }

    @Test
    public void testIterateSimplexWithComplexFunctionAndError() throws FunctionEvaluationException,
            OptimizationException, ConvergenceCheckerException, MaxEvaluationsExceededException {
        simplex[0] = new RealPointValuePair(new double[]{-1.0, -1.0}, Math.E, false);
        simplex[1] = new RealPointValuePair(new double[]{0.0, -1.0}, 2.0, false);
        simplex[2] = new RealPointValuePair(new double[]{1.0, -1.0}, Math.E * Math.E, false);
        simplex[3] = new RealPointValuePair(new double[]{-1.0, 0.0}, Math.SQRT2, false);
        simplex[4] = new RealPointValuePair(new double[]{0.0, 0.0}, 0.0, false);
        simplex[5] = new RealPointValuePair(new double[]{1.0, 0.0}, Math.PI, false);

        testFunction = new TestFunctionWithException();

        try {
            optimizer.optimize(testFunction, simplex, new RealConvergenceChecker(1e-6));
            assertFalse(true);
        } catch (FunctionEvaluationException fex) {
            assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5}, getEvaluatedPoints(simplex));
            assertEquals(4, fex.getNumberEvaluations());
        }
    }

    private int[] getEvaluatedPoints(RealPointValuePair[] points) {
        int[] result = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            result[i] = (int) points[i].getValue();
        }
        return result;
    }

}

class TestFunction implements RealFunction {
    @Override
    public double value(double[] x) {
        return Math.pow(Math.pow(x[0], 2.0) + Math.pow(x[1], 2.0), 0.5);
    }
}

class TestFunctionWithException implements RealFunction {
    @Override
    public double value(double[] x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException("Exception message", new Exception());
    }
}