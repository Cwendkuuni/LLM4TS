
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {
    @Test
    public void testSolve() {
        // Test with a simple linear function
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return 3*x - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, f, -10, 10);
        assertEquals("The result should be 4/3", 4/3, result, 0.001);
    }

    @Test
    public void testSolveWithMaxEval() {
        // Test with a simple quadratic function
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(10, f, 1, 10);
        assertEquals("The result should be 2", 2, result, 0.001);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithNoBracketingException() {
        // Test with a decreasing function
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return 4 - 3*x;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, f, 1, 10);
    }
}

