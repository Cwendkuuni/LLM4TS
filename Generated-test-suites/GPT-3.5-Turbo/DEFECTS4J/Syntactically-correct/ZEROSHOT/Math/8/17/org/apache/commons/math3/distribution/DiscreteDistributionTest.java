import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> discreteDistribution;

    @Before
    public void setUp() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        discreteDistribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, discreteDistribution.probability(1), 0.0001);
        assertEquals(0.3, discreteDistribution.probability(2), 0.0001);
        assertEquals(0.5, discreteDistribution.probability(3), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(1, 0.2));
        expectedSamples.add(new Pair<>(2, 0.3));
        expectedSamples.add(new Pair<>(3, 0.5));

        List<Pair<Integer, Double>> actualSamples = discreteDistribution.getSamples();

        assertEquals(expectedSamples, actualSamples);
    }

    @Test
    public void testSample() {
        int value = discreteDistribution.sample();
        assertTrue(value >= 1 && value <= 3);
    }

    @Test
    public void testSampleWithSampleSize() throws NotStrictlyPositiveException {
        int sampleSize = 5;
        Integer[] sample = discreteDistribution.sample(sampleSize);
        assertEquals(sampleSize, sample.length);
        for (int value : sample) {
            assertTrue(value >= 1 && value <= 3);
        }
    }
}