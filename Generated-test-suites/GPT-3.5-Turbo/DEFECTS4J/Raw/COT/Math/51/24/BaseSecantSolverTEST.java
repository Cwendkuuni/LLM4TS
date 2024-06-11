import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {
    @Test
    public void solveWithMaxEvalMethodMinMaxStartValueAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;

        double result = solver.solve(100, function, min, max, startValue, allowedSolution);

        Assert.assertEquals(0.0, result, 0.000001);
    }

    @Test
    public void solveWithMaxEvalMethodMinMaxAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = -10.0;
        double max = 10.0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;

        double result = solver.solve(100, function, min, max, allowedSolution);

        Assert.assertEquals(0.0, result, 0.000001);
    }

    @Test
    public void solveWithMaxEvalMethodMinMaxStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;

        double result = solver.solve(100, function, min, max, startValue);

        Assert.assertEquals(0.0, result, 0.000001);
    }

    @Test
    public void solveWithMaxEvalMethodMinMaxStartValueAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;

        double result = solver.solve(100, function, min, max, startValue, allowedSolution);

        Assert.assertEquals(0.0, result, 0.000001);
    }

    @Test
    public void solveWithMaxEvalMethodMinMaxStartValueAllowedSolution1() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ABOVE_SIDE;

        double result = solver.solve(100, function, min, max, startValue, allowedSolution);

        Assert.assertEquals(1.0, result, 0.000001);
    }

    @Test
    public void solveWithMaxEvalMethodMinMaxStartValueAllowedSolution2() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.BELOW_SIDE;

        double result = solver.solve(100, function, min, max, startValue, allowedSolution);

        Assert.assertEquals(-1.0, result, 0.000001);
    }

    @Test
    public void solveWithMaxEvalMethodMinMaxStartValueAllowedSolution3() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.RIGHT_SIDE;

        double result = solver.solve(100, function, min, max, startValue, allowedSolution);

        Assert.assertEquals(0.0, result, 0.000001);
    }

    @Test
    public void solveWithMaxEvalMethodMinMaxStartValueAllowedSolution4() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.LEFT_SIDE;

        double result = solver.solve(100, function, min, max, startValue, allowedSolution);

        Assert.assertEquals(0.0, result, 0.000001);
    }

    @Test(expected = MathInternalError.class)
    public void solveWithMaxEvalMethodMinMaxStartValueAllowedSolution5() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ABOVE_SIDE;

        solver.solve(100, function, min, max, startValue, allowedSolution);
    }
}