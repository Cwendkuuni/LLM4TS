import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.0);
        assertEquals(3.1780538303479453, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(2.0, 3.0);
        assertEquals(0.3233235838169366, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(2.0, 3.0);
        assertEquals(0.6766764161830634, result, 0.0000001);
    }
}