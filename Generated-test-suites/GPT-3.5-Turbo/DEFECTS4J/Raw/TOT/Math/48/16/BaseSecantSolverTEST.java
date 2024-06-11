import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WhenFunctionHasRoot_ReturnsRoot() {
        BaseSecantSolver solver = new DerivedSecantSolver(0.000001, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5, root, 0.000001);
    }

    @Test
    public void testSolve_WhenFunctionHasNoRoot_ThrowsConvergenceException() {
        BaseSecantSolver solver = new DerivedSecantSolver(0.000001, 
                                                          BaseSecantSolver.Method.REGULA_FALSI);
        try {
            double root = solver.solve(100, new UnivariateRealFunction() {
                public double value(double x) {
                    return x + 5;
                }
            }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected ConvergenceException to be thrown");
        } catch (ConvergenceException ex) {
            // Exception should be thrown
        }
    }

    @Test
    public void testSolve_WhenFunctionHasMultipleRoots_ReturnsClosestRoot() {
        BaseSecantSolver solver = new DerivedSecantSolver(0.000001, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                if (x < 10) {
                    return x - 5;
                } else {
                    return x - 15;
                }
            }
        }, 0, 20, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5, root, 0.000001);
    }

    @Test
    public void testSolve_WhenMaxIterationsExceeded_ThrowsConvergenceException() {
        BaseSecantSolver solver = new DerivedSecantSolver(0.000001, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            double root = solver.solve(5, new UnivariateRealFunction() {
                public double value(double x) {
                    return x - 5;
                }
            }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected ConvergenceException to be thrown");
        } catch (ConvergenceException ex) {
            // Exception should be thrown
        }
    }

    private static class DerivedSecantSolver extends BaseSecantSolver {

        public DerivedSecantSolver(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }

        @Override
        protected double computeObjectiveValue(double x) {
            // Dummy implementation for testing purposes
            return 0;
        }

        @Override
        protected void verifyBracketing(double min, double max) {
            // Dummy implementation for testing purposes
        }

    }
}