
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> discreteDistribution;

    @Before
    public void setUp() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.3));
        discreteDistribution = new DiscreteDistribution<>(samples);
    }

    @After
    public void tearDown() {
        discreteDistribution = null;
    }

    @Test
    public void testProbability() {
        double probability1 = discreteDistribution.probability(1);
        double probability2 = discreteDistribution.probability(2);
        double probability3 = discreteDistribution.probability(3);
        double probabilityNonExistent = discreteDistribution.probability(4);

        Assert.assertEquals(0.2, probability1, 0);
        Assert.assertEquals(0.5, probability2, 0);
        Assert.assertEquals(0.3, probability3, 0);
        Assert.assertEquals(0, probabilityNonExistent, 0);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(1, 0.2));
        expectedSamples.add(new Pair<>(2, 0.5));
        expectedSamples.add(new Pair<>(3, 0.3));

        List<Pair<Integer, Double>> actualSamples = discreteDistribution.getSamples();

        Assert.assertEquals(expectedSamples, actualSamples);
    }

    @Test
    public void testSample() {
        int sample = discreteDistribution.sample();

        Assert.assertTrue(sample >= 1 && sample <= 3);
    }

    @Test
    public void testSampleWithSize() throws NotStrictlyPositiveException {
        int sampleSize = 10;
        Integer[] sample = discreteDistribution.sample(sampleSize);

        Assert.assertEquals(sampleSize, sample.length);
        for (Integer value : sample) {
            Assert.assertTrue(value >= 1 && value <= 3);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNonPositiveSize() throws NotStrictlyPositiveException {
        int sampleSize = 0;
        discreteDistribution.sample(sampleSize);
    }

    @Test
    public void testReseedRandomGenerator() {
        long seed = 12345L;
        discreteDistribution.reseedRandomGenerator(seed);

        // Since the reseeding is not directly observable, we can only check if the method does not throw an exception
    }

    @Test
    public void testConstructorWithInvalidSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, -0.5));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> invalidDistribution = new DiscreteDistribution<>(samples);
            Assert.fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testConstructorWithZeroProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.0));

        try {
            DiscreteDistribution<Integer> invalidDistribution = new DiscreteDistribution<>(samples);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testConstructorWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution<Integer> invalidDistribution = new DiscreteDistribution<>(samples);
            Assert.fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getClass().getSimpleName());
        }
    }
}
