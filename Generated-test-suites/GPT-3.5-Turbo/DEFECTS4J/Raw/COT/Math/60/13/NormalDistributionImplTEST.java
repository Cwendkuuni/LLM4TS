import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assert(dist.getMean() == 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assert(dist.getStandardDeviation() == 1);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assert(dist.density(0) == 0.3989422804014327);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assert(dist.cumulativeProbability(0) == 0.5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNotStrictlyPositiveException() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assert(dist.inverseCumulativeProbability(0.5) == 0);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double value = dist.sample();
        // no easy way to test sample, just test if value is a number
        assert(Double.isFinite(value));
    }

}