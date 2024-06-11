package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LineSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.BracketFinder;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleUnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleUnivariateValueChecker;
import org.junit.Test;

import static org.apache.commons.math3.optim.direct.PowellOptimizer.*;

public class PowellOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer();
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customChecker = mock(ConvergenceChecker.class);
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01, customChecker);
        assertEquals(0.001, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.01, optimizer.getAbsoluteThreshold(), 0.001);
        assertEquals(customChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testMinimizeSimpleFunction() {
        PowellOptimizer optimizer = new PowellOptimizer();
        UnivariateFunction function = new SimpleUnivariateFunction("sin(x)", x -> Math.sin(x));
        double[] startPoint = {3.14};
        PointValuePair result = optimizer.optimize(function, startPoint);
        double expectedX = 0;
        double expectedFVal = 0;
        assertEquals(expectedX, result.getPoint()[0], 0.001);
        assertEquals(expectedFVal, result.getValue(), 0.001);
    }

    @Test
    public void testMinimizeFunctionWithBounds() {
        PowellOptimizer optimizer = new PowellOptimizer();
        UnivariateFunction function = new SimpleUnivariateFunction("x^2", x -> Math.pow(x, 2));
        double[] startPoint = {2.0};
        SimpleBounds bounds = new SimpleBounds(0.0, 5.0);
        PointValuePair result = optimizer.optimize(function, startPoint, bounds);
        double expectedX = 0;
        double expectedFVal = 0;
        assertEquals(expectedX, result.getPoint()[0], 0.001);
        assertEquals(expectedFVal, result.getValue(), 0.001);
    }

    @Test
    public void testMinimizeFunctionWithCustomLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer();
        UnivariateFunction function = new SimpleUnivariateFunction("sin(x)", x -> Math.sin(x));
        double[] startPoint = {3.14};
        LineSearch lineSearch = mock(LineSearch.class);
        optimizer.setLineSearch(lineSearch);
        PointValuePair result = optimizer.optimize(function, startPoint);
        double expectedX = 0;
        double expectedFVal = 0;
        assertEquals(expectedX, result.getPoint()[0], 0.001);
        assertEquals(expectedFVal, result.getValue(), 0.001);
    }

    @Test
    public void testMinimizeFunctionWithCustomConvergenceCheckerAndCustomLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer();
        UnivariateFunction function = new SimpleUnivariateFunction("sin(x)", x -> Math.sin(x));
        double[] startPoint = {3.14};
        ConvergenceChecker<PointValuePair> customChecker = mock(ConvergenceChecker.class);
        LineSearch lineSearch = mock(LineSearch.class);
        optimizer.setConvergenceChecker(customChecker);
        optimizer.setLineSearch(lineSearch);
        PointValuePair result = optimizer.optimize(function, startPoint);
        double expectedX = 0;
        double expectedFVal = 0;
        assertEquals(expectedX, result.getPoint()[0], 0.001);
        assertEquals(expectedFVal, result.getValue(), 0.001);
    }

    @Test
    public void testMaximizeSimpleFunction() {
        PowellOptimizer optimizer = new PowellOptimizer();
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariateFunction function = new SimpleUnivariateFunction("sin(x)", x -> Math.sin(x));
        double[] startPoint = {3.14};
        PointValuePair result = optimizer.optimize(function, startPoint);
        double expectedX = Math.PI;
        double expectedFVal = Math.sin(Math.PI);
        assertEquals(expectedX, result.getPoint()[0], 0.001);
        assertEquals(expectedFVal, result.getValue(), 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMaximizeFunctionWithBounds() {
        PowellOptimizer optimizer = new PowellOptimizer();
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariateFunction function = new SimpleUnivariateFunction("x^2", x -> Math.pow(x, 2));
        double[] startPoint = {2.0};
        SimpleBounds bounds = new SimpleBounds(0.0, 5.0);
        PointValuePair result = optimizer.optimize(function, startPoint, bounds);
        fail("Expected UnsupportedOperationException");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMaximizeFunctionWithCustomLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer();
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariateFunction function = new SimpleUnivariateFunction("sin(x)", x -> Math.sin(x));
        double[] startPoint = {3.14};
        LineSearch lineSearch = mock(LineSearch.class);
        optimizer.setLineSearch(lineSearch);
        PointValuePair result = optimizer.optimize(function, startPoint);
        fail("Expected UnsupportedOperationException");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMaximizeFunctionWithCustomConvergenceCheckerAndCustomLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer();
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariateFunction function = new SimpleUnivariateFunction("sin(x)", x -> Math.sin(x));
        double[] startPoint = {3.14};
        ConvergenceChecker<PointValuePair> customChecker = mock(ConvergenceChecker.class);
        LineSearch lineSearch = mock(LineSearch.class);
        optimizer.setConvergenceChecker(customChecker);
        optimizer.setLineSearch(lineSearch);
        PointValuePair result = optimizer.optimize(function, startPoint);
        fail("Expected UnsupportedOperationException");
    }
}