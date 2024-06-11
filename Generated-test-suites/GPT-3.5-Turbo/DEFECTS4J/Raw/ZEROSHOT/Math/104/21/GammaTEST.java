import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double x = 1.0;
        double expected = 0.0;
        double result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.0001);
        
        x = 0.5;
        expected = 0.5723649429247001;
        result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.0001);
        
        x = 5.0;
        expected = 3.1780538303479458;
        result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testRegularizedGammaP() {
        double a = 1.0;
        double x = 1.0;
        double expected = 0.6321205588285577;
        double result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.0001);
        
        a = 2.0;
        x = 2.0;
        expected = 0.5939941502901617;
        result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.0001);
        
        a = 5.0;
        x = 3.0;
        expected = 0.5506702987107675;
        result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testRegularizedGammaQ() {
        double a = 1.0;
        double x = 1.0;
        double expected = 0.36787944117144233;
        double result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.0001);
        
        a = 2.0;
        x = 2.0;
        expected = 0.40600584970983835;
        result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.0001);
        
        a = 5.0;
        x = 3.0;
        expected = 0.4493297012892325;
        result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.0001);
    }
}