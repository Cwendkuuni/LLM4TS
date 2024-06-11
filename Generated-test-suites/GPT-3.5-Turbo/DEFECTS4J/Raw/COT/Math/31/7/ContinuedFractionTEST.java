
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import static org.junit.Assert.*;
import org.junit.*;

public class ContinuedFractionTest {

    private ContinuedFraction cf;

    @Before
    public void setUp() {
        cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }
            protected double getB(int n, double x) {
                return n + x;
            }
        };
    }

    @Test
    public void testEvaluate() {
        double result = cf.evaluate(2.0);
        assertEquals(20.0, result, 0.001);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        double result = cf.evaluate(2.0, 0.001);
        assertEquals(20.0, result, 0.001);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        double result = cf.evaluate(2.0, Integer.MAX_VALUE);
        assertEquals(20.0, result, 0.001);
    }

    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        double result = cf.evaluate(2.0, 0.001, Integer.MAX_VALUE);
        assertEquals(20.0, result, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithNonConvergingSequence() {
        cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        cf.evaluate(2.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithInfinityDivergence() {
        cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }
            protected double getB(int n, double x) {
                return 0.0;
            }
        };
        cf.evaluate(2.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithNaNDivergence() {
        cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return Double.NaN;
            }
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        cf.evaluate(2.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithMaxIterationsExceeded() {
        cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        cf.evaluate(2.0, 10);
    }
}

