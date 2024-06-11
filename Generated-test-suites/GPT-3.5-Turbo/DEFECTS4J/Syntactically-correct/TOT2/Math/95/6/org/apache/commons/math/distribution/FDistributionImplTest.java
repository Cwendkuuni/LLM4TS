import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fdistribution = new FDistributionImpl(5.0, 10.0);
        assertEquals(0.0, fdistribution.cumulativeProbability(-1.0), 0.0001);
        assertEquals(0.5414, fdistribution.cumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fdistribution = new FDistributionImpl(5.0, 10.0);
        assertEquals(0.0, fdistribution.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, fdistribution.inverseCumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fdistribution = new FDistributionImpl(5.0, 10.0);
        fdistribution.setNumeratorDegreesOfFreedom(7.0);
        assertEquals(7.0, fdistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fdistribution = new FDistributionImpl(5.0, 10.0);
        fdistribution.setDenominatorDegreesOfFreedom(15.0);
        assertEquals(15.0, fdistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomException() {
        FDistributionImpl fdistribution = new FDistributionImpl(5.0, 10.0);
        fdistribution.setNumeratorDegreesOfFreedom(0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomException() {
        FDistributionImpl fdistribution = new FDistributionImpl(5.0, 10.0);
        fdistribution.setDenominatorDegreesOfFreedom(0.0);
    }

}