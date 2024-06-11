import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double x = 2.0;
        double expected = 0.6931471805599453;
        double result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.000001);
        
        x = 3.0;
        expected = 0.6931471805599453;
        result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.000001);
        
        x = 4.0;
        expected = 1.791759469228055;
        result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaP() {
        double a = 2.0;
        double x = 3.0;
        double expected = 0.977578682222919;
        double result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.000001);
        
        a = 3.0;
        x = 4.0;
        expected = 0.8753478122044116;
        result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.000001);
        
        a = 4.0;
        x = 5.0;
        expected = 0.8852813724135885;
        result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaQ() {
        double a = 2.0;
        double x = 3.0;
        double expected = 0.02242131777708103;
        double result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.000001);
        
        a = 3.0;
        x = 4.0;
        expected = 0.1246521877955884;
        result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.000001);
        
        a = 4.0;
        x = 5.0;
        expected = 0.1147186275864115;
        result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.000001);
    }
}