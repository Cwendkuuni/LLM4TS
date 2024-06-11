import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() {

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double solution = solver.solve(100, new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 0.0001);

    }

    @Test
    public void testSolveWithStartValue() {

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double solution = solver.solve(100, new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 0.0001);

    }

}