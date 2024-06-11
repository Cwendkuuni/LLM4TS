import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, 1, 3);
            assertEquals(2.0, result, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown in testSolve");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() throws Exception {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.solve(f, 1, 3);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveInvalidInterval() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };
        UnivariateRealSolverUtils.solve(f, 3, 1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveErrorFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new ArithmeticException("Function evaluation error");
            }
        };
        UnivariateRealSolverUtils.solve(f, 1, 3);
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, 1, 3, 0.01);
            assertEquals(2.0, result, 0.01);
        } catch (Exception e) {
            fail("Exception thrown in testSolveWithAccuracy");
        }
    }

    // Additional test cases for other methods ...

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 1, 0, 3);
            assertEquals(1.0, result[0], 0.0001);
            assertEquals(3.0, result[1], 0.0001);
        } catch (Exception e) {
            fail("Exception thrown in testBracket");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketNullFunction() throws Exception {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.bracket(f, 1, 0, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidBounds() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };
        UnivariateRealSolverUtils.bracket(f, 1, 3, 1);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketMaxIterations() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };
        UnivariateRealSolverUtils.bracket(f, 1, 0, 10, 1);
    }

    @Test
    public void testMidpoint() {
        double a = 2.0;
        double b = 4.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(3.0, result, 0.0001);
    }

    @Test
    public void testSetup() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };
        try {
            UnivariateRealSolverUtils.setup(f);
        } catch (Exception e) {
            fail("Exception thrown in testSetup");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetupNullFunction() throws Exception {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.setup(f);
    }

}