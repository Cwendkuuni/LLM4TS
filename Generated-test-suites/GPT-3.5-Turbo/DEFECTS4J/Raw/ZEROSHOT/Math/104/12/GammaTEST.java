import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double x = 1.5;
        double expected = 0.5723649429247001;
        double actual = Gamma.logGamma(x);
        assertEquals(expected, actual, 0.0000001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double a = 2.0;
        double x = 3.0;
        double epsilon = 10e-9;
        int maxIterations = Integer.MAX_VALUE;
        double expected = 0.8586956521739131;
        double actual = Gamma.regularizedGammaP(a, x, epsilon, maxIterations);
        assertEquals(expected, actual, 0.0000001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double a = 2.0;
        double x = 3.0;
        double epsilon = 10e-9;
        int maxIterations = Integer.MAX_VALUE;
        double expected = 0.1413043478260869;
        double actual = Gamma.regularizedGammaQ(a, x, epsilon, maxIterations);
        assertEquals(expected, actual, 0.0000001);
    }
}