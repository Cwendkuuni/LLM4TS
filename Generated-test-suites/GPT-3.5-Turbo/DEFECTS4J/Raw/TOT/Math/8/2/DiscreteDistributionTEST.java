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
    public void testProbability_WithNonNullX_ReturnsCorrectProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        // Arrange
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);

        // Act
        double probability = dist.probability(2);

        // Assert
        Assert.assertEquals(0.3, probability, 0.0);
    }

    @Test
    public void testProbability_WithNullX_ReturnsCorrectProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        // Arrange
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);

        // Act
        double probability = dist.probability(null);

        // Assert
        Assert.assertEquals(0.0, probability, 0.0);
    }

    @Test
    public void testGetSamples_ReturnsCorrectSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        // Arrange
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);

        // Act
        List<Pair<Integer, Double>> result = dist.getSamples();

        // Assert
        Assert.assertEquals(samples, result);
    }

    @Test
    public void testSample_ReturnsRandomValue() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        // Arrange
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);

        // Act
        Integer result = dist.sample();

        // Assert
        Assert.assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(result)));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_WithNonPositiveSampleSize_ThrowsException() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException, NotStrictlyPositiveException {
        // Arrange
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);

        // Act
        Integer[] result = dist.sample(-1);
    }

    @Test
    public void testSample_WithPositiveSampleSize_ReturnsArrayofCorrectSize() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException, NotStrictlyPositiveException {
        // Arrange
        int sampleSize = 5;
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);

        // Act
        Integer[] result = dist.sample(sampleSize);

        // Assert
        Assert.assertEquals(sampleSize, result.length);
    }

}