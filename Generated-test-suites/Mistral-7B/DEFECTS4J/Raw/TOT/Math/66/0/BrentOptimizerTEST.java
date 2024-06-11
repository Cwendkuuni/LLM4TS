package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeCriterion;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValue;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValueFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private static UnivariateRealFunction testFunction;
    private static UnivariateOptimizer optimizer;

    @BeforeClass
    public static void setUp() {
        testFunction = new SimpleFunction();
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testMinFunction() throws MaxIterationsExceededException {
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        assertEquals(-2.4142135625, result.getPoint(), 0.001);
        assertEquals(5.5428571342e-9, result.getValue(), 0.001);
    }

    @Test
    public void testMaxFunction() throws MaxIterationsExceededException {
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        assertEquals(2.4142135625, result.getPoint(), 0.001);
        assertEquals(-5.5428571342e-9, result.getValue(), 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException {
        optimizer.setMaxIterations(1);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws FunctionEvaluationException {
        testFunction = new ThrowingFunction();
        optimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, -5.0, 0.0);
    }

    @Test
    public void testBoundaryConditions() throws MaxIterationsExceededException {
        PointValuePair result = optimizer.optimize(new SquareFunction(), GoalType.MINIMIZE, -10.0, 10.0, 0.0);
        assertEquals(-1.0, result.getPoint(), 0.001);
        assertEquals(1.0, result.getValue(), 0.001);
    }

    @Test
    public void testEdgeCases() throws MaxIterationsExceededException {
        PointValuePair result = optimizer.optimize(new AbsoluteValueFunction(), GoalType.MINIMIZE, -1.0e10, 1.0e10, 0.0);
        assertEquals(0.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    static class SimpleFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(x) - 0.5 * x * x;
        }

        @Override
        public double derivative(double x) {
            return 2.0 * x - Math.cos(x);
        }
    }

    static class ThrowingFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException();
        }

        @Override
        public double derivative(double x) {
            return 0.0;
        }
    }

    static class SquareFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public double derivative(double x) {
            return 2.0 * x;
        }
    }

    static class AbsoluteValueFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.abs(x);
        }

        @Override
        public double derivative(double x) {
            return 1.0;
        }
    }
}