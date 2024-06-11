import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n + x;
            }

            protected double getB(int n, double x) {
                return n - x;
            }
        };

        assertEquals(5.0, cf.getA(2, 3.0), 0.0001);
        assertEquals(2.0, cf.getA(1, 1.0), 0.0001);
        assertEquals(4.0, cf.getA(3, 2.0), 0.0001);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n + x;
            }

            protected double getB(int n, double x) {
                return n - x;
            }
        };

        assertEquals(1.0, cf.getB(2, 3.0), 0.0001);
        assertEquals(-1.0, cf.getB(1, 1.0), 0.0001);
        assertEquals(-1.0, cf.getB(3, 2.0), 0.0001);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n + x;
            }

            protected double getB(int n, double x) {
                return n - x;
            }
        };

        assertEquals(2.3, cf.evaluate(1.0), 0.0001);
        assertEquals(7.5, cf.evaluate(2.0), 0.0001);
        assertEquals(22.1, cf.evaluate(3.0), 0.0001);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testEvaluateMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n + x;
            }

            protected double getB(int n, double x) {
                return n - x;
            }
        };

        cf.evaluate(1.0, 10e-9, 2);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfiniteConvergence() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n + x;
            }

            protected double getB(int n, double x) {
                return n - x;
            }
        };

        cf.evaluate(1.0, 10e-9, 500);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testEvaluateNaNConvergence() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return Double.NaN;
            }

            protected double getB(int n, double x) {
                return Double.NaN;
            }
        };

        cf.evaluate(1.0, 10e-9, 500);
    }

}