import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Test
    public void testSolve_ExactRoot() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return 0.0;
            }
        };

        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return 0.0;
            }
        }, 0.0, 1.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_BracketedInterval() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                if (x < 0.5) {
                    return -1.0;
                } else {
                    return 1.0;
                }
            }
        };

        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                if (x < 0.5) {
                    return -1.0;
                } else {
                    return 1.0;
                }
            }
        }, 0.0, 1.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.5, result, 1e-6);
    }

    @Test
    public void testSolve_UnbracketedInterval() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x;
            }
        };

        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        }, 0.0, 1.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(0.5, result, 1e-6);
    }
}