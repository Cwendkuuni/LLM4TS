import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.AbstractUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {
    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            protected double computeObjectiveValue(double x) {
                // Define a simple function here.
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolveWithSimpleFunctionAndAllowedSolutionAnySide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double solution = solver.solve(100, f, -5, 5, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolveWithSimpleFunctionAndAllowedSolutionLeftSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double solution = solver.solve(100, f, -5, 5, AllowedSolution.LEFT_SIDE);
        Assert.assertEquals(-2.0, solution, 1e-6);
    }

    @Test
    public void testSolveWithSimpleFunctionAndAllowedSolutionRightSide() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double solution = solver.solve(100, f, -5, 5, AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testComputeObjectiveValueWithSimpleFunction() {
        double result = solver.computeObjectiveValue(2.5);
        Assert.assertEquals(2.25, result, 1e-6);
    }
}