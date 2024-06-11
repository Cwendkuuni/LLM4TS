package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnivariateOptimizer;
import org.apache.commons.math3.optimization.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;

/**
 * Test cases for PowellOptimizer class.
 */
public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizerMinimization() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[] {2.0}, new MaxEval(100));
        double expectedX = 0.0;
        double expectedFval = 0.0;
        assertEquals(expectedX, result.getPoint()[0], 1e-6);
        assertEquals(expectedFval, result.getValue(), 1e-6);
    }

    @Test
    public void testPowellOptimizerMinimizationWithCustomChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-7, 1e-7));
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[] {3.14159265}, new MaxEval(100));
        double expectedX = 0.0;
        double expectedFval = 0.0;
        assertEquals(expectedX, result.getPoint()[0], 1e-7);
        assertEquals(expectedFval, result.getValue(), 1e-7);
    }

    @Test
    public void testPowellOptimizerMaximization() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -x * x;
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, new double[] {2.0}, new MaxEval(100));
        double expectedX = 0.0;
        double expectedFval = 0.0;
        assertEquals(expectedX, result.getPoint()[0], 1e-6);
        assertEquals(expectedFval, result.getValue(), 1e-6);
    }

    @Test
    public void testPowellOptimizerMaximizationWithCustomChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-7, 1e-7));
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(Math.sin(x), 2);
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, new double[] {3.14159265}, new MaxEval(100));
        double expectedX = Math.PI;
        double expectedFval = 1.0;
        assertEquals(expectedX, result.getPoint()[0], 1e-6);
        assertEquals(expectedFval, result.getValue(), 1e-6);
    }

    @Test
    public void testPowellOptimizerBracketFinderMaximization() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(Math.sin(x), 2);
            }
        };
        double[] bracketedValues = {1.0, 3.14159265, 5.0};
        BracketFinder bracketFinder = new BracketFinder();
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, bracketedValues, new MaxEval(100));
        double expectedX = Math.PI;
        double expectedFval = 1.0;
        assertEquals(expectedX, result.getPoint()[0], 1e-6);
        assertEquals(expectedFval, result.getValue(), 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testPowellOptimizerWithNegativeAbsThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, -1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testPowellOptimizerWithNegativeRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(-1e-6, 1e-6);
    }
}