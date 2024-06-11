import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        // Typical Use Case
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10);
        assertEquals(2.0, result, 1e-6);
        
        // Edge Case
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - x - 1;
            }
        }, 0, 2);
        assertEquals(1.324717957244746, result, 1e-6);
        
        // Error Scenario
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1 / x; // Division by zero
            }
        }, 1, 5);
        assertTrue(Double.isNaN(result));

    }

}