import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        double min = 0;
        double max = 5;
        double result = solver.solve(f, min, max);

        assertEquals(2, result, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        double min = 0;
        double max = 5;
        solver.setMaximalIterationCount(1);
        solver.solve(f, min, max);
    }

    @Test
    public void testSolveWithInitial() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        double min = 0;
        double max = 5;
        double initial = 3;
        double result = solver.solve(f, min, max, initial);

        assertEquals(2, result, 0.0001);
    }

    @Test
    public void testSolveWithDeprecatedParameters() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver(f);

        double min = 0;
        double max = 5;
        double initial = 3;
        double result = solver.solve(min, max, initial);

        assertEquals(2, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyIntervalWithInvalidInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        double min = 5;
        double max = 0;
        solver.solve(f, min, max);
    }

    @Test
    public void testVerifyIntervalWithValidInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        double min = 0;
        double max = 5;
        double result = solver.solve(f, min, max);

        assertEquals(2, result, 0.0001);
    }
}