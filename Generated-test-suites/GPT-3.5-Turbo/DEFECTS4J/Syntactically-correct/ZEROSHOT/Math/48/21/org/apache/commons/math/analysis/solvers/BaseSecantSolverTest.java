import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolve() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(2, result, 1e-6);
    }

    @Test
    public void testSolveWithInvalidStartValue() {
        try {
            double result = solver.solve(100, new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x - 4;
                }
            }, 0, 5, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException ex) {
            // Expected
        }
    }

}