//Test for NonLinearConjugateGradientOptimizer class

package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
    
    //Test doOptimize() method
    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        
        //Test with minimum value problem
        //2x^2 + 3x - 4 = 0, Solve for x
        double[] startPoint = {0};
        GoalType goal = GoalType.MINIMIZE;
        
        PointValuePair result = optimizer.doOptimize(startPoint, goal);
        
        Assert.assertArrayEquals(new double[]{-1.5}, result.getPoint(), 0.0001);
        Assert.assertEquals(5, result.getValue(), 0.0001);
        
        //Test with maximum value problem
        //y = -x^2 + 2x + 1, Maximize y
        startPoint = new double[]{0};
        goal = GoalType.MAXIMIZE;
        
        result = optimizer.doOptimize(startPoint, goal);
        
        Assert.assertArrayEquals(new double[]{1}, result.getPoint(), 0.0001);
        Assert.assertEquals(2, result.getValue(), 0.0001);
    }
    
    //Test findUpperBound() method
    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        
        //Test with a=0, h=1
        UnivariateFunction f = new LineSearchFunction(new double[]{1});
        double a = 0;
        double h = 1;
        
        double upperBound = optimizer.findUpperBound(f, a, h);
        
        Assert.assertEquals(4, upperBound, 0.0001);
        
        //Test with a=0, h=5
        f = new LineSearchFunction(new double[]{1});
        a = 0;
        h = 5;
        
        upperBound = optimizer.findUpperBound(f, a, h);
        
        Assert.assertEquals(8, upperBound, 0.0001);
        
        //Test with a=0, h=0.5
        f = new LineSearchFunction(new double[]{1});
        a = 0;
        h = 0.5;
        
        upperBound = optimizer.findUpperBound(f, a, h);
        
        Assert.assertEquals(2, upperBound, 0.0001);
    }
    
    //Test LineSearchFunction class
    @Test
    public void testLineSearchFunction() {
        double[] searchDirection = {1};
        NonLinearConjugateGradientOptimizer.LineSearchFunction function = new NonLinearConjugateGradientOptimizer.LineSearchFunction(searchDirection);
        
        //Test when x=0
        double result = function.value(0);
        Assert.assertEquals(0, result, 0.0001);
        
        //Test when x=2
        result = function.value(2);
        Assert.assertEquals(2, result, 0.0001);
        
        //Test when x=-2
        result = function.value(-2);
        Assert.assertEquals(-2, result, 0.0001);
    }
    
    //Test IdentityPreconditioner class
    @Test
    public void testIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        
        double[] variables = {1, 2, 3};
        double[] r = {3, 2, 1};
        double[] result = preconditioner.precondition(variables, r);
        
        Assert.assertArrayEquals(r, result, 0.0001);
    }
    
}