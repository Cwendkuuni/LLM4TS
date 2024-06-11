
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Test;

public class BaseSecantSolverTest {
    
    @Test(expected = ConvergenceException.class)
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 0, 1);
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testSolve_ExactRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 0, 3);
        assertEquals(2.0, result, 1e-6);
    }
    
    @Test
    public void testSolve_FunctionValueZero() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 0, 3);
        assertEquals(2.0, result, 1e-6);
    }
    
    @Test
    public void testSolve_SmallFunctionValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1e-8;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 0, 3);
        assertEquals(1e-4, result, 1e-6);
    }
    
    @Test
    public void testSolve_AboveSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.setAllowed(BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        double result = solver.solve(100, f, 0, 1);
        assertEquals(Double.POSITIVE_INFINITY, result, 1e-6);
    }
    
    @Test
    public void testSolve_BelowSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.setAllowed(BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        double result = solver.solve(100, f, 0, 3);
        assertEquals(Double.POSITIVE_INFINITY, result, 1e-6);
    }
    
    @Test
    public void testSolve_RightSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.setAllowed(BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        double result = solver.solve(100, f, 0, 1);
        assertEquals(1.0, result, 1e-6);
    }
    
    @Test
    public void testSolve_LeftSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.setAllowed(BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        double result = solver.solve(100, f, 0, 3);
        assertEquals(3.0, result, 1e-6);
    }
}
