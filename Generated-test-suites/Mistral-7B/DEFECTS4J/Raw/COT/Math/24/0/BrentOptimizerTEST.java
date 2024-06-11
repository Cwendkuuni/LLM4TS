package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.apache.commons.math3.util.Precision;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

/**
 * JUnit 4 test cases for the {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest extends AbstractMathTestCase {
    //

    @org.junit.Test
    public void testBrentOptimizerConstructor() {
        // Test constructor with relative and absolute thresholds and checker
        double relThreshold = 1e-5;
        double absThreshold = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = new MaxEval(100);
        BrentOptimizer optimizer = new BrentOptimizer(relThreshold, absThreshold, checker);
        assertEquals(relThreshold, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(absThreshold, optimizer.getAbsoluteThreshold(), 1e-9);
        assertNotNull(optimizer.getConvergenceChecker());

        // Test constructor with only relative and absolute thresholds
        relThreshold = 1e-6;
        absThreshold = 1e-7;
        optimizer = new BrentOptimizer(relThreshold, absThreshold);
        assertEquals(relThreshold, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(absThreshold, optimizer.getAbsoluteThreshold(), 1e-9);
        assertNull(optimizer.getConvergenceChecker());
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructorWithInvalidAbsoluteThreshold() {
        double relThreshold = 1e-5;
        double absThreshold = 0; // Threshold should be positive
        new BrentOptimizer(relThreshold, absThreshold);
    }

    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructorWithInvalidRelativeThreshold() {
        double relThreshold = 0; // Threshold should be greater than 0
        double absThreshold = 1e-6;
        new BrentOptimizer(relThreshold, absThreshold);
    }

    @org.junit.Test
    public void testDoOptimizeWithSimpleFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);

        // Test optimization with a simple function
        UnivariateFunction function = new SimpleUnivariateFunction();
        UnivariatePointValuePair result = optimizer.optimize(function);

        // Test the result
        double expectedX = 1.5;
        double expectedValue = -2.25;
        assertEquals(expectedX, result.getPoint(), 1e-9);
        assertEquals(expectedValue, result.getValue(), 1e-9);
    }

    @org.junit.Test
    public void testDoOptimizeWithComplexFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);

        // Test optimization with a complex function
        UnivariateFunction function = new ComplexUnivariateFunction();
        UnivariatePointValuePair result = optimizer.optimize(function);

        // Test the result
        double expectedX = 1.0;
        double expectedValue = 0.5;
        assertEquals(expectedX, result.getPoint(), 1e-9);
        assertEquals(expectedValue, result.getValue(), 1e-9);
    }

    @org.junit.Test
    public void testDoOptimizeWithFunctionThatHasNoMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);

        // Test optimization with a function that has no minimum
        UnivariateFunction function = new FunctionWithNoMinimum();
        UnivariatePointValuePair result = optimizer.optimize(function);

        // The optimization should not finish
        assertNull(result);
    }

    @org.junit.Test
    public void testDoOptimizeWithFunctionThatHasMultipleMinima() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);

        // Test optimization with a function that has multiple minima
        UnivariateFunction function = new FunctionWithMultipleMinima();
        UnivariatePointValuePair result = optimizer.optimize(function);

        // The optimization should converge to one of the minima
        double minX1 = 0.3;
        double minX2 = 2.7;
        List<Double> expectedValues = Arrays.asList(-0.6, -1.2);
        double expectedValue = Precision.choose(expectedValues.iterator().next(), expectedValues.iterator().next());
        assertEquals(minX1, result.getPoint(), 1e-9);
        assertEquals(expectedValue, result.getValue(), 1e-9);
    }

    @org.junit.Test
    public void testDoOptimizeWithFunctionThatHasMaxima() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);

        // Test optimization with a function that has maxima
        UnivariateFunction function = new FunctionWithMaxima();
        UnivariatePointValuePair result = optimizer.optimize(function);

        // The optimization should not converge to a maximum
        assertNull(result);
    }

    @org.junit.Test
    public void testDoOptimizeWithFunctionThatHasNoRealRoots() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);

        // Test optimization with a function that has no real roots
        UnivariateFunction function = new FunctionWithNoRealRoots();
        UnivariatePointValuePair result = optimizer.optimize(function);

        // The optimization should not finish
        assertNull(result);
    }

    @org.junit.Test
    public void testDoOptimizeWithFunctionThatHasInfiniteRoots() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);

        // Test optimization with a function that has infinite roots
        UnivariateFunction function = new FunctionWithInfiniteRoots();
        UnivariatePointValuePair result = optimizer.optimize(function);

        // The optimization should not finish
        assertNull(result);
    }

    @org.junit.Test
    public void testDoOptimizeWithFunctionThatHasComplexRoots() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);

        // Test optimization with a function that has complex roots
        UnivariateFunction function = new FunctionWithComplexRoots();
        UnivariatePointValuePair result = optimizer.optimize(function);

        // The optimization should not finish
        assertNull(result);
    }

    @org.junit.Test
    public void testDoOptimizeWithFunctionThatHasSingularPoints() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);

        // Test optimization with a function that has singular points
        UnivariateFunction function = new FunctionWithSingularPoints();
        UnivariatePointValuePair result = optimizer.optimize(function);

        // The optimization should not finish
        assertNull(result);
    }

    @org.junit.Test
    public void testDoOptimizeWithFunctionThatHasNanPoints() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);

        // Test optimization with a function that has NaN points
        UnivariateFunction function = new FunctionWithNaNPoints();
        UnivariatePointValuePair result = optimizer.optimize(function);

        // The optimization should not finish
        assertNull(result);
    }

    //
}

class SimpleUnivariateFunction implements UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(x, 2) - 2;
    }

}

class ComplexUnivariateFunction implements UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(x, 2) + Math.sin(x);
    }

}

class FunctionWithNoMinimum implements UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.abs(x) - 1;
    }

}

class FunctionWithMultipleMinima implements UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(x - 0.5, 2) - 0.25;
    }

}

class FunctionWithMaxima implements UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(x, 2) + 1;
    }

}

class FunctionWithNoRealRoots implements UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.sqrt(Math.abs(x) + 1) - Math.sqrt(2);
    }

}

class FunctionWithInfiniteRoots implements UnivariateFunction {

    @Override
    public double value(double x) {
        return 1 / x;
    }

}

class FunctionWithComplexRoots implements UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(x, 2) + Math.sqrt(Math.abs(x) + 1) * Math.sin(Math.PI * x / 2);
    }

}

class FunctionWithSingularPoints implements UnivariateFunction {

    @Override
    public double value(double x) {
        if (x == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return Math.pow(x, 2) / x;
    }

}

class FunctionWithNaNPoints implements UnivariateFunction {

    @Override
    public double value(double x) {
        if (x > 0) {
            return Math.log(x);
        } else {
            throw new ArithmeticException("NaN");
        }
    }

}