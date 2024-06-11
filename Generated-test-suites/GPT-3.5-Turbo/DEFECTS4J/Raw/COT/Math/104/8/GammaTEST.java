import org.apache.commons.math.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Base test case
        assertEquals(Math.log(Math.gamma(5)), Gamma.logGamma(5), 0.00001);
        
        // Edge cases
        assertEquals(Math.log(Math.gamma(0.5)), Gamma.logGamma(0.5), 0.00001);
        assertEquals(Math.log(Math.gamma(1)), Gamma.logGamma(1), 0.00001);
        
        // Exception handling
        assertEquals(Double.NaN, Gamma.logGamma(-1), 0.00001);
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.00001);
    }

    @Test
    public void testRegularizedGammaP() {
        // Base test case
        try {
            assertEquals(Math.gammaP(5, 2), Gamma.regularizedGammaP(5, 2), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }

        // Edge cases
        try {
            assertEquals(Math.gammaP(0.5, 2), Gamma.regularizedGammaP(0.5, 2), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(Math.gammaP(1, 2), Gamma.regularizedGammaP(1, 2), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
        
        // Exception handling
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaP(5, -1), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaP(-1, 2), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 2), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testRegularizedGammaQ() {
        // Base test case
        try {
            assertEquals(Math.gammaQ(5, 2), Gamma.regularizedGammaQ(5, 2), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }

        // Edge cases
        try {
            assertEquals(Math.gammaQ(0.5, 2), Gamma.regularizedGammaQ(0.5, 2), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(Math.gammaQ(1, 2), Gamma.regularizedGammaQ(1, 2), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
        
        // Exception handling
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(5, -1), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1, 2), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 2), 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
}