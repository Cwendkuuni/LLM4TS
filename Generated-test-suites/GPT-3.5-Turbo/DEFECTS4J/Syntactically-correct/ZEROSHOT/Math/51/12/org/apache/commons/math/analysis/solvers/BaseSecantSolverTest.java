import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private class DummyFunction implements UnivariateRealFunction {

        public double value(double x) {
            return 0;
        }
    }

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve() {

        // Test using a dummy function that returns 0 for all values of x
        UnivariateRealFunction f = new DummyFunction();
        double result = solver.solve(100, f, 0, 10, 5);
        assertEquals(5, result, 1e-6);
    }

    @Test
    public void testSolveMaxEvalReached() {
        UnivariateRealFunction f = new DummyFunction();
        double result = solver.solve(0, f, 0, 10, 5);
        assertEquals(Double.NaN, result, 1e-6);
    }

    @Test
    public void testSolveBoundaryConditions() {
        // Test when the exact root is at the minimum boundary
        UnivariateRealFunction f = new DummyFunction();
        double result = solver.solve(100, f, 0, 10, 0);
        assertEquals(0, result, 1e-6);

        // Test when the exact root is at the maximum boundary
        result = solver.solve(100, f, 0, 10, 10);
        assertEquals(10, result, 1e-6);

        // Test when the f0 = 0
        DummyFunction f0 = new DummyFunction() {
            public double value(double x) {
                return 0;
            }
        };
        result = solver.solve(100, f0, 0, 10, 0);
        assertEquals(0, result, 1e-6);

        // Test when the f1 = 0
        DummyFunction f1 = new DummyFunction() {
            public double value(double x) {
                return 0;
            }
        };
        result = solver.solve(100, f1, 0, 10, 10);
        assertEquals(10, result, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveWrongMethod() {
        // Test when the given method is wrong
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new DummyFunction();
        double result = solver.solve(100, f, 0, 10, 5);
    }

    @Test
    public void testDoSolve() {
        // Create a subclass of BaseSecantSolver to test doSolve
        BaseSecantSolverTestClass solver = new BaseSecantSolverTestClass(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new DummyFunction();
        double result = solver.doSolve(f, 0, 10, 5);
        assertEquals(5, result, 1e-6);
    }

    private class BaseSecantSolverTestClass extends BaseSecantSolver {

        protected BaseSecantSolverTestClass(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }

        protected double doSolve(UnivariateRealFunction f, double min, double max, double startValue) {
            this.allowed = AllowedSolution.ANY_SIDE;
            this.setMin(min);
            this.setMax(max);
            this.setStartValue(startValue);
            return super.doSolve();
        }
    }
}