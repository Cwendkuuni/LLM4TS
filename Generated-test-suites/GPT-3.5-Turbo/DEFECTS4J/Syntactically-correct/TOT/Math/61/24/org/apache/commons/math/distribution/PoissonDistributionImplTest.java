import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new PoissonDistributionImpl(5.0);
    }

    @After
    public void tearDown() {
        distribution = null;
    }

    // Test for probability method

    @Test
    public void testProbability_PositiveInteger_ReturnsCorrectProbability() {
        double probability = distribution.probability(3);
        assertEquals(0.14037389581428022, probability, 0.000001);
    }

    @Test
    public void testProbability_NegativeInteger_ReturnsZeroProbability() {
        double probability = distribution.probability(-3);
        assertEquals(0.0, probability, 0.0);
    }

    @Test
    public void testProbability_Zero_ReturnsCorrectProbability() {
        double probability = distribution.probability(0);
        assertEquals(0.03368973499542734, probability, 0.000001);
    }

    // Test for cumulativeProbability method

    @Test
    public void testCumulativeProbability_PositiveInteger_ReturnsCorrectCumulativeProbability() throws MathException {
        double cumulativeProbability = distribution.cumulativeProbability(3);
        assertEquals(0.2650259152973619, cumulativeProbability, 0.000001);
    }

    @Test
    public void testCumulativeProbability_NegativeInteger_ReturnsZeroCumulativeProbability() throws MathException {
        double cumulativeProbability = distribution.cumulativeProbability(-3);
        assertEquals(0.0, cumulativeProbability, 0.0);
    }

    @Test
    public void testCumulativeProbability_Zero_ReturnsCorrectCumulativeProbability() throws MathException {
        double cumulativeProbability = distribution.cumulativeProbability(0);
        assertEquals(0.03368973499542734, cumulativeProbability, 0.000001);
    }

    // Test for normalApproximateProbability method

    @Test
    public void testNormalApproximateProbability_PositiveInteger_ReturnsCorrectProbability() throws MathException {
        double probability = distribution.normalApproximateProbability(3);
        assertEquals(0.8712337790695359, probability, 0.000001);
    }

    @Test
    public void testNormalApproximateProbability_NegativeInteger_ReturnsCorrectProbability() throws MathException {
        double probability = distribution.normalApproximateProbability(-3);
        assertEquals(0.02921791665180194, probability, 0.000001);
    }

    @Test
    public void testNormalApproximateProbability_Zero_ReturnsCorrectProbability() throws MathException {
        double probability = distribution.normalApproximateProbability(0);
        assertEquals(0.01507373507401855, probability, 0.000001);
    }

    // Test for sample method

    @Test
    public void testSample_ReturnsRandomValue() throws MathException {
        int sample1 = distribution.sample();
        int sample2 = distribution.sample();
        
        assertNotEquals(sample1, sample2);
    }
}