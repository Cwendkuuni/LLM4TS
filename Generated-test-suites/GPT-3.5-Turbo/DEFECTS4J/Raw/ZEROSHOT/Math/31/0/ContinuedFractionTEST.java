import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        int n = 1;
        double x = 2.0;
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }
            protected double getB(int n, double x) {
                return n * x + 1;
            }
        };

        double expectedResult = 2.0;

        assertEquals(expectedResult, cf.getA(n, x), 0.0);
    }

    @Test
    public void testGetB() {
        int n = 1;
        double x = 2.0;
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }
            protected double getB(int n, double x) {
                return n * x + 1;
            }
        };

        double expectedResult = 3.0;

        assertEquals(expectedResult, cf.getB(n, x), 0.0);
    }

    @Test
    public void testEvaluate() {
        double x = 1.0;
        double epsilon = 0.0001;
        int maxIterations = 100;
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }
            protected double getB(int n, double x) {
                return n * x + 1;
            }
        };

        double expectedResult = 1.0;

        assertEquals(expectedResult, cf.evaluate(x), 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluate_ConvergenceException() {
        double x = 1.0;
        double epsilon = 0.0001;
        int maxIterations = 1;
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }
            protected double getB(int n, double x) {
                return n * x + 1;
            }
        };

        cf.evaluate(x, epsilon, maxIterations);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluate_MaxCountExceededException() {
        double x = 1.0;
        double epsilon = 0.0001;
        int maxIterations = 1;
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }
            protected double getB(int n, double x) {
                return n * x;
            }
        };

        cf.evaluate(x, epsilon, maxIterations);
    }
}