import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        ContinuedFraction fraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 2.0;
            }
        };

        double a = fraction.getA(1, 2.0);
        assertEquals(1.0, a, 0.0);

        double b = fraction.getA(2, 3.0);
        assertEquals(1.0, b, 0.0);
    }

    @Test
    public void testGetB() {
        ContinuedFraction fraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 2.0;
            }
        };

        double a = fraction.getB(1, 2.0);
        assertEquals(2.0, a, 0.0);

        double b = fraction.getB(2, 3.0);
        assertEquals(2.0, b, 0.0);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction fraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 2.0;
            }
        };

        double result = fraction.evaluate(2.0);
        assertEquals(0.625, result, 0.0);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction fraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 2.0;
            }
        };

        double result = fraction.evaluate(2.0, 0.1);
        assertEquals(0.625, result, 0.0);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction fraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 2.0;
            }
        };

        double result = fraction.evaluate(2.0, 0.1, 10);
        assertEquals(0.625, result, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateInvalidConvergence() {
        ContinuedFraction fraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 0.0;
            }
        };

        fraction.evaluate(2.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterationsExceeded() {
        ContinuedFraction fraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 2.0;
            }
        };

        fraction.evaluate(2.0, 0.1, 1);
    }
}