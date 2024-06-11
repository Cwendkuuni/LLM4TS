import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5;
            }
        };
        double x0 = 0;
        double x1 = 10;
        
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        
        assertEquals(5, result, 0);
    }
    
    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5;
            }
        };
        double x0 = 0;
        double x1 = 10;
        double absoluteAccuracy = 0.1;
        
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        
        assertEquals(5, result, absoluteAccuracy);
    }
    
    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5;
            }
        };
        double initial = 3;
        double lowerBound = 0;
        double upperBound = 10;
        
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        
        assertEquals(2, result.length);
        assertTrue(result[0] < initial && initial < result[1]);
        assertTrue(f.value(result[0]) * f.value(result[1]) <= 0);
    }
    
    @Test
    public void testBracketWithIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5;
            }
        };
        double initial = 3;
        double lowerBound = 0;
        double upperBound = 10;
        int maximumIterations = 100;
        
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        
        assertEquals(2, result.length);
        assertTrue(result[0] < initial && initial < result[1]);
        assertTrue(f.value(result[0]) * f.value(result[1]) <= 0);
    }
    
    @Test
    public void testMidpoint() {
        double a = 0;
        double b = 10;
        
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        
        assertEquals(5, result, 0);
    }
}