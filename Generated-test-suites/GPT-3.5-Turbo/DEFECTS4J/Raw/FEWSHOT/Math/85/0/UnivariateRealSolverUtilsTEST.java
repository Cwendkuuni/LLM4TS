import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

public class UnivariateRealSolverUtilsTest {
    @Test
    public void testSolve() throws FunctionEvaluationException, ConvergenceException {
        // Test 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5;
            }
        };
        double result1 = UnivariateRealSolverUtils.solve(f1, 0, 10);
        Assert.assertEquals(5, result1, 0);
        
        // Test 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        double result2 = UnivariateRealSolverUtils.solve(f2, 0, Math.PI);
        Assert.assertEquals(Math.PI, result2, 0);
    }

    @Test
    public void testSolveWithAccuracy() throws FunctionEvaluationException, ConvergenceException {
        // Test 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5;
            }
        };
        double result1 = UnivariateRealSolverUtils.solve(f1, 0, 10, 0.01);
        Assert.assertEquals(5, result1, 0.01);
        
        // Test 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        double result2 = UnivariateRealSolverUtils.solve(f2, 0, Math.PI, 0.01);
        Assert.assertEquals(Math.PI, result2, 0.01);
    }

    @Test
    public void testBracket() throws FunctionEvaluationException, ConvergenceException {
        // Test 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5;
            }
        };
        double[] result1 = UnivariateRealSolverUtils.bracket(f1, 0, -10, 10);
        Assert.assertEquals(-5, result1[0], 0);
        Assert.assertEquals(5, result1[1], 0);
        
        // Test 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        double[] result2 = UnivariateRealSolverUtils.bracket(f2, 1, 0, Math.PI);
        Assert.assertEquals(0, result2[0], 0);
        Assert.assertEquals(Math.PI, result2[1], 0);
    }

    @Test
    public void testBracketWithMaxIterations() throws FunctionEvaluationException, ConvergenceException {
        // Test 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5;
            }
        };
        double[] result1 = UnivariateRealSolverUtils.bracket(f1, 0, -10, 10, 20);
        Assert.assertEquals(-5, result1[0], 0);
        Assert.assertEquals(5, result1[1], 0);
        
        // Test 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        double[] result2 = UnivariateRealSolverUtils.bracket(f2, 1, 0, Math.PI, 20);
        Assert.assertEquals(0, result2[0], 0);
        Assert.assertEquals(Math.PI, result2[1], 0);
    }

    @Test
    public void testMidpoint() {
        Assert.assertEquals(2.5, UnivariateRealSolverUtils.midpoint(0, 5), 0);
        Assert.assertEquals(0, UnivariateRealSolverUtils.midpoint(-5, 5), 0);
        Assert.assertEquals(1.5, UnivariateRealSolverUtils.midpoint(0, 3), 0);
    }
}