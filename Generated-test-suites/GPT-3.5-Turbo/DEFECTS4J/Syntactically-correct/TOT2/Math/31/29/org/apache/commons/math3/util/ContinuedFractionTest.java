import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        // Test getting coefficient a for n = 0 and x = 1.0
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        assertEquals(2.0, cf.getA(0, 1.0), 0.0);
    }

    @Test
    public void testGetB() {
        // Test getting coefficient b for n = 1 and x = 2.0
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        assertEquals(1.0, cf.getB(1, 2.0), 0.0);
    }

    @Test
    public void testEvaluate() {
        // Test evaluating continued fraction with x = 2.0, default epsilon and max iterations
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        assertEquals(1.5, cf.evaluate(2.0), 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        // Test evaluating continued fraction with non-converging series
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        cf.evaluate(0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
        // Test evaluating continued fraction with max iterations exceeded
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 2.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        cf.evaluate(2.0, 1e-9, 5);
    }
}