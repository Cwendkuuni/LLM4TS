import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        // Test case for Method.REGULA_FALSI

        // Typical use case
        BaseSecantSolver solver1 = new MockSecantSolver(1e-6, Method.REGULA_FALSI);
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result1 = solver1.solve(100, f1, 0, 5, AllowedSolution.ANY_SIDE);
        assertEquals(result1, 2.0, 1e-6);

        // Edge case
        BaseSecantSolver solver2 = new MockSecantSolver(1e-6, Method.REGULA_FALSI);
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result2 = solver2.solve(100, f2, -2, 2, AllowedSolution.ANY_SIDE);
        assertEquals(result2, -2.0, 1e-6);

        // Error scenario
        BaseSecantSolver solver3 = new MockSecantSolver(1e-6, Method.REGULA_FALSI);
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0.0) {
                    throw new ArithmeticException("Division by zero");
                }
                return x * x - 4;
            }
        };
        try {
            double result3 = solver3.solve(100, f3, 0, 5, AllowedSolution.ANY_SIDE);
            fail("Expected ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            assertEquals("Division by zero", e.getMessage());
        }
    }

    @Test
    public void testSolve_WithMaxEval() {
        // Test case for Method.ILLINOIS

        // Typical use case
        BaseSecantSolver solver1 = new MockSecantSolver(1e-6, Method.ILLINOIS);
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result1 = solver1.solve(10, f1, 0, 5, AllowedSolution.ANY_SIDE);
        assertEquals(result1, 2.0, 1e-6);

        // Edge case
        BaseSecantSolver solver2 = new MockSecantSolver(1e-6, Method.ILLINOIS);
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result2 = solver2.solve(10, f2, -2, 2, AllowedSolution.ANY_SIDE);
        assertEquals(result2, -2.0, 1e-6);

        // Error scenario
        BaseSecantSolver solver3 = new MockSecantSolver(1e-6, Method.ILLINOIS);
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0.0) {
                    throw new ArithmeticException("Division by zero");
                }
                return x * x - 4;
            }
        };
        try {
            double result3 = solver3.solve(10, f3, 0, 5, AllowedSolution.ANY_SIDE);
            fail("Expected ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            assertEquals("Division by zero", e.getMessage());
        }
    }

    @Test
    public void testSolve_WithStartValue() {
        // Test case for Method.PEGASUS

        // Typical use case
        BaseSecantSolver solver1 = new MockSecantSolver(1e-6, Method.PEGASUS);
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result1 = solver1.solve(100, f1, 0, 5, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals(result1, 2.0, 1e-6);

        // Edge case
        BaseSecantSolver solver2 = new MockSecantSolver(1e-6, Method.PEGASUS);
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result2 = solver2.solve(100, f2, -2, 2, -1.0, AllowedSolution.ANY_SIDE);
        assertEquals(result2, -2.0, 1e-6);

        // Error scenario
        BaseSecantSolver solver3 = new MockSecantSolver(1e-6, Method.PEGASUS);
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0.0) {
                    throw new ArithmeticException("Division by zero");
                }
                return x * x - 4;
            }
        };
        try {
            double result3 = solver3.solve(100, f3, 0, 5, 3.0, AllowedSolution.ANY_SIDE);
            fail("Expected ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            assertEquals("Division by zero", e.getMessage());
        }
    }
}