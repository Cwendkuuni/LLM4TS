import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BisectionSolverTest {

    @Test
    public void testSolveSimpleFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -2.0, 2.0);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolveFunctionWithNegativeRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x + 1.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -2.0, 2.0);
        assertEquals(-1.0, result, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.setMaximalIterationCount(0);
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(function, -1.0, 1.0));
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Function evaluation error");
            }
        };

        BisectionSolver solver = new BisectionSolver();
        assertThrows(FunctionEvaluationException.class, () -> solver.solve(function, -1.0, 1.0));
    }
}