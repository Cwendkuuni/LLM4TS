import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 2.0 * n;
            }
            protected double getB(int n, double x) {
                return 1.0 * n;
            }
        };

        double result = cf.getA(5, 1.5);
        assertEquals(10.0, result, 0.0001);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 2.0 * n;
            }
            protected double getB(int n, double x) {
                return 1.0 * n;
            }
        };

        double result = cf.getB(5, 1.5);
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 2.0 * n;
            }
            protected double getB(int n, double x) {
                return 1.0 * n;
            }
        };

        double result = cf.evaluate(1.5);
        assertEquals(1.5, result, 0.0001);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 2.0 * n;
            }
            protected double getB(int n, double x) {
                return 1.0 * n;
            }
        };

        double result = cf.evaluate(1.5, 0.001);
        assertEquals(1.5, result, 0.001);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 2.0 * n;
            }
            protected double getB(int n, double x) {
                return 1.0 * n;
            }
        };

        double result = cf.evaluate(1.5, 1000);
        assertEquals(1.5, result, 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateFailToConverge() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 2.0 * n;
            }
            protected double getB(int n, double x) {
                return 1.0 * n;
            }
        };

        double result = cf.evaluate(1.5, 0.0001, 10);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterationsExceeded() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 2.0 * n;
            }
            protected double getB(int n, double x) {
                return 1.0 * n;
            }
        };

        double result = cf.evaluate(1.5, 0.0001, 100);
    }

}