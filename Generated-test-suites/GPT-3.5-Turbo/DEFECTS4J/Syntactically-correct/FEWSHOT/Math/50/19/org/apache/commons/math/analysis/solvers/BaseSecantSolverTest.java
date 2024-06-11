import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double doSolve() throws MathInternalError {
                return 0;
            }
        };
    }

    @Test
    public void testSolveAnySide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.ANY_SIDE);
        double result = solver.solve(100, f, 0, 2 * Math.PI);
        assertEquals(0, result, 1e-6);
    }

    @Test
    public void testSolveLeftSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        double result = solver.solve(100, f, 0, 2 * Math.PI);
        assertEquals(0, result, 1e-6);
    }

    @Test
    public void testSolveRightSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        double result = solver.solve(100, f, 0, 2 * Math.PI);
        assertEquals(2 * Math.PI, result, 1e-6);
    }

    @Test
    public void testSolveBelowSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        double result = solver.solve(100, f, 0, 2 * Math.PI);
        assertEquals(Math.PI / 6, result, 1e-6);
    }

    @Test
    public void testSolveAboveSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) + 0.5;
            }
        };

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        double result = solver.solve(100, f, 0, 2 * Math.PI);
        assertEquals(11 * Math.PI / 6, result, 1e-6);
    }
}