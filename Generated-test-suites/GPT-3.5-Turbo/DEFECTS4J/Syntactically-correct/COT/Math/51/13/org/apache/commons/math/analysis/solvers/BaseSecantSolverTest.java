// JUnit test cases for BaseSecantSolver class
// Author: [Your Name]

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BaseSecantSolverTest {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-6;

    private BaseSecantSolver solver;
    
    @Before
    public void setup() {
        solver = new BaseSecantSolverStub(DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve_ExactRoot() {
        UnivariateRealFunction f = x -> x - 2;
        double root = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(2.0, root, DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_BracketingInterval() {
        UnivariateRealFunction f = x -> (x - 3) * (x - 1);
        double root = solver.solve(100, f, 2, 4, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertTrue(root > 2.0 && root < 3.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_InvalidBracketingInterval() {
        UnivariateRealFunction f = x -> (x - 3) * (x - 1);
        solver.solve(100, f, 1, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathInternalError.class)
    public void testDoSolve_InfiniteLoop() {
        UnivariateRealFunction f = x -> x * x * x;
        solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    private static class BaseSecantSolverStub extends BaseSecantSolver {
        public BaseSecantSolverStub(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }

        @Override
        protected double computeObjectiveValue(double point) {
            return 0;
        }

        @Override
        protected void verifyBracketing(double lower, double upper) {
            if (lower >= upper) {
                throw new IllegalArgumentException("Invalid bracketing interval");
            }
        }
    }
}