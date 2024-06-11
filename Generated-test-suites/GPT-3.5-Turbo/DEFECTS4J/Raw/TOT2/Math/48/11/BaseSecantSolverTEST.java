import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {
    
    @Test
    public void testSolve() {
        // Test the solve method with typical use case
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 3, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }
    
    @Test
    public void testSolveEdgeCases() {
        // Test the solve method with edge cases
        // Edge case 1: Zero function value
        BaseSecantSolver solver1 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result1 = solver1.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, -10, 10, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, result1, 1e-6);
        
        // Edge case 2: Extreme function values
        BaseSecantSolver solver2 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result2 = solver2.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x * x * x; // Very high growth function
            }
        }, -100, 100, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, result2, 1e-6);
    }
    
    @Test
    public void testSolveExceptions() {
        // Test the solve method with exception scenarios
        // Empty function
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            double result = solver.solve(100, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Double.NaN; // NaN function
                }
            }, -10, 10, AllowedSolution.ANY_SIDE);
        } catch (MathInternalError e) {
            // This exception is expected
        }
    }
}