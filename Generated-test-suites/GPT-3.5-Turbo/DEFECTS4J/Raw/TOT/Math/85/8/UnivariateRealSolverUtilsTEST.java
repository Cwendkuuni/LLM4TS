import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Assert;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{3, -5, 2});
        double x0 = 0;
        double x1 = 3;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        Assert.assertEquals(1.0, result, 0.00001);
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_NullFunction() throws FunctionEvaluationException {
        UnivariateRealFunction f = null;
        double x0 = 0;
        double x1 = 3;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolve_InvalidInterval() throws FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{3, -5, 2});
        double x0 = 3;
        double x1 = 0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testSolve_ConvergenceException() throws FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{3, -5, 2});
        double x0 = 0;
        double x1 = 3;
        UnivariateRealSolverUtils.solve(f, x0, x1, 0.00001);
    }
    
    @Test
    public void testBracket() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{3, -5, 2});
        double initial = 1;
        double lowerBound = 0;
        double upperBound = 3;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        Assert.assertEquals(0.0, result[0], 0.00001);
        Assert.assertEquals(3.0, result[1], 0.00001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_NullFunction() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = null;
        double initial = 1;
        double lowerBound = 0;
        double upperBound = 3;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_InvalidInterval() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{3, -5, 2});
        double initial = 4;
        double lowerBound = 0;
        double upperBound = 3;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_InvalidMaximumIterations() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{3, -5, 2});
        double initial = 1;
        double lowerBound = 0;
        double upperBound = 3;
        int maximumIterations = -1;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testBracket_ConvergenceException() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{3, -5, 2});
        double initial = 1;
        double lowerBound = 0;
        double upperBound = 3;
        int maximumIterations = 1;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }
    
    @Test
    public void testMidpoint() {
        double a = 0;
        double b = 2;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        Assert.assertEquals(1.0, result, 0.00001);
    }
    
}