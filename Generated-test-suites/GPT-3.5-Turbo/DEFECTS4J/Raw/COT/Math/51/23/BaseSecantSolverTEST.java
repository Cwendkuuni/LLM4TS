import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test case 1: Root is between min and max
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double result1 = solver.solve(100, f1, 0, 2);
        assertEquals(1.0, result1, 1e-6);

        // Test case 2: Root is the exact min
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double result2 = solver.solve(100, f2, 0, 2);
        assertEquals(0.0, result2, 1e-6);

        // Test case 3: Root is the exact max
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double result3 = solver.solve(100, f3, 0, 2);
        assertEquals(2.0, result3, 1e-6);

        // Test case 4: f(x) always returns 0
        UnivariateRealFunction f4 = new UnivariateRealFunction() {
            public double value(double x) {
                return 0;
            }
        };
        double result4 = solver.solve(100, f4, 0, 2);
        assertEquals(0.0, result4, 1e-6);
    }

    @Test
    public void testSolve_allowedSolution() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test case 1: AllowedSolution is ANY_SIDE
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double result1 = solver.solve(100, f1, 0, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.0, result1, 1e-6);

        // Test case 2: AllowedSolution is LEFT_SIDE
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double result2 = solver.solve(100, f2, 0, 2, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, result2, 1e-6);

        // Test case 3: AllowedSolution is RIGHT_SIDE
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double result3 = solver.solve(100, f3, 0, 2, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, result3, 1e-6);

        // Test case 4: AllowedSolution is BELOW_SIDE
        UnivariateRealFunction f4 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double result4 = solver.solve(100, f4, 0, 2, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(1.0, result4, 1e-6);

        // Test case 5: AllowedSolution is ABOVE_SIDE
        UnivariateRealFunction f5 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double result5 = solver.solve(100, f5, 0, 2, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(1.0, result5, 1e-6);
    }

    @Test
    public void testSolve_maxEval() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test case 1: maxEval exceeded
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double result1 = solver.solve(3, f1, 0, 2);
        assertTrue(Double.isNaN(result1));

        // Test case 2: maxEval not exceeded
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double result2 = solver.solve(100, f2, 0, 2);
        assertEquals(1.0, result2, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_Exception() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test case 1: Unexpected error occurs
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException();
            }
        };
        double result1 = solver.solve(100, f1, 0, 2);
    }

    @Test
    public void testDoSolve() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test case 1: Root is between min and max
        double result1 = solver.doSolve();
        assertEquals(0.0, result1, 1e-6);
    }

    @Test
    public void testVerifyBracketing() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test case 1: min is less than max
        solver.verifyBracketing(0, 2);

        // Test case 2: min is equal to max
        try {
            solver.verifyBracketing(1, 1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {
            // As expected
        }

        // Test case 3: min is greater than max
        try {
            solver.verifyBracketing(2, 0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {
            // As expected
        }
    }

}