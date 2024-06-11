
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        // Test case 1: x = 0
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        double result = fd.cumulativeProbability(0);
        Assert.assertEquals(0, result, 0);

        // Test case 2: x = 5
        result = fd.cumulativeProbability(5);
        Assert.assertEquals(0.082, result, 0.001);

        // Test case 3: x = -5
        result = fd.cumulativeProbability(-5);
        Assert.assertEquals(0, result, 0);

        // Test case 4: x = Infinity
        result = fd.cumulativeProbability(Double.POSITIVE_INFINITY);
        Assert.assertEquals(1, result, 0);
    }

    @Test
    public void testInverseCumulativeProbability() {
        // Test case 1: p = 0
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        double result = fd.inverseCumulativeProbability(0);
        Assert.assertEquals(0, result, 0);

        // Test case 2: p = 1
        result = fd.inverseCumulativeProbability(1);
        Assert.assertEquals(Double.POSITIVE_INFINITY, result, 0);

        // Test case 3: p = 0.5
        result = fd.inverseCumulativeProbability(0.5);
        Assert.assertEquals(0.778, result, 0.001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);

        // Test case 1: p = 0
        double result = fd.getDomainLowerBound(0);
        Assert.assertEquals(0, result, 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);

        // Test case 1: p = 0
        double result = fd.getDomainUpperBound(0);
        Assert.assertEquals(Double.MAX_VALUE, result, 0);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);

        // Test case 1: p = 0
        double result = fd.getInitialDomain(0);
        Assert.assertEquals(1.25, result, 0);
    }
}
