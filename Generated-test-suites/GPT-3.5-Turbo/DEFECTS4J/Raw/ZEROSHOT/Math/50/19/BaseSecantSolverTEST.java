import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        // Call the solve method and check the result
        double result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(2.0, result, 0.000001);
    }

    @Test
    public void testSolveWithStartValue() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
        
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        // Call the solve method with a start value and check the result
        double result = solver.solve(100, f, 0, 10, 5, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(2.0, result, 0.000001);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
        
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        // Call the solve method with an allowed solution and check the result
        double result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        Assert.assertEquals(-2.0, result, 0.000001);
    }
}