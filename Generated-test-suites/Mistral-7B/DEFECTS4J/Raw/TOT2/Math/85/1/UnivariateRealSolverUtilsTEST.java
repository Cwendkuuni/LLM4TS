import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Assert;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_FunctionWithRoot() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new FunctionWithRoot();
        double root = UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
        Assert.assertEquals(0.0, root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_FunctionWithoutRoot() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new FunctionWithoutRoot();
        UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_FunctionWithError() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new FunctionWithError();
        UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
    }

    @Test
    public void testBracket_FunctionWithRoot() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new FunctionWithRoot();
        double[] bracket = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
        Assert.assertArrayEquals(new double[]{ -1.0, 0.0 }, bracket, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket_FunctionWithoutRoot() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new FunctionWithoutRoot();
        UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracket_FunctionWithError() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new FunctionWithError();
        UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
    }
}

public class FunctionWithRoot extends UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2) - 1.0;
    }
}

public class FunctionWithoutRoot extends UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2) + 1.0;
    }
}

public class FunctionWithError extends UnivariateRealFunction {
    @Override
    public double value(double x) {
        throw new ArithmeticException(" Function error");
    }
}