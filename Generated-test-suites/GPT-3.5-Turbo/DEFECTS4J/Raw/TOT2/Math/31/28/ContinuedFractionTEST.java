import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {
    
    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n * x;
            }

            @Override
            protected double getB(int n, double x) {
                return n * x + 1;
            }
        };

        assertEquals(0.0, cf.getA(0, 0.0), 0.0);
        assertEquals(3.0, cf.getA(1, 3.0), 0.0);
        assertEquals(-2.5, cf.getA(2, -5.0), 0.0);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n * x;
            }

            @Override
            protected double getB(int n, double x) {
                return n * x + 1;
            }
        };

        assertEquals(1.0, cf.getB(0, 1.0), 0.0);
        assertEquals(7.0, cf.getB(2, 3.0), 0.0);
        assertEquals(-10.5, cf.getB(3, -5.5), 0.0);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n * x;
            }

            @Override
            protected double getB(int n, double x) {
                return n * x + 1;
            }
        };

        assertEquals(0.5, cf.evaluate(0.5), 0.01);
        assertEquals(1.5, cf.evaluate(1.5), 0.01);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateException() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n * x;
            }

            @Override
            protected double getB(int n, double x) {
                return 0; // Causes division by zero
            }
        };

        cf.evaluate(2.0);
    }
}