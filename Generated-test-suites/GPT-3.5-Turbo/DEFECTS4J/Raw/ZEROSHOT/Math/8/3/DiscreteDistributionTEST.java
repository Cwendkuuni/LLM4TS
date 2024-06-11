import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        double p1 = dist.probability("A");
        assertEquals(0.2, p1, 0.0001);

        double p2 = dist.probability("B");
        assertEquals(0.3, p2, 0.0001);

        double p3 = dist.probability("C");
        assertEquals(0.5, p3, 0.0001);

        double p4 = dist.probability("D");
        assertEquals(0.0, p4, 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        List<Pair<String, Double>> result = dist.getSamples();

        assertEquals(3, result.size());
        assertEquals("A", result.get(0).getKey());
        assertEquals(0.2, result.get(0).getValue(), 0.0001);
        assertEquals("B", result.get(1).getKey());
        assertEquals(0.3, result.get(1).getValue(), 0.0001);
        assertEquals("C", result.get(2).getKey());
        assertEquals(0.5, result.get(2).getValue(), 0.0001);
    }

    @Test
    public void testSample() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        String result = dist.sample();

        assertNotNull(result);
        assertTrue(result.equals("A") || result.equals("B") || result.equals("C"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSampleNegativeSize() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        dist.sample(-1);
    }

    @Test
    public void testSampleSize() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        String[] result = dist.sample(100);

        assertNotNull(result);
        assertEquals(100, result.length);
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        dist.reseedRandomGenerator(123);

        String result1 = dist.sample();
        String result2 = dist.sample();

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotEquals(result1, result2);
    }

}