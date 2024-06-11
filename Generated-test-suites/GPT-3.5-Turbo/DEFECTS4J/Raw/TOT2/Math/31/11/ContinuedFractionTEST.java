import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        // Test case for getting the coefficient a at index 0
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                if (n == 0) {
                    return 1.0;
                } else {
                    return 0.0;
                }
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };

        assertEquals(1.0, cf.getA(0, 1.0), 0.0001);
    }

    @Test
    public void testGetB() {
        // Test case for getting the coefficient b at index 1
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.0;
            }

            @Override
            protected double getB(int n, double x) {
                if (n == 1) {
                    return 2.0;
                } else {
                    return 0.0;
                }
            }
        };

        assertEquals(2.0, cf.getB(1, 1.0), 0.0001);
    }

    @Test
    public void testEvaluateDefaultEpsilon() {
        // Test case for evaluating the continued fraction with default epsilon
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        assertEquals(1.0, cf.evaluate(1.0), 0.0001);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        // Test case for evaluating the continued fraction with specified epsilon
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

        assertEquals(2.5, cf.evaluate(1.0, 0.1), 0.0001);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        // Test case for evaluating the continued fraction with specified max iterations
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        assertEquals(1.0, cf.evaluate(1.0, 0.0001, 10), 0.0001);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterationsExceeded() {
        // Test case for evaluating the continued fraction with max iterations exceeded
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };

        cf.evaluate(1.0, 0.0001, 5);
    }

}