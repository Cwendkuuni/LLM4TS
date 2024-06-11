import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverEmptyConstructor() {
        BisectionSolver solver = new BisectionSolver();
        Assert.assertEquals(100, solver.getMaxIterations());
        Assert.assertEquals(1E-6, solver.getRelativeAccuracy(), 0.00001);
    }

    @Test
    public void testSolveSimpleFunction() {
        UnivariateRealFunction function = new PolynomialFunction(3, 1);
        BisectionSolver solver = new BisectionSolver();
        double x = solver.solve(function, -1.0, 1.0);
        Assert.assertEquals(0.0, x, 0.00001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        UnivariateRealFunction function = new PolynomialFunction(3, 1);
        BisectionSolver solver = new BisectionSolver();
        solver.setMaxIterations(1);
        solver.solve(function, -1.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction function = new FunctionWithException();
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.0, 1.0);
    }

    @Test
    public void testSolveEdgeCases() {
        UnivariateRealFunction function = new PolynomialFunction(3, 1);
        BisectionSolver solver = new BisectionSolver();

        // min and max are very close
        double x = solver.solve(function, -1.000001E-7, -1.000001E-7);
        Assert.assertEquals(0.0, x, 0.00001);

        // min and max have the same value
        x = solver.solve(function, -1.0, -1.0);
        Assert.assertEquals(NaN.NaN, x, 0.0);

        // function does not change sign in the interval
        function = new PolynomialFunction(1, 3);
        x = solver.solve(function, -1.0, 1.0);
        Assert.assertEquals(NaN.NaN, x, 0.0);
    }

    @Test
    public void testSolveInitialValueOutsideInterval() {
        UnivariateRealFunction function = new PolynomialFunction(3, 1);
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, 1.0, -1.0);
        } catch (FunctionEvaluationException e) {
            Assert.assertFalse(e.isSolverError());
            Assert.assertEquals("The interval [1.0, -1.0] is not valid: the function is not defined at x = -1.0", e.getMessage());
        }
    }

    private static class PolynomialFunction implements UnivariateRealFunction {
        private final double a;
        private final double b;

        PolynomialFunction(double a, double b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public double value(double x) {
            return a * Math.pow(x, 2) + b;
        }
    }

    private static class FunctionWithException implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException("Test exception");
        }
    }
}