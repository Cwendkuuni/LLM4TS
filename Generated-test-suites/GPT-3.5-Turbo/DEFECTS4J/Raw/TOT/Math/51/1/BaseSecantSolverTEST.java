import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    public static class TestFunction implements UnivariateRealFunction {

        private double expectedValue;

        public TestFunction(double expectedValue) {
            this.expectedValue = expectedValue;
        }

        public double value(double x) {
            return x - expectedValue;
        }
    }

    @Test
    public void testSolve_ExactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 1.0;
            }
        };

        double result = solver.solve(10, new TestFunction(1.0), 0.0, 2.0);

        Assert.assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testSolve_MaxIterationsReached() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 2.0;
            }
        };

        double result = solver.solve(5, new TestFunction(1.0), 0.0, 2.0);

        Assert.assertEquals(2.0, result, 0.0);
    }

    @Test
    public void testSolve_FunctionValueAccuracyReached() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 1e-8;
            }
        };

        double result = solver.solve(10, new TestFunction(1.0), 0.0, 2.0);

        Assert.assertEquals(1e-8, result, 0.0);
    }

    @Test
    public void testSolve_RelativeAccuracyReached() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 1e-4;
            }
        };

        double result = solver.solve(10, new TestFunction(1.0), 0.0, 2.0);

        Assert.assertEquals(1e-4, result, 0.0);
    }

    @Test
    public void testSolve_AbsoluteAccuracyReached() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                1e-7, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 1e-6;
            }
        };

        double result = solver.solve(10, new TestFunction(1.0), 0.0, 2.0);

        Assert.assertEquals(1e-6, result, 0.0);
    }

    @Test
    public void testSolve_AnySideAllowed() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 1.0;
            }
        };

        double result = solver.solve(10, new TestFunction(1.0), 0.0, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        Assert.assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testSolve_LeftSideAllowed() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 1.0;
            }
        };

        double result = solver.solve(10, new TestFunction(1.0), 0.0, 2.0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        Assert.assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testSolve_RightSideAllowed() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 1.0;
            }
        };

        double result = solver.solve(10, new TestFunction(1.0), 0.0, 2.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        Assert.assertEquals(2.0, result, 0.0);
    }

    @Test
    public void testSolve_BelowSideAllowed() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 1.0;
            }
        };

        double result = solver.solve(10, new TestFunction(1.0), 0.0, 2.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);

        Assert.assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testSolve_AboveSideAllowed() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x - 1.0;
            }
        };

        double result = solver.solve(10, new TestFunction(1.0), 0.0, 2.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);

        Assert.assertEquals(1.0, result, 0.0);
    }
}