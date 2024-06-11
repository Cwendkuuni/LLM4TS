package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class GammaTest {
  
    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(1);
        assertEquals(0.0, result, 0.0001);

        result = Gamma.logGamma(2);
        assertEquals(0.0, result, 0.0001);

        result = Gamma.logGamma(3);
        assertEquals(0.6931, result, 0.0001);

        result = Double.NaN;
        try {
            result = Gamma.logGamma(-1);
            fail("MathException should have been thrown");
        } catch (MathException e) {
            assertTrue(Double.isNaN(result));
        }
    }

    @Test
    public void testRegularizedGammaP() {
        double result = Gamma.regularizedGammaP(0, 0);
        assertEquals(0.0, result, 0.0001);

        result = Gamma.regularizedGammaP(1, 0);
        assertEquals(0.0, result, 0.0001);

        result = Gamma.regularizedGammaP(1, 1);
        assertEquals(0.6321, result, 0.0001);
    }

    @Test
    public void testRegularizedGammaQ() {
        double result = Gamma.regularizedGammaQ(0, 0);
        assertEquals(1.0, result, 0.0001);

        result = Gamma.regularizedGammaQ(1, 0);
        assertEquals(1.0, result, 0.0001);

        result = Gamma.regularizedGammaQ(1, 1);
        assertEquals(0.3679, result, 0.0001);
    }
    
}