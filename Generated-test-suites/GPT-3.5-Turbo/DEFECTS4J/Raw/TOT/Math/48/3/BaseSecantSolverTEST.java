import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-6;

    // Test computeObjectiveValue() method
    @Test
    public void testComputeObjectiveValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double doSolve() {
                // We won't use this method in this test case.
                return 0;
            }
        };

        double result = solver.computeObjectiveValue(2.0);
        assertEquals(result, 0.0, EPSILON);
    }

    // Test solve() method with default allowed solution
    @Test
    public void testSolve_DefaultAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double doSolve() {
                // We won't use this method in this test case.
                return 0;
            }
        };

        double result = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(result, 2.0, EPSILON);
    }

    // Test solve() method with LEFT_SIDE allowed solution
    @Test
    public void testSolve_LeftSideAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double doSolve() {
                // We won't use this method in this test case.
                return 0;
            }
        };

        double result = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(result, -2.0, EPSILON);
    }

    // Test solve() method with RIGHT_SIDE allowed solution
    @Test
    public void testSolve_RightSideAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double doSolve() {
                // We won't use this method in this test case.
                return 0;
            }
        };

        double result = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(result, 2.0, EPSILON);
    }

    // Test solve() method with BELOW_SIDE allowed solution
    @Test
    public void testSolve_BelowSideAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double doSolve() {
                // We won't use this method in this test case.
                return 0;
            }
        };

        double result = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(result, -2.0, EPSILON);
    }

    // Test solve() method with ABOVE_SIDE allowed solution
    @Test
    public void testSolve_AboveSideAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double doSolve() {
                // We won't use this method in this test case.
                return 0;
            }
        };

        double result = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(result, 2.0, EPSILON);
    }

    // Test solve() method with incorrect allowed solution
    @Test(expected = UnsupportedOperationException.class)
    public void testSolve_IncorrectAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double doSolve() {
                // We won't use this method in this test case.
                return 0;
            }
        };

        solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.SAME_SIDE);
    }

    // Test doSolve() method with maximum number of iterations
    @Test(expected = ConvergenceException.class)
    public void testDoSolve_MaxIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                // We won't use this method in this test case.
                return 0;
            }
        };

        solver.setMaximalIterationCount(5);
        solver.solve(5, f, -10, 10);
    }

}