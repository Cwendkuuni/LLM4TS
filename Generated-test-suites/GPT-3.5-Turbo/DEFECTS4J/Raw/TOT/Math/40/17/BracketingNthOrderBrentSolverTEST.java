import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.util.FastMath;
import org.junit.Test;

import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testSolve_QuadraticFunction() throws NumberIsTooSmallException {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x + 2 * x - 3;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(100, function, -10, 10,
                BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);

        assertEquals(1.0, root, EPSILON);
    }

    @Test
    public void testSolve_LinearFunction() throws NumberIsTooSmallException {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return 2 * x + 3;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(100, function, -10, 10,
                BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);

        assertEquals(-1.5, root, EPSILON);
    }

    @Test
    public void testSolve_ConstantFunction() throws NumberIsTooSmallException {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return 5;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(100, function, -10, 10,
                BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);

        assertEquals(Double.NaN, root, EPSILON);
    }

    @Test
    public void testSolve_SineFunction() throws NumberIsTooSmallException {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return FastMath.sin(x);
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(100, function, -10, 10,
                BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.0, root, EPSILON);
    }

}