import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {

        List<Pair<Integer, Double>> samples = new ArrayList<>();

        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void probabilityTest() {
        assertEquals(0.5, distribution.probability(1), 0);
        assertEquals(0.3, distribution.probability(2), 0);
        assertEquals(0.2, distribution.probability(3), 0);
    }

    @Test
    public void getSamplesTest() {
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();

        expectedSamples.add(new Pair<>(1, 0.5));
        expectedSamples.add(new Pair<>(2, 0.3));
        expectedSamples.add(new Pair<>(3, 0.2));

        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();

        assertEquals(expectedSamples.size(), actualSamples.size());
        for (int i = 0; i < expectedSamples.size(); i++) {
            assertEquals(expectedSamples.get(i).getKey(), actualSamples.get(i).getKey());
            assertEquals(expectedSamples.get(i).getValue(), actualSamples.get(i).getValue(), 0);
        }
    }

    @Test
    public void sampleTest() {
        Integer sample = distribution.sample();

        assertTrue(sample >= 1 && sample <= 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void sampleSizeNotStrictlyPositive() throws NotStrictlyPositiveException {
        distribution.sample(0);
    }

    @Test
    public void sampleSizeTest() throws NotStrictlyPositiveException {
        Integer[] samples = distribution.sample(10);

        assertEquals(10, samples.length);

        for (Integer sample : samples) {
            assertTrue(sample >= 1 && sample <= 3);
        }
    }
}