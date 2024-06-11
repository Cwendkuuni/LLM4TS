import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver(f);

        // Test for typical use case
        try {
            double result = solver.solve(0, 5);
            assertEquals(2.0, result, EPSILON);
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }

        // Test for edge case with an interval of length 0
        try {
            double result = solver.solve(0, 0);
            assertEquals(0.0, result, EPSILON);
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }

        // Test for edge case with an interval of length 0 and initial value outside the interval
        try {
            double result = solver.solve(0, 0, -1);
            assertEquals(0.0, result, EPSILON);
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }

        // Test for error scenario where the function throws an exception
        UnivariateRealFunction errorFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };

        try {
            double result = solver.solve(errorFunction, 0, 5);
            fail("Expected FunctionEvaluationException was not thrown");
        } catch (FunctionEvaluationException e) {
            // The expected exception was thrown
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }
    }
}