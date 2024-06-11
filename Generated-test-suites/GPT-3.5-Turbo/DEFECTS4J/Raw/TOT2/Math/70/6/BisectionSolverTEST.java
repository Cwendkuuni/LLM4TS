import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

    private UnivariateRealFunction linearFunction = new UnivariateRealFunction() {
        public double value(double x) {
            return x - 1;
        }
    };

    private UnivariateRealFunction constantFunction = new UnivariateRealFunction() {
        public double value(double x) {
            return 5;
        }
    };

    private BisectionSolver bisectionSolver = new BisectionSolver();

    @Test
    public void testSolveLinearFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        double solution = bisectionSolver.solve(linearFunction, 0, 2);
        assertEquals(1.0, solution, 1e-6);
    }

    @Test
    public void testSolveConstantFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        double solution = bisectionSolver.solve(constantFunction, -10, 10);
        assertEquals(Double.NaN, solution, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        bisectionSolver.solve(linearFunction, -100, 100);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        bisectionSolver.solve(constantFunction, -10, 10);
    }
}