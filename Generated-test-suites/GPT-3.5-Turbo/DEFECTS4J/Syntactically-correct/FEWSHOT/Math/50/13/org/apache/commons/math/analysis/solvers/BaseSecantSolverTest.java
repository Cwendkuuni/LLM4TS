import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class BaseSecantSolverTest {
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(2.0, solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE), 1e-6);
        assertEquals(Double.NaN, solver.solve(100, f, -5, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE), 1e-6);
        assertNull(solver.solve(100, f, -5, 1, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE));
    }
}