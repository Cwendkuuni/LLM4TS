package org.apache.commons.math.distribution;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructor() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5.0, 3.0);
        assertEquals(5.0, normalDistribution.getMean(), 0.01);
        assertEquals(3.0, normalDistribution.getStandardDeviation(), 0.01);
    }
}