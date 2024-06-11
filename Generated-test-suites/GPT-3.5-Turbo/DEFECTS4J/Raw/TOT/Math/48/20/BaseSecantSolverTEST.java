import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {
    
    @Test
    public void testSolve_ExactRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2; // exact root = 2
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(2, result, solver.getAbsoluteAccuracy());
    }

    @Test
    public void testSolve_RootWithinAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x); // root = 0
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, -1, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0, result, solver.getAbsoluteAccuracy());
    }

    @Test
    public void testSolve_InvalidBracketing() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1; // root = 1 and -1
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
        try {
            double result = solver.solve(100, f, 0, 1, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
            Assert.fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Function values at endpoints do not have different signs.", e.getMessage());
        }
    }

    // Additional test cases...

}