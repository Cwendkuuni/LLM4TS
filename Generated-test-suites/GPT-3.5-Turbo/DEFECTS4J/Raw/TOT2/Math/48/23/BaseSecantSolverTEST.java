import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {
    
    @Test
    public void testSolve_WhenRootIsInMiddle_ReturnsRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };
        double result = solver.solve(100, f, 4, 6);
        assertEquals(5.0, result, 1e-6);
    }
    
    @Test
    public void testSolve_WhenRootIsExact_ReturnsRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, 1, 3);
        assertEquals(2.0, result, 1e-6);
    }
    
    @Test
    public void testSolve_WhenFunctionValueIsTooSmall_ReturnsCurrentApproximation() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double result = solver.solve(100, f, 1, 5);
        assertEquals(1.570796, result, 1e-6);
    }
    
    @Test
    public void testSolve_WhenFunctionValueIsZeroAtBound_ReturnsBound() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };
        double result = solver.solve(100, f, -10, 0);
        assertEquals(-10.0, result, 1e-6);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testSolve_WhenNumberOfIterationsExceedsMaxEval_ThrowsConvergenceException() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        solver.solve(5, f, 1, 3);
    }
    
    @Test(expected = MathInternalError.class)
    public void testSolve_WhenInvalidMethodIsUsed_ThrowsMathInternalError() {
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        solver.solve(100, f, 1, 3, AllowedSolution.LEFT_SIDE);
    }
}