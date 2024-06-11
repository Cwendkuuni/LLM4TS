import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test for x = 0.0
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0);

        // Test for x < 0.0
        assertEquals(Double.NaN, Gamma.logGamma(-1.0), 0.0);

        // Test for x = 1.0
        assertEquals(0.0, Gamma.logGamma(1.0), 0.0);
        
        // Test for x = 0.5
        assertEquals(0.5723649429247001, Gamma.logGamma(0.5), 0.0);
        
        // Test for x = 10.0
        assertEquals(12.801827480081469, Gamma.logGamma(10.0), 0.0);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        // Test for a = 0.0 and x = 0.0
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 0.0), 0.0);

        // Test for a = 1.0 and x = 0.0
        assertEquals(0.0, Gamma.regularizedGammaP(1.0, 0.0), 0.0);

        // Test for a = 1.0 and x = 1.0
        assertEquals(0.6321205588285577, Gamma.regularizedGammaP(1.0, 1.0), 0.0);

        // Test for a = 2.0 and x = 12.0
        assertEquals(0.9977330728428655, Gamma.regularizedGammaP(2.0, 12.0), 0.0);

        // Test for a = 5.0 and x = 1.0
        assertEquals(0.0036667697039680437, Gamma.regularizedGammaP(5.0, 1.0), 0.0);
    }
    
    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Test for a = 0.0 and x = 0.0
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 0.0), 0.0);

        // Test for a = 1.0 and x = 0.0
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 0.0), 0.0);

        // Test for a = 1.0 and x = 1.0
        assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(1.0, 1.0), 0.0);

        // Test for a = 2.0 and x = 12.0
        assertEquals(0.002266927157134537, Gamma.regularizedGammaQ(2.0, 12.0), 0.0);

        // Test for a = 5.0 and x = 1.0
        assertEquals(0.9963332302960319, Gamma.regularizedGammaQ(5.0, 1.0), 0.0);
    }
}