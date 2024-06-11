import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private UnivariateRealFunction f;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        f = mock(UnivariateRealFunction.class);
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Test case 1
        when(f.value(1.0)).thenReturn(-2.0);
        when(f.value(3.0)).thenReturn(2.0);
        double result1 = solver.solve(f, 1.0, 3.0);
        assertEquals(2.0, result1, 1E-6);

        // Test case 2
        when(f.value(-2.0)).thenReturn(-2.0);
        when(f.value(-1.0)).thenReturn(2.0);
        double result2 = solver.solve(f, -2.0, -1.0);
        assertEquals(-1.5, result2, 1E-6);

        // Test case 3
        when(f.value(0.5)).thenReturn(-1.0);
        when(f.value(1.0)).thenReturn(1.0);
        double result3 = solver.solve(f, 0.5, 1.0);
        assertEquals(0.75, result3, 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        when(f.value(-10.0)).thenReturn(-1.0);
        when(f.value(10.0)).thenReturn(1.0);
        solver.setMaximalIterationCount(5);
        solver.solve(f, -10.0, 10.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        when(f.value(1.0)).thenThrow(new FunctionEvaluationException(1.0));
        solver.solve(f, -1.0, 1.0);
    }
}