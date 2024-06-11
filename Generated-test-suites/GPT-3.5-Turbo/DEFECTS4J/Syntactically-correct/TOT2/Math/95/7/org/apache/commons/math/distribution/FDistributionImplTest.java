import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(4.0, 6.0);
        try {
            assertEquals(0.0, fd.cumulativeProbability(0.0), 0.0001);
            assertEquals(0.5328, fd.cumulativeProbability(2.0), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(4.0, 6.0);
        try {
            assertEquals(0.0, fd.inverseCumulativeProbability(0.0), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, fd.inverseCumulativeProbability(1.0), 0.0001);
            assertEquals(2.4577, fd.inverseCumulativeProbability(0.95), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(4.0, 6.0);
        fd.setNumeratorDegreesOfFreedom(10.0);
        assertEquals(10.0, fd.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(4.0, 6.0);
        fd.setDenominatorDegreesOfFreedom(8.0);
        assertEquals(8.0, fd.getDenominatorDegreesOfFreedom(), 0.0001);
    }

}