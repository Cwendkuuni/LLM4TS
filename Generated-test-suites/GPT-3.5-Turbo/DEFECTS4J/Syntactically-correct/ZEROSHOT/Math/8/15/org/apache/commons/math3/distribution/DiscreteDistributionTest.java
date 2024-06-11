import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        // create a DiscreteDistribution object
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>();

        // test probability for a known value
        String x = "A";
        double expectedProbability = 0.5;
        double actualProbability = distribution.probability(x);
        assertEquals(expectedProbability, actualProbability, 0.0001);

        // test probability for an unknown value
        x = "B";
        expectedProbability = 0.0;
        actualProbability = distribution.probability(x);
        assertEquals(expectedProbability, actualProbability, 0.0001);
    }

    @Test
    public void testGetSamples() {
        // create a DiscreteDistribution object
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>();

        // get the samples from the distribution
        List<Pair<String, Double>> samples = distribution.getSamples();

        // check if the samples are not null
        assertNotNull(samples);

        // check if the number of samples is correct
        int expectedSampleSize = 2;
        int actualSampleSize = samples.size();
        assertEquals(expectedSampleSize, actualSampleSize);

        // check if the samples contain the correct values and probabilities
        Pair<String, Double> sample = samples.get(0);
        String expectedValue = "A";
        String actualValue = sample.getKey();
        assertEquals(expectedValue, actualValue);
        double expectedProbability = 0.5;
        double actualProbability = sample.getValue();
        assertEquals(expectedProbability, actualProbability, 0.0001);

        sample = samples.get(1);
        expectedValue = "B";
        actualValue = sample.getKey();
        assertEquals(expectedValue, actualValue);
        expectedProbability = 0.5;
        actualProbability = sample.getValue();
        assertEquals(expectedProbability, actualProbability, 0.0001);
    }

    @Test
    public void testSample() {
        // create a DiscreteDistribution object
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>();

        // generate a random sample
        String sample = distribution.sample();

        // check if the sample is not null
        assertNotNull(sample);

        // check if the sample is either "A" or "B"
        assertTrue(sample.equals("A") || sample.equals("B"));
    }

    @Test
    public void testSampleWithSampleSize() {
        // create a DiscreteDistribution object
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>();

        // generate a random sample with sample size 3
        int sampleSize = 3;
        String[] samples = distribution.sample(sampleSize);

        // check if the samples are not null
        assertNotNull(samples);

        // check if the number of samples is correct
        int expectedSampleSize = 3;
        int actualSampleSize = samples.length;
        assertEquals(expectedSampleSize, actualSampleSize);

        // check if the samples are either "A" or "B"
        for (String sample : samples) {
            assertTrue(sample.equals("A") || sample.equals("B"));
        }
    }
}