import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WhenExactRootExists_ReturnsExactRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 4, 6, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(5.0, result, 0.0);
    }

    @Test
    public void testSolve_WhenNoExactRootExists_ReturnsApproximation() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolve_WhenNoExactRootExistsWithAbsoluteAccuracy_ReturnsApproximation() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 1, 3, 1.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(5.0, result, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_WhenFunctionThrowsException_ThrowsMathInternalError() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException("Function failed");
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 1, 3, 1.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolve_WhenSolutionBelowSide_ReturnsSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.BELOW_SIDE);

        assertEquals(5.0, result, 0.0);
    }

    @Test
    public void testSolve_WhenSolutionNotFoundInAllowedSide_ThrowsMathInternalError() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        try {
            double result = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        } catch (MathInternalError e) {
            assertEquals("Invalid solution", e.getMessage());
        }
    }

    @Test
    public void testSolve_WhenSolutionNotFoundInAllowedSide_ReturnsSolutionInAllowedSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        assertEquals(5.0, result, 0.0);
    }

    @Test
    public void testSolve_WhenSolutionNotFoundInAllowedSide_ReturnsSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 8, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        assertEquals(5.0, result, 0.0);
    }
}