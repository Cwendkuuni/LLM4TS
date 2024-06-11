import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the DiscreteDistribution class.
 */
public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        try {
            distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test probability method with valid input.
     */
    @Test
    public void testProbabilityValidInput() {
        Assert.assertEquals(0.3, distribution.probability((Integer) 1), 0.001);
    }

    /**
     * Test probability method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityNullInput() {
        distribution.probability(null);
    }

    /**
     * Test probability method with wrong input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityWrongInput() {
        distribution.probability(2.5);
    }

    /**
     * Test getSamples method.
     */
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        Assert.assertEquals(3, samples.size());
        Assert.assertEquals(0.3, samples.get(0).getValue(), 0.001);
    }

    /**
     * Test sample method with valid input.
     */
    @Test
    public void testSampleValidInput() {
        Assert.assertEquals((Integer) 1, distribution.sample());
    }

    /**
     * Test sample method with custom seed.
     */
    @Test
    public void testSampleCustomSeed() {
        long seed = System.nanoTime();
        distribution.reseedRandomGenerator(seed);
        Assert.assertEquals((Integer) 3, distribution.sample());
    }

    /**
     * Test sample method with large sample size.
     */
    @Test
    public void testSampleLargeSampleSize() throws NotStrictlyPositiveException {
        int sampleSize = 1000;
        T[] sample = distribution.sample(sampleSize);
        // Add some assertions here to validate the sample
    }

    /**
     * Test reseedRandomGenerator method with negative seed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReseedRandomGeneratorNegativeSeed() {
        distribution.reseedRandomGenerator(-1);
    }

    /**
     * Test reseedRandomGenerator method with zero seed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReseedRandomGeneratorZeroSeed() {
        distribution.reseedRandomGenerator(0);
    }
}