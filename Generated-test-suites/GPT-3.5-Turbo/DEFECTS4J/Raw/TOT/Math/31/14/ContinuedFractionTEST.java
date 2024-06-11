import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        // Test case for retrieving a coefficient
        // n = 0, x = 1.0
        // Expected result: 1.0
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };
        double result = cf.getA(0, 1.0);
        Assert.assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testGetB() {
        // Test case for retrieving b coefficient
        // n = 0, x = 1.0
        // Expected result: 0.0
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };
        double result = cf.getB(0, 1.0);
        Assert.assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testEvaluateDefault() {
        // Test case for evaluating the continued fraction with default epsilon and maximum iterations
        // x = 1.0
        // Expected result: 1.0
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };
        double result = cf.evaluate(1.0);
        Assert.assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        // Test case for evaluating the continued fraction with specified epsilon
        // x = 1.0, epsilon = 0.001
        // Expected result: 1.0
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };
        double result = cf.evaluate(1.0, 0.001);
        Assert.assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        // Test case for evaluating the continued fraction with specified maximum iterations
        // x = 1.0, maxIterations = 100
        // Expected result: 1.0
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };
        double result = cf.evaluate(1.0, 100);
        Assert.assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        // Test case for evaluating the continued fraction with specified epsilon and maximum iterations
        // x = 1.0, epsilon = 0.001, maxIterations = 100
        // Expected result: 1.0
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };
        double result = cf.evaluate(1.0, 0.001, 100);
        Assert.assertEquals(1.0, result, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        // Test case for evaluating the continued fraction which fails to converge
        // x = 1.0, with a coefficient that always evaluates to 0
        // Expected result: ConvergenceException
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        cf.evaluate(1.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
        // Test case for evaluating the continued fraction which exceeds maximum iterations
        // x = 1.0, with constant a coefficient that never breaks the loop
        // Expected result: MaxCountExceededException
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };
        cf.evaluate(1.0, Integer.MAX_VALUE);
    }

}