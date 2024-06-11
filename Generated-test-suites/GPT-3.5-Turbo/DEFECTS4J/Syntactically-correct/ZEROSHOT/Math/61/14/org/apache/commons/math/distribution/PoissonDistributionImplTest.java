import org.junit.Test;
import org.junit.Assert;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.10377687435514876, dist.probability(0), 0.00001);
        Assert.assertEquals(0.2594421858878719, dist.probability(1), 0.00001);
        Assert.assertEquals(0.3243027323598399, dist.probability(2), 0.00001);
        Assert.assertEquals(0.2702522769665333, dist.probability(3), 0.00001);
        Assert.assertEquals(0.16890767310408332, dist.probability(4), 0.00001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.0820849986238988, dist.cumulativeProbability(0), 0.00001);
        Assert.assertEquals(0.3411134627263795, dist.cumulativeProbability(1), 0.00001);
        Assert.assertEquals(0.6654169693493297, dist.cumulativeProbability(2), 0.00001);
        Assert.assertEquals(0.879669246245863, dist.cumulativeProbability(3), 0.00001);
        Assert.assertEquals(0.982576918349947, dist.cumulativeProbability(4), 0.00001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.08008876785090526, dist.normalApproximateProbability(0), 0.00001);
        Assert.assertEquals(0.34944866722783746, dist.normalApproximateProbability(1), 0.00001);
        Assert.assertEquals(0.6674958864454126, dist.normalApproximateProbability(2), 0.00001);
        Assert.assertEquals(0.8808650407633368, dist.normalApproximateProbability(3), 0.00001);
        Assert.assertEquals(0.9796576998783462, dist.normalApproximateProbability(4), 0.00001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int sample = dist.sample();
        Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }
}