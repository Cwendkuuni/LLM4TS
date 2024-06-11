import org.junit.Test;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

    @Test
    public void testDensity() {
        double result = normalDistribution.density(0);
        assertEquals(0.3989422804014327, result, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            double result = normalDistribution.cumulativeProbability(1);
            assertEquals(0.8413447460685429, result, 0.0001);
        } catch (MathException e) {
            fail("Unexpected MathException");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            double result = normalDistribution.inverseCumulativeProbability(0.5);
            assertEquals(0, result, 0.0001);
        } catch (MathException e) {
            fail("Unexpected MathException");
        }
    }

    @Test
    public void testSample() {
        try {
            double result = normalDistribution.sample();
            assertNotNull(result);
        } catch (MathException e) {
            fail("Unexpected MathException");
        }
    }
}