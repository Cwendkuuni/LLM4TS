import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithValidInput_ShouldReturnRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // create a simple linear function f(x) = 2x - 5
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x - 5;
            }
        };

        double root = solver.solve(100, f, 1, 10, AllowedSolution.ANY_SIDE);

        assertEquals(2.5, root, 1e-6);
    }

    @Test
    public void testSolve_WithSameBounds_ShouldReturnOneOfTheBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // create a simple linear function f(x) = x + 5
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 5;
            }
        };

        double root = solver.solve(100, f, 3, 3, AllowedSolution.ANY_SIDE);

        assertEquals(3, root, 1e-6);
    }

    @Test
    public void testSolve_WithInvalidInput_ShouldThrowMathInternalError() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // create a simple linear function f(x) = x + 5
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 5;
            }
        };

        try {
            // min > max
            double root = solver.solve(100, f, 10, 5, AllowedSolution.ANY_SIDE);
            fail("Expected MathInternalError to be thrown");
        } catch (MathInternalError e) {
            // test passed
        }
    }

    @Test
    public void testSolve_WithFunctionValueBelowSide_ShouldReturnRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // create a simple linear function f(x) = 2x - 5
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x - 5;
            }
        };

        double root = solver.solve(100, f, 1, 10, AllowedSolution.BELOW_SIDE);

        assertEquals(2.5, root, 1e-6);
    }

    @Test
    public void testSolve_WithFunctionValueAboveSide_ShouldReturnRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // create a simple linear function f(x) = 2x - 5
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x - 5;
            }
        };

        double root = solver.solve(100, f, -10, -1, AllowedSolution.ABOVE_SIDE);

        assertEquals(-2.5, root, 1e-6);
    }

    @Test
    public void testSolve_WithFunctionValueLeftSide_ShouldReturnRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // create a simple linear function f(x) = 2x - 5
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x - 5;
            }
        };

        double root = solver.solve(100, f, -10, 2.49, AllowedSolution.LEFT_SIDE);

        assertEquals(-2.5, root, 1e-6);
    }

    @Test
    public void testSolve_WithFunctionValueRightSide_ShouldReturnRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // create a simple linear function f(x) = 2x - 5
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x - 5;
            }
        };

        double root = solver.solve(100, f, 2.51, 10, AllowedSolution.RIGHT_SIDE);

        assertEquals(2.5, root, 1e-6);
    }

}