import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.util.LocalizedFormats;

public class NormalDistributionImplTest {

    // Test the constructor with mean and standard deviation
    @Test
    public void testConstructor() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, distribution.getMean(), 0.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    // Test the constructor with mean, standard deviation, and inverse cumulative distribution accuracy
    @Test
    public void testConstructorWithAccuracy() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0, 1e-6);
        assertEquals(5.0, distribution.getMean(), 0.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
        assertEquals(1e-6, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    // Test the default constructor with mean 0 and standard deviation 1
    @Test
    public void testDefaultConstructor() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    // Test the density method
    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(0.026995483, distribution.density(5.0), 1e-9);
    }

    // Test the cumulativeProbability method
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(0.5, distribution.cumulativeProbability(5.0), 1e-9);
    }

    // Test the inverseCumulativeProbability method
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, distribution.inverseCumulativeProbability(0.5), 1e-9);

        try {
            distribution.inverseCumulativeProbability(-0.1);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            assertEquals(LocalizedFormats.OUT_OF_RANGE_SIMPLE, e.getSpecifier());
        }

        try {
            distribution.inverseCumulativeProbability(1.1);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            assertEquals(LocalizedFormats.OUT_OF_RANGE_SIMPLE, e.getSpecifier());
        }
    }

    // Test the sample method
    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        double value = distribution.sample();
        assertTrue(value >= Double.NEGATIVE_INFINITY);
        assertTrue(value <= Double.POSITIVE_INFINITY);
    }

    // Test the getDomainLowerBound method
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.3), 0.0);
        assertEquals(5.0, distribution.getDomainLowerBound(0.7), 0.0);
    }

    // Test the getDomainUpperBound method
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, distribution.getDomainUpperBound(0.3), 0.0);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.7), 0.0);
    }

    // Test the getInitialDomain method
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(3.0, distribution.getInitialDomain(0.3), 0.0);
        assertEquals(7.0, distribution.getInitialDomain(0.7), 0.0);
        assertEquals(5.0, distribution.getInitialDomain(0.5), 0.0);
    }
}

// 
// All JUnit 4 test cases go here
// 