import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import org.junit.Test;

public class FDistributionImplTest {

    private final double DEFAULT_EPSILON = 1e-15;

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        try {
            double result = fDistribution.cumulativeProbability(2.5);
            assertEquals(0.2943285539038937, result, DEFAULT_EPSILON);
        } catch (MathException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        try {
            double result = fDistribution.inverseCumulativeProbability(0.5);
            assertTrue(result >= 0);
        } catch (MathException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        fDistribution.setNumeratorDegreesOfFreedom(10);
        assertEquals(10, fDistribution.getNumeratorDegreesOfFreedom(), DEFAULT_EPSILON);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        fDistribution.setDenominatorDegreesOfFreedom(20);
        assertEquals(20, fDistribution.getDenominatorDegreesOfFreedom(), DEFAULT_EPSILON);
    }
}