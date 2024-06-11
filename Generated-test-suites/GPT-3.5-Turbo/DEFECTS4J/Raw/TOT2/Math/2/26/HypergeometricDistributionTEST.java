import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstructor() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
            assertNotNull(distribution);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Constructor should not throw an exception");
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.01);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.01);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.01);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.probability(-1), 0.01);
        assertEquals(0.0, distribution.probability(6), 0.01);
        assertEquals(0.2, distribution.probability(2), 0.01);
    }

    // Add more test cases for other methods...

}