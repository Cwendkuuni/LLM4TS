import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private UnivariateRealFunction f;

    @Before
    public void setUp() {
        f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        solver = new BisectionSolver(f);
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(0, 3);
        assertEquals("Incorrect root found", 2.0, result, 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(0, 3);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new FunctionEvaluationException(x);
            }
        };
        double result = solver.solve(f, 0, 3);
    }

    @Test
    public void testSolveWithInitialGuess() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(0, 3, 1);
        assertEquals("Incorrect root found", 2.0, result, 0.001);
    }

    @Test
    public void testSolveWithNegativeValues() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(-3, 0);
        assertEquals("Incorrect root found", -2.0, result, 0.001);
    }

    @Test
    public void testSolveWithZeroInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(2, 2);
        assertEquals("Incorrect root found", 2.0, result, 0.001);
    }

}