import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BisectionSolverTest {

    private static final double EPSILON = 1E-10;

    // Test case for solving with a given UnivariateRealFunction
    @Test
    public void testSolveFunction() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, 0, 3);

        assertEquals(2.0, result, EPSILON);
    }

    // Test case for solving with initial guess
    @Test
    public void testSolveWithInitialGuess() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, 1, 4, 2);

        assertEquals(3.1415926535, result, EPSILON);
    }

    // Test case for solving without initial guess
    @Test
    public void testSolveWithoutInitialGuess() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x);
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -1, 1);

        assertEquals(0.0, result, EPSILON);
    }

    // Test case for handling max iterations exceeded exception
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(x + 1);
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -2, 2);
    }
}