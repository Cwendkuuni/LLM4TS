import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        try {
            assertEquals(0.919, distribution.cumulativeProbability(2.5), 0.001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        try {
            assertEquals(1.216, distribution.inverseCumulativeProbability(0.9), 0.001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        try {
            assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.0);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        try {
            assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        distribution.setNumeratorDegreesOfFreedom(8.0);
        assertEquals(8.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        distribution.setDenominatorDegreesOfFreedom(15.0);
        assertEquals(15.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
}