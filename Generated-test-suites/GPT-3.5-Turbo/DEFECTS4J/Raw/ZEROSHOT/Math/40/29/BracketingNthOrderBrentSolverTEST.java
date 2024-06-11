import org.junit.Test;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.exception.NoBracketingException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.util.Precision;

public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;

    @Test
    public void testZeroRoot() {
        solver = new BracketingNthOrderBrentSolver();
        solve(1, new UnivariateFunction() {
            public double value(double x) {
                return x;
            }
        }, -1, 1, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testBoundaryRoot() {
        solver = new BracketingNthOrderBrentSolver();
        solve(1, new UnivariateFunction() {
            public double value(double x) {
                return x;
            }
        }, 0, 1, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testArbitraryRoot() {
        solver = new BracketingNthOrderBrentSolver();
        solve(1, new UnivariateFunction() {
            public double value(double x) {
                return x-2;
            }
        }, 0, 3, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathInternalError.class)
    public void testNoBracket() {
        solver = new BracketingNthOrderBrentSolver();
        solve(1, new UnivariateFunction() {
            public double value(double x) {
                return x-2;
            }
        }, 0, 1, AllowedSolution.ANY_SIDE);
    }

    private void solve(int maxEval, UnivariateFunction f, double min, double max, AllowedSolution allowed) {
        solver.solve(maxEval, f, min, max, allowed);
    }

}