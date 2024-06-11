import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double x1 = -2.0;
        double x2 = 0.0;
        double x3 = 1.0;
        double x4 = 5.0;

        double expected1 = Double.NaN;
        double expected2 = Double.NaN;
        double expected3 = 0.0;
        double expected4 = 3.1780538303479458;

        double result1 = Gamma.logGamma(x1);
        double result2 = Gamma.logGamma(x2);
        double result3 = Gamma.logGamma(x3);
        double result4 = Gamma.logGamma(x4);

        assertEquals(expected1, result1, 0.0);
        assertEquals(expected2, result2, 0.0);
        assertEquals(expected3, result3, 0.0);
        assertEquals(expected4, result4, 0.0);
    }

    @Test
    public void testRegularizedGammaP() {
        double a1 = 0.0;
        double a2 = 1.0;
        double a3 = 2.0;

        double x1 = 0.0;
        double x2 = 1.0;
        double x3 = 5.0;

        double epsilon = 10e-9;
        int maxIterations = Integer.MAX_VALUE;

        double expected1 = Double.NaN;
        double expected2 = 1.0;
        double expected3 = 0.9595723180054877;

        double result1;
        double result2;
        double result3;

        try {
            result1 = Gamma.regularizedGammaP(a1, x1, epsilon, maxIterations);
            result2 = Gamma.regularizedGammaP(a2, x2, epsilon, maxIterations);
            result3 = Gamma.regularizedGammaP(a3, x3, epsilon, maxIterations);
        } catch (MathException e) {
            result1 = Double.NaN;
            result2 = Double.NaN;
            result3 = Double.NaN;
        }

        assertEquals(expected1, result1, 0.0);
        assertEquals(expected2, result2, 0.0);
        assertEquals(expected3, result3, 0.0);
    }

    @Test
    public void testRegularizedGammaQ() {
        double a1 = 0.0;
        double a2 = 1.0;
        double a3 = 2.0;

        double x1 = 0.0;
        double x2 = 1.0;
        double x3 = 5.0;

        double epsilon = 10e-9;
        int maxIterations = Integer.MAX_VALUE;

        double expected1 = Double.NaN;
        double expected2 = 0.0;
        double expected3 = 0.0404276819945123;

        double result1;
        double result2;
        double result3;

        try {
            result1 = Gamma.regularizedGammaQ(a1, x1, epsilon, maxIterations);
            result2 = Gamma.regularizedGammaQ(a2, x2, epsilon, maxIterations);
            result3 = Gamma.regularizedGammaQ(a3, x3, epsilon, maxIterations);
        } catch (MathException e) {
            result1 = Double.NaN;
            result2 = Double.NaN;
            result3 = Double.NaN;
        }

        assertEquals(expected1, result1, 0.0);
        assertEquals(expected2, result2, 0.0);
        assertEquals(expected3, result3, 0.0);
    }

}