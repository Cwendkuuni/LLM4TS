import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.NoBracketingException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.util.FastMath;

public class BracketingNthOrderBrentSolverTest {

    // Test computeObjectiveValue() method

    @Test
    public void testComputeObjectiveValue() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double value = solver.computeObjectiveValue(f, 2.0);
        
        assertEquals(0.0, value, 1e-6);
    }

    // Test constructor

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_withMaximalOrderLessThan2() {
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    // Test solve() method

    @Test
    public void testSolve_withPerfectRoot() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return (x - 2) * (x + 2);
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        double root = solver.solve(100, f, 1.9, 2.1);
        assertEquals(2.0, root, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolve_withNoBracketing() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return (x - 2) * (x + 2);
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        solver.solve(100, f, -1, 1);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testSolve_withMaxEvaluationsReached() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return (x - 2) * (x + 2);
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        solver.solve(2, f, 1.9, 2.1);
    }

    // Test getMaximalOrder() method

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        int maxOrder = solver.getMaximalOrder();
        assertEquals(5, maxOrder);
    }

}