import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    private static final double EPSILON = 1E-6;

    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 2;
            }
        };

        BisectionSolver solver = new BisectionSolver(f);

        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithoutFunction() {
        BisectionSolver solver = new BisectionSolver();

        assertNotNull(solver);
    }

    @Test
    public void testSolveWithFunctionAndInitial() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 2;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, 0, 2, 1.5);

        assertEquals(1.414213562, result, EPSILON);
    }

    @Test
    public void testSolveWithFunction() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 2;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, 0, 2);

        assertEquals(1.414213562, result, EPSILON);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithFunctionAndInvalidInterval() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 2;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0, 0);
    }

    @Test
    public void testSolveWithFunctionAndSmallInterval() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 2;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, 1, 2);

        assertEquals(1.414213562, result, EPSILON);
    }

    @Test
    public void testSolveWithFunctionAndLargeInterval() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 2;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, -10, 10);

        assertEquals(1.414213562, result, EPSILON);
    }
}


