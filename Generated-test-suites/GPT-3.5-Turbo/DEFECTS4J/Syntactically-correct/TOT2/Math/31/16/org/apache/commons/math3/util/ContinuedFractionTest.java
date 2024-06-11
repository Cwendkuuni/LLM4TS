import static org.junit.Assert.*;
import org.junit.Test;

public class ContinuedFractionTest {
    @Test
    public void testGetA() {
        // Test case for positive index
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n + x;
            }

            @Override
            protected double getB(int n, double x) {
                return n - x;
            }
        };

        assertEquals(5.6, cf.getA(3, 2.6), 0.0);
    }

    @Test
    public void testGetB() {
        // Test case for negative index
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n + x;
            }

            @Override
            protected double getB(int n, double x) {
                return n - x;
            }
        };

        assertEquals(1.4, cf.getB(-2, 3.4), 0.0);
    }

    @Test
    public void testEvaluate() {
        // Test case for evaluating continued fraction with default epsilon
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n + x;
            }

            @Override
            protected double getB(int n, double x) {
                return n - x;
            }
        };

        assertEquals(9.0, cf.evaluate(2.0, 10e-9, 100), 0.0);
    }

    @Test
    public void testEvaluateWithCustomEpsilon() {
        // Test case for evaluating continued fraction with custom epsilon
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n + x;
            }

            @Override
            protected double getB(int n, double x) {
                return n - x;
            }
        };

        assertEquals(6.0, cf.evaluate(3.0, 0.1, 100), 0.0);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        // Test case for evaluating continued fraction with custom maxIterations
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n + x;
            }

            @Override
            protected double getB(int n, double x) {
                return n - x;
            }
        };

        assertEquals(2.0, cf.evaluate(1.0, 10e-9, 5), 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        // Test case for ConvergenceException
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n + x;
            }

            @Override
            protected double getB(int n, double x) {
                return n - x;
            }
        };

        cf.evaluate(0.0, 10e-9, 100);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
        // Test case for MaxCountExceededException
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n + x;
            }

            @Override
            protected double getB(int n, double x) {
                return n - x;
            }
        };

        cf.evaluate(10.0, 10e-9, 5);
    }
}