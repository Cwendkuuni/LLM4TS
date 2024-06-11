import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BisectionSolverTest {

    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @Test
    public void testSolve() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction func = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double result;
        try {
            result = solver.solve(func, 0, 10, 0);
            assertEquals(5.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException ex) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSolveEdgeCases() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction func = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result;
        try {
            result = solver.solve(func, 0, 10, 0);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException ex) {
            fail("Exception thrown");
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testInvalidFunctionEvaluation() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction func = new UnivariateRealFunction() {
            public double value(double x) {
                throw new FunctionEvaluationException(x);
            }
        };
        solver.solve(func, 0, 10, 0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction func = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        solver.solve(func, 0, 10, 0);
    }
}