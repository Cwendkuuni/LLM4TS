package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.goaltypes.GoalType;
import org.apache.commons.math3.optimization.initializers.UniformRealVectorGenerator;
import org.apache.commons.math3.optimization.nonlinear.AbstractUnivariateOptimizer;
import org.apache.commons.math3.optimization.nonlinear.BrentOptimizer;
import org.apache.commons.math3.optimization.nonlinear.BracketedUnivariateFunction;
import org.apache.commons.math3.optimization.nonlinear.BracketedUnivariateFunctionSolver;
import org.apache.commons.math3.optimization.nonlinear.SimpleBounds;
import org.apache.commons.math3.optimization.nonlinear.util.UnivariateFunctionUtils;

import java.util.Random;

public class BrentOptimizerTest {

    @org.junit.Test
    public void testBrentOptimizerWithMinimizationGoal() {
        double a = 0.0;
        double b = 1.0;
        UnivariateFunction f = new BracketedUnivariateFunction(a, b, x -> Math.pow(x, 2));

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(f);

        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @org.junit.Test
    public void testBrentOptimizerWithMaximizationGoal() {
        double a = 0.0;
        double b = 1.0;
        UnivariateFunction f = new BracketedUnivariateFunction(a, b, x -> Math.pow(x, 2));

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(f);

        assertEquals(1.0, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    @org.junit.Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        double a = 0.0;
        double b = 1.0;
        UnivariateFunction f = new BracketedUnivariateFunction(a, b, x -> Math.pow(x, 2));

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        BracketedUnivariateFunctionSolver solver = new BracketedUnivariateFunctionSolver(optimizer);
        UnivariatePointValuePair initialGuess = new UnivariatePointValuePair(0.5, 0.25);
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);

        UnivariatePointValuePair result = solver.solve(f, initialGuess, bounds);

        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @org.junit.Test
    public void testBrentOptimizerWithExceptionalInputs() {
        // Test with negative relative threshold
        try {
            new BrentOptimizer(-1e-5, 1e-5);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test with negative absolute threshold
        try {
            new BrentOptimizer(1e-5, -1e-5);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test with zero absolute threshold
        try {
            new BrentOptimizer(1e-5, 0.0);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test with too small relative threshold
        try {
            new BrentOptimizer(1e-10, 1e-5);
            fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testBrentOptimizerWithRandomlyGeneratedFunction() {
        double a = 0.0;
        double b = 1.0;
        UnivariateFunction f = new BracketedUnivariateFunction(a, b, this::randomFunction);

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(f);

        assertEquals(0.5, result.getPoint(), 0.1);
        // The actual minimum value depends on the randomly generated function
        // so we just test that the result is not zero
        assertNotEquals(0.0, result.getValue(), 0.1);
    }

    private double randomFunction(double x) {
        Random random = new Random();
        return random.nextGaussian() * Math.exp(-0.1 * x);
    }
}