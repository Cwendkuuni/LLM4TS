import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    // Test getA() method
    @Test
    public void testGetA() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0 / ((n + 1) * (n + 2));
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };

        double a0 = continuedFraction.getA(0, 1.0);
        assertEquals(1.0, a0, 0.0);

        double a1 = continuedFraction.getA(1, 1.0);
        assertEquals(0.16666666666666666, a1, 0.0);

        double a2 = continuedFraction.getA(2, 1.0);
        assertEquals(0.044444444444444446, a2, 0.0);

        double a3 = continuedFraction.getA(3, 1.0);
        assertEquals(0.017857142857142856, a3, 0.0);
    }

    // Test getB() method
    @Test
    public void testGetB() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0 / ((n + 1) * (n + 2));
            }
        };

        double b0 = continuedFraction.getB(0, 1.0);
        assertEquals(0.16666666666666666, b0, 0.0);

        double b1 = continuedFraction.getB(1, 1.0);
        assertEquals(0.044444444444444446, b1, 0.0);

        double b2 = continuedFraction.getB(2, 1.0);
        assertEquals(0.017857142857142856, b2, 0.0);

        double b3 = continuedFraction.getB(3, 1.0);
        assertEquals(0.008333333333333333, b3, 0.0);
    }

    // Test evaluate() method with default epsilon and maximum iterations
    @Test
    public void testEvaluate() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double result = continuedFraction.evaluate(1.0);
        assertEquals(1.0, result, 0.0);
    }

    // Test evaluate() method with specified epsilon
    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double result = continuedFraction.evaluate(1.0, 0.0001);
        assertEquals(1.0, result, 0.0);
    }

    // Test evaluate() method with specified maximum iterations
    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double result = continuedFraction.evaluate(1.0, 10);
        assertEquals(0.7083333333333333, result, 0.0);
    }

    // Test evaluate() method with specified epsilon and maximum iterations
    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double result = continuedFraction.evaluate(1.0, 0.0001, 10);
        assertEquals(0.7083333333333333, result, 0.0);
    }

    // Test evaluate() method with convergence exception
    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithConvergenceException() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n * n;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        continuedFraction.evaluate(1.0);
    }

    // Test evaluate() method with max count exceeded exception
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithMaxCountExceededException() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        continuedFraction.evaluate(1.0, 0.0001, 5);
    }
}