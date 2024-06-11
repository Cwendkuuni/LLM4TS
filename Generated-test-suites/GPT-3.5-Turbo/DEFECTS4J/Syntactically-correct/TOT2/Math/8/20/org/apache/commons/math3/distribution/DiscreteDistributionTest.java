import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setup() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Failed to create DiscreteDistribution");
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(1), 0.0001);
        assertEquals(0.3, distribution.probability(2), 0.0001);
        assertEquals(0.5, distribution.probability(3), 0.0001);
    }

    @Test
    public void testSample() {
        Integer sample = distribution.sample();
        assertNotNull(sample);
    }

    @Test
    public void testSampleWithSize() {
        int sampleSize = 5;
        Integer[] samples = null;
        
        try {
            samples = distribution.sample(sampleSize);
        } catch (NotStrictlyPositiveException e) {
            fail("Failed to generate sample with size");
        }

        assertNotNull(samples);
        assertEquals(sampleSize, samples.length);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithInvalidSize() throws NotStrictlyPositiveException {
        distribution.sample(-1);
    }

    @Test
    public void testInvalidConstructorInput() {
        List<Pair<Integer, Double>> invalidSamples = new ArrayList<>();
        invalidSamples.add(new Pair<>(1, -0.2));
        invalidSamples.add(new Pair<>(2, 0.3));

        try {
            distribution = new DiscreteDistribution<>(invalidSamples);
            fail("Failed to throw NotPositiveException");
        } catch (NotPositiveException e) {
            // Pass
        } catch (MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception thrown");
        }
    }

}