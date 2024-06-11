import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.MathException;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    private NormalDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl(0.0, 1.0);
    }

    @Test
    public void testGetMean() {
        double mean = distribution.getMean();
        assertEquals(0.0, mean, 0.0);
    }

    @Test
    public void testSetMean() {
        distribution.setMean(1.0);
        double mean = distribution.getMean();
        assertEquals(1.0, mean, 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        double sd = distribution.getStandardDeviation();
        assertEquals(1.0, sd, 0.0);
    }

    @Test
    public void testSetStandardDeviation() {
        distribution.setStandardDeviation(2.0);
        double sd = distribution.getStandardDeviation();
        assertEquals(2.0, sd, 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double probability = distribution.cumulativeProbability(0.0);
        assertEquals(0.5, probability, 0.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double x = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0.0, x, 0.0);
    }
}