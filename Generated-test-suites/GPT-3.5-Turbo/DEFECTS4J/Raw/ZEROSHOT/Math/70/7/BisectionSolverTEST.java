import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BisectionSolverTest {

    private BisectionSolver solver;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        try {
            double result = solver.solve(f, 0, 3);

            assertEquals(2.0, result, 1E-6);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };

        double result = solver.solve(f, 0, Math.PI, 1);

        fail("Expected MaxIterationsExceededException to be thrown");
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException();
            }
        };

        double result = solver.solve(f, 0, 1);

        fail("Expected FunctionEvaluationException to be thrown");
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        try {
            double result = solver.solve(f, 0, 3, 1E-6);

            assertEquals(2.0, result, 1E-6);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithAbsoluteAccuracyAndMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };

        double result = solver.solve(f, 0, Math.PI, 1E-6, 100);

        fail("Expected MaxIterationsExceededException to be thrown");
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithAbsoluteAccuracyAndFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException();
            }
        };

        double result = solver.solve(f, 0, 1, 1E-6);

        fail("Expected FunctionEvaluationException to be thrown");
    }
}