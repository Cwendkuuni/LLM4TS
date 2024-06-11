import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return function.value(x);
            }
        };

        double root = solver.solve(100, function, 0, 10);
        assertEquals(5.0, root, 1e-5);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return function.value(x);
            }
        };

        double root1 = solver.solve(100, function, 0, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(5.0, root1, 1e-5);

        double root2 = solver.solve(100, function, 0, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(5.0, root2, 1e-5);
    }

    @Test
    public void testSolveWithInvalidSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return function.value(x);
            }
        };

        double root = solver.solve(100, function, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertTrue(Double.isNaN(root));
    }

    @Test(expected = MathInternalError.class)
    public void testSolveWithInvalidMethod() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.PEGASUS) {
            protected double computeObjectiveValue(double x) {
                return function.value(x);
            }
        };

        solver.solve(100, function, 0, 10);
    }
}