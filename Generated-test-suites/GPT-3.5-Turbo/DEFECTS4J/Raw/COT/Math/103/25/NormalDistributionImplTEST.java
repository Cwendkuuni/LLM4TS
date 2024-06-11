
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
    
    @Test
    public void testGetMean() {
        double expectedMean = 0.0;
        double actualMean = normalDistribution.getMean();
        
        assertEquals(expectedMean, actualMean, 0.00001);
    }
    
    @Test
    public void testSetMean() {
        double expectedMean = 10.0;
        normalDistribution.setMean(expectedMean);
        double actualMean = normalDistribution.getMean();
        
        assertEquals(expectedMean, actualMean, 0.00001);
    }

    @Test
    public void testGetStandardDeviation() {
        double expectedStandardDeviation = 1.0;
        double actualStandardDeviation = normalDistribution.getStandardDeviation();
        
        assertEquals(expectedStandardDeviation, actualStandardDeviation, 0.00001);
    }

    @Test
    public void testSetStandardDeviation() {
        double expectedStandardDeviation = 5.0;
        normalDistribution.setStandardDeviation(expectedStandardDeviation);
        double actualStandardDeviation = normalDistribution.getStandardDeviation();
        
        assertEquals(expectedStandardDeviation, actualStandardDeviation, 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 0.0;
        double expectedCumulativeProbability = 0.5;
        double actualCumulativeProbability;
        try {
            actualCumulativeProbability = normalDistribution.cumulativeProbability(x);
            assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        double p = 0.5;
        double expectedInverseCumulativeProbability = 0.0;
        double actualInverseCumulativeProbability;
        try {
            actualInverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(p);
            assertEquals(expectedInverseCumulativeProbability, actualInverseCumulativeProbability, 0.00001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        double p = 0.5;
        double expectedDomainLowerBound = 0.0;
        double actualDomainLowerBound = normalDistribution.getDomainLowerBound(p);
        
        assertEquals(expectedDomainLowerBound, actualDomainLowerBound, 0.00001);
    }

    @Test
    public void testGetDomainUpperBound() {
        double p = 0.5;
        double expectedDomainUpperBound = Double.MAX_VALUE;
        double actualDomainUpperBound = normalDistribution.getDomainUpperBound(p);
        
        assertEquals(expectedDomainUpperBound, actualDomainUpperBound, 0.00001);
    }

    @Test
    public void testGetInitialDomain() {
        double p = 0.5;
        double expectedInitialDomain = 0.0;
        double actualInitialDomain = normalDistribution.getInitialDomain(p);
        
        assertEquals(expectedInitialDomain, actualInitialDomain, 0.00001);
    }
}

