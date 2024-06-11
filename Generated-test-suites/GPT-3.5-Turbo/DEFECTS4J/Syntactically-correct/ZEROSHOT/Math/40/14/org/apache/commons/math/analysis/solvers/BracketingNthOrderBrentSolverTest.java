import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.exception.NoBracketingException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        // Test case 1
        UnivariateFunction f1 = new UnivariateFunction() {
            public double value(double x) {
                return FastMath.sin(x);
            }
        };
        double root1 = solver.solve(100, f1, 0, 1);
        Assert.assertEquals(0.0, f1.value(root1), 1e-6);

        // Test case 2
        UnivariateFunction f2 = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root2 = solver.solve(100, f2, 1, 3);
        Assert.assertEquals(0.0, f2.value(root2), 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveNoBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return FastMath.sin(x);
            }
        };
        double root = solver.solve(100, f, 0, 0.5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorMaximalOrderTooSmall() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        Assert.assertEquals(5, solver.getMaximalOrder());
    }

}