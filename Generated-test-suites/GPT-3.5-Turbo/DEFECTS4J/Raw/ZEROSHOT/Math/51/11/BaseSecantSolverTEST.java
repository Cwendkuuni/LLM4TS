import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return Math.pow(x, 2) - 4;
            }

            protected void verifyBracketing(double lower, double upper) {
                if (computeObjectiveValue(lower) * computeObjectiveValue(upper) >= 0) {
                    throw new MathInternalError("Function values at endpoints do not have different signs.");
                }
            }
        };
    }

    @Test
    public void testSolve() {
        double result = solver.solve(100, null, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2, result, 1e-6);
    }
}