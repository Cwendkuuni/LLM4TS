import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import static org.junit.Assert.*;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n + 1;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double a1 = cf.getA(0, 1.0);
        assertEquals(1, a1, 0);

        double a2 = cf.getA(1, 1.0);
        assertEquals(3, a2, 0);

        double a3 = cf.getA(5, 1.0);
        assertEquals(11, a3, 0);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n + 1;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double b1 = cf.getB(0, 1.0);
        assertEquals(1, b1, 0);

        double b2 = cf.getB(1, 1.0);
        assertEquals(2, b2, 0);

        double b3 = cf.getB(5, 1.0);
        assertEquals(6, b3, 0);
    }

    @Test
    public void testEvaluateDefault() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n + 1;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double result = cf.evaluate(1.0);
        assertEquals(0.765625, result, 0.00001);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n + 1;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double result = cf.evaluate(1.0, 0.00001);
        assertEquals(0.765625, result, 0.00001);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n + 1;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double result = cf.evaluate(1.0, Integer.MAX_VALUE);
        assertEquals(0.765625, result, 0.00001);
    }

    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n + 1;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double result = cf.evaluate(1.0, 0.00001, Integer.MAX_VALUE);
        assertEquals(0.765625, result, 0.00001);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };

        cf.evaluate(1.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n + 1;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        cf.evaluate(1.0, 1, 0);
    }

}