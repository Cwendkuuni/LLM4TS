import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {
    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0);
        assertEquals(-Infinity, Gamma.logGamma(-1), 0);
        assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(Double.POSITIVE_INFINITY), 0);
        assertEquals(3.1135153099374947, Gamma.logGamma(5), 0);
    }
    
    @Test
    public void testRegularizedGammaP() {
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 0), 0);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(0, Double.NaN), 0);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NEGATIVE_INFINITY, 0), 0);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(0, Double.NEGATIVE_INFINITY), 0);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(0, Double.POSITIVE_INFINITY), 0);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.POSITIVE_INFINITY, 0), 0);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), 0);
        } catch (MathException e) {
            //Expected exception
        }
        
        try {
            assertEquals(0, Gamma.regularizedGammaP(0, 0), 0);
            assertEquals(0.6321205588285577, Gamma.regularizedGammaP(1, 0), 0);
            assertEquals(0.9, Gamma.regularizedGammaP(1, 2), 0);
            assertEquals(0.9996281301280206, Gamma.regularizedGammaP(1, 10), 0);
            assertEquals(1, Gamma.regularizedGammaP(1, Double.POSITIVE_INFINITY), 0);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }
    
    @Test
    public void testRegularizedGammaQ() {
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 0), 0);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(0, Double.NaN), 0);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NEGATIVE_INFINITY, 0), 0);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(0, Double.NEGATIVE_INFINITY), 0);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(0, Double.POSITIVE_INFINITY), 0);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.POSITIVE_INFINITY, 0), 0);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), 0);
        } catch (MathException e) {
            //Expected exception
        }
        
        try {
            assertEquals(1, Gamma.regularizedGammaQ(0, 0), 0);
            assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(1, 0), 0);
            assertEquals(0.1, Gamma.regularizedGammaQ(1, 2), 0);
            assertEquals(0.00037186987197933345, Gamma.regularizedGammaQ(1, 10), 0);
            assertEquals(0, Gamma.regularizedGammaQ(1, Double.POSITIVE_INFINITY), 0);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }
}