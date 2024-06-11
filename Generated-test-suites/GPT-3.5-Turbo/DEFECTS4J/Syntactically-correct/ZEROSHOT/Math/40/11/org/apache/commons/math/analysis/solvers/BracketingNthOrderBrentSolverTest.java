import static org.junit.Assert.*;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDoSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
		
		// Test case 1: simple root
        UnivariateFunction f1 = new UnivariateFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        assertEquals(2.0, solver.solve(100, f1, 1, 3), 1e-6);
		
		// Test case 2: root within range but not within default absolute accuracy
        UnivariateFunction f2 = new UnivariateFunction() {
            public double value(double x) {
                return x*x + 2*x + 1;
            }
        };
        assertEquals(-1.0, solver.solve(100, f2, -10, 10), 1e-6);
		
		// Test case 3: root outside of range
        UnivariateFunction f3 = new UnivariateFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        assertNull(solver.solve(100, f3, 1, 3));
    }
}