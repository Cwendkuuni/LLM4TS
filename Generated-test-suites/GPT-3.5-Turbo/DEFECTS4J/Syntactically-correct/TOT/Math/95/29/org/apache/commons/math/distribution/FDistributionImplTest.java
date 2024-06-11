import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(5, 10);

        // Typical use case
        double typicalResult = fd.cumulativeProbability(2);
        assertEquals(0.180, typicalResult, 0.001);

        // Edge case
        double edgeResult = fd.cumulativeProbability(0);
        assertEquals(0.0, edgeResult, 0.001);

        // Error scenario
        boolean errorOccurred = false;
        try {
            fd.cumulativeProbability(-1);
        } catch (MathException e) {
            errorOccurred = true;
        }
        assertTrue(errorOccurred);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(5, 10);

        // Typical use case
        double typicalResult = fd.inverseCumulativeProbability(0.5);
        assertEquals(1.060, typicalResult, 0.001);

        // Edge case
        double edgeResult = fd.inverseCumulativeProbability(0);
        assertEquals(0.0, edgeResult, 0.001);

        // Error scenario
        boolean errorOccurred = false;
        try {
            fd.inverseCumulativeProbability(1.1);
        } catch (MathException e) {
            errorOccurred = true;
        }
        assertTrue(errorOccurred);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);

        // Typical use case
        double typicalResult = fd.getDomainLowerBound(0.5);
        assertEquals(0.0, typicalResult, 0.001);

        // Edge case
        double edgeResult = fd.getDomainLowerBound(0);
        assertEquals(0.0, edgeResult, 0.001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);

        // Typical use case
        double typicalResult = fd.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, typicalResult, 0.001);

        // Edge case
        double edgeResult = fd.getDomainUpperBound(1);
        assertEquals(Double.MAX_VALUE, edgeResult, 0.001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);

        // Typical use case
        double typicalResult = fd.getInitialDomain(0.5);
        assertEquals(1.25, typicalResult, 0.001);

        // Edge case
        double edgeResult = fd.getInitialDomain(1);
        assertEquals(Double.POSITIVE_INFINITY, edgeResult, 0.001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);

        // Typical use case
        fd.setNumeratorDegreesOfFreedom(8);
        assertEquals(8, fd.getNumeratorDegreesOfFreedom(), 0.001);

        // Error scenario
        boolean errorOccurred = false;
        try {
            fd.setNumeratorDegreesOfFreedom(0);
        } catch (IllegalArgumentException e) {
            errorOccurred = true;
        }
        assertTrue(errorOccurred);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);

        // Typical use case
        fd.setDenominatorDegreesOfFreedom(12);
        assertEquals(12, fd.getDenominatorDegreesOfFreedom(), 0.001);

        // Error scenario
        boolean errorOccurred = false;
        try {
            fd.setDenominatorDegreesOfFreedom(0);
        } catch (IllegalArgumentException e) {
            errorOccurred = true;
        }
        assertTrue(errorOccurred);

    }
}