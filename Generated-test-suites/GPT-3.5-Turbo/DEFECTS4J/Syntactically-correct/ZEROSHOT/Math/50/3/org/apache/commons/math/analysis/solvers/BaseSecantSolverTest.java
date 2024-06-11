import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
        };

        double result = solver.solve(100, null, 0, 10, null);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_withAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double computeObjectiveValue(double x) {
                return FastMath.sin(x);
            }
        };

        double result = solver.solve(100, null, 0, 10, 2, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(3.14159, result, 1e-6);
    }

    @Test
    public void testSolve_withMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double computeObjectiveValue(double x) {
                return FastMath.exp(x) - 1;
            }
        };

        double result = solver.solve(10, null, -10, 10, null);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_withBoundaries() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 9;
            }
        };

        double result = solver.solve(100, null, -3, 3, null);
        assertEquals(3.0, result, 1e-6);
    }

    @Test
    public void testSolve_withFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
        };

        double result = solver.solve(100, null, 0, 10, null);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_withRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-4, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double computeObjectiveValue(double x) {
                return FastMath.exp(x) - 1;
            }
        };

        double result = solver.solve(100, null, -10, 10, null);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_withAllowedSolutionAndMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 9;
            }
        };

        double result = solver.solve(10, null, -3, 3, null, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(3.0, result, 1e-6);
    }

    @Test
    public void testSolve_withAllowedSolutionAndBoundaries() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double computeObjectiveValue(double x) {
                return FastMath.sin(x);
            }
        };

        double result = solver.solve(100, null, 0, 10, 2, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(3.14159, result, 1e-6);
    }

    @Test
    public void testSolve_withAllowedSolutionAndFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double computeObjectiveValue(double x) {
                return FastMath.exp(x) - 1;
            }
        };

        double result = solver.solve(100, null, -10, 10, null, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(0.693147, result, 1e-6);
    }

    @Test
    public void testSolve_withAllowedSolutionAndRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
        };

        double result = solver.solve(100, null, -10, 10, null, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(-2.0, result, 1e-6);
    }
}