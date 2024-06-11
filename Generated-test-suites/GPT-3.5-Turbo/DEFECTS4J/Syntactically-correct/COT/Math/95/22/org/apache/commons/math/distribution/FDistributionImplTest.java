
import org.apache.commons.math.distribution.FDistributionImpl;
import org.apache.commons.math.MathException;
import org.junit.Test;

import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 6);
        try {
            assertEquals(0.274068619, fDistribution.cumulativeProbability(1.5), 1e-9);
        } catch (MathException e) {
            fail("An exception occurred");
        }
    }

    @Test
    public void testCumulativeProbabilityEdgeCase() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 6);
        try {
            assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 1e-9);
        } catch (MathException e) {
            fail("An exception occurred");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 6);
        try {
            assertEquals(0.992653899, fDistribution.inverseCumulativeProbability(0.9), 1e-9);
        } catch (MathException e) {
            fail("An exception occurred");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityEdgeCase() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 6);
        try {
            assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 1e-9);
        } catch (MathException e) {
            fail("An exception occurred");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 6);
        fDistribution.setNumeratorDegreesOfFreedom(0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 6);
        assertEquals(3, fDistribution.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 6);
        fDistribution.setDenominatorDegreesOfFreedom(0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 6);
        assertEquals(6, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }

}
