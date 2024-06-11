import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        // Create an instance of PoissonDistributionImpl
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);

        // Test probability() method
        assert pd.probability(0) == 0.04978706836786395;
        assert pd.probability(1) == 0.14936120510359185;
        assert pd.probability(2) == 0.22404180765538775;
        assert pd.probability(3) == 0.22404180765538775;
        assert pd.probability(4) == 0.1680313557415408;
        assert pd.probability(5) == 0.10081881344492452;
        assert pd.probability(6) == 0.05040940672246226;
    }

    @Test
    public void testCumulativeProbability() {
        // Create an instance of PoissonDistributionImpl
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);

        // Test cumulativeProbability() method
        try {
            assert pd.cumulativeProbability(0) == 0.049787068367863944;
            assert pd.cumulativeProbability(1) == 0.19914827347145582;
            assert pd.cumulativeProbability(2) == 0.4231900811268426;
            assert pd.cumulativeProbability(3) == 0.6472318887822294;
            assert pd.cumulativeProbability(4) == 0.8152632445237702;
            assert pd.cumulativeProbability(5) == 0.9160820579686947;
            assert pd.cumulativeProbability(6) == 0.9664914646911569;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNormalApproximateProbability() {
        // Create an instance of PoissonDistributionImpl
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);

        // Test normalApproximateProbability() method
        try {
            assert pd.normalApproximateProbability(0) == 0.07468380916243252;
            assert pd.normalApproximateProbability(1) == 0.1666423622830787;
            assert pd.normalApproximateProbability(2) == 0.2709833236713687;
            assert pd.normalApproximateProbability(3) == 0.38860479705166694;
            assert pd.normalApproximateProbability(4) == 0.5126925496867914;
            assert pd.normalApproximateProbability(5) == 0.6352367906620819;
            assert pd.normalApproximateProbability(6) == 0.7488290647923612;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        // Create an instance of PoissonDistributionImpl
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);

        // Test sample() method
        try {
            int sample = pd.sample();

            assert sample >= 0 && sample <= Integer.MAX_VALUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}