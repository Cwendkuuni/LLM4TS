import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.FunctionUtils;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Before;
import org.junit.Test;

public class BisectionSolverTest {
    private BisectionSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testConstructor() {
        assertNotNull(solver);
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        double root = solver.solve(function, 0, 5);
        assertEquals(2.0, root, 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction exceptionFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };

        solver.solve(exceptionFunction, 0, 5);
    }
}