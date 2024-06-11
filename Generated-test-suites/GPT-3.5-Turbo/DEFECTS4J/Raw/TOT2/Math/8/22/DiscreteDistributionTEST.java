
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        assertEquals(0.2, distribution.probability("A"), 0.0001);
        assertEquals(0.3, distribution.probability("B"), 0.0001);
    }

    @Test
    public void testGetSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.3));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertEquals(samples, distribution.getSamples());
    }

    @Test
    public void testSample() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<>("A", 0.5));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        String sample = distribution.sample();
        assertTrue(sample.equals("A"));
    }

    @Test
    public void testSampleSize() throws NotPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(1, 0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer[] sampleArray = distribution.sample(5);
        assertEquals(5, sampleArray.length);
    }
}
