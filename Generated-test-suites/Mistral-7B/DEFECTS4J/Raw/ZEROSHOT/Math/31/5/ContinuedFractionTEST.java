import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ContinuedFractionTest {

    @Test
    public void testSqrtOfTwo() {
        ContinuedFraction sqrtOfTwo = new SqrtOfTwoContinuedFraction();
        double epsilon = 1e-10;
        double x = 2.0;
        double result = sqrtOfTwo.evaluate(x, epsilon);
        double expectedResult = 1.41421356237309552441;
        assertEquals(expectedResult, result, epsilon);
    }

    @Test(expected = ConvergenceException.class)
    public void testSqrtOfTwoThrowsConvergenceException() {
        ContinuedFraction sqrtOfTwo = new SqrtOfTwoContinuedFraction();
        double x = 1.5;
        sqrtOfTwo.evaluate(x, 1e-10, 10);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSqrtOfTwoThrowsMaxCountExceededException() {
        ContinuedFraction sqrtOfTwo = new SqrtOfTwoContinuedFraction();
        double x = 1.5;
        sqrtOfTwo.evaluate(x, 1e-10, 1);
    }

    @Test
    public void testGoldenRatio() {
        ContinuedFraction goldenRatio = new GoldenRatioContinuedFraction();
        double epsilon = 1e-10;
        double x = 0.0;
        double result = goldenRatio.evaluate(x, epsilon);
        double expectedResult = 1.61803398875;
        assertEquals(expectedResult, result, epsilon);
    }

    @Test(expected = ConvergenceException.class)
    public void testGoldenRatioThrowsConvergenceException() {
        ContinuedFraction goldenRatio = new GoldenRatioContinuedFraction();
        double x = -1.0;
        goldenRatio.evaluate(x, 1e-10, 10);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testGoldenRatioThrowsMaxCountExceededException() {
        ContinuedFraction goldenRatio = new GoldenRatioContinuedFraction();
        double x = 0.0;
        goldenRatio.evaluate(x, 1e-10, 1);
    }

    private static class SqrtOfTwoContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n % 2 == 0) {
                return 2.0;
            } else {
                return FastMath.sqrt(x);
            }
        }

        @Override
        protected double getB(int n, double x) {
            return 1.0;
        }
    }

    private static class GoldenRatioContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n % 2 == 0) {
                return (1.0 + FastMath.sqrt(5.0)) / 2.0;
            } else {
                return (1.0 - FastMath.sqrt(5.0)) / 2.0;
            }
        }

        @Override
        protected double getB(int n, double x) {
            return (n & 1) == 0 ? 1.0 : 0.0;
        }
    }

}