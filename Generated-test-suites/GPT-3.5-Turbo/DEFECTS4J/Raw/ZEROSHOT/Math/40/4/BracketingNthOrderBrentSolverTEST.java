import org.junit.*;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() {

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, new UnivariateFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        }, 0, 10, 1, AllowedSolution.ANY_SIDE);

        assertEquals(2.0, result, 0.0001);

    }

    @Test
    public void testSolve2() {

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        }, -4, 4, 0, AllowedSolution.ANY_SIDE);

        assertEquals(Math.PI, result, 0.0001);

    }
}