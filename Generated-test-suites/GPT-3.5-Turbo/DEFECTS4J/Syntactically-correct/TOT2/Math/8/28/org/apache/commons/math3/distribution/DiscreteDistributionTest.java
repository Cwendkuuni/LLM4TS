import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> discreteDistribution;

    @Before
    public void setUp() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.2));

        try {
            discreteDistribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.3, discreteDistribution.probability(1), 0.001);
        assertEquals(0.5, discreteDistribution.probability(2), 0.001);
        assertEquals(0.2, discreteDistribution.probability(3), 0.001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = discreteDistribution.getSamples();
        assertEquals(3, samples.size());
        assertEquals(1, (int) samples.get(0).getKey());
        assertEquals(0.3, samples.get(0).getValue(), 0.001);
    }

    @Test
    public void testSample() {
        Integer sample = discreteDistribution.sample();
        assertTrue(sample == 1 || sample == 2 || sample == 3);
    }

    @Test
    public void testSampleWithSize() {
        int sampleSize = 5;
        Integer[] samples = null;
        try {
            samples = discreteDistribution.sample(sampleSize);
        } catch (NotStrictlyPositiveException e) {
            e.printStackTrace();
        }
        assertEquals(sampleSize, samples.length);
    }
}