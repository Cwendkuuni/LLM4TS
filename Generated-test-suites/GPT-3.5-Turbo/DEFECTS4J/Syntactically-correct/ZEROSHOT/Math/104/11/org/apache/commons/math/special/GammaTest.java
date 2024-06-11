import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {
  
    @Test
    public void testLogGamma() {
        // Test NaN input
        assertTrue(Double.isNaN(Gamma.logGamma(Double.NaN)));

        // Test negative input
        assertTrue(Double.isNaN(Gamma.logGamma(-1.0)));

        // Test zero input
        assertTrue(Double.isInfinite(Gamma.logGamma(0.0)));

        // Test positive input
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-9);
        assertEquals(0.5723649429247001, Gamma.logGamma(2.0), 1e-9);
        assertEquals(1.791759469228055, Gamma.logGamma(3.0), 1e-9);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        // Test NaN input
        assertTrue(Double.isNaN(Gamma.regularizedGammaP(Double.NaN, 1.0)));

        // Test negative a
        assertTrue(Double.isNaN(Gamma.regularizedGammaP(-1.0, 1.0)));

        // Test zero a
        assertTrue(Double.isNaN(Gamma.regularizedGammaP(0.0, 1.0)));

        // Test negative x
        assertTrue(Double.isNaN(Gamma.regularizedGammaP(1.0, -1.0)));

        // Test x = 0
        assertEquals(0.0, Gamma.regularizedGammaP(1.0, 0.0), 1e-9);

        // Test a >= 1 && x > a
        assertEquals(0.36787944117144233, Gamma.regularizedGammaP(2.0, 3.0), 1e-9);
        assertEquals(0.1353352832366127, Gamma.regularizedGammaP(3.0, 4.0), 1e-9);

        // Test series
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 1.0), 1e-9);
        assertEquals(0.633973487642943, Gamma.regularizedGammaP(2.5, 2.0), 1e-9);
        assertEquals(0.006389950337843146, Gamma.regularizedGammaP(0.1, 0.5), 1e-9);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Test NaN input
        assertTrue(Double.isNaN(Gamma.regularizedGammaQ(Double.NaN, 1.0)));

        // Test negative a
        assertTrue(Double.isNaN(Gamma.regularizedGammaQ(-1.0, 1.0)));

        // Test zero a
        assertTrue(Double.isNaN(Gamma.regularizedGammaQ(0.0, 1.0)));

        // Test negative x
        assertTrue(Double.isNaN(Gamma.regularizedGammaQ(1.0, -1.0)));

        // Test x = 0
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 0.0), 1e-9);

        // Test a >= 1 && x < a
        assertEquals(0.6321205588285577, Gamma.regularizedGammaQ(2.0, 1.0), 1e-9);
        assertEquals(0.8646647167633873, Gamma.regularizedGammaQ(3.0, 2.0), 1e-9);
        
        // Test series
        assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 1.0), 1e-9);
        assertEquals(0.36602651235705703, Gamma.regularizedGammaQ(2.5, 2.0), 1e-9);
        assertEquals(0.9936100496621569, Gamma.regularizedGammaQ(0.1, 0.5), 1e-9);
    }
}