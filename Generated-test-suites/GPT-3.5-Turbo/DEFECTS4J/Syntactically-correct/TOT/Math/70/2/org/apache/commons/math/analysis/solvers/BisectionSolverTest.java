
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;

public class BisectionSolverTest {

    BisectionSolver solver;
    UnivariateRealFunction f;

    @Before
    public void setup() {
        solver = new BisectionSolver();
        f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return 2*(x-1);
            }
        };
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(f, -5, 5);
        Assert.assertEquals(1.0, result, 1E-6);
    }

    @Test(expected=MaxIterationsExceededException.class)
    public void testSolve_MaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        solver.setAbsoluteAccuracy(1E-8);
        solver.setMaximalIterationCount(5);
        solver.solve(f, -10, 10);
    }

    @Test(expected=FunctionEvaluationException.class)
    public void testSolve_FunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        solver.solve(f, -5, 5);
    }

    @Test
    public void testSolve_IntervalSizeEqualToAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(f, 0, 2);
        Assert.assertEquals(1.0, result, 1E-6);
    }

    @Test
    public void testSolve_IntervalSizeSmallerThanAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(f, 0, 1);
        Assert.assertEquals(1.0, result, 1E-6);
    }

    @Test
    public void testSolve_IntervalSizeLargerThanAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(f, 0, 10);
        Assert.assertEquals(1.0, result, 1E-6);
    }

    @Test
    public void testSolve_RootOutsideInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(f, 5, 10);
        Assert.assertEquals(Double.NaN, result, 1E-6);
    }
}

