import org.junit.*;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDoSolve() {
        double absoluteAccuracy = 1e-6;
        int maximalOrder = 5;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy, maximalOrder);
        double result = solver.doSolve();

        // Test the result
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testGetMaximalOrder() {
        double absoluteAccuracy = 1e-6;
        int maximalOrder = 5;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy, maximalOrder);
        int result = solver.getMaximalOrder();

        // Test the result
        assertEquals(5, result);
    }

    @Test
    public void testSolve() {
        double absoluteAccuracy = 1e-6;
        int maximalOrder = 5;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy, maximalOrder);
        int maxEval = 100;
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double min = 0;
        double max = 10;
        double result = solver.solve(maxEval, f, min, max, AllowedSolution.ANY_SIDE);

        // Test the result
        assertEquals(2.0, result, 1e-6);
    }
}