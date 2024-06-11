import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    /**
     * Test the constructor with valid inputs.
     */
    @Test
    public void testConstructor() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        
        assertEquals(samples, dist.getSamples());
    }

    /**
     * Test the constructor with negative probability.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.2));
        samples.add(new Pair<>(2, 0.3));
        
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
    }

    /**
     * Test the constructor with probabilities that sum to zero.
     */
    @Test(expected = MathArithmeticException.class)
    public void testConstructorZeroSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, -0.5));
        
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
    }

    /**
     * Test the constructor with infinite probability.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.3));
        
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
    }

    /**
     * Test the probability method.
     */
    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        
        double prob = dist.probability(1);
        
        assertEquals(0.2, prob, 0.000001);
    }

    /**
     * Test the probability method with null input.
     */
    @Test
    public void testProbabilityNull() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(null, 0.2));
        samples.add(new Pair<>(2, 0.3));
        
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        
        double prob = dist.probability(null);
        
        assertEquals(0.2, prob, 0.000001);
    }

    /**
     * Test the getSamples method.
     */
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        
        List<Pair<Integer, Double>> result = dist.getSamples();
        
        assertEquals(samples, result);
    }

    /**
     * Test the sample method.
     */
    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        
        Integer sample = dist.sample();
        
        assertNotNull(sample);
    }

    /**
     * Test the sample method with a negative sample size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        
        Integer[] sample = dist.sample(-3);
    }

    /**
     * Test the sample method with zero sample size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleZeroSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        
        Integer[] sample = dist.sample(0);
    }

    /**
     * Test the sample method with a positive sample size.
     */
    @Test
    public void testSamplePositiveSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        
        Integer[] sample = dist.sample(4);
        
        assertEquals(4, sample.length);
    }

}