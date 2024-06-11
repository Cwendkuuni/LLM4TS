import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-10, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-10, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, function, -2, 2, BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-10);
    }

    @Test(expected = MathInternalError.class)
    public void testInvalidMethod() {
        new BaseSecantSolver(1e-10, BaseSecantSolver.Method.PEGASUS);
    }

    // Additional edge cases and error scenarios could be added here.
}