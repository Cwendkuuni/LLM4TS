import org.junit.Test;
import org.apache.commons.math.special.Beta;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    private static double EPSILON = 1e-15;

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDist = new FDistributionImpl(4, 6);
        double x = 0.5;
        double expected = Beta.regularizedBeta((4 * x) / (6 + 4 * x), 2.0, 3.0);
        assertEquals(expected, fDist.cumulativeProbability(x), EPSILON);
        
        x = 1.0;
        expected = Beta.regularizedBeta((4 * x) / (6 + 4 * x), 2.0, 3.0);
        assertEquals(expected, fDist.cumulativeProbability(x), EPSILON);
        
        x = 2.0;
        expected = Beta.regularizedBeta((4 * x) / (6 + 4 * x), 2.0, 3.0);
        assertEquals(expected, fDist.cumulativeProbability(x), EPSILON);
        
        x = -1.0;
        expected = 0.0;
        assertEquals(expected, fDist.cumulativeProbability(x), EPSILON);
        
        x = 3.0;
        expected = 1.0;
        assertEquals(expected, fDist.cumulativeProbability(x), EPSILON);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDist = new FDistributionImpl(4, 6);
        
        double p = 0.0;
        double expected = 0.0;
        assertEquals(expected, fDist.inverseCumulativeProbability(p), EPSILON);
        
        p = 0.5;
        expected = 1.681792830507429;
        assertEquals(expected, fDist.inverseCumulativeProbability(p), EPSILON);
        
        p = 1.0;
        expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, fDist.inverseCumulativeProbability(p), EPSILON);
        
        p = 0.25;
        expected = 0.988684155162469;
        assertEquals(expected, fDist.inverseCumulativeProbability(p), EPSILON);
        
        p = 0.75;
        expected = 4.351538270819322;
        assertEquals(expected, fDist.inverseCumulativeProbability(p), EPSILON);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDist = new FDistributionImpl(4, 6);

        double p = 0.0;
        double expected = 0.0;
        assertEquals(expected, fDist.getDomainLowerBound(p), EPSILON);
        
        p = 0.5;
        expected = 0.0;
        assertEquals(expected, fDist.getDomainLowerBound(p), EPSILON);
        
        p = 1.0;
        expected = 0.0;
        assertEquals(expected, fDist.getDomainLowerBound(p), EPSILON);
        
        p = 0.25;
        expected = 0.0;
        assertEquals(expected, fDist.getDomainLowerBound(p), EPSILON);
        
        p = 0.75;
        expected = 0.0;
        assertEquals(expected, fDist.getDomainLowerBound(p), EPSILON);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDist = new FDistributionImpl(4, 6);

        double p = 0.0;
        double expected = Double.MAX_VALUE;
        assertEquals(expected, fDist.getDomainUpperBound(p), EPSILON);
        
        p = 0.5;
        expected = Double.MAX_VALUE;
        assertEquals(expected, fDist.getDomainUpperBound(p), EPSILON);
        
        p = 1.0;
        expected = Double.MAX_VALUE;
        assertEquals(expected, fDist.getDomainUpperBound(p), EPSILON);
        
        p = 0.25;
        expected = Double.MAX_VALUE;
        assertEquals(expected, fDist.getDomainUpperBound(p), EPSILON);
        
        p = 0.75;
        expected = Double.MAX_VALUE;
        assertEquals(expected, fDist.getDomainUpperBound(p), EPSILON);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDist = new FDistributionImpl(4, 6);

        double p = 0.0;
        double expected = 1.5;
        assertEquals(expected, fDist.getInitialDomain(p), EPSILON);
        
        p = 0.5;
        expected = 1.5;
        assertEquals(expected, fDist.getInitialDomain(p), EPSILON);
        
        p = 1.0;
        expected = 1.5;
        assertEquals(expected, fDist.getInitialDomain(p), EPSILON);
        
        p = 0.25;
        expected = 1.5;
        assertEquals(expected, fDist.getInitialDomain(p), EPSILON);
        
        p = 0.75;
        expected = 1.5;
        assertEquals(expected, fDist.getInitialDomain(p), EPSILON);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(4, 6);
        fDist.setNumeratorDegreesOfFreedom(0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(4, 6);
        fDist.setDenominatorDegreesOfFreedom(0);
    }
}