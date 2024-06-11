import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import static org.junit.Assert.*;

public class ContinuedFractionTest {

    @Test
    public void evaluate_defaultEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        double result = cf.evaluate(1.0);

        assertEquals(1.0, result, 0.00001);
    }

    @Test
    public void evaluate_customEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        double result = cf.evaluate(1.0, 0.0001);

        assertEquals(1.0, result, 0.00001);
    }

    @Test
    public void evaluate_customMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        double result = cf.evaluate(1.0, 10);

        assertEquals(1.0, result, 0.00001);
    }

    @Test(expected = ConvergenceException.class)
    public void evaluate_convergenceException() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 0.0;
            }
        };

        double result = cf.evaluate(1.0, 0.0001);
    }

    @Test(expected = MaxCountExceededException.class)
    public void evaluate_maxCountExceededException() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        double result = cf.evaluate(1.0, 1e-9, 10);
    }
}