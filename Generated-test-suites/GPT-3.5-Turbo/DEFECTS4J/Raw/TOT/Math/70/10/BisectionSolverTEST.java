import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BisectionSolverTest {

    private UnivariateRealFunction testFunction;
    private BisectionSolver solver;

    @Before
    public void setUp() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        solver = new BisectionSolver(testFunction);
    }

    @Test
    public void testSolve() throws Exception {
        double solution = solver.solve(-10, 10);
        assertEquals(2, solution, 0.0001);
    }

    @Test
    public void testSolveWithInitial() throws Exception {
        double solution = solver.solve(-10, 10, 1);
        assertEquals(2, solution, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        solver.solve(-10, 10, 1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testInvalidFunctionEvaluation() throws Exception {
        solver.solve(-10, 10, Double.NaN);
    }

    @Test
    public void testSolveWithZeroInterval() throws Exception {
        double solution = solver.solve(2, 2);
        assertEquals(2, solution, 0.0001);
    }

    @Test
    public void testSolveWithNegativeInterval() throws Exception {
        double solution = solver.solve(-10, -5);
        assertEquals(-3, solution, 0.0001);
    }

    @Test
    public void testSolveWithZeroFunction() throws Exception {
        UnivariateRealFunction zeroFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };

        BisectionSolver zeroSolver = new BisectionSolver(zeroFunction);
        double solution = zeroSolver.solve(-10, 10);
        assertEquals(0, solution, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInterval() throws Exception {
        solver.solve(10, -10);
    }

}