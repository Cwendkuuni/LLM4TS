import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionWithValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        // Test probability method
        assertEquals(0.2, distribution.probability(1), 0.01);
        assertEquals(0.3, distribution.probability(2), 0.01);
        assertEquals(0.5, distribution.probability(3), 0.01);

        // Test getSamples method
        List<Pair<Integer, Double>> samplesFromDistribution = distribution.getSamples();
        assertEquals(3, samplesFromDistribution.size());
        for (int i = 0; i < samplesFromDistribution.size(); i++) {
            assertEquals(singletonList(i + 1), samplesFromDistribution.get(i).getFirst());
            assertEquals(distribution.probability(i + 1), samplesFromDistribution.get(i).getSecond(), 0.01);
        }

        // Test sample method
        assertEquals(1, distribution.sample());
        assertEquals(2, distribution.sample());
        assertEquals(3, distribution.sample());

        // Test sample method with array
        Integer[] sampleArray = distribution.sample(3);
        for (int i = 0; i < 3; i++) {
            assertEquals(i + 1, sampleArray[i]);
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail();
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionWithProbabilitiesSumZero() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, -0.1));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail();
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail();
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

}