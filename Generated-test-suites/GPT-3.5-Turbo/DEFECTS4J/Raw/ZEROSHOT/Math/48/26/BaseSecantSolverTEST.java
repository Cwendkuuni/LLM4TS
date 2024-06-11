import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.optimization.BaseSecantSolver;

public class BaseSecantSolverTest {

    @Test
    public void testSolveAnySide() {
        BaseSecantSolver solver = createSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double solution = solver.solve(100, f, 0, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        Assert.assertEquals(1.0, solution, 0.000001);
    }

    @Test
    public void testSolveLeftSide() {
        BaseSecantSolver solver = createSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double solution = solver.solve(100, f, 0, 2, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        Assert.assertEquals(0.0, solution, 0.000001);
    }

    @Test
    public void testSolveRightSide() {
        BaseSecantSolver solver = createSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double solution = solver.solve(100, f, 0, 2, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(2.0, solution, 0.000001);
    }

    @Test
    public void testSolveBelowSide() {
        BaseSecantSolver solver = createSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double solution = solver.solve(100, f, 0, 2, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        Assert.assertEquals(0.0, solution, 0.000001);
    }

    @Test
    public void testSolveAboveSide() {
        BaseSecantSolver solver = createSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double solution = solver.solve(100, f, 0, 2, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        Assert.assertEquals(2.0, solution, 0.000001);
    }

    private BaseSecantSolver createSolver() {
        return new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return 0;
            }
            protected void verifyBracketing(double x0, double x1) {
                if (x1 < x0) {
                    throw new IllegalArgumentException("x1 is smaller than x0");
                }
            }
        };
    }

}