import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    // Method: getA()
    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }
            protected double getB(int n, double x) {
                return n;
            }
        };
        assertEquals(0.0, cf.getA(0, 0.0), 0);
        assertEquals(1.0, cf.getA(1, 0.0), 0);
    }

    // Method: getB()
    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }
            protected double getB(int n, double x) {
                return n;
            }
        };
        assertEquals(0.0, cf.getB(0, 0.0), 0);
        assertEquals(1.0, cf.getB(1, 0.0), 0);
    }

    // Method: evaluate(double)
    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }
            protected double getB(int n, double x) {
                return n;
            }
        };
        assertEquals(1.0, cf.evaluate(1.0), 0);
        assertEquals(1.0, cf.evaluate(1.0, 0.01), 0);
        assertEquals(1.0, cf.evaluate(1.0, 100), 0);
    }

    // Method: evaluate(double, double)
    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }
            protected double getB(int n, double x) {
                return n;
            }
        };
        assertEquals(1.0, cf.evaluate(1.0, 0.01), 0);
    }

    // Method: evaluate(double, int)
    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }
            protected double getB(int n, double x) {
                return n;
            }
        };
        assertEquals(1.0, cf.evaluate(1.0, 100), 0);
    }

    // Method: evaluate(double, double, int)
    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }
            protected double getB(int n, double x) {
                return n;
            }
        };
        assertEquals(1.0, cf.evaluate(1.0, 0.01, 100), 0);
    }

    // Method: evaluate(double, double, int) - ConvergenceException
    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithEpsilonAndMaxIterationsConvergenceException() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }
            protected double getB(int n, double x) {
                return n;
            }
        };
        // Pass invalid values to trigger ConvergenceException
        cf.evaluate(1.0, 0.0, 1);
    }

    // Method: evaluate(double, double, int) - MaxCountExceededException
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithEpsilonAndMaxIterationsMaxCountExceededException() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }
            protected double getB(int n, double x) {
                return n;
            }
        };
        // Pass invalid values to trigger MaxCountExceededException
        cf.evaluate(1.0, 0.01, 1);
    }

}