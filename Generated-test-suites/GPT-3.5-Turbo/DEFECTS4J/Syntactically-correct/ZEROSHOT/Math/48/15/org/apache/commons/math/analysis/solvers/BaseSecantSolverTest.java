import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 1 , 3);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolveWithMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(10, f, 1 , 3);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithNoSolution() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 1 , 2);
    }

    @Test
    public void testSolveWithDifferentMethod() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 1 , 3);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 1 , 3, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, f, 1 , 3, 2.5);
        assertEquals(2.0, solution, 1e-6);
    }

    private class BaseSecantSolverTestImpl extends BaseSecantSolver {
        public BaseSecantSolverTestImpl(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }
        public double computeObjectiveValue(double x) {
            return 0.0;
        }
        public double getMin() {
            return 0.0;
        }
        public double getMax() {
            return 0.0;
        }
    }
}