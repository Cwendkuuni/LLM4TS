import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BisectionSolverTest {

    private static final double TEST_TOLERANCE = 1e-6;

    @Test
    public void testSolveWithFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new TestFunction();
        BisectionSolver solver = new BisectionSolver();
        
        double result = solver.solve(0.0, 2.0, function);
        
        assertEquals(1.0, result, TEST_TOLERANCE);
    }

    @Test
    public void testSolveWithMinAndMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new TestFunction();
        BisectionSolver solver = new BisectionSolver();
        
        double result = solver.solve(function, 0.0, 2.0);
        
        assertEquals(1.0, result, TEST_TOLERANCE);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new TestFunction();
        BisectionSolver solver = new BisectionSolver();
        
        solver.solve(function, -1.0, 1.0);
    }

    @Test
    public void testEdgeCaseWithZeroRoot() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new TestFunction2();
        BisectionSolver solver = new BisectionSolver();
        
        double result = solver.solve(function, -0.5, 0.5);
        
        assertEquals(0.0, result, TEST_TOLERANCE);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new TestFunction3();
        BisectionSolver solver = new BisectionSolver();
        
        solver.solve(function, -1.0, 1.0);
    }

    private static class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x - 1.0;
        }
    }

    private static class TestFunction2 implements UnivariateRealFunction {
        public double value(double x) {
            return x;
        }
    }

    private static class TestFunction3 implements UnivariateRealFunction {
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException(x);
        }
    }
}