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
                return 2 * n;
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };

        assertEquals(0.0, cf.getA(0, 0.0), 0.0);
        assertEquals(2.0, cf.getA(1, 0.0), 0.0);
        assertEquals(4.0, cf.getA(2, 0.0), 0.0);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n;
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };

        assertEquals(0.0, cf.getB(0, 0.0), 0.0);
        assertEquals(1.0, cf.getB(1, 0.0), 0.0);
        assertEquals(2.0, cf.getB(2, 0.0), 0.0);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n;
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };

        assertEquals(1.5, cf.evaluate(0.0), 0.0);
        assertEquals(1.8333333333333333, cf.evaluate(1.0), 1e-9);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithConvergenceException() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n;
            }

            @Override
            protected double getB(int n, double x) {
                return 1;
            }
        };

        cf.evaluate(0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithMaxCountExceededException() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2 * n;
            }

            @Override
            protected double getB(int n, double x) {
                return n;
            }
        };

        cf.evaluate(0.0, 1e-9, 10);
    }
}