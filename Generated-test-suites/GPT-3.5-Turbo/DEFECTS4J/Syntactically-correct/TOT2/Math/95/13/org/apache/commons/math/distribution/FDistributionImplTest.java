import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.FDistributionImpl;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        
        try {
            assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.0001);
            assertEquals(0.25, fDistribution.cumulativeProbability(1.0), 0.0001);
            assertEquals(1.0, fDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown in cumulativeProbability test");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        
        try {
            assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown in inverseCumulativeProbability test");
        }
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        
        try {
            fDistribution.setNumeratorDegreesOfFreedom(5);
            assertEquals(5, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
        } catch (IllegalArgumentException e) {
            fail("Exception thrown in setNumeratorDegreesOfFreedom test");
        }
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        
        try {
            fDistribution.setDenominatorDegreesOfFreedom(6);
            assertEquals(6, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
        } catch (IllegalArgumentException e) {
            fail("Exception thrown in setDenominatorDegreesOfFreedom test");
        }
    }
}