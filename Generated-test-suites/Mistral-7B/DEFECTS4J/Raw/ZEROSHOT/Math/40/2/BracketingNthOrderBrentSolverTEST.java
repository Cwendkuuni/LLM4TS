import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 3);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        assertEquals(0.0, solver.solve(100, f, -2.5, 2.5, AllowedSolution.ANY_SIDE), 1e-5);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 3);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        solver.solve(100, f, 1.0, 1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, -3);
    }

    @Test
    public void testSolveLeftSide() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 3, AllowedSolution.LEFT_SIDE);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        assertEquals(-sqrt(2.0), solver.solve(100, f, -2.5, 2.5), 1e-5);
    }

    @Test
    public void testSolveRightSide() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 3, AllowedSolution.RIGHT_SIDE);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        assertEquals(sqrt(2.0), solver.solve(100, f, -2.5, 2.5), 1e-5);
    }

    @Test
    public void testSolveBelowSide() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 3, AllowedSolution.BELOW_SIDE);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        assertEquals(-sqrt(2.0), solver.solve(100, f, -2.5, 2.5), 1e-5);
    }

    @Test
    public void testSolveAboveSide() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 3, AllowedSolution.ABOVE_SIDE);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        assertEquals(sqrt(2.0), solver.solve(100, f, -2.5, 2.5), 1e-5);
    }

    private double sqrt(double d) {
        return Math.sqrt(d);
    }
}