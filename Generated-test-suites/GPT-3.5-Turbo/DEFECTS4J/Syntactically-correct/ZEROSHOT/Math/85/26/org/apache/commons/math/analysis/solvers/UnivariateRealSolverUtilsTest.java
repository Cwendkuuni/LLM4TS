import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

import org.junit.Assert;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double result = UnivariateRealSolverUtils.solve(f, -2, 2);
        
        Assert.assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double result = UnivariateRealSolverUtils.solve(f, -2, 2, 0.0001);
        
        Assert.assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -2, 2);
        
        Assert.assertArrayEquals(new double[]{-1, 1}, result, 0.0001);
    }
    
    @Test
    public void testBracketWithMaximumIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -2, 2, 100);
        
        Assert.assertArrayEquals(new double[]{-1, 1}, result, 0.0001);
    }
    
    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(-2, 2);
        
        Assert.assertEquals(0.0, result, 0.0001);
    }
}