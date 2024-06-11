import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver() {
        // Test default constructor
        BaseSecantSolver solver = new BaseSecantSolver(DefaultSolverType.REGULA_FALSI);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(Method.REGULA_FALSI, solver.getMethod());

        // Test constructor with absolute accuracy
        solver = new BaseSecantSolver(1e-7, DefaultSolverType.ILLINOIS);
        assertEquals(1e-7, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(Method.ILLINOIS, solver.getMethod());

        // Test constructor with relative accuracy and absolute accuracy
        solver = new BaseSecantSolver(1e-4, 1e-6, DefaultSolverType.PEGASUS);
        assertEquals(1e-4, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(Method.PEGASUS, solver.getMethod());

        // Test constructor with relative accuracy, absolute accuracy, and function value accuracy
        solver = new BaseSecantSolver(1e-3, 1e-6, 1e-8, DefaultSolverType.ILLINOIS);
        assertEquals(1e-3, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(1e-8, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(Method.ILLINOIS, solver.getMethod());

        // Test solve method with min, max, and function
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        double result = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(-1.0, result, 1e-6);

        // Test solve method with min, max, start value, and function
        result = solver.solve(10, function, -2.0, 2.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(-1.0, result, 1e-6);

        // Test solve method with min, max, start value, function, and allowed solution
        result = solver.solve(10, function, -2.0, 2.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertTrue(result < 0);

        // Test solve method with min, max, start value, function, and allowed solution (right side)
        result = solver.solve(10, function, -2.0, 2.0, 1.0, AllowedSolution.RIGHT_SIDE);
        assertTrue(result > 0);

        // Test solve method with exception
        function = x -> 1.0 / x;
        try {
            solver.solve(10, function, 0.0, 1.0, AllowedSolution.ANY_SIDE);
        } catch (NoBracketingException e) {
            //expected
        }

        // Test maxEval exception
        function = x -> Math.log(x);
        try {
            solver.solve(0, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        } catch (MaxEvalException e) {
            //expected
        }

        // Test solve method with relative accuracy
        solver.setRelativeAccuracy(1e-5);
        result = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(-1.0, result, 1e-5);

        // Test solve method with absolute accuracy
        solver.setAbsoluteAccuracy(1e-5);
        result = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(-1.0, result, 1e-5);

        // Test solve method with function value accuracy
        solver.setFunctionValueAccuracy(1e-5);
        result = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(-1.0, result, 1e-5);

        // Test solve method with relative accuracy, absolute accuracy, and function value accuracy
        solver.setRelativeAccuracy(1e-3);
        solver.setAbsoluteAccuracy(1e-6);
        solver.setFunctionValueAccuracy(1e-8);
        result = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(-1.0, result, 1e-6);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIllegalArguments() {
        UnivariateRealFunction function = x -> 1 / x;
        BaseSecantSolver solver = new BaseSecantSolver(DefaultSolverType.PEGASUS);
        solver.solve(10, function, 0.0, 1.0, AllowedSolution.ANY_SIDE);
    }
}