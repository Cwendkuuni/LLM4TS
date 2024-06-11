import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.junit.Assert;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        double x0 = 1.0;
        double x1 = 2.0;
        double expectedResult = 1.4142135623730955;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        Assert.assertEquals(expectedResult, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        double x0 = 0.1;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Evaluation error");
            }
        };

        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveIllegalArgumentException() throws Exception {
        UnivariateRealFunction f = null;
        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.cos(x);
            }
        };

        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 1e-5;
        double expectedResult = 0.741065576784357;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        Assert.assertEquals(expectedResult, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        double x0 = 0.1;
        double x1 = 1.0;
        double absoluteAccuracy = 1e-6;
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionEvaluationException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Evaluation error");
            }
        };

        double x0 = 0.0;
        double x1 = 1.0;
        double absoluteAccuracy = 1e-6;
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithIllegalArgumentException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };

        double x0 = 1.0;
        double x1 = 0.0;
        double absoluteAccuracy = 1e-6;
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        double initial = 1.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;
        double[] expectedResult = {-1.5, 1.5};

        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        Assert.assertArrayEquals(expectedResult, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketConvergenceException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        double initial = 0.1;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketFunctionEvaluationException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Evaluation error");
            }
        };

        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketIllegalArgumentException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };

        double initial = 1.0;
        double lowerBound = 2.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test
    public void testBracketMaxIterations() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        double initial = 1.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;
        int maximumIterations = 0;
        double[] expectedResult = null;

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            Assert.fail();
        } catch (ConvergenceException e) {
            expectedResult = e.getMessage().split("\\s+");
            Assert.assertArrayEquals(expectedResult, new String[]{"number", "of", "iterations=", "maximum", "iterations=", "initial=", "lower", "bound=", "upper", "bound=", "final", "a", "value=", "final", "b", "value=", "f(a)=", "f(b)=", "0"}, e.getMessage().split("\\s+"));
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
        UnivariateRealFunction f = null;

        try {
            UnivariateRealSolverUtils.setup(f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    
}