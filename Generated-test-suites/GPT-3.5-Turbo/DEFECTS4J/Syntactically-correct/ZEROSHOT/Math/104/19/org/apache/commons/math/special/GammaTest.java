import org.junit.Test;

import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Math.log(Math.tgamma(1)), Gamma.logGamma(1), 0);
        assertEquals(Math.log(Math.tgamma(2)), Gamma.logGamma(2), 0);
        assertEquals(Math.log(Math.tgamma(5)), Gamma.logGamma(5), 0);
        assertEquals(Math.log(Math.tgamma(10)), Gamma.logGamma(10), 0);
        assertEquals(Math.log(Math.tgamma(100)), Gamma.logGamma(100), 0);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.0, Gamma.regularizedGammaP(1, 0), 0);
        assertEquals(1.0, Gamma.regularizedGammaP(1, 10), 0);
        assertEquals(0.6321205588285577, Gamma.regularizedGammaP(2, 1), 1e-15);
        assertEquals(0.9888910034617576, Gamma.regularizedGammaP(2, 10), 1e-15);
        assertEquals(0.9374098544646672, Gamma.regularizedGammaP(5, 1), 1e-15);
        assertEquals(1.0, Gamma.regularizedGammaP(5, 10), 0);
        assertEquals(0.999999999978263, Gamma.regularizedGammaP(10, 1), 1e-15);
        assertEquals(1.0, Gamma.regularizedGammaP(10, 10), 0);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0, Gamma.regularizedGammaQ(1, 0), 0);
        assertEquals(0.0, Gamma.regularizedGammaQ(1, 10), 0);
        assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(2, 1), 1e-15);
        assertEquals(0.01110899653824236, Gamma.regularizedGammaQ(2, 10), 1e-15);
        assertEquals(0.0625901455353328, Gamma.regularizedGammaQ(5, 1), 1e-15);
        assertEquals(0.0, Gamma.regularizedGammaQ(5, 10), 0);
        assertEquals(2.1736917142837806e-11, Gamma.regularizedGammaQ(10, 1), 1e-15);
        assertEquals(0.0, Gamma.regularizedGammaQ(10, 10), 0);
    }

}