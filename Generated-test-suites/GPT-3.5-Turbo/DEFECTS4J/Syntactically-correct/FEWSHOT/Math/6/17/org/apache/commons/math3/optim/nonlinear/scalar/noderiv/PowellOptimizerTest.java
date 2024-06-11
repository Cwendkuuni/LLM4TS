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
import org.junit.Assert;
import org.junit.Test;

public class PowellOptimizerTest {
    
    //Create an instance of PowellOptimizer
    private PowellOptimizer powellOptimizer = new PowellOptimizer(1.0, 0.1);
    
    //Create a custom convergence checker
    private ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
        
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return false;
        }
    };
    
    //Test doOptimize()
    @Test
    public void testDoOptimize() {
        powellOptimizer.setConvergenceChecker(customChecker);
        PointValuePair result = powellOptimizer.doOptimize();
        Assert.assertNull(result);
    }
    
    //Test newPointAndDirection()
    @Test
    public void testNewPointAndDirection() {
        int n = 5;
        double[] p = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        double[] d = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        double alpha = 1.0;
        
        PowellOptimizer powellOptimizer = new PowellOptimizer(1.0, 0.1);
        double[][] result = powellOptimizer.newPointAndDirection(p, d, alpha);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.length);
        Assert.assertEquals(n, result[0].length);
        Assert.assertEquals(n, result[1].length);
    }
    
    //Test search() in LineSearch class
    @Test
    public void testLineSearchSearch() {
        int n = 5;
        double[] p = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        double[] d = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        
        PowellOptimizer powellOptimizer = new PowellOptimizer(1.0, 0.1);
        PowellOptimizer.LineSearch lineSearch = powellOptimizer.new LineSearch(1.0, 0.1);
        
        UnivariatePointValuePair result = lineSearch.search(p, d);
        Assert.assertNotNull(result);
    }
}