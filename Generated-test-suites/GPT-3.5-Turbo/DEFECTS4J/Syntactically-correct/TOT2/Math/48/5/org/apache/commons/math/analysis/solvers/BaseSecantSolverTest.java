import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_ExactRoot() {
        // Given
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        
        // When
        double result = solver.solve(100, f, -1, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        // Then
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_Bracketing() {
        // Given
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 2; // Root at sqrt(2)
            }
        };
        
        // When
        double result = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        // Then
        assertEquals(Math.sqrt(2), result, 1e-6);
    }

    @Test
    public void testSolve_FunctionValueAccuracy() {
        // Given
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 2; // Root at sqrt(2)
            }
        };
        
        // When
        double result = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        // Then
        assertEquals(Math.sqrt(2), result, 1e-6);
    }
}