import static org.junit.Assert.*;

import org.apache.commons.math.analysis.SimpleUnivariateRealFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Test;

public class BaseSecantSolverTest {

    // Test solve method with a simple linear function
    @Test
    public void testSolve_LinearFunction() {
        UnivariateRealFunction f = new SimpleUnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.Illinois);

        double solution = solver.solve(100, f, 0, 5);

        assertEquals(2.0, solution, 1e-6);
    }

    // Test solve method with a constant function
    @Test
    public void testSolve_ConstantFunction() {
        UnivariateRealFunction f = new SimpleUnivariateRealFunction() {
            public double value(double x) {
                return 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.Illinois);

        double solution = solver.solve(100, f, 0, 5);

        assertEquals(0.0, solution, 1e-6);
    }

    // Test solve method with a quadratic function
    @Test
    public void testSolve_QuadraticFunction() {
        UnivariateRealFunction f = new SimpleUnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.Illinois);

        double solution = solver.solve(100, f, 0, 5);

        assertEquals(1.0, solution, 1e-6);
    }

    // Test solve method with a converged solution
    @Test
    public void testSolve_ConvergedSolution() {
        UnivariateRealFunction f = new SimpleUnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.Illinois);

        double solution = solver.solve(1, f, 0, 5);

        assertEquals(2.0, solution, 1e-6);
    }

    // Test solve method with a diverged solution
    @Test(expected = ConvergenceException.class)
    public void testSolve_DivergedSolution() {
        UnivariateRealFunction f = new SimpleUnivariateRealFunction() {
            public double value(double x) {
                return x + 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.Illinois);

        solver.solve(100, f, 0, 5);
    }

}