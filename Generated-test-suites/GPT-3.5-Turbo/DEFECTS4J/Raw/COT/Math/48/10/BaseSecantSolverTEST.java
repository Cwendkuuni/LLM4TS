import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;


public class BaseSecantSolverTest {

    @Test
    public void testSolve_ExactRoot() {
        // Given
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double min = 0;
        double max = 10;
        double startValue = 7;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;

        // When
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, min, max, startValue, allowedSolution);

        // Then
        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolve_BracketingInterval() {
        // Given
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double min = 0;
        double max = 5;
        double startValue = 3;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;

        // When
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, min, max, startValue, allowedSolution);

        // Then
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_FunctionValueAccuracy() {
        // Given
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double min = 0;
        double max = 5;
        double startValue = 3;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;

        // When
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-9, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, min, max, startValue, allowedSolution);

        // Then
        assertEquals(2.0, result, 1e-9);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_MaximumIterationsExceeded() {
        // Given
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double min = 0;
        double max = 5;
        double startValue = 3;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;

        // When
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(5, f, min, max, startValue, allowedSolution);

        // Then
        // Expect ConvergenceException
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_InvalidMethod() {
        // Given
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double min = 0;
        double max = 5;
        double startValue = 3;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;

        // When
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.SECANT);
        solver.solve(100, f, min, max, startValue, allowedSolution);

        // Then
        // Expect MathInternalError
    }
}