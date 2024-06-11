import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

import org.junit.Test;

public class BaseSecantSolverTest {
    @Test
    public void testSolve() {
        // Test case 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BaseSecantSolver solver1 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result1 = solver1.solve(100, f1, 1, 10);
        assertEquals(5, result1, 0.01);

        // Test case 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver2 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double result2 = solver2.solve(100, f2, 0, Math.PI);
        assertEquals(0.523598775598299, result2, 0.01);

        // Test case 3
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BaseSecantSolver solver3 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result3 = solver3.solve(100, f3, 0, 10);
        assertEquals(2, result3, 0.01);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_InvalidMethod() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, null);
        solver.solve(100, f, 1, 10);
    }
}