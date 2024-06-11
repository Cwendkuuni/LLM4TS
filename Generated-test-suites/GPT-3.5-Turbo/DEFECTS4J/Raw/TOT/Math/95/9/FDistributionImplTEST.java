import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        // Create an instance of FDistributionImpl for testing
        fDistribution = new FDistributionImpl(3, 4);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        // Typical use case: x = 2.0
        double x1 = 2.0;
        double expected1 = Beta.regularizedBeta((3 * x1) / (4 + 3 * x1), 0.5 * 3, 0.5 * 4);
        double actual1 = fDistribution.cumulativeProbability(x1);
        Assert.assertEquals(expected1, actual1, 1e-5);

        // Edge case: x = 0.0
        double x2 = 0.0;
        double expected2 = 0.0;
        double actual2 = fDistribution.cumulativeProbability(x2);
        Assert.assertEquals(expected2, actual2, 1e-5);

        // Error scenario: x = -1.0
        double x3 = -1.0;
        try {
            fDistribution.cumulativeProbability(x3);
            Assert.fail("Expected MathException to be thrown");
        } catch (MathException e) {
            // Test passed
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        // Typical use case: p = 0.5
        double p1 = 0.5;
        double expected1 = super.inverseCumulativeProbability(p1);
        double actual1 = fDistribution.inverseCumulativeProbability(p1);
        Assert.assertEquals(expected1, actual1, 1e-5);

        // Edge case: p = 0.0
        double p2 = 0.0;
        double expected2 = 0.0;
        double actual2 = fDistribution.inverseCumulativeProbability(p2);
        Assert.assertEquals(expected2, actual2, 1e-5);

        // Edge case: p = 1.0
        double p3 = 1.0;
        double expected3 = Double.POSITIVE_INFINITY;
        double actual3 = fDistribution.inverseCumulativeProbability(p3);
        Assert.assertEquals(expected3, actual3, 1e-5);

        // Error scenario: p = -0.5
        double p4 = -0.5;
        try {
            fDistribution.inverseCumulativeProbability(p4);
            Assert.fail("Expected MathException to be thrown");
        } catch (MathException e) {
            // Test passed
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        // Typical use case: p = 0.5
        double p1 = 0.5;
        double expected1 = 0.0;
        double actual1 = fDistribution.getDomainLowerBound(p1);
        Assert.assertEquals(expected1, actual1, 1e-5);

        // Error scenario: p = 2.0
        double p2 = 2.0;
        double expected2 = 0.0;
        double actual2 = fDistribution.getDomainLowerBound(p2);
        Assert.assertEquals(expected2, actual2, 1e-5);
    }

    @Test
    public void testGetDomainUpperBound() {
        // Typical use case: p = 0.5
        double p1 = 0.5;
        double expected1 = Double.MAX_VALUE;
        double actual1 = fDistribution.getDomainUpperBound(p1);
        Assert.assertEquals(expected1, actual1, 1e-5);

        // Error scenario: p = 2.0
        double p2 = 2.0;
        double expected2 = Double.MAX_VALUE;
        double actual2 = fDistribution.getDomainUpperBound(p2);
        Assert.assertEquals(expected2, actual2, 1e-5);
    }

    @Test
    public void testGetInitialDomain(){
        // Typical use case: p = 0.5
        double p1 = 0.5;
        double d = fDistribution.getDenominatorDegreesOfFreedom();
        double expected1 = d / (d - 2.0);
        double actual1 = fDistribution.getInitialDomain(p1);
        Assert.assertEquals(expected1, actual1, 1e-5);

        // Error scenario: p = 2.0
        double p2 = 2.0;
        double expected2 = 0.0;
        double actual2 = fDistribution.getInitialDomain(p2);
        Assert.assertEquals(expected2, actual2, 1e-5);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom(){
        // Typical use case: Set numerator degrees of freedom to 5
        double degreesOfFreedom1 = 5;
        fDistribution.setNumeratorDegreesOfFreedom(degreesOfFreedom1);
        double expected1 = degreesOfFreedom1;
        double actual1 = fDistribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(expected1, actual1, 1e-5);

        // Error scenario: Set numerator degrees of freedom to -1
        double degreesOfFreedom2 = -1;
        try{
            fDistribution.setNumeratorDegreesOfFreedom(degreesOfFreedom2);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e){
            // Test passed
        }
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom(){
        // Typical use case: Set denominator degrees of freedom to 10
        double degreesOfFreedom1 = 10;
        fDistribution.setDenominatorDegreesOfFreedom(degreesOfFreedom1);
        double expected1 = degreesOfFreedom1;
        double actual1 = fDistribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(expected1, actual1, 1e-5);

        // Error scenario: Set denominator degrees of freedom to -5
        double degreesOfFreedom2 = -5;
        try{
            fDistribution.setDenominatorDegreesOfFreedom(degreesOfFreedom2);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e){
            // Test passed
        }
    }
}