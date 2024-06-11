import org.junit.Test;

import static org.junit.Assert.*;

public class GammaTest {
    
    @Test
    public void testLogGamma() {
        double x = 1;
        double expected = 0;
        double result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.000001);
        
        x = 5;
        expected = 3.1780538303479458;
        result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.000001);
        
        x = 0;
        expected = Double.NaN;
        result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.000001);
    }
    
    @Test
    public void testRegularizedGammaP() throws MathException {
        double a = 2;
        double x = 1;
        double expected = 0.5939941502901619;
        double result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.000001);
        
        a = 5;
        x = 3;
        expected = 0.47096368440062814;
        result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.000001);
        
        a = 0;
        x = 1;
        expected = Double.NaN;
        result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.000001);
    }
    
    @Test
    public void testRegularizedGammaQ() throws MathException {
        double a = 2;
        double x = 1;
        double expected = 0.40600584970983814;
        double result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.000001);
        
        a = 5;
        x = 3;
        expected = 0.5290363155993719;
        result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.000001);
        
        a = 0;
        x = 1;
        expected = Double.NaN;
        result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.000001);
    }
}