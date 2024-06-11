import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case
        double result1 = fd.cumulativeProbability(2.5);
        assertEquals(0.5410774512490492, result1, 0.0001);
        
        // Edge case: x = 0
        double result2 = fd.cumulativeProbability(0);
        assertEquals(0.0, result2, 0.0001);
        
        // Edge case: x = 1
        double result3 = fd.cumulativeProbability(1);
        assertEquals(0.20603885871550993, result3, 0.0001);
        
        // Error scenario: x < 0
        try {
            double result4 = fd.cumulativeProbability(-2);
            fail("Expected MathException was not thrown");
        } catch (MathException e) {
            // Expected exception
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);

        // Typical use case
        double result1 = fd.inverseCumulativeProbability(0.5);
        assertEquals(0.977167369092478, result1, 0.0001);
        
        // Edge case: p = 0
        double result2 = fd.inverseCumulativeProbability(0);
        assertEquals(0.0, result2, 0.0001);
        
        // Edge case: p = 1
        double result3 = fd.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, result3, 0.0001);
        
        // Error scenario: p > 1
        try {
            double result4 = fd.inverseCumulativeProbability(1.5);
            fail("Expected MathException was not thrown");
        } catch (MathException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetDomainLowerBound() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case
        double result1 = fd.getDomainLowerBound(0.25);
        assertEquals(0.0, result1, 0.0001);
        
        // Edge case: p = 0
        double result2 = fd.getDomainLowerBound(0);
        assertEquals(0.0, result2, 0.0001);
        
        // Edge case: p = 1
        double result3 = fd.getDomainLowerBound(1);
        assertEquals(0.0, result3, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case
        double result1 = fd.getDomainUpperBound(0.25);
        assertEquals(Double.MAX_VALUE, result1, 0.0001);
        
        // Edge case: p = 0
        double result2 = fd.getDomainUpperBound(0);
        assertEquals(Double.MAX_VALUE, result2, 0.0001);
        
        // Edge case: p = 1
        double result3 = fd.getDomainUpperBound(1);
        assertEquals(Double.MAX_VALUE, result3, 0.0001);
    }

    @Test
    public void testGetInitialDomain() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case
        double result1 = fd.getInitialDomain(0.25);
        assertEquals(0.6666666666666666, result1, 0.0001);
        
        // Edge case: p = 0
        double result2 = fd.getInitialDomain(0);
        assertEquals(0.6666666666666666, result2, 0.0001);
        
        // Edge case: p = 1
        double result3 = fd.getInitialDomain(1);
        assertEquals(0.6666666666666666, result3, 0.0001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case
        fd.setNumeratorDegreesOfFreedom(7);
        assertEquals(7.0, fd.getNumeratorDegreesOfFreedom(), 0.0001);
        
        // Error scenario: degreesOfFreedom <= 0
        try {
            fd.setNumeratorDegreesOfFreedom(-2);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case
        double result1 = fd.getNumeratorDegreesOfFreedom();
        assertEquals(5.0, result1, 0.0001);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case
        fd.setDenominatorDegreesOfFreedom(15);
        assertEquals(15.0, fd.getDenominatorDegreesOfFreedom(), 0.0001);
        
        // Error scenario: degreesOfFreedom <= 0
        try {
            fd.setDenominatorDegreesOfFreedom(-2);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() throws Exception {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        
        // Typical use case
        double result1 = fd.getDenominatorDegreesOfFreedom();
        assertEquals(10.0, result1, 0.0001);
    }
}