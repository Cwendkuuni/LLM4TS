import org.junit.Test;
import org.junit.Assert;

public class BisectionSolverTest {

    @Test
    public void testSolve() throws Exception {
        BisectionSolver solver = new BisectionSolver();

        // Test 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5;
            }
        };
        double result1 = solver.solve(f1, 0, 10);
        Assert.assertEquals(5.0, result1, 0);

        // Test 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        double result2 = solver.solve(f2, 0, Math.PI);
        Assert.assertEquals(Math.PI / 2, result2, 0.01);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5;
            }
        };

        double result = solver.solve(f, 0, 10, 0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };

        double result = solver.solve(f, 0, 10);
    }
}