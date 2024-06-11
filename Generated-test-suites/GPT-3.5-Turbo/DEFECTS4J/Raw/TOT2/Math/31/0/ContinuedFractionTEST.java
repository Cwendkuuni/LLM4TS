import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ContinuedFractionTest {

    @Test
    public void evaluateTest() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double result = cf.evaluate(2.0);
        assertEquals(1.5, result, 0.01);
    }

    @Test
    public void evaluateWithEpsilonTest() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double result = cf.evaluate(2.0, 0.01);
        assertEquals(1.5, result, 0.01);
    }

    @Test
    public void evaluateWithMaxIterationsTest() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        double result = cf.evaluate(2.0, 0.01, 10);
        assertEquals(1.5, result, 0.01);
    }

    @Test(expected = ConvergenceException.class)
    public void evaluateConvergenceExceptionTest() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };

        cf.evaluate(2.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void evaluateMaxCountExceededExceptionTest() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1;
            }
        };

        cf.evaluate(2.0, 0.01, 5);
    }
}