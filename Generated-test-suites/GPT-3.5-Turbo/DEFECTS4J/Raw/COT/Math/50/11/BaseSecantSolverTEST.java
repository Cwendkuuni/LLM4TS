import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        // Test basic functionality of solve method
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, 0, 10);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_maxEval() {
        // Test that solve method terminates after maxEval iterations
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(10, f, 0, 10);
        assertEquals(Double.NaN, result, 1e-6);
    }

    @Test
    public void testSolve_allowedSolution() {
        // Test that solve method only returns solutions allowed by the allowedSolution parameter
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(Double.NaN, result, 1e-6);
    }

    @Test
    public void testSolve_approximationAccuracy() {
        // Test that solve method returns solution within the specified approximation accuracy
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, 0, 10, 1.9);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_exactRoot() {
        // Test that solve method returns exact root if it exists
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, -2, 10, 2);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_zeroFunctionValue() {
        // Test that solve method returns root when function value is 0
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        double result = solver.solve(100, f, -2, 10);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_noBracketing() {
        // Test that solve method throws exception when there is no bracketing interval
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            solver.solve(100, f, 0, 1);
            fail("Exception expected");
        } catch (RuntimeException e) {
            assertTrue(e instanceof MathInternalError);
        }
    }
}