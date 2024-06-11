import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {
    
    private BaseSecantSolver solver;
    
    @Before
    public void setUp() {
        solver = new BaseSecantSolverStub();
    }
    
    @Test
    public void testSolveWithFunctionValueEqualToZero() {
        double result = solver.solve(100, new UnivariateRealFunctionStub(), -10, 10, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, result, 0.0);
    }
    
    @Test
    public void testSolveWithFunctionValueWithinAccuracies() {
        double result = solver.solve(100, new UnivariateRealFunctionStub(), -10, 10, 5, AllowedSolution.ANY_SIDE);
        assertEquals(5.0, result, 0.0);
    }
    
    @Test
    public void testSolveWithFunctionValueGreaterThanZeroWhenAllowedSolutionIsBelowSide() {
        double result = solver.solve(100, new UnivariateRealFunctionStub(), -10, 10, AllowedSolution.BELOW_SIDE);
        assertEquals(-5.0, result, 0.0);
    }
    
    @Test
    public void testSolveWithFunctionValueLessThanZeroWhenAllowedSolutionIsAboveSide() {
        double result = solver.solve(100, new UnivariateRealFunctionStub(), -10, 10, AllowedSolution.ABOVE_SIDE);
        assertEquals(5.0, result, 0.0);
    }
    
    @Test
    public void testSolveWithFunctionValueAccuraciesAreNotMet() {
        double result = solver.solve(100, new UnivariateRealFunctionStub(), -10, 10, AllowedSolution.LEFT_SIDE);
        assertEquals(-10.0, result, 0.0);
    }
    
    private static class BaseSecantSolverStub extends BaseSecantSolver {
        
        public BaseSecantSolverStub() {
            super(1e-6, Method.REGULA_FALSI); // Using default absolute accuracy
        }
        
        @Override
        protected double computeObjectiveValue(double point) {
            return point * point - 25; // x^2 - 25 = 0
        }
        
        @Override
        protected void verifyBracketing(double lower, double upper) {
            // No implementation needed for testing
        }
        
        private static class UnivariateRealFunctionStub implements UnivariateRealFunction {
            @Override
            public double value(double x) {
                return x * x - 25; // x^2 - 25
            }
        }
    }
}