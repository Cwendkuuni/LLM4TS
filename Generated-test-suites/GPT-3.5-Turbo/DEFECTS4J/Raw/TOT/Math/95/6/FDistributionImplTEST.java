import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(3, 5);
    }

    @Test
    public void testCumulativeProbability_typicalCase() {
        try {
            double result = fDistribution.cumulativeProbability(1.0);
            Assert.assertEquals(0.419423339231, result, 0.0000001);
        } catch (MathException e) {
            Assert.fail();
        }
    }

    @Test
    public void testCumulativeProbability_edgeCase() {
        try {
            double result = fDistribution.cumulativeProbability(0.0);
            Assert.assertEquals(0.0, result, 0.0);
        } catch (MathException e) {
            Assert.fail();
        }
    }

    @Test
    public void testCumulativeProbability_errorCase() {
        try {
            double result = fDistribution.cumulativeProbability(-1.0);
            Assert.fail();
        } catch (MathException e) {
            Assert.assertEquals("x must be non-negative.", e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbability_typicalCase() {
        try {
            double result = fDistribution.inverseCumulativeProbability(0.5);
            Assert.assertEquals(0.667700449883, result, 0.0000001);
        } catch (MathException e) {
            Assert.fail();
        }
    }

    @Test
    public void testInverseCumulativeProbability_edgeCase() {
        try {
            double result = fDistribution.inverseCumulativeProbability(0.0);
            Assert.assertEquals(0.0, result, 0.0);
        } catch (MathException e) {
            Assert.fail();
        }
    }

    @Test
    public void testInverseCumulativeProbability_errorCase() {
        try {
            double result = fDistribution.inverseCumulativeProbability(1.1);
            Assert.fail();
        } catch (MathException e) {
            Assert.assertEquals("p must be between 0 and 1, inclusive.", e.getMessage());
        }
    }

    @Test
    public void testGetDomainLowerBound_typicalCase() {
        double result = fDistribution.getDomainLowerBound(0.5);
        Assert.assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testGetDomainUpperBound_typicalCase() {
        double result = fDistribution.getDomainUpperBound(0.5);
        Assert.assertEquals(Double.MAX_VALUE, result, 0.0);
    }

    @Test
    public void testGetInitialDomain_typicalCase() {
        double result = fDistribution.getInitialDomain(0.5);
        Assert.assertEquals(0.625, result, 0.0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom_typicalCase() {
        fDistribution.setNumeratorDegreesOfFreedom(4);
        Assert.assertEquals(4, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom_errorCase() {
        try {
            fDistribution.setNumeratorDegreesOfFreedom(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("degrees of freedom must be positive.", e.getMessage());
        }
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom_typicalCase() {
        fDistribution.setDenominatorDegreesOfFreedom(6);
        Assert.assertEquals(6, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom_errorCase() {
        try {
            fDistribution.setDenominatorDegreesOfFreedom(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("degrees of freedom must be positive.", e.getMessage());
        }
    }
}