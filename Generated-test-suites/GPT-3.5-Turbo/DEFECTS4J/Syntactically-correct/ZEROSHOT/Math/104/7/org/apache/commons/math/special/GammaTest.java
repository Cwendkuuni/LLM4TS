import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(-Infinity, Gamma.logGamma(Double.NaN), 0.00001);
        assertEquals(-Infinity, Gamma.logGamma(0.0), 0.00001);
        assertEquals(Infinity, Gamma.logGamma(Double.POSITIVE_INFINITY), 0.00001);
        assertEquals(NaN, Gamma.logGamma(-1.0), 0.00001);
        assertEquals(InfInity, Gamma.logGamma(1.0), 0.00001);
        assertEquals(0.5723649429247001, Gamma.logGamma(1.5), 0.00001);
        assertEquals(40.19140644432541, Gamma.logGamma(10.0), 0.00001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(NaN, Gamma.regularizedGammaP(Double.NaN, 2.0), 0.00001);
        assertEquals(NaN, Gamma.regularizedGammaP(1.0, Double.NaN), 0.00001);
        assertEquals(NaN, Gamma.regularizedGammaP(-1.0, 2.0), 0.00001);
        assertEquals(NaN, Gamma.regularizedGammaP(1.0, -2.0), 0.00001);
        assertEquals(0.0, Gamma.regularizedGammaP(2.0, 0.0), 0.00001);
        assertEquals(NaN, Gamma.regularizedGammaP(2.0, 2.0), 0.00001);
        assertEquals(0.6321205588285577, Gamma.regularizedGammaP(2.0, 1.0), 0.00001);
        assertEquals(0.49936374664911656, Gamma.regularizedGammaP(2.0, 0.5), 0.00001);
        assertEquals(0.8646647167633873, Gamma.regularizedGammaP(3.0, 1.0), 0.00001);
        assertEquals(0.08326451676631978, Gamma.regularizedGammaP(3.0, 2.0), 0.00001);
        assertEquals(0.004678174210031616, Gamma.regularizedGammaP(3.0, 3.0), 0.00001);
        assertEquals(1.2706842445818083, Gamma.regularizedGammaP(3.0, 0.1), 0.00001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(NaN, Gamma.regularizedGammaQ(Double.NaN, 2.0), 0.00001);
        assertEquals(NaN, Gamma.regularizedGammaQ(1.0, Double.NaN), 0.00001);
        assertEquals(NaN, Gamma.regularizedGammaQ(-1.0, 2.0), 0.00001);
        assertEquals(NaN, Gamma.regularizedGammaQ(1.0, -2.0), 0.00001);
        assertEquals(1.0, Gamma.regularizedGammaQ(2.0, 0.0), 0.00001);
        assertEquals(NaN, Gamma.regularizedGammaQ(2.0, 2.0), 0.00001);
        assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(2.0, 1.0), 0.00001);
        assertEquals(0.5006362533508834, Gamma.regularizedGammaQ(2.0, 0.5), 0.00001);
        assertEquals(0.1353352832366127, Gamma.regularizedGammaQ(3.0, 1.0), 0.00001);
        assertEquals(0.9167354832336802, Gamma.regularizedGammaQ(3.0, 2.0), 0.00001);
        assertEquals(0.9953218257899684, Gamma.regularizedGammaQ(3.0, 3.0), 0.00001);
        assertEquals(0.7293157554181917, Gamma.regularizedGammaQ(3.0, 0.1), 0.00001);
    }
}