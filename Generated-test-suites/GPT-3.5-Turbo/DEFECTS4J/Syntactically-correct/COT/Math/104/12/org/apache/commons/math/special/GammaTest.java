
package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class GammaTest {
    
    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(0.5);
        assertEquals(-0.056243716497674235, result, 1e-15);
    }
    
    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(0.5, 1);
        assertEquals(0.31731050786291415, result, 1e-15);
    }
        
    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(0.5, 1);
        assertEquals(0.6826894921370859, result, 1e-15);
    }
    
    @Test
    public void testRegularizedGammaPWithEpsilon() throws MathException {
        double result = Gamma.regularizedGammaP(0.5, 1, 1e-6, Integer.MAX_VALUE);
        assertEquals(0.31731050786291415, result, 1e-15);
    }
    
    @Test
    public void testRegularizedGammaPWithMaxIterations() throws MathException {
        double result = Gamma.regularizedGammaP(0.5, 1, Gamma.DEFAULT_EPSILON, 100);
        assertEquals(0.31731050786291415, result, 1e-15);
    }
    
    @Test
    public void testRegularizedGammaQWithEpsilon() throws MathException {
        double result = Gamma.regularizedGammaQ(0.5, 1, 1e-6, Integer.MAX_VALUE);
        assertEquals(0.6826894921370859, result, 1e-15);
    }
    
    @Test
    public void testRegularizedGammaQWithMaxIterations() throws MathException {
        double result = Gamma.regularizedGammaQ(0.5, 1, Gamma.DEFAULT_EPSILON, 100);
        assertEquals(0.6826894921370859, result, 1e-15);
    }
    
}
