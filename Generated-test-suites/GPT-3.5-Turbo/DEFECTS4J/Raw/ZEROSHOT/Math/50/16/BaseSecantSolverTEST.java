import org.junit.Test;

public class BaseSecantSolverTest {

@Test
public void testSolve() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
        protected double computeObjectiveValue(double arg0) {
            return Math.exp(arg0) - arg0;
        }
    };
    double solution = solver.solve(100, null, 0, 1);
    assertEquals(0.567143, solution, 1e-6);
}

@Test
public void testSolve_InvalidSolution() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
        protected double computeObjectiveValue(double arg0) {
            return Math.exp(arg0) + arg0;
        }
    };
    double solution = solver.solve(100, null, 0, 1);
    assertEquals(-0.567143, solution, 1e-6);
}

@Test
public void testSolve_MaximumEvaluationsExceeded() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
        protected double computeObjectiveValue(double arg0) {
            return Math.exp(arg0) - arg0;
        }
    };
    double solution = solver.solve(5, null, 0, 1);
    assertEquals(Double.NaN, solution, 1e-6);
}

@Test
public void testSolve_NaNInput() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
        protected double computeObjectiveValue(double arg0) {
            return Math.exp(arg0) - arg0;
        }
    };
    double solution = solver.solve(100, null, Double.NaN, 1);
    assertEquals(Double.NaN, solution, 1e-6);
}

@Test
public void testSolve_InfiniteInput() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
        protected double computeObjectiveValue(double arg0) {
            return Math.exp(arg0) - arg0;
        }
    };
    double solution = solver.solve(100, null, Double.POSITIVE_INFINITY, 1);
    assertEquals(Double.NaN, solution, 1e-6);
}

@Test
public void testSolve_MinEqualsMax() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
        protected double computeObjectiveValue(double arg0) {
            return Math.exp(arg0) - arg0;
        }
    };
    double solution = solver.solve(100, null, 0, 0);
    assertEquals(0, solution, 1e-6);
}

@Test
public void testSolve_MaxLessThanMin() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
        protected double computeObjectiveValue(double arg0) {
            return Math.exp(arg0) - arg0;
        }
    };
    double solution = solver.solve(100, null, 2, 1);
    assertEquals(Double.NaN, solution, 1e-6);
}

}