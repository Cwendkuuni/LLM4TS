import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.junit.Assert;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        double x0 = 1.0;
        double x1 = 2.0;
        double expectedResult = -1.0;

        double result = UnivariateRealSolverUtils.solve(function, x0, x1);
        Assert.assertEquals(expectedResult, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_ConvergenceException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };

        double x0 = 0.0;
        double x1 = 0.0;

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_FunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Evaluation error");
            }
        };

        double x0 = 0.0;
        double x1 = 0.0;

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_IllegalArgumentException() throws Exception {
        UnivariateRealFunction function = null;

        double x0 = 0.0;
        double x1 = 0.0;

        UnivariateRealSolverUtils.solve(function, x0, x1);
    }

    @Test
    public void testSolve_AbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.abs(Math.sin(x) - 0.5) * 1e6;
            }
        };

        double x0 = 0.0;
        double x1 = 0.0;
        double absoluteAccuracy = 1e-3;

        double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);

        // The result may differ due to floating point errors
        double tolerance = 1e-5;
        Assert.assertTrue("The result is not within the given absolute accuracy: " +
                "expected {0}, but got {1}",
                FunctionUtils.isClose(result, 0.5, tolerance));
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_FunctionEvaluationException_AbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Evaluation error");
            }
        };

        double x0 = 0.0;
        double x1 = 0.0;
        double absoluteAccuracy = 1e-3;

        UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_IllegalArgumentException_AbsoluteAccuracy() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        double x0 = 0.0;
        double x1 = 0.0;
        double absoluteAccuracy = -1.0;

        UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
    }

    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };

        double initial = 0.0;
        double lowerBound = -3.0;
        double upperBound = 3.0;

        double[] expectedResult = {-1.279697579687365, 1.279697579687365};

        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);

        Assert.assertArrayEquals(expectedResult, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket_ConvergenceException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x) + 0.001;
            }
        };

        double initial = 0.0;
        double lowerBound = -3.0;
        double upperBound = 3.0;

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracket_FunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Evaluation error");
            }
        };

        double initial = 0.0;
        double lowerBound = -3.0;
        double upperBound = 3.0;

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracket_IllegalArgumentException() throws Exception {
        UnivariateRealFunction function = null;

        double initial = 0.0;
        double lowerBound = -3.0;
        double upperBound = 3.0;

        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }

    @Test
    public void testBracket_MaxIterationsExceededException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };

        double initial = 0.0;
        double lowerBound = -3.0;
        double upperBound = 3.0;
        int maximumIterations = 0;

        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
            Assert.fail("The expected MaxIterationsExceededException was not thrown.");
        } catch (ConvergenceException e) {
            // Expected ConvergenceException because of the loop condition
        } catch (MaxIterationsExceededException e) {
            // The test passes if the MaxIterationsExceededException is thrown
        }
    }

    @Test
    public void testMidpoint() throws Exception {
        double a = 1.0;
        double b = 2.0;

        double expectedResult = 1.5;

        double result = UnivariateRealSolverUtils.midpoint(a, b);

        Assert.assertEquals(expectedResult, result, 1e-6);
    }

    @Test
    public void testSetup() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        try {
            UnivariateRealSolverUtils.setup(function);
        } catch (IllegalArgumentException e) {
            Assert.fail("The expected IllegalArgumentException was not thrown.");
        }
    }

    
}