import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    
    @Before
    public void setUp() {
        solver = new FakeBaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
    }
    
    @Test
    public void testSolver_withAnySideAllowance() {
        solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 3;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        // Assert that the solution is accurate
        assertEquals(3, solver.getSolution(), 1e-6);
    }
    
    @Test
    public void testSolver_withLeftSideAllowance() {
        solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 3;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        // Assert that the solution is accurate
        assertEquals(3, solver.getSolution(), 1e-6);
    }
    
    @Test
    public void testSolver_withRightSideAllowance() {
        solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 3;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        // Assert that the solution is accurate
        assertEquals(3, solver.getSolution(), 1e-6);
    }
    
    @Test
    public void testSolver_withBelowSideAllowance() {
        solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 3;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        // Assert that the solution is accurate
        assertEquals(3, solver.getSolution(), 1e-6);
    }
    
    @Test
    public void testSolver_withAboveSideAllowance() {
        solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 3;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        // Assert that the solution is accurate
        assertEquals(3, solver.getSolution(), 1e-6);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolver_withInvalidAllowance() {
        solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 3;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.NONE);
    }
    
    @Test
    public void testSolver_withExactRootAtLeftBound() {
        solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 0;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        // Assert that the solution is accurate
        assertEquals(0, solver.getSolution(), 1e-6);
    }
    
    @Test
    public void testSolver_withExactRootAtRightBound() {
        solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 10;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        // Assert that the solution is accurate
        assertEquals(10, solver.getSolution(), 1e-6);
    }
    
    private static class FakeBaseSecantSolver extends BaseSecantSolver {
        
        public FakeBaseSecantSolver(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }
        
        @Override
        protected double computeObjectiveValue(double x) {
            // This is a fake implementation for testing purposes only
            return x;
        }
        
        @Override
        protected void verifyBracketing(double lower, double upper) {
            // This is a fake implementation for testing purposes only
        }
    }
}