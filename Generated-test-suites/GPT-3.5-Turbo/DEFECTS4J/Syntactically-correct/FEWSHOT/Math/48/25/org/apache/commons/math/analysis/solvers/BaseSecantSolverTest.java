import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.analysis.UnivariateRealFunction;
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
        double result = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }
}