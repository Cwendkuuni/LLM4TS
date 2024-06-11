import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<String> discreteDistribution;

    @Before
    public void setUp() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.1));
        samples.add(new Pair<>("B", 0.2));
        samples.add(new Pair<>("C", 0.3));
        samples.add(new Pair<>("D", 0.4));
        discreteDistribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability_NullInput_ReturnZero() {
        Assert.assertEquals(0.0, discreteDistribution.probability(null), 0.0001);
    }

    @Test
    public void testProbability_ValidInput_ReturnProbability() {
        Assert.assertEquals(0.1, discreteDistribution.probability("A"), 0.0001);
        Assert.assertEquals(0.2, discreteDistribution.probability("B"), 0.0001);
        Assert.assertEquals(0.3, discreteDistribution.probability("C"), 0.0001);
        Assert.assertEquals(0.4, discreteDistribution.probability("D"), 0.0001);
    }

    @Test
    public void testProbability_InvalidInput_ReturnZero() {
        Assert.assertEquals(0.0, discreteDistribution.probability("E"), 0.0001);
        Assert.assertEquals(0.0, discreteDistribution.probability("F"), 0.0001);
    }

    @Test
    public void testGetSamples_ValidInput_ReturnSamples() {
        List<Pair<String, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>("A", 0.1));
        expectedSamples.add(new Pair<>("B", 0.2));
        expectedSamples.add(new Pair<>("C", 0.3));
        expectedSamples.add(new Pair<>("D", 0.4));

        List<Pair<String, Double>> actualSamples = discreteDistribution.getSamples();

        Assert.assertEquals(expectedSamples, actualSamples);
    }

    @Test
    public void testSample_ReturnRandomValue() {
        String sample = discreteDistribution.sample();

        Assert.assertNotNull(sample);
        Assert.assertTrue(sample.equals("A") || sample.equals("B") || sample.equals("C") || sample.equals("D"));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_InvalidSampleSize_ThrowException() {
        discreteDistribution.sample(-1);
    }

    @Test
    public void testSample_ValidSampleSize_ReturnSampleArray() throws NotStrictlyPositiveException {
        String[] sampleArray = discreteDistribution.sample(5);

        Assert.assertNotNull(sampleArray);
        Assert.assertEquals(5, sampleArray.length);

        for (String sample : sampleArray) {
            Assert.assertTrue(sample.equals("A") || sample.equals("B") || sample.equals("C") || sample.equals("D"));
        }
    }

    @Test
    public void testReseedRandomGenerator_ValidSeed_ReseedSuccessfully() {
        RandomGenerator randomGeneratorBeforeReseed = discreteDistribution.random;
        
        discreteDistribution.reseedRandomGenerator(123);
        
        RandomGenerator randomGeneratorAfterReseed = discreteDistribution.random;
        
        Assert.assertNotEquals(randomGeneratorBeforeReseed, randomGeneratorAfterReseed);
    }
}