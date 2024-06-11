import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.special.Gamma;

public class GammaTest {
    
    // Test logGamma method
    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0001);
        assertEquals(0.5723649429247001, Gamma.logGamma(0.5), 0.0001);
        assertEquals(-0.1207822376352452, Gamma.logGamma(1.0), 0.0001);
        assertEquals(-0.2966775535406001, Gamma.logGamma(1.5), 0.0001);
        assertEquals(0.6931471805599453, Gamma.logGamma(2.0), 0.0001);
        assertEquals(0.9513507698668732, Gamma.logGamma(2.5), 0.0001);
    }
    
    // Test regularizedGammaP method
    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(0.756255922105651, Gamma.regularizedGammaP(1.0, 0.5), 0.0001);
        assertEquals(0.7275430046222193, Gamma.regularizedGammaP(1.5, 1.0), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaP(2.0, 0.0), 0.0001);
        assertEquals(0.3934693402873666, Gamma.regularizedGammaP(2.5, 2.0), 0.0001);
        assertEquals(0.9513507698668732, Gamma.regularizedGammaP(2.5, Double.POSITIVE_INFINITY), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 1.0), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 1.0), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, Double.NaN), 0.0001);
    }
    
    // Test regularizedGammaQ method
    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(0.24374407789434903, Gamma.regularizedGammaQ(1.0, 0.5), 0.0001);
        assertEquals(0.2724569953777807, Gamma.regularizedGammaQ(1.5, 1.0), 0.0001);
        assertEquals(2.220446049250313E-16, Gamma.regularizedGammaQ(2.0, 0.0), 0.0001);
        assertEquals(0.6065306597126334, Gamma.regularizedGammaQ(2.5, 2.0), 0.0001);
        assertEquals(0.04864923013312685, Gamma.regularizedGammaQ(2.5, Double.POSITIVE_INFINITY), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 1.0), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 1.0), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, Double.NaN), 0.0001);
    }

}