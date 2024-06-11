import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.NoBracketingException;
import org.junit.Assert;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    /** Default absolute accuracy. */
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-6;

    /** Default maximal order. */
    private static final int DEFAULT_MAXIMAL_ORDER = 5;

    @Test
    public void testDoSolve() throws NumberIsTooSmallException, NoBracketingException {
        // Create instance
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(DEFAULT_ABSOLUTE_ACCURACY, DEFAULT_MAXIMAL_ORDER);

        // Test case 1
        UnivariateFunction f1 = new UnivariateFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double result1 = solver.solve(100, f1, 0, 10);
        Assert.assertEquals(5.0, result1, DEFAULT_ABSOLUTE_ACCURACY);

        // Test case 2
        UnivariateFunction f2 = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double result2 = solver.solve(100, f2, 0, 2 * Math.PI);
        Assert.assertEquals(Math.PI / 6, result2, DEFAULT_ABSOLUTE_ACCURACY);

        // Test case 3
        UnivariateFunction f3 = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 2;
            }
        };
        double result3 = solver.solve(100, f3, 0, 2);
        Assert.assertEquals(Math.sqrt(2), result3, DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test(expected=NoBracketingException.class)
    public void testDoSolveException() throws NumberIsTooSmallException, NoBracketingException {
        // Create instance
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(DEFAULT_ABSOLUTE_ACCURACY, DEFAULT_MAXIMAL_ORDER);

        // Test case
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 2;
            }
        };
        solver.solve(100, f, 0, 1);
    }
}