import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.NoBracketingException;
import org.apache.commons.math.exception.NumberIsTooSmallException;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() {
        // create a test function
        UnivariateFunction testFunction = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        // create an instance of the solver
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        // test a typical use case
        double result = solver.solve(100, testFunction, 1, 3);
        assertEquals(2.0, result, 1e-6);

        // test edge cases
        result = solver.solve(100, testFunction, 0, 3);
        assertEquals(2.0, result, 1e-6);

        result = solver.solve(100, testFunction, 1, 4);
        assertEquals(2.0, result, 1e-6);

        result = solver.solve(100, testFunction, 2, 4);
        assertEquals(2.0, result, 1e-6);

        // test error scenarios
        boolean exceptionThrown = false;
        try {
            result = solver.solve(100, testFunction, 1, 1);
        } catch (NoBracketingException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            result = solver.solve(100, testFunction, 1, 3, 5);
        } catch (NoBracketingException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            result = solver.solve(100, testFunction, 1, 3, 2.5);
        } catch (NoBracketingException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}