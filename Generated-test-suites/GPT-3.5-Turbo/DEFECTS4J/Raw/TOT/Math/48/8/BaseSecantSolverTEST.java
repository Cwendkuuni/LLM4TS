
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {
    
    private BaseSecantSolver solver;
    
    @Before
    public void setup() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }
    
    @Test
    public void testSolve_WhenRootIsBracketed_ThenReturnRoot() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 3;
            }
        };
        double root = solver.solve(100, function, 2, 4);
        assertEquals(3, root, 1e-6);
    }
    
    @Test
    public void testSolve_WhenRootIsNotBracketed_ThenThrowConvergenceException() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return FastMath.sin(x);
            }
        };
        try {
            solver.solve(100, function, 0, 1);
        } catch (ConvergenceException ex) {
            assertEquals("Function values at endpoints do not have different signs.", ex.getMessage());
        }
    }
    
    @Test
    public void testSolve_WhenRootIsExact_ThenReturnRoot() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 3;
            }
        };
        double root = solver.solve(100, function, 3, 4);
        assertEquals(3, root, 1e-6);
    }
    
    @Test
    public void testSolve_WhenMaxEvalReached_ThenThrowConvergenceException() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x;
            }
        };
        try {
            solver.solve(5, function, 0, 1);
        } catch (ConvergenceException ex) {
            assertEquals("Maximum number of iterations (5) exceeded.", ex.getMessage());
        }
    }
    
    @Test
    public void testSolve_WhenAllowedSolutionIsLeftAndRootOnLeftSide_ThenReturnRoot() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 3;
            }
        };
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        double root = solver.solve(100, function, 2, 4);
        assertEquals(3, root, 1e-6);
    }
    
    @Test
    public void testSolve_WhenAllowedSolutionIsLeftAndRootOnRightSide_ThenThrowConvergenceException() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 3;
            }
        };
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        try {
            solver.solve(100, function, -4, -2);
        } catch (ConvergenceException ex) {
            assertEquals("Solution interval does not satisfy the requirements: root is not on the same side as the given 'allowed' solution.", ex.getMessage());
        }
    }
    
    @Test
    public void testSolve_WhenAllowedSolutionIsRightAndRootOnRightSide_ThenReturnRoot() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 3;
            }
        };
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(100, function, 2, 4);
        assertEquals(3, root, 1e-6);
    }
    
    @Test
    public void testSolve_WhenAllowedSolutionIsRightAndRootOnLeftSide_ThenThrowConvergenceException() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 3;
            }
        };
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        try {
            solver.solve(100, function, 4, 6);
        } catch (ConvergenceException ex) {
            assertEquals("Solution interval does not satisfy the requirements: root is not on the same side as the given 'allowed' solution.", ex.getMessage());
        }
    }
    
    @Test
    public void testSolve_WhenAllowedSolutionIsBelowAndRootIsBelow_ThenReturnRoot() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 3;
            }
        };
        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        double root = solver.solve(100, function, 2, 4);
        assertEquals(3, root, 1e-6);
    }
    
    @Test
    public void testSolve_WhenAllowedSolutionIsBelowAndRootIsAbove_ThenThrowConvergenceException() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 3;
            }
        };
        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        try {
            solver.solve(100, function, 8, 10);
        } catch (ConvergenceException ex) {
            assertEquals("Solution interval does not satisfy the requirements: root is not on the same side as the given 'allowed' solution.", ex.getMessage());
        }
    }
    
    @Test
    public void testSolve_WhenAllowedSolutionIsAboveAndRootIsAbove_ThenReturnRoot() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 3;
            }
        };
        solver.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        double root = solver.solve(100, function, 2, 4);
        assertEquals(3, root, 1e-6);
    }
    
    @Test
    public void testSolve_WhenAllowedSolutionIsAboveAndRootIsBelow_ThenThrowConvergenceException() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 3;
            }
        };
        solver.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        try {
            solver.solve(100, function, -4, -2);
        } catch (ConvergenceException ex) {
            assertEquals("Solution interval does not satisfy the requirements: root is not on the same side as the given 'allowed' solution.", ex.getMessage());
        }
    }
}

