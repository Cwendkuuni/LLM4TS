import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        double result = UnivariateRealSolverUtils.solve(f, 0, 3);
        assertEquals(2.0, result, 0.01);
        
        double result2 = UnivariateRealSolverUtils.solve(f, 0, 3, 0.01);
        assertEquals(2.0, result2, 0.01);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        UnivariateRealSolverUtils.solve(f, -1, 1); // No solution exists in this interval
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                if (x == 1) {
                    throw new RuntimeException();
                } else {
                    return x * x - 4;
                }
            }
        };
        
        UnivariateRealSolverUtils.solve(f, 0, 3); // Exception occurs while evaluating the function
    }
    
    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -3, 3);
        assertEquals(-1.0, result[0], 0.01);
        assertEquals(1.0, result[1], 0.01);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testBracketConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        UnivariateRealSolverUtils.bracket(f, 0, -1, 1); // No brackets possible in this interval
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testBracketFunctionEvaluationException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                if (x == 1) {
                    throw new RuntimeException();
                } else {
                    return x * x - 4;
                }
            }
        };
        
        UnivariateRealSolverUtils.bracket(f, 0, -3, 3); // Exception occurs while evaluating the function
    }
    
    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(2, 4);
        assertEquals(3.0, result, 0.01);
    }
}