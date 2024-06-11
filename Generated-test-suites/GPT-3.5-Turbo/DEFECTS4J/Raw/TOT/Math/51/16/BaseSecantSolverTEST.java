import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve_rootExists() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.pow(x, 2) - 4;
            }
        };
        double result = solver.solve(100, f, -4, 4);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_maxIterationsReached() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x);
            }
        };
        double result = solver.solve(5, f, 0, FastMath.PI);
        assertTrue(result > 2.0 && result < 3.0);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_invalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, null);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.pow(x, 2) - 4;
            }
        };
        solver.solve(100, f, -4, 4);
    }

    @Test
    public void testSolve_anySideSolution() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        solver.setAllowed(BaseSecantSolver.AllowedSolution.ANY_SIDE);
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.sin(x);
            }
        };
        double result1 = solver.solve(100, f1, 0, FastMath.PI);
        assertTrue(result1 > 2.0 && result1 < 3.0);

        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.cos(x);
            }
        };
        double result2 = solver.solve(100, f2, -FastMath.PI, 0);
        assertTrue(result2 > -3.0 && result2 < -2.0);
    }

    @Test
    public void testSolve_leftSideSolution() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        solver.setAllowed(BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.log(x);
            }
        };
        double result = solver.solve(100, f, 1e-15, 1);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_rightSideSolution() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        solver.setAllowed(BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.log(x);
            }
        };
        double result = solver.solve(100, f, 1, 1.0 + 1e-15);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_belowSideSolution() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        solver.setAllowed(BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.log(x);
            }
        };
        double result = solver.solve(100, f, 1e-15, FastMath.PI);
        assertTrue(result > 0.0 && result < 1.0);
    }

    @Test
    public void testSolve_aboveSideSolution() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        solver.setAllowed(BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return FastMath.log(x);
            }
        };
        double result = solver.solve(100, f, FastMath.PI, 2.0 * FastMath.PI);
        assertTrue(result > 1.0 && result < 2.0);
    }

}