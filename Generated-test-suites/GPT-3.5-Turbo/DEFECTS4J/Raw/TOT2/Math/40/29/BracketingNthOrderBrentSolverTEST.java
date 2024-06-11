import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testConstructorDefaultAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertNotNull(solver);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithCustomAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        assertNotNull(solver);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(3, solver.getMaximalOrder());
    }

    @Test
    public void testSolveWithFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        
        // Test typical use case
        double result = solver.solve(100, new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 4);
        assertEquals(2.0, result, 0.0);
        
        // Test edge case where function value is 0
        result = solver.solve(100, new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        }, 0, 4);
        assertEquals(0.0, result, 0.0);
        
        // Test error scenario where function has no root within given interval
        try {
            solver.solve(100, new UnivariateFunction() {
                @Override
                public double value(double x) {
                    return x * x + 4;
                }
            }, 0, 4);
            fail("NoBracketingException should have been thrown");
        } catch (NoBracketingException e) {
            // Exception expected
        }
    }

    // Add more test cases for other methods as needed

}