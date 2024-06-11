import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.solvers.BisectionSolver;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private static final double EPSILON = 1e-10;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double result = solver.solve(f, -2, 2);
        assertEquals(2.0, result, EPSILON);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        solver.solve(f, -2, 2, 1E-15);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };

        solver.solve(f, -2, 2);
    }

    @Test
    public void testSolveEndpointResult() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double result = solver.solve(f, -2, -1);
        assertEquals(-2.0, result, EPSILON);

        result = solver.solve(f, 1, 2);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    public void testSolveEqualEndpoints() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double result = solver.solve(f, -2, -2);
        assertEquals(-2.0, result, EPSILON);

        result = solver.solve(f, 0, 0);
        assertEquals(0.0, result, EPSILON);

        result = solver.solve(f, 2, 2);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    public void testSolveNegativeInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double result = solver.solve(f, -2, 0);
        assertEquals(-2.0, result, EPSILON);
    }

    @Test
    public void testSolvePositiveInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double result = solver.solve(f, 0, 2);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    public void testSolveTolerance() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double result = solver.solve(f, -2, 2, 1E-3);
        assertEquals(2.0, result, EPSILON);
    }

}