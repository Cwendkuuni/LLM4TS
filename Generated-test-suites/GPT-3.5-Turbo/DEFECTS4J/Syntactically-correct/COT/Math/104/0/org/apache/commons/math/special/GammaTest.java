import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class GammaTest {
    
    // Gamma class instance for testing
    private Gamma gamma;

    @Before
    public void setUp() {
        gamma = new Gamma();
    }

    @Test
    public void testLogGamma() {
        double x = 5.0;
        double expected = 3.1780538303479458;
        double result = gamma.logGamma(x);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double a = 2.0;
        double x = 1.0;
        double epsilon = 10e-9;
        int maxIterations = Integer.MAX_VALUE;
        double expected = 0.26424111765711534;
        double result = gamma.regularizedGammaP(a, x, epsilon, maxIterations);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double a = 2.0;
        double x = 1.0;
        double epsilon = 10e-9;
        int maxIterations = Integer.MAX_VALUE;
        double expected = 0.7357588823428847;
        double result = gamma.regularizedGammaQ(a, x, epsilon, maxIterations);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testRegularizedGammaQEdgeCase() throws MathException {
        double a = 0.5;
        double x = 3.0;
        double epsilon = 10e-9;
        int maxIterations = Integer.MAX_VALUE;
        double expected = 1.0;
        double result = gamma.regularizedGammaQ(a, x, epsilon, maxIterations);
        assertEquals(expected, result, 0.001);
    }

}