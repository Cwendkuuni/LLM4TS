import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.0);
        assertEquals(3.1780538303479453, result, 0.00001);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            double result = Gamma.regularizedGammaP(2.0, 1.0);
            assertEquals(0.5939941502901614, result, 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            double result = Gamma.regularizedGammaQ(2.0, 1.0);
            assertEquals(0.4060058497098386, result, 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegularizedGammaP_WithInvalidValues() {
        try {
            double result = Gamma.regularizedGammaP(0.0, 1.0);
            assertEquals(Double.NaN, result, 0);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegularizedGammaQ_WithInvalidValues() {
        try {
            double result = Gamma.regularizedGammaQ(-1.0, 2.0);
            assertEquals(Double.NaN, result, 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
}