import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(3, dist.getSampleSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativePopulationSize() {
        new HypergeometricDistribution(-10, 5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, -5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeSampleSize() {
        new HypergeometricDistribution(10, 5, -3);
    }
}