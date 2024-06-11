import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class GammaTest {
    
    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0);
        assertEquals(Double.NaN, Gamma.logGamma(-1), 0);
        assertEquals(0.6931471805599453, Gamma.logGamma(1), 0);
        assertEquals(0.6931471805599453, Gamma.logGamma(2), 0);
        assertEquals(1.791759469228055, Gamma.logGamma(3), 0);
        assertEquals(3.1780538303479458, Gamma.logGamma(5), 0);
        assertEquals(4.787491742782046, Gamma.logGamma(7), 0);
        assertEquals(6.579251212010101, Gamma.logGamma(10), 0);
    }
    
    @Test
    public void testRegularizedGammaP() {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 1), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1, Double.NaN), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1, 1), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1, -1), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0, 0), 0);
        assertEquals(0.3678794411714423, Gamma.regularizedGammaP(1, 1), 0);
        assertEquals(0.18126924692201818, Gamma.regularizedGammaP(2, 2), 0);
        assertEquals(0.6321205588285577, Gamma.regularizedGammaP(1, 3), 0);
        assertEquals(0.9661051464753107, Gamma.regularizedGammaP(2, 3), 0);
        assertEquals(0.6321205588285577, Gamma.regularizedGammaP(3, 3), 0);
    }
    
    @Test
    public void testRegularizedGammaQ() {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 1), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1, Double.NaN), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1, 1), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1, -1), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0, 0), 0);
        assertEquals(0.6321205588285577, Gamma.regularizedGammaQ(1, 1), 0);
        assertEquals(0.8187307530779818, Gamma.regularizedGammaQ(2, 2), 0);
        assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(1, 3), 0);
        assertEquals(0.032803973226814985, Gamma.regularizedGammaQ(2, 3), 0);
        assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(3, 3), 0);
    }

}