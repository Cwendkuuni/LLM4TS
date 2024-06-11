import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.FunctionUtils;
import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private UnivariateRealFunction f;
    private double min;
    private double max;
    private double initial;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        f = new QuinticFunction();
        min = -100;
        max = 100;
        initial = 1;
    }

    @Test
    public void testSolve_minimumToMaximum() throws MaxIterationsExceededException, FunctionEvaluationException {
        double expected = 0.0;
        double result = solver.solve(f, min, max);
        Assert.assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_maximumToMinimum() throws MaxIterationsExceededException, FunctionEvaluationException {
        double expected = 0.0;
        double result = solver.solve(f, max, min);
        Assert.assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_minimumMinimum() throws MaxIterationsExceededException, FunctionEvaluationException {
        double expected = min;
        double result = solver.solve(f, min, min);
        Assert.assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_maximumMaximum() throws MaxIterationsExceededException, FunctionEvaluationException {
        double expected = max;
        double result = solver.solve(f, max, max);
        Assert.assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_outsideOfRange() throws MaxIterationsExceededException, FunctionEvaluationException {
        double expected = Double.NaN;
        double result = solver.solve(f, min - 1, max + 1);
        Assert.assertEquals(expected, result, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_maxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        int maxIterations = solver.getMaximalIterationCount();
        try {
            solver.setMaximalIterationCount(1);
            solver.solve(f, min, max);
        } finally {
            solver.setMaximalIterationCount(maxIterations);
        }
    }

}