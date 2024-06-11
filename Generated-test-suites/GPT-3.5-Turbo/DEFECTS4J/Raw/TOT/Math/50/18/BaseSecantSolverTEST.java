import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x - 2;
        }
    }

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double doSolve() throws MathInternalError {
                return 0.0;
            }

            @Override
            protected double computeObjectiveValue(double x) throws MathInternalError {
                return 0.0;
            }

            @Override
            protected void verifyBracketing(double x0, double x1) throws MathInternalError {
                // Do nothing
            }
        };
    }

    @Test
    public void testSolve_WithRootInRange_ReturnsRoot() {
        UnivariateRealFunction f = new TestFunction();
        double root = solver.solve(100, f, 1, 3, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_WithRootAtMinBound_ReturnsMinBound() {
        UnivariateRealFunction f = new TestFunction();
        double root = solver.solve(100, f, 2, 3, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_WithRootAtMaxBound_ReturnsMaxBound() {
        UnivariateRealFunction f = new TestFunction();
        double root = solver.solve(100, f, 1, 2, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_WithNoRootInRange_ThrowsException() {
        UnivariateRealFunction f = new TestFunction();
        try {
            solver.solve(100, f, 1, 1.5, AllowedSolution.ANY_SIDE);
            fail("Expected MathInternalError to be thrown");
        } catch (MathInternalError e) {
            // Expected
        }
    }

    @Test
    public void testSolve_WithInvalidAllowedSolution_ThrowsException() {
        UnivariateRealFunction f = new TestFunction();
        try {
            solver.solve(100, f, 1, 3, AllowedSolution.LEFT_SIDE);
            fail("Expected MathInternalError to be thrown");
        } catch (MathInternalError e) {
            // Expected
        }
    }

}