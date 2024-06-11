package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0, Gamma.logGamma(0), 0.001);
        assertEquals(-0.17627433972573381, Gamma.logGamma(0.5), 0.001);
        assertEquals(0.4058292624563771, Gamma.logGamma(1), 0.001);
        assertEquals(1.0425093031526112, Gamma.logGamma(2), 0.001);
        assertEquals(1.3641915534225357, Gamma.logGamma(3), 0.001);
        assertEquals(1.9736755578149402, Gamma.logGamma(4), 0.001);
        assertEquals(2.890573902928852, Gamma.logGamma(5), 0.001);
        assertEquals(4.318837190864838, Gamma.logGamma(6), 0.001);
        assertEquals(5.964818326438105, Gamma.logGamma(7), 0.001);
        assertEquals(8.026213631198526, Gamma.logGamma(8), 0.001);
        assertEquals(10.280346451896325, Gamma.logGamma(9), 0.001);
        assertEquals(12.871748031722776, Gamma.logGamma(10), 0.001);

        assertNaN(Gamma.logGamma(-0.5), 0.001);
        assertNaN(Gamma.logGamma(0.5, Double.POSITIVE_INFINITY), 0.001);
        assertNaN(Gamma.logGamma(1.5, Double.POSITIVE_INFINITY), 0.001);
        assertNaN(Gamma.logGamma(-1), 0.001);
        assertNaN(Gamma.logGamma(0), 0.001);
        assertNaN(Gamma.logGamma(1, Double.NEGATIVE_INFINITY), 0.001);
        assertNaN(Gamma.logGamma(-2), 0.001);
        assertNaN(Gamma.logGamma(1, Double.NEGATIVE_INFINITY), 0.001);

        assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(Double.POSITIVE_INFINITY), 0.001);
        assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(Double.NEGATIVE_INFINITY), 0.001);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.5, Gamma.regularizedGammaP(0.5), 0.001);
        assertEquals(0.865065333112652, Gamma.regularizedGammaP(1.5), 0.001);
        assertEquals(1.212223315940315, Gamma.regularizedGammaP(2.5), 0.001);
        assertEquals(1.461961524834782, Gamma.regularizedGammaP(3.5), 0.001);
        assertEquals(1.701411885905579, Gamma.regularizedGammaP(4.5), 0.001);
        assertEquals(1.977465663337767, Gamma.regularizedGammaP(5.5), 0.001);
        assertEquals(2.271202782041695, Gamma.regularizedGammaP(6.5), 0.001);
        assertEquals(2.604860136846556, Gamma.regularizedGammaP(7.5), 0.001);
        assertEquals(3.015504661179068, Gamma.regularizedGammaP(8.5), 0.001);
        assertEquals(3.502763015796363, Gamma.regularizedGammaP(9.5), 0.001);
        assertEquals(4.132976971022783, Gamma.regularizedGammaP(10.5), 0.001);

        assertNaN(Gamma.regularizedGammaP(-0.5), 0.001);
        assertNaN(Gamma.regularizedGammaP(0.5, Double.POSITIVE_INFINITY), 0.001);
        assertNaN(Gamma.regularizedGammaP(1.5, Double.POSITIVE_INFINITY), 0.001);
        assertNaN(Gamma.regularizedGammaP(-1), 0.001);
        assertNaN(Gamma.regularizedGammaP(0), 0.001);
        assertNaN(Gamma.regularizedGammaP(1, Double.NEGATIVE_INFINITY), 0.001);
        assertNaN(Gamma.regularizedGammaP(-2), 0.001);
        assertNaN(Gamma.regularizedGammaP(1, Double.NEGATIVE_INFINITY), 0.001);

        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 1e-9, Integer.MAX_VALUE), 0.001);
        assertEquals(0.822672318462743, Gamma.regularizedGammaP(1.5, 1e-7, 1000), 0.001);
        assertEquals(1.202333334075671, Gamma.regularizedGammaP(2.5, 1e-6, 500), 0.001);
        assertEquals(1.453526580876362, Gamma.regularizedGammaP(3.5, 1e-5, 200), 0.001);
        assertEquals(1.700829244570725, Gamma.regularizedGammaP(4.5, 1e-4, 100), 0.001);
        assertEquals(1.969831497752121, Gamma.regularizedGammaP(5.5, 1e-3, 50), 0.001);
        assertEquals(2.269894528885757, Gamma.regularizedGammaP(6.5, 1e-2, 20), 0.001);
        assertEquals(2.602113066111514, Gamma.regularizedGammaP(7.5, 1e-1, 10), 0.001);
        assertEquals(3.008386352965495, Gamma.regularizedGammaP(8.5, 0.5, 5), 0.001);
        assertEquals(3.502763015796363, Gamma.regularizedGammaP(9.5, 0.1, 2), 0.001);
        assertEquals(4.132976971022783, Gamma.regularizedGammaP(10.5, 0.05, 1), 0.001);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(0.5, 1.0 - Gamma.regularizedGammaP(0.5), 0.001);
        assertEquals(0.134934666887347, 1.0 - Gamma.regularizedGammaP(1.5), 0.001);
        assertEquals(0.185734533792309, 1.0 - Gamma.regularizedGammaP(2.5), 0.001);
        assertEquals(0.221459623283319, 1.0 - Gamma.regularizedGammaP(3.5), 0.001);
        assertEquals(0.258382864431211, 1.0 - Gamma.regularizedGammaP(4.5), 0.001);
        assertEquals(0.293251364416023, 1.0 - Gamma.regularizedGammaP(5.5), 0.001);
        assertEquals(0.330521641513557, 1.0 - Gamma.regularizedGammaP(6.5), 0.001);
        assertEquals(0.371367377703042, 1.0 - Gamma.regularizedGammaP(7.5), 0.001);
        assertEquals(0.416024028113173, 1.0 - Gamma.regularizedGammaP(8.5), 0.001);
        assertEquals(0.464777093562266, 1.0 - Gamma.regularizedGammaP(9.5), 0.001);
        assertEquals(0.518757930537101, 1.0 - Gamma.regularizedGammaP(10.5), 0.001);

        assertEquals(0.5, 1.0 - Gamma.regularizedGammaP(0.5, 1e-9, Integer.MAX_VALUE), 0.001);
        assertEquals(0.467327681537257, 1.0 - Gamma.regularizedGammaP(1.5, 1e-7, 1000), 0.001);
        assertEquals(0.397666666782885, 1.0 - Gamma.regularizedGammaP(2.5, 1e-6, 500), 0.001);
        assertEquals(0.325633333317181, 1.0 - Gamma.regularizedGammaP(3.5, 1e-5, 200), 0.001);
        assertEquals(0.260205555568428, 1.0 - Gamma.regularizedGammaP(4.5, 1e-4, 100), 0.001);
        assertEquals(0.202111111111111, 1.0 - Gamma.regularizedGammaP(5.5, 1e-3, 50), 0.001);
        assertEquals(0.150520202020202, 1.0 - Gamma.regularizedGammaP(6.5, 1e-2, 20), 0.001);
        assertEquals(0.108282828282828, 1.0 - Gamma.regularizedGammaP(7.5, 1e-1, 10), 0.001);
        assertEquals(0.073878787878788, 1.0 - Gamma.regularizedGammaP(8.5, 0.5, 5), 0.001);
        assertEquals(0.0500000000000004, 1.0 - Gamma.regularizedGammaP(9.5, 0.1, 2), 0.001);
        assertEquals(0.036787878787879, 1.0 - Gamma.regularizedGammaP(10.5, 0.05, 1), 0.001);
    }
}