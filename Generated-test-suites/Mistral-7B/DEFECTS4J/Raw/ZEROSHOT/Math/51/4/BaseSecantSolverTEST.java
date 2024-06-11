import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver.IlinoisSolver(1e-6);
        function = f -> Math.pow(Math.E, -x) - x;
    }

    @Test
    public void testSolve_minMax() throws MaxEvalException, NumberIsTooLargeException, TooManyIterationsException {
        double result = solver.solve(10, function, -5.0, 5.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, Precision.round(result, 6), 0.0);
    }

    @Test
    public void testSolve_minStartValue() throws MaxEvalException, NumberIsTooLargeException, TooManyIterationsException {
        double result = solver.solve(10, function, -5.0, 5.0, -3.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, Precision.round(result, 6), 0.0);
    }

    @Test
    public void testSolve_maxStartValue() throws MaxEvalException, NumberIsTooLargeException, TooManyIterationsException {
        double result = solver.solve(10, function, -5.0, 5.0, 3.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, Precision.round(result, 6), 0.0);
    }

    @Test
    public void testSolve_minStartValueInverseInterval() throws MaxEvalException, NumberIsTooLargeException, TooManyIterationsException {
        solver = new BaseSecantSolver.IllinoisSolver(1e-6);
        double result = solver.solve(10, function, 5.0, -5.0, -3.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, Precision.round(result, 6), 0.0);
    }

    @Test
    public void testSolve_maxStartValueInverseInterval() throws MaxEvalException, NumberIsTooLargeException, TooManyIterationsException {
        solver = new BaseSecantSolver.IllinoisSolver(1e-6);
        double result = solver.solve(10, function, 5.0, -5.0, 3.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, Precision.round(result, 6), 0.0);
    }
}