import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolve_WithDefaultSolver() {
        // Typical use case
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        double x0 = 0.0;
        double x1 = 5.0;
        
        try {
            double result1 = UnivariateRealSolverUtils.solve(f1, x0, x1);
            assertEquals(2.0, result1, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("An exception was thrown");
        }
        
        // Edge case
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        x0 = -10.0;
        x1 = 10.0;
        
        try {
            double result2 = UnivariateRealSolverUtils.solve(f2, x0, x1);
            assertEquals(1.0, result2, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("An exception was thrown");
        }
        
        // Error scenario
        UnivariateRealFunction f3 = null;
        x0 = -10.0;
        x1 = 10.0;
        
        try {
            double result3 = UnivariateRealSolverUtils.solve(f3, x0, x1);
            fail("An exception should have been thrown");
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testSolve_WithCustomSolver() {
        // Typical use case
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        double x0 = 0.0;
        double x1 = 5.0;
        double acc = 0.01;
        
        try {
            double result1 = UnivariateRealSolverUtils.solve(f1, x0, x1, acc);
            assertEquals(2.0, result1, acc);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("An exception was thrown");
        }
        
        // Edge case
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        x0 = -10.0;
        x1 = 10.0;
        
        try {
            double result2 = UnivariateRealSolverUtils.solve(f2, x0, x1, acc);
            assertEquals(1.0, result2, acc);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("An exception was thrown");
        }
        
        // Error scenario
        UnivariateRealFunction f3 = null;
        x0 = -10.0;
        x1 = 10.0;
        
        try {
            double result3 = UnivariateRealSolverUtils.solve(f3, x0, x1, acc);
            fail("An exception should have been thrown");
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testBracket() {
        // Typical use case
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        double initial = 0.0;
        double lowerBound = -10.0;
        double upperBound = 10.0;
        int maxIterations = 100;
        
        try {
            double[] result1 = UnivariateRealSolverUtils.bracket(f1, initial, lowerBound, upperBound, maxIterations);
            assertEquals(-2.0, result1[0], 0.0001);
            assertEquals(2.0, result1[1], 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("An exception was thrown");
        }
        
        // Edge case
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        initial = 0.0;
        lowerBound = -10.0;
        upperBound = 10.0;
        maxIterations = 100;
        
        try {
            double[] result2 = UnivariateRealSolverUtils.bracket(f2, initial, lowerBound, upperBound, maxIterations);
            assertEquals(0.0, result2[0], 0.0001);
            assertEquals(2.0, result2[1], 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("An exception was thrown");
        }
        
        // Error scenario
        UnivariateRealFunction f3 = null;
        initial = 0.0;
        lowerBound = -10.0;
        upperBound = 10.0;
        maxIterations = 100;
        
        try {
            double[] result3 = UnivariateRealSolverUtils.bracket(f3, initial, lowerBound, upperBound, maxIterations);
            fail("An exception should have been thrown");
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;
        
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2.0, result, 0.0001);
    }
}