import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertNotNull(solver);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithAccuracyAndMaximalOrder() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        assertNotNull(solver);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(7, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() throws NumberIsTooSmallException {
        new BracketingNthOrderBrentSolver(1e-8, 1);
    }

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());

        BracketingNthOrderBrentSolver solverWithMaximalOrder = new BracketingNthOrderBrentSolver(0.01, 10);
        assertEquals(10, solverWithMaximalOrder.getMaximalOrder());
    }

    @Test
    public void testSolveWithBracketingInterval() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(10, f, -2.5, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(0.5, root, 1e-5);
    }

    @Test
    public void testSolveWithStartValue() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(10, f, -2.5, 1.5, 0.5, AllowedSolution.ANY_SIDE);
        assertEquals(0.5, root, 1e-5);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) - Math.cos(x);
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(10, f, -5, 5, AllowedSolution.ANY_SIDE);
    }

}