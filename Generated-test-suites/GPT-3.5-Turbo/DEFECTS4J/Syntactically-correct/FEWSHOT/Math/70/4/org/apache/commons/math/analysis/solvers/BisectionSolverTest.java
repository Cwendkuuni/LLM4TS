import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.QuinticFunction;


public class BisectionSolverTest {

    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @Test
    public void testSolve() {
        UnivariateRealFunction function = new QuinticFunction();

        BisectionSolver solver = new BisectionSolver();

        try {
            double result = solver.solve(function, 0.0, 2.0);
            assertTrue(Math.abs(function.value(result)) <= solver.getAbsoluteAccuracy());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithInitialGuess() {
        UnivariateRealFunction function = new QuinticFunction();

        BisectionSolver solver = new BisectionSolver();

        try {
            double result = solver.solve(function, 0.0, 2.0, 0.5);
            assertTrue(Math.abs(function.value(result)) <= solver.getAbsoluteAccuracy());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        UnivariateRealFunction function = new QuinticFunction();

        int maxIterations = 10;
        double absoluteAccuracy = 1e-6;

        BisectionSolver solver = new BisectionSolver(maxIterations, absoluteAccuracy);

        try {
            solver.solve(function, 0.0, 2.0);
        } catch (MaxIterationsExceededException e) {
            assertEquals(maxIterations, e.getMaxIterations());
            throw e;
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException1() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };

        BisectionSolver solver = new BisectionSolver();

        try {
            solver.solve(function, 0.0, 2.0);
        } catch (FunctionEvaluationException e) {
            assertEquals(0.0, e.getArgument(), 0.0);
            throw e;
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException2() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };

        BisectionSolver solver = new BisectionSolver();

        try {
            solver.solve(function, 0.0, 2.0, 0.5);
        } catch (FunctionEvaluationException e) {
            assertEquals(0.5, e.getArgument(), 0.0);
            throw e;
        }
    }
}