import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-6;

    @Test
    public void testSolve_WithDefaultMethod_Success() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        assertEquals(2.0, result, DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_WithDifferentMethod_Success() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
        
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        assertEquals(2.0, result, DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_WithBoundaryValues_Success() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
        
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, -100, 100, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        
        assertEquals(2.0, result, DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_WithInvalidMethod_ExceptionThrown() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, null);
        
        solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

}