import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    @Test
    public void testMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(10, distribution.getMean(), 0);
    }

    @Test
    public void testStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(5);
        assertEquals(5, distribution.getMean(), 0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3);
        assertEquals(3, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            double result = distribution.cumulativeProbability(1);
            assertEquals(0.841344, result, 0.000001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            double result = distribution.inverseCumulativeProbability(0.841344);
            assertEquals(1.0, result, 0.000001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            double result = distribution.inverseCumulativeProbability(0);
            assertEquals(Double.NEGATIVE_INFINITY, result, 0.000001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            double result = distribution.inverseCumulativeProbability(1);
            assertEquals(Double.POSITIVE_INFINITY, result, 0.000001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
}