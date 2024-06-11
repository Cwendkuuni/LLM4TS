import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BisectionSolverTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testSolveWithFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        try {
            double result = solver.solve(1, 3);
            assertEquals(2.0, result, EPSILON);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testSolveNoFunction() {
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(1, 3);
            assertTrue(Double.isNaN(result));
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testSolveWithInitial() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(function, 1, 3, 2);
            assertEquals(2.0, result, EPSILON);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testSolveSmallInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x + 2;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        try {
            double result = solver.solve(-1, 1);
            assertEquals(-2.0, result, EPSILON);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testSolveMaxIterationsExceeded() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        try {
            solver.solve(1, 3);
            fail("MaxIterationsExceededException not thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        } catch (FunctionEvaluationException e) {
            fail("Unexpected FunctionEvaluationException");
        }
    }
}