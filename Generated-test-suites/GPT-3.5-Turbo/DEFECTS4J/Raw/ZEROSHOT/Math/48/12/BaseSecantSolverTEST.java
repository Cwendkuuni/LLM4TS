import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {
    
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolverTest(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Test when the function returns the exact root
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return 0.0;
            }
        };
        double root1 = solver.solve(100, f1, 0.0, 10.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals("Test when the function returns the exact root", 0.0, root1, 0.0);
        
        // Test when the function value is too small
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return 1e-10;
            }
        };
        double root2 = solver.solve(100, f2, 0.0, 10.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals("Test when the function value is too small", 5.0, root2, 0.0);
        
        // Test when the current interval is within the given accuracies
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return 6.0 - x;
            }
        };
        double root3 = solver.solve(100, f3, 0.0, 10.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals("Test when the current interval is within the given accuracies", 6.0, root3, 0.0);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() {
        BaseSecantSolver solver = new BaseSecantSolverTest(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1.0;
            }
        };
        solver.solve(100, f, 0.0, 10.0, 5.0, AllowedSolution.ANY_SIDE);
    }
    
    @Test(expected = MathInternalError.class)
    public void testSolveMathInternalError() {
        BaseSecantSolver solver = new BaseSecantSolverTest(1e-6, null);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        solver.solve(100, f, 0.0, 10.0, 5.0, AllowedSolution.ANY_SIDE);
    }
}