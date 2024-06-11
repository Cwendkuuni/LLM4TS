import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {
    
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }
    
    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }
    
    @Test
    public void testSolveWithStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, 1);
        assertEquals(2.0, solution, 1e-6);
    }
    
    @Test
    public void testSolveWithFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.setFunctionValueAccuracy(1e-4);
        double solution = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 1e-4);
    }
    
    @Test
    public void testSolveWithRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 1e-4);
    }
    
    @Test
    public void testSolveWithAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.setAbsoluteAccuracy(1e-4);
        double solution = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 1e-4);
    }
    
    @Test
    public void testSolveWithMaxEvaluations() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(10, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }
    
    @Test
    public void testSolveWithInvalidInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            double solution = solver.solve(100, new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x - 4;
                }
            }, 5, 0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    @Test
    public void testSolveWithExactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 2, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }

}