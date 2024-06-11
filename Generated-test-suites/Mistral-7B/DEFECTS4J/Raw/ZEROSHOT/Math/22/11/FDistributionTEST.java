import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        try {
            FDistribution fd = new FDistribution(2.0, 3.0);
            Assert.assertNotNull(fd);
        } catch (NotStrictlyPositiveException e) {
            //expected
        }

        try {
            FDistribution fd = new FDistribution(3.0, 2.0);
            Assert.assertNotNull(fd);
        } catch (NotStrictlyPositiveException e) {
            //expected
        }

        try {
            FDistribution fd = new FDistribution(1.0, 3.0);
            Assert.assertNotNull(fd);
        } catch (NotStrictlyPositiveException e) {
            //expected
        }

        try {
            FDistribution fd = new FDistribution(3.0, 1.0);
            Assert.assertNotNull(fd);
        } catch (NotStrictlyPositiveException e) {
            //expected
        }

        try {
            FDistribution fd = new FDistribution(-2.0, 3.0);
            Assert.fail();
        } catch (NotStrictlyPositiveException e) {
            //expected
        }

        try {
            FDistribution fd = new FDistribution(3.0, -3.0);
            Assert.fail();
        } catch (NotStrictlyPositiveException e) {
            //expected
        }
    }

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(2.0, 3.0);

        double density = fd.density(1.5);
        Assert.assertNotNull(density);
        Assert.assertTrue(density > 0);

        density = fd.density(3.0);
        Assert.assertNotNull(density);
        Assert.assertTrue(density > 0);

        density = fd.density(0.5);
        Assert.assertNotNull(density);
        Assert.assertTrue(density > 0);

        density = fd.density(5.0);
        Assert.assertNotNull(density);
        Assert.assertTrue(density > 0);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);

        double p = fd.cumulativeProbability(0);
        Assert.assertEquals(0.0, p, 0.001);

        p = fd.cumulativeProbability(0.5);
        Assert.assertTrue(p > 0.0 && p < 0.5);

        p = fd.cumulativeProbability(1);
        Assert.assertEquals(1.0, p, 0.001);

        p = fd.cumulativeProbability(2);
        Assert.assertTrue(p > 0.5 && p < 1.0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double degreesOfFreedom = fd.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(2.0, degreesOfFreedom);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double degreesOfFreedom = fd.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(3.0, degreesOfFreedom);
    }

    @Test
    public void testNumericalMean() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double mean = fd.getNumericalMean();
        Assert.assertEquals(3.0 / 2.0, mean, 0.001);
    }

    @Test
    public void testNumericalVariance() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double variance = fd.getNumericalVariance();
        Assert.assertEquals(0.1728, variance, 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInverseCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        fd.inverseCumulativeProbability(0.5);
    }
}