import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.solvers.BisectionSolver;

public class BisectionSolverTest {

    @Test
    public void testSolveWithInitial() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(f, 0, 5, 1);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithoutInitial() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(f, 0, 5);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSolveHandlesMaxIterationsExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(f, 0, 5);
            fail("MaxIterationsExceededException not thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        } catch (FunctionEvaluationException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSolveHandlesFunctionEvaluationException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(f, 0, 5);
            fail("FunctionEvaluationException not thrown");
        } catch (FunctionEvaluationException e) {
            // Expected exception
        } catch (MaxIterationsExceededException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    // Add more test cases for edge cases and error scenarios as needed

}