import org.junit.Test;

import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double epsilon = 10e-9;
        
        // Test for x = NaN
        double result1 = Gamma.logGamma(Double.NaN);
        assertTrue(Double.isNaN(result1));
        
        // Test for x <= 0
        double result2 = Gamma.logGamma(0);
        assertTrue(Double.isNaN(result2));
        
        // Test for x > 0
        double result3 = Gamma.logGamma(1);
        assertEquals(0.0, result3, epsilon);
        
        // Test for x > 0
        double result4 = Gamma.logGamma(2);
        assertEquals(0.6931471805599453, result4, epsilon);
        
        // Test for x > 0
        double result5 = Gamma.logGamma(3);
        assertEquals(1.791759469228055, result5, epsilon);
    }

    @Test
    public void testRegularizedGammaP() {
        double epsilon = 10e-9;
        
        // Test for a = NaN
        try {
            double result1 = Gamma.regularizedGammaP(Double.NaN, 1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Exception expected
        }
        
        // Test for x = NaN
        try {
            double result2 = Gamma.regularizedGammaP(1, Double.NaN);
            fail("Expected MathException");
        } catch (MathException e) {
            // Exception expected
        }
        
        // Test for a <= 0
        try {
            double result3 = Gamma.regularizedGammaP(0, 1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Exception expected
        }
        
        // Test for x < 0
        try {
            double result4 = Gamma.regularizedGammaP(1, -1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Exception expected
        }
        
        // Test for a = 1, x = 0
        double result5 = Gamma.regularizedGammaP(1, 0);
        assertEquals(0.0, result5, epsilon);
        
        // Test for a = 1, x > 0
        double result6 = Gamma.regularizedGammaP(1, 1);
        assertEquals(0.6321205588285577, result6, epsilon);
        
        // Test for a > 1, x > a
        double result7 = Gamma.regularizedGammaP(2, 3);
        assertEquals(0.8008517265285441, result7, epsilon);
    }

    @Test
    public void testRegularizedGammaQ() {
        double epsilon = 10e-9;
        
        // Test for a = NaN
        try {
            double result1 = Gamma.regularizedGammaQ(Double.NaN, 1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Exception expected
        }
        
        // Test for x = NaN
        try {
            double result2 = Gamma.regularizedGammaQ(1, Double.NaN);
            fail("Expected MathException");
        } catch (MathException e) {
            // Exception expected
        }
        
        // Test for a <= 0
        try {
            double result3 = Gamma.regularizedGammaQ(0, 1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Exception expected
        }
        
        // Test for x < 0
        try {
            double result4 = Gamma.regularizedGammaQ(1, -1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Exception expected
        }
        
        // Test for a = 1, x = 0
        double result5 = Gamma.regularizedGammaQ(1, 0);
        assertEquals(1.0, result5, epsilon);
        
        // Test for a = 1, x > 0
        double result6 = Gamma.regularizedGammaQ(1, 1);
        assertEquals(0.36787944117144233, result6, epsilon);
        
        // Test for a > 1, x > a
        double result7 = Gamma.regularizedGammaQ(2, 3);
        assertEquals(0.19914827347145605, result7, epsilon);
    }
}