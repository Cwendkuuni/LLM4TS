import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(3.0);
        assertEquals(0.6931471805599453, result, 0.0000000001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(2.0, 5.0);
        assertEquals(0.9932620530009145, result, 0.0000000001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(2.0, 5.0);
        assertEquals(0.006737946999085476, result, 0.0000000001);
    }

}