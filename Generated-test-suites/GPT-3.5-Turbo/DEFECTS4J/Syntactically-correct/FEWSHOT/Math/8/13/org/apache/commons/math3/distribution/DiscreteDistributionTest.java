
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;
    private RandomGenerator rng;

    @Before
    public void setUp() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        rng = mock(RandomGenerator.class);
        distribution = createDiscreteDistribution();
    }

    private DiscreteDistribution<Integer> createDiscreteDistribution() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>(
                Arrays.asList(
                        new Pair<Integer, Double>(1, 0.2),
                        new Pair<Integer, Double>(2, 0.4),
                        new Pair<Integer, Double>(3, 0.3),
                        new Pair<Integer, Double>(4, 0.1)
                )
        );
        return new DiscreteDistribution<Integer>(rng, samples);
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(1), 0.000001);
        assertEquals(0.4, distribution.probability(2), 0.000001);
        assertEquals(0.3, distribution.probability(3), 0.000001);
        assertEquals(0.1, distribution.probability(4), 0.000001);
        assertEquals(0, distribution.probability(0), 0.000001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expected = new ArrayList<Pair<Integer, Double>>(
                Arrays.asList(
                        new Pair<Integer, Double>(1, 0.2),
                        new Pair<Integer, Double>(2, 0.4),
                        new Pair<Integer, Double>(3, 0.3),
                        new Pair<Integer, Double>(4, 0.1)
                )
        );
        List<Pair<Integer, Double>> actual = distribution.getSamples();
        assertEquals(expected, actual);
    }

    @Test
    public void testSample() {
        when(rng.nextDouble()).thenReturn(0.15); // First bin
        assertEquals((Integer) 1, distribution.sample());

        when(rng.nextDouble()).thenReturn(0.35); // Second bin
        assertEquals((Integer) 2, distribution.sample());

        when(rng.nextDouble()).thenReturn(0.65); // Third bin
        assertEquals((Integer) 3, distribution.sample());

        when(rng.nextDouble()).thenReturn(0.95); // Fourth bin
        assertEquals((Integer) 4, distribution.sample());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithInvalidSampleSize() throws NotStrictlyPositiveException {
        distribution.sample(0);
    }

    @Test
    public void testSampleWithValidSampleSize() throws NotStrictlyPositiveException {
        int sampleSize = 5;
        when(rng.nextDouble()).thenReturn(0.15, 0.35, 0.65, 0.95, 0.75); // All bins
        Integer[] expected = {1, 2, 3, 4, 3};
        Integer[] actual = distribution.sample(sampleSize);
        assertEquals(Arrays.asList(expected), Arrays.asList(actual));
    }
}
