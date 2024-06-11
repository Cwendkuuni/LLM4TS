import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        try {
            assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.0001);
            assertEquals(0.0599994, fDistribution.cumulativeProbability(0.1), 0.0001);
            assertEquals(0.6663714, fDistribution.cumulativeProbability(1.0), 0.0001);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        try {
            assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 0.0001);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        try {
            fDistribution.setNumeratorDegreesOfFreedom(5.0);
            assertEquals(5.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        try {
            fDistribution.setDenominatorDegreesOfFreedom(4.0);
            assertEquals(4.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testExceptions() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        try {
            fDistribution.cumulativeProbability(-1.0);
            fail("Expected MathException");
        } catch (Exception e) {
            assertTrue(e instanceof MathException);
        }
    }
}