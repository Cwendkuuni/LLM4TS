import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double result = UnivariateRealSolverUtils.solve(f, -10, 10);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double result = UnivariateRealSolverUtils.solve(f, -10, 10, 0.0001);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testSolveWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        // Interval does not contain a root
        UnivariateRealSolverUtils.solve(f, 5, 10);
    }
    
    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -10, 10);
        assertEquals(-2.0, result[0], 0.0001);
        assertEquals(2.0, result[1], 0.0001);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testBracketWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        // Interval does not contain a root
        UnivariateRealSolverUtils.bracket(f, 10, -10, 0);
    }
    
    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 4);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test(expected = MathRuntimeException.class)
    public void testSetupWithNullFunction() {
        UnivariateRealSolverUtils.setup(null);
    }
}