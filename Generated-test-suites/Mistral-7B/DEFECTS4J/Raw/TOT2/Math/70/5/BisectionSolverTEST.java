import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;

public class BisectionSolverTest {

    @Test
    public void testSolveConstructionWithFunction() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0}, new double[]{0.0, 1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver(function);
        double root = solver.solve(0.0, 1.0);
        Assert.assertEquals(0.5, root, 1.0e-5);
    }

    @Test
    public void testSolveBasic() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0}, new double[]{0.0, 1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 0.0, 1.0);
        Assert.assertEquals(0.5, root, 1.0e-5);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0}, new double[]{0.0, 1.0, 1.0, 0.0});
        BisectionSolver solver = new BisectionSolver();
        solver.setMaximalIterationCount(0);
        solver.solve(function, 0.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Function evaluation error");
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 0.0, 1.0);
    }
}