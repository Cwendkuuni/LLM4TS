package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnivariateOptimizer;
import org.apache.commons.math3.optimization.SimpleBoundingMethod;
import org.apache.commons.math3.optimization.BracketedRealValuedFunction;
import org.apache.commons.math3.optimization.UnivariateFunctionAdapter;
import org.apache.commons.math3.optimization.InitialGuess;

public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizer() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8);

        // Test optimization of a simple function
        UnivariateFunction function = new UnivariateFunctionAdapter(f -> f * f) {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        PointValuePair result = optimizer.optimize(function, new InitialGuess(0.5), new GoalType(GoalType.MINIMIZE));
        assertEquals(0.25, result.getPoint(), 1e-8);
        assertEquals(0.0625, result.getValue(), 1e-8);

        // Test optimization of a function with multiple local minima
        function = new UnivariateFunctionAdapter(x -> x * x * (1 - x)) {
            @Override
            public double value(double x) {
                return x * x * (1 - x);
            }
        };

        result = optimizer.optimize(function, new InitialGuess(0.5), new GoalType(GoalType.MINIMIZE));
        assertEquals(0.5, result.getPoint(), 1e-8);
        assertEquals(0.25, result.getValue(), 1e-8);

        // Test optimization of a function with multiple local maxima
        function = new UnivariateFunctionAdapter(x -> Math.pow(Math.sin(x), 2)) {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        result = optimizer.optimize(function, new InitialGuess(0.5), new GoalType(GoalType.MAXIMIZE));
        assertEquals(1.5707963267948966, result.getPoint(), 1e-4);
        assertEquals(1.127626740822519, result.getValue(), 1e-4);

        // Test optimization with custom bracketing
        BracketedRealValuedFunction bracketedFunction = new BracketedRealValuedFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * x + 1;
            }

            @Override
            public double bracketMin(double x1, double x2) {
                return Math.min(Math.min(x1, x2), Math.cbrt(3.0 / 2.0));
            }

            @Override
            public double bracketMax(double x1, double x2) {
                return Math.max(Math.max(x1, x2), Math.cbrt(3.0 / 2.0));
            }
        };

        result = optimizer.optimize(new UnivariateFunctionAdapter(bracketedFunction), new InitialGuess(1.0), new GoalType(GoalType.MINIMIZE));
        assertEquals(0.5, result.getPoint(), 1e-8);
        assertEquals(-0.625, result.getValue(), 1e-8);

        // Test optimization with custom initial guess
        result = optimizer.optimize(function, new InitialGuess(1.0), new GoalType(GoalType.MINIMIZE));
        assertEquals(0.25, result.getPoint(), 1e-8);
        assertEquals(0.0625, result.getValue(), 1e-8);

        // Test optimization with custom convergence checker
        PowellOptimizer optimizerWithCustomChecker = new PowellOptimizer(1e-8, 1e-8, new MyConvergenceChecker());
        result = optimizerWithCustomChecker.optimize(function, new InitialGuess(0.5), new GoalType(GoalType.MINIMIZE));
        assertEquals(0.25, result.getPoint(), 1e-8);
        assertEquals(0.0625, result.getValue(), 1e-8);

        // Test optimization with too many evaluations
        UnivariateOptimizer optimizerWithMaxEval = new PowellOptimizer(1e-8, 1e-8, new MaxEval(100));
        try {
            optimizerWithMaxEval.optimize(function, new InitialGuess(0.5), new GoalType(GoalType.MINIMIZE));
            fail("Expected 'TooManyEvaluationsException'");
        } catch (Exception e) {
            assertTrue(e instanceof org.apache.commons.math3.exception.TooManyEvaluationsException);
        }

        // Test optimization with custom bounding method
        SimpleBoundingMethod boundingMethod = new SimpleBoundingMethod() {
            @Override
            public double upperBound(UnivariateFunction f, double x) {
                return f.value(x) + 1e-8;
            }
        };
        optimizer = new PowellOptimizer(1e-8, 1e-8, boundingMethod);
        result = optimizer.optimize(function, new InitialGuess(0.5), new GoalType(GoalType.MINIMIZE));
        assertEquals(0.25, result.getPoint(), 1e-8);
        assertEquals(0.0625, result.getValue(), 1e-8);
    }

    static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            return Math.abs(previous.getValue() - current.getValue()) < 1e-6;
        }
    }
}