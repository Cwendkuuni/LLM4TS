import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveThrowsMathInternalError() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Double.NaN;
            }
        };
        double root = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolutionThrowsMathInternalError() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

}