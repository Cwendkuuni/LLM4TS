import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.Test;
import org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructor() {
        // Test with valid arguments
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 3.2);
        assertEquals(2.5, dist.getMean(), 0.01);
        assertEquals(3.2, dist.getStandardDeviation(), 0.01);

        // Test with default constructor
        NormalDistributionImpl defaultDist = new NormalDistributionImpl();
        assertEquals(0.0, defaultDist.getMean(), 0.01);
        assertEquals(1.0, defaultDist.getStandardDeviation(), 0.01);
    }
}