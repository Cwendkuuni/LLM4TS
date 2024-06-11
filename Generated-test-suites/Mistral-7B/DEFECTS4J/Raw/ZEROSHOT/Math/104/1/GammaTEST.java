import org.apache.commons.math3.SpecialFunctions; // Import the updated package name for JUnit 4.x
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GammaTest {

    @org.junit.Test
    public void testLogGamma() {
        // Testing logGamma method with valid inputs
        assertEquals(0.0, SpecialFunctions.logGamma(1.0), 1e-8);
        assertEquals(-Math.log(2.0), SpecialFunctions.logGamma(0.5), 1e-8);
        assertEquals(Math.log(120.0) + Math.log(Math.PI), SpecialFunctions.logGamma(3.5), 1e-8);

        // Testing logGamma method with invalid inputs
        assertEquals(Double.NaN, SpecialFunctions.logGamma(0.0), 1e-8);
        assertEquals(Double.NaN, SpecialFunctions.logGamma(-1.0), 1e-8);
        assertEquals(Double.NaN, SpecialFunctions.logGamma(Double.NaN), 1e-8);
    }

    @org.junit.Test
    public void testRegularizedGammaP() {
        // Testing regularizedGammaP method with valid inputs
        assertEquals(0.0, SpecialFunctions.regularizedGammaP(0.5, 0.5), 1e-8);
        assertEquals(1.0, SpecialFunctions.regularizedGammaP(1.0, 1.0), 1e-8);
        assertEquals(0.4227843350985505996, SpecialFunctions.regularizedGammaP(1.5, 2.0), 1e-8);

        // Testing regularizedGammaP method with invalid inputs
        assertEquals(Double.NaN, SpecialFunctions.regularizedGammaP(0.0, 0.5), 1e-8);
        assertEquals(Double.NaN, SpecialFunctions.regularizedGammaP(1.0, 0.5), 1e-8);
        assertEquals(Double.NaN, SpecialFunctions.regularizedGammaP(Double.NaN, 0.5), 1e-8);
        assertEquals(Double.NaN, SpecialFunctions.regularizedGammaP(1.5, -1.0), 1e-8);
    }

    @org.junit.Test
    public void testRegularizedGammaQ() {
        // Testing regularizedGammaQ method with valid inputs
        assertEquals(1.0, SpecialFunctions.regularizedGammaQ(0.5, 0.5), 1e-8);
        assertEquals(0.0, SpecialFunctions.regularizedGammaQ(1.0, 1.0), 1e-8);
        assertEquals(1.0 - 0.4227843350985505996, SpecialFunctions.regularizedGammaQ(1.5, 2.0), 1e-8);

        // Testing regularizedGammaQ method with invalid inputs
        assertEquals(Double.NaN, SpecialFunctions.regularizedGammaQ(0.0, 0.5), 1e-8);
        assertEquals(Double.NaN, SpecialFunctions.regularizedGammaQ(1.0, 0.5), 1e-8);
        assertEquals(Double.NaN, SpecialFunctions.regularizedGammaQ(Double.NaN, 0.5), 1e-8);
        assertEquals(Double.NaN, SpecialFunctions.regularizedGammaQ(1.5, -1.0), 1e-8);
    }
}