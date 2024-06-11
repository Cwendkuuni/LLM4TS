import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertEquals(100, solver.getMaximalIterationCount());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testSolveWithInitial() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        try {
            double solution = solver.solve(f, 0, 10, 2);
            assertEquals(5.0, solution, 0.0);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSolveWithoutInitial() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            double solution = solver.solve(f, 0, 2);
            assertEquals(2.0, solution, 0.0);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        solver.solve(f, 0, 2);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new FunctionEvaluationException(x);
            }
        };
        solver.solve(f, 0, 2);
    }
}