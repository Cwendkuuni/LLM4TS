import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver.Method;
import org.apache.commons.math.analysis.solvers.AllowedSolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;



public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, Method.REGULA_FALSI);

        // Test solving a simple linear equation.
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution1 = solver.solve(100, f1, 1, 3);
        assertEquals(2, solution1, 1e-6);

        // Test solving an equation with multiple roots.
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return (x - 2) * (x - 5);
            }
        };
        double solution2 = solver.solve(100, f2, 1, 6);
        assertTrue(FastMath.abs(solution2 - 2) < 1e-6 || FastMath.abs(solution2 - 5) < 1e-6);

        // Test solving an equation with no solutions.
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        try {
            solver.solve(100, f3, 1, 3);
            fail("No solutions found, should throw MathInternalError");
        } catch (MathInternalError e) {
            // Expected exception, do nothing.
        }
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, Method.REGULA_FALSI);

        // Test solving with ALLOWED_SOLUTION set to ANY_SIDE.
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution1 = solver.solve(100, f1, 1, 3, AllowedSolution.ANY_SIDE);
        assertEquals(2, solution1, 1e-6);

        // Test solving with ALLOWED_SOLUTION set to LEFT_SIDE.
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution2 = solver.solve(100, f2, 1, 3, AllowedSolution.LEFT_SIDE);
        assertEquals(2, solution2, 1e-6);

        // Test solving with ALLOWED_SOLUTION set to RIGHT_SIDE.
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double solution3 = solver.solve(100, f3, 1, 3, AllowedSolution.RIGHT_SIDE);
        assertEquals(2, solution3, 1e-6);

        // Test solving with ALLOWED_SOLUTION set to BELOW_SIDE but the solution is not below.
        UnivariateRealFunction f4 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        try {
            solver.solve(100, f4, 1, 3, AllowedSolution.BELOW_SIDE);
            fail("Solution above, should throw MathInternalError");
        } catch (MathInternalError e) {
            // Expected exception, do nothing.
        }

        // Test solving with ALLOWED_SOLUTION set to ABOVE_SIDE but the solution is not above.
        UnivariateRealFunction f5 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        try {
            solver.solve(100, f5, 1, 3, AllowedSolution.ABOVE_SIDE);
            fail("Solution below, should throw MathInternalError");
        } catch (MathInternalError e) {
            // Expected exception, do nothing.
        }
    }
}

class BaseSecantSolverImpl extends BaseSecantSolver {

    public BaseSecantSolverImpl(double absoluteAccuracy, Method method) {
        super(absoluteAccuracy, method);
    }

    @Override
    protected double computeObjectiveValue(double x) {
        // Not needed for testing.
        return 0;
    }

    @Override
    protected void verifyBracketing(double x0, double x1) {
        // Not needed for testing.
    }

}
