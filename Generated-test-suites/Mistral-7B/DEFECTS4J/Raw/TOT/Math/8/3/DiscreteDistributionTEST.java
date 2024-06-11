import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for DiscreteDistribution.
 */
public class DiscreteDistributionTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testConstructorValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.6));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
            assertEquals(3, distribution.singletons.size());
            assertEquals(0.1, distribution.probabilities[0], 0.01);
            assertEquals(0.3, distribution.probabilities[1], 0.01);
            assertEquals(0.6, distribution.probabilities[2], 0.01);
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw NotStrictlyPositiveException with valid input.");
        }
    }

    /**
     * Test constructor with negative probabilities.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, -0.1));
        new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

    /**
     * Test constructor with infinite probabilities.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorInfiniteProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, Double.POSITIVE_INFINITY));
        new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

    /**
     * Test constructor with probabilities that don't sum up to 1.
     */
    @Test(expected = MathArithmeticException.class)
    public void testConstructorProbabilitiesSumNotOne() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.4));
        new DiscreteDistribution<Integer>(new Well19937c(), samples);
    }

    /**
     * Test probability method with valid input.
     */
    @Test
    public void testProbabilityValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.6));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
            assertEquals(0.1, distribution.probability((Integer) 1), 0.01);
            assertEquals(0.3, distribution.probability((Integer) 2), 0.01);
            assertEquals(0.6, distribution.probability((Integer) 3), 0.01);
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw NotStrictlyPositiveException with valid input.");
        }
    }

    /**
     * Test probability method with invalid input.
     */
    @Test
    public void testProbabilityInvalidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.6));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw NotStrictlyPositiveException with valid input.");
        }
        assertNull(distribution.probability(null));
        assertNull(distribution.probability((Object) new Object()));
    }

    /**
     * Test sample method with valid input.
     */
    @Test
    public void testSampleValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.6));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
            Integer sample = distribution.sample();
            assertTrue(distribution.singletons.contains(sample));
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw NotStrictlyPositiveException with valid input.");
        }
    }

    /**
     * Test sample method with invalid input.
     */
    @Test
    public void testSampleInvalidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.6));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw NotStrictlyPositiveException with valid input.");
        }
        assertNull(distribution.sample());
        assertNull(distribution.sample((Object) new Object()));
    }

    /**
     * Test sample method with multiple samples.
     */
    @Test
    public void testSampleMultipleSamples() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.6));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        Integer[] samplesArray = distribution.sample(10);
        for (Integer sample : samplesArray) {
            assertTrue(distribution.singletons.contains(sample));
        }
    }

    /**
     * Test reseedRandomGenerator method.
     */
    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.6));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
            long seed = 123456789;
            distribution.reseedRandomGenerator(seed);
            // Test further with generateSample() method or other testing methods.
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw NotStrictlyPositiveException with valid input.");
        }
    }

    /**
     * Test getSamples method.
     */
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.6));
        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
            List<Pair<Integer, Double>> samplesList = distribution.getSamples();
            assertEquals(3, samplesList.size());
            for (int i = 0; i < samples.size(); i++) {
                assertEquals(samples.get(i).getKey(), samplesList.get(i).getKey());
                assertEquals(samples.get(i).getValue(), samplesList.get(i).getValue(), 0.01);
            }
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw NotStrictlyPositiveException with valid input.");
        }
    }
}