package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for BrentOptimizer.
 */
public class BrentOptimizerTest {
    private UnivariateRealFunction testFunction;
    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        testFunction = new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0, -1.0});
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testConstructor() {
        assertNotNull(optimizer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() throws Exception {
        optimizer.doOptimize();
    }

    @Test
    public void testOptimizeMin() throws Exception {
        double minResult = optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
        double expectedResult = -1.0;
        assertEquals(expectedResult, minResult, 1E-9);
    }

    @Test
    public void testOptimizeMax() throws Exception {
        double maxResult = optimizer.optimize(testFunction, GoalType.MAXIMIZE, -10.0, 10.0, 0.0);
        double expectedResult = 1.0;
        assertEquals(expectedResult, maxResult, 1E-9);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        optimizer.setMaxIterations(1);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        testFunction = new FunctionWithEvaluationException();
        optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() throws Exception {
        optimizer.setMin(0.0);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 0.0, 0.0);
    }

    @Test
    public void testEdgeCases() throws Exception {
        optimizer.optimize(new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0, -1.0}), GoalType.MINIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0);
        optimizer.optimize(new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0, -1.0}), GoalType.MAXIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0);
        optimizer.optimize(new PolynomialFunction(3, new double[]{1.0, -1.0, -1.0, 1.0}), GoalType.MINIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0);
        optimizer.optimize(new PolynomialFunction(3, new double[]{1.0, -1.0, -1.0, 1.0}), GoalType.MAXIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0);
        optimizer.optimize(new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0, -1.0}), GoalType.MINIMIZE, -1.0, 1.0, 0.0);
        optimizer.optimize(new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0, -1.0}), GoalType.MAXIMIZE, -1.0, 1.0, 0.0);
        optimizer.optimize(new PolynomialFunction(3, new double[]{1.0, -1.0, -1.0, 1.0}), GoalType.MINIMIZE, -1.0, 1.0, 0.0);
        optimizer.optimize(new PolynomialFunction(3, new double[]{1.0, -1.0, -1.0, 1.0}), GoalType.MAXIMIZE, -1.0, 1.0, 0.0);
    }

    static class FunctionWithEvaluationException extends UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException("Test Evaluation Exception");
        }
    }
}