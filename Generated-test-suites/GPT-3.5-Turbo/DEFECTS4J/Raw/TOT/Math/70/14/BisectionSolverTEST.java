import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.sin.SineFunction;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import java.util.Arrays;

public class BisectionSolverTest {

    @Test
    public void testSolveWithSineFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Create a sine function to use in the solver
        UnivariateRealFunction f = new SineFunction();

        // Create the solver and set the desired accuracy and iterations
        BisectionSolver solver = new BisectionSolver();
        solver.setAbsoluteAccuracy(1e-6);
        solver.setMaximalIterationCount(100);

        // Test typical use cases
        double result = solver.solve(f, 0, 4);
        Assert.assertEquals(Math.PI, result, 1e-6);

        result = solver.solve(f, -5, -1);
        Assert.assertEquals(-Math.PI, result, 1e-6);

        // Test edge cases
        result = solver.solve(f, 0, 2 * Math.PI);
        Assert.assertEquals(Math.PI, result, 1e-6);

        result = solver.solve(f, -2 * Math.PI, 0);
        Assert.assertEquals(-Math.PI, result, 1e-6);

        // Test error scenarios
        try {
            result = solver.solve(f, 1, 2);
            Assert.fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected Exception
        }

        try {
            result = solver.solve(f, -2, -1);
            Assert.fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected Exception
        }
    }

    @Test
    public void testSolveWithPolynomialFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Create a polynomial function to use in the solver
        UnivariateRealFunction f = new PolynomialFunction(new double[] {-2, 0, 0, 1});

        // Create the solver and set the desired accuracy and iterations
        BisectionSolver solver = new BisectionSolver();
        solver.setAbsoluteAccuracy(1e-6);
        solver.setMaximalIterationCount(100);

        // Test typical use cases
        double result = solver.solve(f, -10, -3);
        Assert.assertEquals(-2.0, result, 1e-6);

        result = solver.solve(f, -1, 10);
        Assert.assertEquals(2.0, result, 1e-6);

        // Test edge cases
        result = solver.solve(f, -Math.PI, Math.PI);
        Assert.assertEquals(-2.0, result, 1e-6);

        result = solver.solve(f, -10, 10);
        Assert.assertEquals(2.0, result, 1e-6);

        // Test error scenarios
        try {
            result = solver.solve(f, -1, 1);
            Assert.fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected Exception
        }

        try {
            result = solver.solve(f, -2, -1);
            Assert.fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected Exception
        }
    }
}