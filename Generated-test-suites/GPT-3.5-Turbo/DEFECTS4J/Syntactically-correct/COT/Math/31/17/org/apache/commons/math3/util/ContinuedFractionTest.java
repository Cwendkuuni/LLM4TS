
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        assertEquals(1.0, continuedFraction.getA(0, 1.0), 0.0);
    }

    @Test
    public void testGetB() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        assertEquals(1.0, continuedFraction.getB(0, 1.0), 0.0);
    }

    @Test
    public void testEvaluateWithDefaultEpsilonAndMaxIterations() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        double result = continuedFraction.evaluate(1.0);

        assertEquals(1.618033988749895, result, 1e-9);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        double result = continuedFraction.evaluate(1.0, 1e-6);

        assertEquals(1.618033988749895, result, 1e-6);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        double result = continuedFraction.evaluate(1.0, 1e-9, 100);

        assertEquals(1.618033988749895, result, 1e-9);
    }
}
