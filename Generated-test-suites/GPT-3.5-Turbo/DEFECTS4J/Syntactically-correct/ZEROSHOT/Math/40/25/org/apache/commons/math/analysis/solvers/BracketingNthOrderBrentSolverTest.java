import org.junit.Assert;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() {
        // Create an instance of the BracketingNthOrderBrentSolver class
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        // Define the function
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        // Solve the function
        double result = solver.solve(100, function, 1, 3, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);

        // Verify that the result is within the expected range
        Assert.assertTrue(result >= 1 && result <= 3);
    }

    @Test
    public void testSolveWithStartValue() {
        // Create an instance of the BracketingNthOrderBrentSolver class
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        // Define the function
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        // Solve the function
        double result = solver.solve(100, function, 1, 3, 2, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);

        // Verify that the result is within the expected range
        Assert.assertTrue(result >= 1 && result <= 3);
    }

}