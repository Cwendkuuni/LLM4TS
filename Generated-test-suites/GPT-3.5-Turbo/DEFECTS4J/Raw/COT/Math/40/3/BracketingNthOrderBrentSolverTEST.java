
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDoSolve() {
        // Test case 1: Bracketing interval that contains the root
        BracketingNthOrderBrentSolver solver1 = new BracketingNthOrderBrentSolver();
        double solution1 = solver1.solve(100, new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        }, -2.0, 2.0);
        assertEquals(0.5235987755982989, solution1, 0.000001);

        // Test case 2: Bracketing interval that does not contain the root
        BracketingNthOrderBrentSolver solver2 = new BracketingNthOrderBrentSolver();
        try {
            solver2.solve(100, new UnivariateFunction() {
                public double value(double x) {
                    return Math.sin(x) - 0.5;
                }
            }, 0.5, 2.0);
        } catch (NoBracketingException e) {
            assertEquals("No bracketing interval found for function values between 0.7163365934200707 and 0.7127026696430635", e.getMessage());
        }

        // Test case 3: Function value equals zero at start value
        BracketingNthOrderBrentSolver solver3 = new BracketingNthOrderBrentSolver();
        double solution3 = solver3.solve(100, new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        }, -2.0, 2.0, 1.5707963267948966);
        assertEquals(1.5707963267948966, solution3, 0.000001);
    }
}
