import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Typical Use Case
        double result1 = Gamma.logGamma(5);
        Assert.assertEquals(3.1780538303479453, result1, 0.000001);

        // Edge Cases
        double result2 = Gamma.logGamma(1);
        Assert.assertEquals(0.0, result2, 0.000001);

        double result3 = Gamma.logGamma(0.5);
        Assert.assertEquals(0.5723649429247001, result3, 0.000001);

        // Error Scenario
        double result4 = Gamma.logGamma(-2);
        Assert.assertTrue(Double.isNaN(result4));
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        // Typical Use Case
        double result1 = Gamma.regularizedGammaP(2, 3);
        Assert.assertEquals(0.5939941502901619, result1, 0.000001);

        // Edge Cases
        double result2 = Gamma.regularizedGammaP(1, 0);
        Assert.assertEquals(0.0, result2, 0.000001);

        double result3 = Gamma.regularizedGammaP(0.5, 1);
        Assert.assertEquals(0.6826894921370859, result3, 0.000001);

        // Error Scenario
        double result4 = Gamma.regularizedGammaP(-2, 4);
        Assert.assertTrue(Double.isNaN(result4));
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Typical Use Case
        double result1 = Gamma.regularizedGammaQ(2, 3);
        Assert.assertEquals(0.40600584970983814, result1, 0.000001);

        // Edge Cases
        double result2 = Gamma.regularizedGammaQ(1, 0);
        Assert.assertEquals(1.0, result2, 0.000001);

        double result3 = Gamma.regularizedGammaQ(0.5, 1);
        Assert.assertEquals(0.31731050786291415, result3, 0.000001);

        // Error Scenario
        double result4 = Gamma.regularizedGammaQ(-2, 4);
        Assert.assertTrue(Double.isNaN(result4));
    }

}