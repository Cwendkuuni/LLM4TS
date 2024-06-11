import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        // Test typical use case
        double solution = solver.solve(f, 1, 3);
        Assert.assertEquals(2.0, solution, 0.0000001);

        // Test edge cases
        solution = solver.solve(f, 0, 4);
        Assert.assertEquals(2.0, solution, 0.0000001);

        solution = solver.solve(f, -4, 0);
        Assert.assertEquals(-2.0, solution, 0.0000001);

        solution = solver.solve(f, -1, 5);
        Assert.assertEquals(2.0, solution, 0.0000001);

        // Test error scenarios

        // Test for function with no roots
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x + 4;
            }
        };

        boolean exceptionThrown = false;
        try {
            solution = solver.solve(f2, -3, 3);
        } catch (MaxIterationsExceededException e) {
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
    }

}