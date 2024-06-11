import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbability_SingleValue_Success() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        // Arrange
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        // Act
        double probabilityA = distribution.probability("A");
        double probabilityB = distribution.probability("B");

        // Assert
        Assert.assertEquals(0.2, probabilityA, 0.0001);
        Assert.assertEquals(0.3, probabilityB, 0.0001);
    }

    @Test
    public void testProbability_NullValue_Success() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        // Arrange
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>(null, 0.5));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        // Act
        double probabilityA = distribution.probability("A");
        double probabilityNull = distribution.probability(null);

        // Assert
        Assert.assertEquals(0.2, probabilityA, 0.0001);
        Assert.assertEquals(0.5, probabilityNull, 0.0001);
    }

    @Test
    public void testGetSamples_SampleList_Success() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        // Arrange
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        // Act
        List<Pair<String, Double>> actualSamples = distribution.getSamples();

        // Assert
        Assert.assertEquals(samples.size(), actualSamples.size());
        Assert.assertEquals(samples.get(0).getKey(), actualSamples.get(0).getKey());
        Assert.assertEquals(samples.get(0).getValue(), actualSamples.get(0).getValue(), 0.0001);
        Assert.assertEquals(samples.get(1).getKey(), actualSamples.get(1).getKey());
        Assert.assertEquals(samples.get(1).getValue(), actualSamples.get(1).getValue(), 0.0001);
    }

    @Test
    public void testSample_SingleValue_Success() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        // Arrange
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.8));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        // Act
        String sample = distribution.sample();

        // Assert
        Assert.assertNotNull(sample);
        Assert.assertTrue(sample.equals("A") || sample.equals("B"));
    }

    @Test
    public void testSample_SampleSize_Success() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        // Arrange
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.8));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        // Act
        String[] sample = distribution.sample(5);

        // Assert
        Assert.assertNotNull(sample);
        Assert.assertEquals(5, sample.length);
        for (String s : sample) {
            Assert.assertTrue(s.equals("A") || s.equals("B"));
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_InvalidSampleSize_ExceptionThrown() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        // Arrange
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.8));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        // Act
        distribution.sample(0);
    }
}