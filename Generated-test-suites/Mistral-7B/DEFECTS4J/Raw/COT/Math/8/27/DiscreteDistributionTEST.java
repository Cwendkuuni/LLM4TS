import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstructorWithValidInputs() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.3));
        samples.add(new Pair<>(2.0, 0.4));
        samples.add(new Pair<>(3.0, 0.3));

        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(new Well19937c(), samples);
        } catch (MathIllegalArgumentException | NotPositiveException | MathArithmeticException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertNotNull(distribution);
        Assert.assertEquals(3, distribution.singletons.size());
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionConstructorWithInvalidInput1() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.3));
        samples.add(new Pair<>(2.0, 0.4));
        samples.add(new Pair<>(-1.0, 0.3)); // Negative value

        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.fail("Expected MathIllegalArgumentException");
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionConstructorWithInvalidInput2() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.3));
        samples.add(new Pair<>(2.0, 0.4));
        samples.add(new Pair<>(Double.POSITIVE_INFINITY, 0.3)); // Infinite value

        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(new Well19937c(), samples);
        } catch (NotPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.fail("Expected MathArithmeticException");
    }

    @Test
    public void testProbabilityMethodWithValidInputs() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.3));
        samples.add(new Pair<>(2.0, 0.4));
        samples.add(new Pair<>(3.0, 0.3));

        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Number testValue = 2.0;
        double expectedProbability = 0.4;
        double actualProbability = distribution.probability(testValue);

        Assert.assertEquals(expectedProbability, actualProbability, 1e-6);
    }

    @Test
    public void testProbabilityMethodWithInvalidInput() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.3));
        samples.add(new Pair<>(2.0, 0.4));
        samples.add(new Pair<>(3.0, 0.3));

        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Number testValue = null;
        double expectedProbability = 0.0;
        double actualProbability = distribution.probability(testValue);

        Assert.assertEquals(expectedProbability, actualProbability);
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.3));
        samples.add(new Pair<>(2.0, 0.4));
        samples.add(new Pair<>(3.0, 0.3));

        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        List<Pair<Number, Double>> actualSamples = distribution.getSamples();
        List<Pair<Number, Double>> expectedSamples = samples;

        Assert.assertEquals(expectedSamples, actualSamples);
    }

    @Test
    public void testSampleMethodWithValidInputs() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.3));
        samples.add(new Pair<>(2.0, 0.4));
        samples.add(new Pair<>(3.0, 0.3));

        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Number expectedSample = 2.0;
        Number actualSample = distribution.sample();

        Assert.assertEquals(expectedSample, actualSample);
    }

    @Test
    public void testSampleMethodWithMultipleSamples() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.3));
        samples.add(new Pair<>(2.0, 0.4));
        samples.add(new Pair<>(3.0, 0.3));

        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Number[] expectedSamples = {2.0, 1.0, 3.0, 2.0, 2.0};
        Number[] actualSamples = null;

        for (int i = 0; i < 5; i++) {
            actualSamples = (Number[]) java.lang.reflect.Array.newInstance(singletonType, 1);
            actualSamples[0] = distribution.sample();
        }

        Assert.assertArrayEquals(expectedSamples, actualSamples);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleMethodWithInvalidInput() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.3));
        samples.add(new Pair<>(2.0, 0.4));
        samples.add(new Pair<>(3.0, 0.3));

        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        distribution.sample(-1);

        Assert.fail("Expected NotStrictlyPositiveException");
    }

    @Test
    public void testReseedRandomGeneratorMethod() {
        List<Pair<Number, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.3));
        samples.add(new Pair<>(2.0, 0.4));
        samples.add(new Pair<>(3.0, 0.3));

        DiscreteDistribution<Number> distribution = null;
        try {
            distribution = new DiscreteDistribution<Number>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        long seed = 42;
        distribution.reseedRandomGenerator(seed);
    }

}