import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver.Method;
import org.apache.commons.math.exception.ConvergenceException;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        // Creating a simple linear function: f(x) = 2x + 3
        UnivariateRealFunction f = new PolynomialFunction(new double[]{3, 2});

        // Creating a BaseSecantSolver with Regula Falsi method
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);

        // Testing a typical use case: finding the root of a linear function
        double result = solver.solve(100, f, 0, 5);
        assertEquals(-1.5, result, 1e-6);

        // Testing an edge case: function that has only one root
        UnivariateRealFunction f2 = new PolynomialFunction(new double[]{1, 0, 0});
        double result2 = solver.solve(100, f2, -10, 10);
        assertEquals(0, result2, 1e-6);

        // Testing an error scenario: function that does not converge
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        try {
            solver.solve(100, f3, -1, 1);
            fail("ConvergenceException should have been thrown");
        } catch (ConvergenceException e) {
            // Pass
        }
    }
}