import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BisectionSolverTest {

    private BisectionSolver solver;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }

    @Test
    public void testSolve() throws Exception {
        // Test case 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double result1 = solver.solve(f1, 0, 10);
        assertEquals(5.0, result1, 0.0001);

        // Test case 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result2 = solver.solve(f2, -10, 10);
        assertEquals(2.0, result2, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        solver.solve(f, 0, 10, 100);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        solver.solve(f, 0, 10);
    }
}