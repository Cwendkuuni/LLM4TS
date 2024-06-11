import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testEvaluateSquareRootOfTwo() {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        double result = cf.evaluate(2.0);
        double expected = 1.4142135623730955;
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testEvaluateSquareRootOfTwoWithEpsilon() {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        double result = cf.evaluate(2.0, 1e-6);
        double expected = 1.4142135623730955;
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testEvaluateSquareRootOfTwoWithMaxIterations() {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        double result = cf.evaluate(2.0, Integer.MAX_VALUE - 1);
        double expected = 1.4142135623730955;
        assertEquals(expected, result, 1e-8);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateSquareRootOfTwoWithMaxIterationsReached() {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        double result = cf.evaluate(2.0, Integer.MAX_VALUE);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateSine() {
        ContinuedFraction cf = new SineContinuedFraction();
        double result = cf.evaluate(1.0, 100);
    }

    @Test
    public void testEvaluateE() {
        ContinuedFraction cf = new EContinuedFraction();
        double result = cf.evaluate(0.0);
        double expected = 2.7182818284590452;
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testEvaluatePi() {
        ContinuedFraction cf = new PiContinuedFraction();
        double result = cf.evaluate(0.0);
        double expected = 3.1415926535897932;
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testEvaluateEpsilonTooSmall() {
        ContinuedFraction cf = new MyCustomContinuedFraction();
        assertThrows(IllegalArgumentException.class, () -> {
            cf.evaluate(0.0, 0.0);
        });
    }

    @Test
    public void testEvaluateMaxIterationsTooSmall() {
        ContinuedFraction cf = new MyCustomContinuedFraction();
        assertThrows(IllegalArgumentException.class, () -> {
            cf.evaluate(0.0, 0);
        });
    }

    private static class SquareRootContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return (n % 2 == 0) ? 1.0 / Math.sqrt(x) : -Math.sqrt(x);
        }

        @Override
        protected double getB(int n, double x) {
            return n % 2 == 0 ? 2.0 : 0;
        }
    }

    private static class SineContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return -(FastMath.sin((FastMath.PI * x) / 2.0) / FastMath.sin(FastMath.PI * x / 2.0));
        }

        @Override
        protected double getB(int n, double x) {
            return FastMath.cos(FastMath.PI * x / 2.0) / FastMath.sin(FastMath.PI * x / 2.0);
        }
    }

    private static class EContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return (n > 0) ? 2.0 : 0;
        }

        @Override
        protected double getB(int n, double x) {
            return n > 0 ? (n - 1) : 1;
        }
    }

    private static class PiContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return (n % 2 == 0) ? 1.0 / (2.0 * x) : 1.0 / (2.0 * x + 1);
        }

        @Override
        protected double getB(int n, double x) {
            return (n % 2 == 0) ? 1.0 : 2.0;
        }
    }

    private static class MyCustomContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected double getB(int n, double x) {
            throw new UnsupportedOperationException();
        }
    }

}