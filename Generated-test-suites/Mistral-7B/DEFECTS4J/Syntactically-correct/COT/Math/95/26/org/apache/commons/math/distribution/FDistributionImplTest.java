import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl_ValidConstruction() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        assertNotNull(fd);
    }

    @Test
    public void testCumulativeProbability_ValidInput() throws MathException {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        double result = fd.cumulativeProbability(2.5);
        assertTrue(result > 0.0);
        result = fd.cumulativeProbability(3.0);
        assertEquals(0.5, result, 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbability_InvalidInput() throws MathException {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        fd.cumulativeProbability(-1.0);
    }

    @Test
    public void testInverseCumulativeProbability_ValidInput() throws MathException {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        double p = 0.25;
        double result = fd.inverseCumulativeProbability(p);
        assertTrue(result > 0.0);
        p = 0.75;
        result = fd.inverseCumulativeProbability(p);
        assertTrue(result > 0.0);
        p = 0.5;
        assertEquals(1.0 / 3.0, result, 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_InvalidInput() throws MathException {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        fd.inverseCumulativeProbability(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_NegativeInput() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        fd.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom_ValidInput() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        fd.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_NegativeInput() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        fd.setDenominatorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom_ValidInput() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        fd.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(5.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }
}