import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 9;
            }
        };
        double x0 = -10;
        double x1 = 10;
        
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        
        assertEquals(3.0, result, 0.00001);
    }
    
    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 9;
            }
        };
        double x0 = -10;
        double x1 = 10;
        double absoluteAccuracy = 0.0001;
        
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        
        assertEquals(3.0, result, 0.0001);
    }
    
    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 9;
            }
        };
        double initial = 0;
        double lowerBound = -10;
        double upperBound = 10;
        
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        
        assertEquals(-1.0, result[0], 0.00001);
        assertEquals(1.0, result[1], 0.00001);
    }
    
    @Test
    public void testBracketWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 9;
            }
        };
        double initial = 0;
        double lowerBound = -10;
        double upperBound = 10;
        int maximumIterations = 100;
        
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        
        assertEquals(-1.0, result[0], 0.00001);
        assertEquals(1.0, result[1], 0.00001);
    }
    
    @Test
    public void testMidpoint() {
        double a = -2.0;
        double b = 2.0;
        
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        
        assertEquals(0.0, result, 0.00001);
    }
}