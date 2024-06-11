import org.junitTest;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.2231301601484298, distribution.probability(0), EPSILON);
        assertEquals(0.33469524022264435, distribution.probability(1), EPSILON);
        assertEquals(0.25102193016698325, distribution.probability(2), EPSILON);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.2231301601484298, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(0.5578254003710748, distribution.cumulativeProbability(1), EPSILON);
        assertEquals(0.8088473305380578, distribution.cumulativeProbability(2), EPSILON);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.42074029056089716, distribution.normalApproximateProbability(0), EPSILON);
        assertEquals(0.7546241217517825, distribution.normalApproximateProbability(1), EPSILON);
        assertEquals(0.9340087522640005, distribution.normalApproximateProbability(2), EPSILON);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        distribution.sample();
        //No assertion, only checks if there is no exception
    }
}