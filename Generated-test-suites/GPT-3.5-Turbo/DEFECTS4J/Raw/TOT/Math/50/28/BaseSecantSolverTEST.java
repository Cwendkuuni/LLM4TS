import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.Method;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

public class BaseSecantSolverTest {

    // Test case for the solve method with default allowed solution
    @Test
    public void testSolve_DefaultAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 4 * x + 3;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(0.0001, Method.PEGASUS);
        double result = solver.solve(100, f, -10, 10);
        
        Assert.assertEquals(-0.75, result, 0.0001);
    }
    
    // Test case for the solve method with specified allowed solution
    @Test
    public void testSolve_SpecifiedAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(0.0001, Method.IllinoisSolver);
        double result = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        Assert.assertEquals(1.0, result, 0.0001);
    }
    
    // Test case for the solve method with under-approximation
    @Test
    public void testSolve_UnderApproximation() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.cos(x) - 1e-6;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(0.0001, Method.REGULA_FALSI);
        double result = solver.solve(100, f, -10, 10);
        
        Assert.assertEquals(0.0, result, 0.0001);
    }
    
    // Test case for the solve method with over-approximation
    @Test
    public void testSolve_OverApproximation() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) + 1e-6;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(0.0001, Method.PEGASUS);
        double result = solver.solve(100, f, -10, 10);
        
        Assert.assertEquals(0.0, result, 0.0001);
    }

    // Test case for the solve method with no solution within the given range
    @Test(expected = MathInternalError.class)
    public void testSolve_NoSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) + 1;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(0.0001, Method.IllinoisSolver);
        double result = solver.solve(100, f, -10, 10);
    }
    
}