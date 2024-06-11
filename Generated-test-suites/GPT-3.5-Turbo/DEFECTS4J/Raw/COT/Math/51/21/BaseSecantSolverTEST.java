import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {
    
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10);
        assertEquals("Incorrect solution", 2.0, result, 1e-6);
    }
    
    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals("Incorrect solution", 2.0, result, 1e-6);
    }
    
    @Test
    public void testSolveWithInitialGuess() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, 1);
        assertEquals("Incorrect solution", 2.0, result, 1e-6);
    }
}