import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.solvers.Method;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;

public class BaseSecantSolverTest {
    @Test
    public void testSolve() {
        // Define test function
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // Root at x = 2
            }
        };

        // Create solver with default accuracy and method
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);

        // Test solve method
        double actual = solver.solve(100, testFunction, 0, 5, BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE);
        double expected = 2;
        assertEquals(expected, actual, 1e-6);
    }
}