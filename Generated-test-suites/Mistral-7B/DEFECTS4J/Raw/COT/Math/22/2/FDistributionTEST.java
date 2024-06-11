import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class FDistributionTest {

    @Test
    public void testFDistributionConstructor() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(3.0, 5.0);
        assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(-1.0, 5.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(0.0, 5.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double density = fd.density(1.5);
        assertTrue(Double.isFinite(density));
    }

    @Test
    public void testFDistributionDensityZero() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double density = fd.density(0.0);
        assertEquals(0.0, density, 1e-10);
    }

    @Test
    public void testFDistributionDensityNegative() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double density = fd.density(-1.0);
        assertEquals(Double.NaN, density);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double cp = fd.cumulativeProbability(1.5);
        assertTrue(cp > 0.0 && cp < 1.0);
    }

    @Test
    public void testFDistributionCumulativeProbabilityZero() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double cp = fd.cumulativeProbability(0.0);
        assertEquals(0.0, cp, 1e-10);
    }

    @Test
    public void testFDistributionCumulativeProbabilityOne() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double cp = fd.cumulativeProbability(1.0);
        assertEquals(0.811743329868277, cp, 1e-10);
    }

    @Test
    public void testFDistributionCumulativeProbabilityNegative() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double cp = fd.cumulativeProbability(-1.0);
        assertEquals(Double.NaN, cp);
    }

    @Test
    public void testFDistributionMean() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double mean = fd.getNumericalMean();
        assertEquals(3.161271327525133, mean, 1e-10);
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double variance = fd.getNumericalVariance();
        assertEquals(2.513738255324664, variance, 1e-10);
    }

    @Test(expected = IllegalStateException.class)
    public void testFDistributionVarianceUncalculated() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        fd.getNumericalVariance(); // This should calculate it
        fd.getNumericalVariance(); // This should throw an exception
    }
}