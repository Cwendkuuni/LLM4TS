package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizer() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x;
            }
        };
        double[] x = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(testFunction, x, GoalType.MINIMIZE);
        assertEquals(-1.0, result.getPoint()[0], 0.001);
        assertEquals(-1.0, result.getValue(), 0.001);
    }

    @Test
    public void testPowellOptimizerWithCustomChecker() throws NumberIsTooSmallException {
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                double error = current.getValue() - previous.getValue();
                return Math.abs(error) < 1e-8;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);
        UnivariateFunction testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x;
            }
        };
        double[] x = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(testFunction, x, GoalType.MINIMIZE);
        assertEquals(-1.0, result.getPoint()[0], 0.001);
        assertEquals(-1.0, result.getValue(), 0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testPowellOptimizerWithInvalidRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-9, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testPowellOptimizerWithInvalidAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 0);
    }
}