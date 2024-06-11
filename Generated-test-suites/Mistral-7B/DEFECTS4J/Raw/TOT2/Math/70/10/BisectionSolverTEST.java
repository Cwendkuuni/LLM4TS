import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.SolverFactory;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.solvers.BisectionSolver;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructor() {
        new BisectionSolver();
    }

    @Test
    public void testSolveFunctionWithInitialValue() throws MaxIterationsExceededException, PrecisionException, FunctionEvaluationException {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4);
        double min = -2.0;
        double max = 2.0;
        double initial = 0.0;

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, min, max, initial);

        Assert.assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveFunction() throws MaxIterationsExceededException, PrecisionException, FunctionEvaluationException, NoRootsException {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4);
        double min = -2.0;
        double max = 2.0;

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, min, max);

        Assert.assertEquals(0.0, root, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws FunctionEvaluationException {
        UnivariateFunction function = new PolynomialFunction(x -> 1 / x);
        double min = 0.0;
        double max = 1.0;

        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, min, max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws MaxIterationsExceededException {
        UnivariateFunction function = new PolynomialFunction(x -> { throw new RuntimeException("FunctionEvaluationException"); });
        double min = 0.0;
        double max = 2.0;

        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, min, max);
    }

    @Test
    public void testFunctionWithNoRoots() throws MaxIterationsExceededException, PrecisionException {
        UnivariateFunction function = new PolynomialFunction(x -> Math.sin(x) - Math.cos(x));
        double min = -5.0;
        double max = 5.0;

        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, min, max);
            Assert.fail();
        } catch (NoRootsException e) {
            // Expected result
        }
    }

    @Test
    public void testFunctionWithMultipleRoots() throws MaxIterationsExceededException, PrecisionException {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x - 2, 2) - 4);
        double min = -3.0;
        double max = 3.0;

        BisectionSolver solver = new BisectionSolver();
        double root1 = solver.solve(function, min, max);
        double root2 = solver.solve(function, min, max);

        Assert.assertEquals(2.0, root1, 1e-6);
        Assert.assertEquals(2.0, root2, 1e-6);
    }

    @Test
    public void testFunctionWithLargeRoot() throws MaxIterationsExceededException, PrecisionException {
        UnivariateFunction function = new PolynomialFunction(x -> Math.exp(x) - 1000000.0);
        double min = -100.0;
        double max = 100.0;

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, min, max);

        Assert.assertEquals(Math.log(1000000.0), root, 1e-6);
    }

    @Test
    public void testFunctionWithSmallRoot() throws MaxIterationsExceededException, PrecisionException {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(10, -10) * (Math.sin(x) - Math.cos(x)));
        double min = -10.0;
        double max = 10.0;

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, min, max);

        Assert.assertEquals(0.0, root, 1e-20);
    }

    @Test
    public void testFunctionWithLargeInterval() throws MaxIterationsExceededException, PrecisionException {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        double min = -1000.0;
        double max = 1000.0;

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, min, max);

        Assert.assertNotEquals(0.0, root);
    }

    @Test
    public void testFunctionWithSmallDifferenceBetweenMinAndMax() throws MaxIterationsExceededException, PrecisionException {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 0.00001);
        double min = 0.0;
        double max = 0.00001;

        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, min, max);
            Assert.fail();
        } catch (MaxIterationsExceededException e) {
            // Expected result
        }
    }
}