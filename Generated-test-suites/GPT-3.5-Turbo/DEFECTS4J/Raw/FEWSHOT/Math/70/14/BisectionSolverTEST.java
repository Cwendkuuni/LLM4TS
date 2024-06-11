import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private UnivariateRealFunction testFunction;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        testFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        double solution = solver.solve(testFunction, 1, 3);
        assertEquals(2.0, solution, 0.0001);
    }

    @Test
    public void testSolveWithLargerInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        double solution = solver.solve(testFunction, -10, 10);
        assertEquals(2.0, solution, 0.0001);
    }

    @Test
    public void testSolveWithInitialGuess() throws MaxIterationsExceededException, FunctionEvaluationException {
        double solution = solver.solve(testFunction, 1, 3, 2.5);
        assertEquals(2.0, solution, 0.0001);
    }

    @Test
    public void testSolveWithInvalidInterval() {
        try {
            solver.solve(testFunction, 3, 1);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        } catch (FunctionEvaluationException e) {
            fail("Unexpected FunctionEvaluationException");
        }
    }
}