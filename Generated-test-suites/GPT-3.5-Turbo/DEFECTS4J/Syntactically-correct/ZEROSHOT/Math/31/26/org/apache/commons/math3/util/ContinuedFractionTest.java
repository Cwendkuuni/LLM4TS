import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        double result = cf.getA(0, 2.0);
        assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        double result = cf.getB(0, 2.0);
        assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        double result = cf.evaluate(2.0);
        assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        double result = cf.evaluate(2.0, 0.1);
        assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        double result = cf.evaluate(2.0, 100);
        assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        double result = cf.evaluate(2.0, 0.1, 100);
        assertEquals(1.0, result, 0.0);
    }

}