
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    private static final double ABSOLUTE_ACCURACY = 1e-6;

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) throws MathInternalError {
                return 2 * x - 1; // linear equation f(x) = 2x - 1
            }
        };
    }

    @Test
    public void testSolve() {
        double solution = solver.solve(100, null, 0, 10);
        assertEquals(0.5, solution, ABSOLUTE_ACCURACY);
    }

}
