import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testConstructorWithDefaultValues() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithCustomValues() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(3, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithLowerThanMinimalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1, 1);
    }

    @Test
    public void testSolveSimpleRoot() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, function, -3.0, 3.0);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolveComplexRoot() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, function, -5.0, 5.0);
        assertEquals(1.2732329311142333, result, 1e-5);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(100, function, 2.0, 2.0);
    }

    @Test
    public void testSolveWithCustomAllowedSolution() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, function, -5.0, 5.0, AllowedSolution.BELOW_SIDE);
        assertEquals(1.2732329311142333, result, 1e-5);
    }

}