import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_DefaultAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = x -> x * x - 4; // simple function x^2 - 4

        double result = solver.solve(100, function, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(2.0, result, 1e-6); // Root should be 2
    }

    @Test
    public void testSolve_AbsoluteAccuracyAndAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-10, 1e-7, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = x -> x - 5; // simple linear function x - 5

        double result = solver.solve(100, function, 4, 6, 4.5, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        assertEquals(5.0, result, 1e-7); // Root should be 5
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_InvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS); // Invalid Method
        UnivariateRealFunction function = x -> Math.sin(x); // sine function

        solver.solve(100, function, 0, 2 * Math.PI, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    // Add more test cases for edge cases and exception handling scenarios

}