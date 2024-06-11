import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertNotNull(solver);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithMaximalOrder() {
        try {
            BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 3);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolve() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, f, 0, 3);
        assertEquals(2.0, result, 1e-6);
    }

    // Add more test cases for other methods if needed

}