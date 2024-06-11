package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.UnivariateFunction;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;

public class PowellOptimizerTest {
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbs() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.0, -1.0, null);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithTooSmallRel() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.0, 1.0, null);
        powellOptimizer.setRelTolerance(0.0);
        powellOptimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        }, GoalType.MINIMIZE, null);
    }
    
    @Test
    public void testOptimizeWithDefaultConstructor() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        MultivariatePointValuePair result = powellOptimizer.optimize(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        }, GoalType.MINIMIZE, null);
        assertEquals(-1.0, result.getPoint()[0], 1e-6);
        assertEquals(-1.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
    
    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        MultivariatePointValuePair result = powellOptimizer.optimize(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] * x[0] + x[1] * x[1], 2);
            }
        }, GoalType.MINIMIZE, null);
        assertEquals(-1.0 / Math.sqrt(2), result.getPoint()[0], 1e-6);
        assertEquals(-1.0 / Math.sqrt(2), result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
    
    @Test
    public void testOptimizeWithCustomLineSearchTolerances() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6, 1e-8, 1e-8);
        MultivariatePointValuePair result = powellOptimizer.optimize(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] * x[0] + x[1] * x[1], 2);
            }
        }, GoalType.MINIMIZE, null);
        assertEquals(-1.0 / Math.sqrt(2), result.getPoint()[0], 1e-8);
        assertEquals(-1.0 / Math.sqrt(2), result.getPoint()[1], 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }
    
    @Test
    public void testOptimizeWithCustomGoalType() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        powellOptimizer.setGoalType(GoalType.MAXIMIZE);
        MultivariatePointValuePair result = powellOptimizer.optimize(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return -(x[0] * x[0] + x[1] * x[1]);
            }
        }, GoalType.MAXIMIZE, null);
        assertEquals(-1.0, result.getPoint()[0], 1e-6);
        assertEquals(-1.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
    
    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return previous.getValue() > current.getValue() * 0.1;
        }
    }
}