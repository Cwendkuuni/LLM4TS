import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    private final double EPSILON = 1e-6;

    @Test
    public void testSolveAnySide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolverMock(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 0, 4);
        assertEquals(2, result, EPSILON);
    }

    @Test
    public void testSolveLeftSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolverMock(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 0, 4, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(2, result, EPSILON);
    }

    @Test
    public void testSolveRightSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolverMock(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 0, 4, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2, result, EPSILON);
    }

    @Test
    public void testSolveBelowSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolverMock(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 0, 4, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(2, result, EPSILON);
    }

    @Test
    public void testSolveAboveSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolverMock(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 0, 4, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(2, result, EPSILON);
    }

    private static class BaseSecantSolverMock extends BaseSecantSolver {
        protected BaseSecantSolverMock(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }

        protected BaseSecantSolverMock(double relativeAccuracy, double absoluteAccuracy, Method method) {
            super(relativeAccuracy, absoluteAccuracy, method);
        }

        protected BaseSecantSolverMock(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy, Method method) {
            super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
        }

        @Override
        protected double doSolve() {
            return 2;
        }
    }

}