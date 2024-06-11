import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private static final double ABSOLUTE_ERROR = 1e-6;
    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new BaseSecantSolver(ABSOLUTE_ERROR, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve_withMinAndMaxBounds_returnRoot() {
        // Given
        double min = -10;
        double max = 10;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };

        // When
        double result = solver.solve(100, function, min, max, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Then
        assertEquals(2, result, ABSOLUTE_ERROR);
    }

    @Test
    public void testSolve_withStartValue_returnRoot() {
        // Given
        double startValue = 1;
        double min = -10;
        double max = 10;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };

        // When
        double result = solver.solve(100, function, min, max, startValue, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Then
        assertEquals(2, result, ABSOLUTE_ERROR);
    }

    @Test
    public void testSolve_withMaxEvalExceeded_throwsException() {
        // Given
        double min = -10;
        double max = 10;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };

        try {
            // When
            double result = solver.solve(10, function, min, max, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected MathIllegalStateException to be thrown");
        } catch (MathIllegalStateException ex) {
            // Then
            assertNotNull(ex);
        }
    }

    @Test
    public void testSolve_withInvalidFunction_throwsException() {
        // Given
        double min = -10;
        double max = 10;
        UnivariateRealFunction function = null;

        try {
            // When
            double result = solver.solve(100, function, min, max, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException ex) {
            // Then
            assertNotNull(ex);
        }
    }

}