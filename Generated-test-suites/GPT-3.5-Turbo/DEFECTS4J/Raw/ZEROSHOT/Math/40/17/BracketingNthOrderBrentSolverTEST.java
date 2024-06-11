import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.exception.NoBracketingException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.util.Precision;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BracketingNthOrderBrentSolverTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        // Create a simple function f(x) = x^2 - 2
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 2;
            }
        };

        try {
            double root = 0.0;
            root = solver.solve(100, f, 0.0, 2.0, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);
            assertEquals(Math.sqrt(2.0), root, EPSILON);
        } catch (NoBracketingException e) {
            e.printStackTrace();
            fail();
        }

    }

}