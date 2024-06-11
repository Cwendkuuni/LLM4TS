import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContinuedFractionTest {

    private static final double EPSILON = 1e-9;

    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0;
            }
        };

        double result = cf.getA(1, 1.0);

        assertEquals(0.0, result, EPSILON);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0;
            }
        };

        double result = cf.getB(1, 1.0);

        assertEquals(0.0, result, EPSILON);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };

        double result = cf.evaluate(1.0);

        assertEquals(0.5, result, EPSILON);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                if (n == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };

        cf.evaluate(1.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1;
            }

            @Override
            protected double getB(int n, double x) {
                return 0;
            }
        };

        cf.evaluate(1.0, 10);
    }

}