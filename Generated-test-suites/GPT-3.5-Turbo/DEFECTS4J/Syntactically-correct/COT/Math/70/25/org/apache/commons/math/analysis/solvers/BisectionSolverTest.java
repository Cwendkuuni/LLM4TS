import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;

public class BisectionSolverTest {

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                // Example function: x^2 - 4
                return x * x - 4;
            }
        };

        // Create the solver
        BisectionSolver solver = new BisectionSolver(f);

        // Test solving for a root within the interval [1, 3]
        double root = solver.solve(1, 3);
        assertEquals(2, root, 1E-6); // Assert that the root is found with an accuracy of 1E-6
    }
}