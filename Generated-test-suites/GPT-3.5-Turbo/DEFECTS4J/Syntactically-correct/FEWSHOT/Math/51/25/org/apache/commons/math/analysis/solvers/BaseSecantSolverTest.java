import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {
    
    private static final double EPSILON = 1e-6;

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test case 1: x = 0 is the root
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        assertEquals(0.0, solver.solve(100, f1, -1, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE), EPSILON);

        // Test case 2: x = 2 is the root
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        assertEquals(2.0, solver.solve(100, f2, 0, 4, BaseSecantSolver.AllowedSolution.ANY_SIDE), EPSILON);
        
        // Test case 3: x = -3 is the root
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 3;
            }
        };
        assertEquals(-3.0, solver.solve(100, f3, -5, 0, BaseSecantSolver.AllowedSolution.ANY_SIDE), EPSILON);
    }
}