import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    @Test
    public void testProbability_validInput() {
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(Arrays.asList(
            new Pair<>(1, 0.2),
            new Pair<>(2, 0.3),
            new Pair<>(3, 0.5)
        ));

        assertEquals(0.2, dist.probability(1), 0.00001);
        assertEquals(0.3, dist.probability(2), 0.00001);
        assertEquals(0.5, dist.probability(3), 0.00001);
    }

    @Test
    public void testProbability_nullInput() {
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(Arrays.asList(
            new Pair<>(1, 0.2),
            new Pair<>(2, 0.3),
            new Pair<>(3, 0.5)
        ));

        assertEquals(0.0, dist.probability(null), 0.00001);
    }

    @Test
    public void testProbability_invalidInput() {
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(Arrays.asList(
            new Pair<>(1, 0.2),
            new Pair<>(2, 0.3),
            new Pair<>(3, 0.5)
        ));

        assertEquals(0.0, dist.probability(4), 0.00001);
    }

    @Test
    public void testGetSamples() {
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(Arrays.asList(
            new Pair<>(1, 0.2),
            new Pair<>(2, 0.3),
            new Pair<>(3, 0.5)
        ));

        List<Pair<Integer, Double>> samples = dist.getSamples();

        assertEquals(3, samples.size());

        assertEquals(1, (int) samples.get(0).getKey());
        assertEquals(0.2, samples.get(0).getValue(), 0.00001);

        assertEquals(2, (int) samples.get(1).getKey());
        assertEquals(0.3, samples.get(1).getValue(), 0.00001);

        assertEquals(3, (int) samples.get(2).getKey());
        assertEquals(0.5, samples.get(2).getValue(), 0.00001);
    }

    @Test
    public void testSample_validInput() {
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(Arrays.asList(
            new Pair<>(1, 0.2),
            new Pair<>(2, 0.3),
            new Pair<>(3, 0.5)
        ));

        int samples = 100000;
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;

        for (int i = 0; i < samples; i++) {
            int sample = dist.sample();
            if (sample == 1) {
                count1++;
            } else if (sample == 2) {
                count2++;
            } else if (sample == 3) {
                count3++;
            }
        }

        assertEquals(0.2, count1 / (double) samples, 0.01);
        assertEquals(0.3, count2 / (double) samples, 0.01);
        assertEquals(0.5, count3 / (double) samples, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_invalidInput() {
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(Arrays.asList(
            new Pair<>(1, 0.2),
            new Pair<>(2, 0.3),
            new Pair<>(3, 0.5)
        ));

        dist.sample(-1);
    }
}