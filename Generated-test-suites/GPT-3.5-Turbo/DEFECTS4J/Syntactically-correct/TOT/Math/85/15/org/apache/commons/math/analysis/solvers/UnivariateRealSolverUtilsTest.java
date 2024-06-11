import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Assert;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    private static final double EPSILON = 1e-9;

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 3;
            }
        };

        double x0 = 0;
        double x1 = 10;

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            Assert.assertEquals(3, result, EPSILON);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 3;
            }
        };

        double x0 = 0;
        double x1 = 10;
        double absoluteAccuracy = 0.001;

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
            Assert.assertEquals(3, result, absoluteAccuracy);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 3;
            }
        };

        double initial = 5;
        double lowerBound = 0;
        double upperBound = 10;

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            Assert.assertEquals(3, result[0], EPSILON);
            Assert.assertEquals(5, result[1], EPSILON);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 3;
            }
        };

        double initial = 5;
        double lowerBound = 0;
        double upperBound = 10;
        int maximumIterations = 100;

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            Assert.assertEquals(3, result[0], EPSILON);
            Assert.assertEquals(5, result[1], EPSILON);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testMidpoint() {
        double a = 0;
        double b = 10;

        double result = UnivariateRealSolverUtils.midpoint(a, b);

        Assert.assertEquals(5, result, EPSILON);
    }

}