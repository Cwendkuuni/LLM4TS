import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    // Test for solve method
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        double result = solver.solve(100, f, 1, 3);
        assertEquals(2.0, result, 1e-6);
    }

    // Test for solve method with maximum function evaluations
    @Test
    public void testSolveWithMaxEval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        double result = solver.solve(5, f, 0, 4);
        assertEquals(2.0, result, 1e-6);
    }

    // Test for solve method with allowed solutions
    @Test
    public void testSolveWithAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        double result = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    // Test for solve method with start value
    @Test
    public void testSolveWithStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        double result = solver.solve(100, f, 0, 4, 0.5);
        assertEquals(2.0, result, 1e-6);
    }

    // Test for solve method with start value and allowed solutions
    @Test
    public void testSolveWithStartValueAndAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        double result = solver.solve(100, f, 0, 4, 0.5, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    // Test for computeObjectiveValue method
    @Test
    public void testComputeObjectiveValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        double result = solver.computeObjectiveValue(3.0);
        assertEquals(1.0, result, 1e-6);
    }

    // Test for getFunctionValueAccuracy method
    @Test
    public void testGetFunctionValueAccuracy() {
        double result = solver.getFunctionValueAccuracy();
        assertEquals(1e-6, result, 1e-6);
    }

    // Test for getAbsoluteAccuracy method
    @Test
    public void testGetAbsoluteAccuracy() {
        double result = solver.getAbsoluteAccuracy();
        assertEquals(1e-6, result, 1e-6);
    }

    // Test for getRelativeAccuracy method
    @Test
    public void testGetRelativeAccuracy() {
        double result = solver.getRelativeAccuracy();
        assertEquals(1e-6, result, 1e-6);
    }

    // Test for verifyBracketing method
    @Test
    public void testVerifyBracketing() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };

        try {
            solver.verifyBracketing(1.0, 2.0, f);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Function values at endpoints do not have different signs.", e.getMessage());
        }
    }

    // Test for doSolve method
    @Test
    public void testDoSolve() {
        // Test case where root is 2.0
        solver.setFunctionValueAccuracy(1e-6);
        solver.setAbsoluteAccuracy(1e-6);
        solver.setRelativeAccuracy(1e-6);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        double result = solver.doSolve(f, 1, 3, 2, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, result, 1e-6);

        // Test case where root is 0.0 and allowed solution is RIGHT_SIDE
        solver.setFunctionValueAccuracy(1e-6);
        solver.setAbsoluteAccuracy(1e-6);
        solver.setRelativeAccuracy(1e-6);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        double result = solver.doSolve(f, -1, 1, 0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, result, 1e-6);
    }

}