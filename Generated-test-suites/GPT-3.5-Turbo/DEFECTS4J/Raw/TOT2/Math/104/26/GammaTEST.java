package org.apache.commons.math.special;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5);
        assertEquals(3.1780538303, result, 1e-10);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            double result = Gamma.regularizedGammaP(2, 3);
            assertTrue(result > 0);
        } catch (Exception e) {
            // Handle exception
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            double result = Gamma.regularizedGammaQ(2, 3);
            assertTrue(result > 0);
        } catch (Exception e) {
            // Handle exception
        }
    }
}