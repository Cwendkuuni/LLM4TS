import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BisectionSolverTest {

    @Test
    public void testSolveWithFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(f);

        try {
            double result = solver.solve(0, 5);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testSolveWithInitial() {
        BisectionSolver solver = new BisectionSolver();

        try {
            double result = solver.solve(0, 5, 2);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testSolveWithFunctionAndInitial() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        try {
            double result = solver.solve(f, 0, 5, 2);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testSolveEdgeCases() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        try {
            double result = solver.solve(f, 2.0, 3.0, 2.5);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();

        solver.solve(f, -Math.PI, Math.PI);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException("Error evaluating function");
            }
        };
        BisectionSolver solver = new BisectionSolver();

        solver.solve(f, -10, 10);
    }

}