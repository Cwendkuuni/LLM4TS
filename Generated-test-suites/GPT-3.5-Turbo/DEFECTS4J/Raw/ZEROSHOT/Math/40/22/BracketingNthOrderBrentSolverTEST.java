import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() {

        // Create the solver object
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        // Create a test function
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        // Set the desired allowed solution
        AllowedSolution allowed = AllowedSolution.ANY_SIDE;

        // Define the test cases
        double[][] testCases = {
            {1.0, 2.0, 2.0},
            {-2.0, -1.0, -2.0},
            {-2.0, 2.0, -2.0},
            {-2.0, 2.0, 2.0},
            {-2.0, -1.0, -1.5},
            {-2.0, 2.0, 0.0},
            {-2.0, 2.0, 1.5},
            {-2.0, -1.0, -1.25},
            {-2.0, 2.0, 0.75},
            {-2.0, 2.0, 1.75},
        };

        // Test the solve method for each test case
        for (double[] testCase : testCases) {
            double min = testCase[0];
            double max = testCase[1];
            double expected = testCase[2];

            double result = solver.solve(100, function, min, max, allowed);
            assertEquals(expected, result, 1e-6);
        }
    }
}