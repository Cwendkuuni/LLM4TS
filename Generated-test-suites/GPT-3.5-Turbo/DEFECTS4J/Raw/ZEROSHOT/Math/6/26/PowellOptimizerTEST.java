package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.junit.Test;

import static org.junit.Assert.*;

public class PowellOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0, 1.0, null);
        
        double[] startPoint = {1.0, 2.0};
        double result[] = optimizer.optimize(100, TestMultivariateFunction(), GoalType.MINIMIZE, startPoint).getPoint();
        
        assertEquals(0.0, result[0], Double.MIN_VALUE);
        assertEquals(0.0, result[1], Double.MIN_VALUE);
    }
    
    public MultivariateFunction TestMultivariateFunction() {
        return new MultivariateFunction() {
            public double value(double[] variables) {
                double x = variables[0];
                double y = variables[1];
                
                return x * x + y * y;
            }
        };
    }
}