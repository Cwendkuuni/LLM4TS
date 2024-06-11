import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class GammaTest {
    
    @Test
    public void testLogGammaTypical() {
        double result = Gamma.logGamma(5.5);
        double expected = 3.1780538303479458;
        assertEquals(expected, result, 0.000001);
    }
    
    @Test
    public void testLogGammaZero() {
        double result = Gamma.logGamma(0);
        assertTrue(Double.isNaN(result));
    }
    
    @Test
    public void testLogGammaNegative() {
        double result = Gamma.logGamma(-10);
        assertTrue(Double.isNaN(result));
    }
    
    @Test
    public void testLogGammaNaN() {
        double result = Gamma.logGamma(Double.NaN);
        assertTrue(Double.isNaN(result));
    }
    
    @Test
    public void testRegularizedGammaPTypical() {
        try {
            double result = Gamma.regularizedGammaP(3, 5);
            double expected = 0.9974739831105132;
            assertEquals(expected, result, 0.000001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testRegularizedGammaPZero() {
        try {
            double result = Gamma.regularizedGammaP(0, 5);
            assertEquals(0.0, result, 0.000001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testRegularizedGammaPNegativeA() {
        try {
            double result = Gamma.regularizedGammaP(-5, 5);
            assertTrue(Double.isNaN(result));
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testRegularizedGammaPNegativeX() {
        try {
            double result = Gamma.regularizedGammaP(3, -5);
            assertTrue(Double.isNaN(result));
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testRegularizedGammaPNaN() {
        try {
            double result = Gamma.regularizedGammaP(Double.NaN, 5);
            assertTrue(Double.isNaN(result));
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testRegularizedGammaQTypical() {
        try {
            double result = Gamma.regularizedGammaQ(3, 5);
            double expected = 0.0025260168894868313;
            assertEquals(expected, result, 0.000001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testRegularizedGammaQZero() {
        try {
            double result = Gamma.regularizedGammaQ(0, 5);
            assertEquals(1.0, result, 0.000001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testRegularizedGammaQNegativeA() {
        try {
            double result = Gamma.regularizedGammaQ(-5, 5);
            assertTrue(Double.isNaN(result));
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testRegularizedGammaQNegativeX() {
        try {
            double result = Gamma.regularizedGammaQ(3, -5);
            assertTrue(Double.isNaN(result));
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testRegularizedGammaQNaN() {
        try {
            double result = Gamma.regularizedGammaQ(Double.NaN, 5);
            assertTrue(Double.isNaN(result));
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

}