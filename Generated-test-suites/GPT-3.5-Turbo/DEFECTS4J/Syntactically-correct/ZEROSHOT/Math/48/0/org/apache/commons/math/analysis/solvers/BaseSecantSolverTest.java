import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2, root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 0, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveMathInternalError() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 0, 5, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
    }

    @Test
    public void testSolveWithStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 0, 5, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2, root, 1e-6);
    }

    @Test
    public void testSolveWithStartValueAndAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 0, 5, 1, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(2, root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithStartValueAndAllowedSolutionConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 0, 1, 1, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveWithStartValueAndAllowedSolutionMathInternalError() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 0, 5, 1, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
    }

    @Test
    public void testDoSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.doSolve();
        assertEquals(2, result, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testDoSolveMathInternalError() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result = solver.doSolve();
    }
}