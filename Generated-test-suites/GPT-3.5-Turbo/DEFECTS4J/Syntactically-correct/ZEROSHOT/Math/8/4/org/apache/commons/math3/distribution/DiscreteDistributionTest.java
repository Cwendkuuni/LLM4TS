import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Float> distribution;
    private RandomGenerator mockRandom;

    @Before
    public void setup() {
        mockRandom = mock(RandomGenerator.class);
        distribution = new DiscreteDistribution<>(mockRandom, createSampleData());
    }

    private List<Pair<Float, Double>> createSampleData() {
        List<Pair<Float, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0f, 0.3));
        samples.add(new Pair<>(2.0f, 0.5));
        samples.add(new Pair<>(3.0f, 0.2));
        return samples;
    }

    @Test
    public void testProbability() {
        double probability1 = distribution.probability(1.0f);
        double probability2 = distribution.probability(2.0f);
        double probability3 = distribution.probability(3.0f);
        double probability4 = distribution.probability(4.0f);

        assertEquals(0.3, probability1, 0.0001);
        assertEquals(0.5, probability2, 0.0001);
        assertEquals(0.2, probability3, 0.0001);
        assertEquals(0.0, probability4, 0.0001);

        verify(mockRandom, never()).nextDouble();
    }

    @Test(expected = NotPositiveException.class)
    public void testInvalidSampleData() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Float, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0f, -0.1));
        samples.add(new Pair<>(2.0f, 0.5));
        samples.add(new Pair<>(3.0f, 0.2));
        new DiscreteDistribution<>(samples);
    }

    @Test
    public void testSample() {
        when(mockRandom.nextDouble()).thenReturn(0.2);
        Float sample = distribution.sample();
        assertEquals(2.0f, sample, 0.0001);

        when(mockRandom.nextDouble()).thenReturn(0.7);
        sample = distribution.sample();
        assertEquals(3.0f, sample, 0.0001);

        verify(mockRandom, times(2)).nextDouble();
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSampleSize() throws NotStrictlyPositiveException {
        distribution.sample(0);
    }

    @Test
    public void testSampleArray() throws NotStrictlyPositiveException {
        when(mockRandom.nextDouble()).thenReturn(0.2, 0.7, 0.4);
        Float[] sample = distribution.sample(3);

        assertEquals(2.0f, sample[0], 0.0001);
        assertEquals(3.0f, sample[1], 0.0001);
        assertEquals(2.0f, sample[2], 0.0001);

        verify(mockRandom, times(3)).nextDouble();
    }
}