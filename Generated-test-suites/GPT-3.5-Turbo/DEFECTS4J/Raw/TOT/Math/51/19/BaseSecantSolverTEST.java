import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    // Test case 1: Test solve() method with maximum allowed evaluation set to 1000
    // Typical use case
    @Test
    public void testSolve_maxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(1000, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2, result, 1e-6);
    }

    // Test case 2: Test solve() method with a function where one of the bounds is exact root
    // Typical use case
    @Test
    public void testSolve_exactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, 2, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2, result, 1e-6);
    }

    // Test case 3: Test solve() method with a function where approximation is not possible within given accuracies
    // Edge case
    @Test(expected = MathInternalError.class)
    public void testSolve_unreachableAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    // Test case 4: Test solve() method with a function where the function value has crossed the 0
    // error scenario
    @Test
    public void testSolve_crossedZero() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, 2, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2, result, 1e-6);
    }

}