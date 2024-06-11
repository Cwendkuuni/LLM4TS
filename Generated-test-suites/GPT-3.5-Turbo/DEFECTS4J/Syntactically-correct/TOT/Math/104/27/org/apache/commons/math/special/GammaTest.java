import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.special.Gamma;

public class GammaTest {
    
    // Test logGamma method
    
    @Test
    public void testLogGamma_typicalCase() {
        double x = 5.0;
        double expected = 4.787491742782046;
        double actual = Gamma.logGamma(x);
        assertEquals(expected, actual, 0.000000001);
    }
    
    @Test
    public void testLogGamma_zero() {
        double x = 0.0;
        double expected = Double.NaN;
        double actual = Gamma.logGamma(x);
        assertEquals(expected, actual, 0.0);
    }
    
    @Test
    public void testLogGamma_negative() {
        double x = -3.0;
        double expected = Double.NaN;
        double actual = Gamma.logGamma(x);
        assertEquals(expected, actual, 0.0);
    }
    
    // Test regularizedGammaP method
    
    @Test
    public void testRegularizedGammaP_typicalCase() throws MathException {
        double a = 2.0;
        double x = 3.0;
        double epsilon = 10e-9;
        int maxIterations = Integer.MAX_VALUE;
        double expected = 0.32332358381693654;
        double actual = Gamma.regularizedGammaP(a, x, epsilon, maxIterations);
        assertEquals(expected, actual, 0.000000001);
    }
    
    @Test
    public void testRegularizedGammaP_zero() throws MathException {
        double a = 2.0;
        double x = 0.0;
        double epsilon = 10e-9;
        int maxIterations = Integer.MAX_VALUE;
        double expected = 0.0;
        double actual = Gamma.regularizedGammaP(a, x, epsilon, maxIterations);
        assertEquals(expected, actual, 0.0);
    }
    
    @Test
    public void testRegularizedGammaP_negativeA() throws MathException {
        double a = -1.0;
        double x = 3.0;
        double epsilon = 10e-9;
        int maxIterations = Integer.MAX_VALUE;
        double expected = Double.NaN;
        double actual = Gamma.regularizedGammaP(a, x, epsilon, maxIterations);
        assertEquals(expected, actual, 0.0);
    }
    
    // Test regularizedGammaQ method
    
    @Test
    public void testRegularizedGammaQ_typicalCase() throws MathException {
        double a = 2.0;
        double x = 3.0;
        double epsilon = 10e-9;
        int maxIterations = Integer.MAX_VALUE;
        double expected = 0.6766764161830635;
        double actual = Gamma.regularizedGammaQ(a, x, epsilon, maxIterations);
        assertEquals(expected, actual, 0.000000001);
    }
    
    @Test
    public void testRegularizedGammaQ_zero() throws MathException {
        double a = 2.0;
        double x = 0.0;
        double epsilon = 10e-9;
        int maxIterations = Integer.MAX_VALUE;
        double expected = 1.0;
        double actual = Gamma.regularizedGammaQ(a, x, epsilon, maxIterations);
        assertEquals(expected, actual, 0.0);
    }
    
    @Test
    public void testRegularizedGammaQ_negativeA() throws MathException {
        double a = -1.0;
        double x = 3.0;
        double epsilon = 10e-9;
        int maxIterations = Integer.MAX_VALUE;
        double expected = Double.NaN;
        double actual = Gamma.regularizedGammaQ(a, x, epsilon, maxIterations);
        assertEquals(expected, actual, 0.0);
    }
    
}