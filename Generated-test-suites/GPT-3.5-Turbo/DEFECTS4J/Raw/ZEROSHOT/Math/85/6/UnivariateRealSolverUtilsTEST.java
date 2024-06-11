import org.junit.Test;

import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    private final double EPSILON = 1e-10;

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, 1, 3);
            assertEquals(2.0, result, EPSILON);
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, 1, 3, 1e-6);
            assertEquals(2.0, result, EPSILON);
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 2, 1, 3);
            assertEquals(1.0, result[0], EPSILON);
            assertEquals(3.0, result[1], EPSILON);
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }
    }

    @Test
    public void testBracketWithMaximumIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 2, 1, 3, 100);
            assertEquals(1.0, result[0], EPSILON);
            assertEquals(3.0, result[1], EPSILON);
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2.0, result, EPSILON);
    }

    @Test(expected = MathRuntimeException.class)
    public void testSetupWithNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.setup(f);
    }
}