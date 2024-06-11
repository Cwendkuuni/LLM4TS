import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    private final double epsilon = 1e-9;

    @Test
    public void testLogGamma() {
        assertEquals(0.5723649429247001, Gamma.logGamma(1.5), epsilon);
        assertEquals(0.6931471805599453, Gamma.logGamma(2), epsilon);
        assertEquals(0.9189385332046727, Gamma.logGamma(3), epsilon);
        assertEquals(1.200973602347074, Gamma.logGamma(4), epsilon);
        assertEquals(7.974201799507307, Gamma.logGamma(10), epsilon);
        assertEquals(Double.NaN, Gamma.logGamma(0), epsilon);
        assertEquals(Double.NaN, Gamma.logGamma(-1), epsilon);
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), epsilon);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(9.999999979870096E-6, Gamma.regularizedGammaP(1.5, 1), epsilon);
        assertEquals(0, Gamma.regularizedGammaP(2, 0), epsilon);
        assertEquals(0.1812692487486306, Gamma.regularizedGammaP(3, 3), epsilon);
        assertEquals(0.9419616999930842, Gamma.regularizedGammaP(4, 5), epsilon);
        assertEquals(0.9999999999999999, Gamma.regularizedGammaP(10, 20), epsilon);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0, 1), epsilon);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1, 2), epsilon);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1, Double.NaN), epsilon);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(0.9999900000201299, Gamma.regularizedGammaQ(1.5, 1), epsilon);
        assertEquals(1, Gamma.regularizedGammaQ(2, 0), epsilon);
        assertEquals(0.8187307512513694, Gamma.regularizedGammaQ(3, 3), epsilon);
        assertEquals(0.05803830000691582, Gamma.regularizedGammaQ(4, 5), epsilon);
        assertEquals(1.1102230246251565E-16, Gamma.regularizedGammaQ(10, 20), epsilon);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0, 1), epsilon);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1, 2), epsilon);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1, Double.NaN), epsilon);
    }

}