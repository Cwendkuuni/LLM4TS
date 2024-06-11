import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.exception.NoBracketingException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.util.Precision;

import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };

        double root = solver.solve(100, f, 0, 10);
        assertTrue(Precision.equals(root, 2.0, 1));

        root = solver.solve(100, f, 0, 10, 1);
        assertTrue(Precision.equals(root, 2.0, 1));

        root = solver.solve(100, f, 0, 10, 5, BracketingNthOrderBrentSolver.AllowedSolution.ABOVE_SIDE);
        assertTrue(Precision.equals(root, 2.0, 1));

        root = solver.solve(100, f, 0, 10, 5, BracketingNthOrderBrentSolver.AllowedSolution.BELOW_SIDE);
        assertTrue(Precision.equals(root, 2.0, 1));

        root = solver.solve(100, f, 0, 10, 5, BracketingNthOrderBrentSolver.AllowedSolution.LEFT_SIDE);
        assertTrue(Precision.equals(root, 2.0, 1));

        root = solver.solve(100, f, 0, 10, 5, BracketingNthOrderBrentSolver.AllowedSolution.RIGHT_SIDE);
        assertTrue(Precision.equals(root, 2.0, 1));
    }

}