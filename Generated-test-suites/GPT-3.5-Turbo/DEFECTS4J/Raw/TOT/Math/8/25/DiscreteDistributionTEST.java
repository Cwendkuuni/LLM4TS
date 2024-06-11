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
    public void testConstructor() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        samples.add(new Pair<>(4, 0.1));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            Assert.assertEquals(4, distribution.getSamples().size());
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Exception thrown when constructing DiscreteDistribution: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, -0.2));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            Assert.fail("No exception thrown when constructing DiscreteDistribution with negative probability.");
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Incorrect exception thrown when constructing DiscreteDistribution with negative probability: " + e.getMessage());
        }
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        samples.add(new Pair<>(4, 0.1));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            Assert.assertEquals(0.4, distribution.probability(1), 0.000001);
            Assert.assertEquals(0.3, distribution.probability(2), 0.000001);
            Assert.assertEquals(0.2, distribution.probability(3), 0.000001);
            Assert.assertEquals(0.1, distribution.probability(4), 0.000001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Exception thrown when constructing DiscreteDistribution: " + e.getMessage());
        }
    }

    @Test
    public void testProbabilityWithNullValue() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(null, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            Assert.assertEquals(0.4, distribution.probability(1), 0.000001);
            Assert.assertEquals(0.3, distribution.probability(2), 0.000001);
            Assert.assertEquals(0.3, distribution.probability(null), 0.000001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Exception thrown when constructing DiscreteDistribution: " + e.getMessage());
        }
    }

    @Test
    public void testProbabilityWithNonexistentValue() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(null, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            Assert.assertEquals(0, distribution.probability(3), 0.000001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Exception thrown when constructing DiscreteDistribution: " + e.getMessage());
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        samples.add(new Pair<>(4, 0.1));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            RandomGenerator rng = new Well19937c(12345);

            distribution.reseedRandomGenerator(rng.nextLong());

            for (int i = 0; i < 1000; i++) {
                int sample = distribution.sample();
                Assert.assertTrue(sample >= 1 && sample <= 4);
            }
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Exception thrown when constructing DiscreteDistribution: " + e.getMessage());
        }
    }

    @Test
    public void testSampleWithInvalidSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        samples.add(new Pair<>(4, 0.1));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            distribution.sample(-1);
            Assert.fail("No exception thrown when sampling with invalid size.");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Incorrect exception thrown when sampling with invalid size: " + e.getMessage());
        }
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        samples.add(new Pair<>(4, 0.1));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            RandomGenerator rng = new Well19937c(12345);

            distribution.reseedRandomGenerator(rng.nextLong());

            Integer[] sampleArray = distribution.sample(1000);
            Assert.assertEquals(1000, sampleArray.length);

            for (int i = 0; i < 1000; i++) {
                Assert.assertTrue(sampleArray[i] >= 1 && sampleArray[i] <= 4);
            }
        } catch (NotStrictlyPositiveException | NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Exception thrown when constructing DiscreteDistribution: " + e.getMessage());
        }
    }

    @Test
    public void testSampleArrayWithInvalidSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.4));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        samples.add(new Pair<>(4, 0.1));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            distribution.sample(-1);
            Assert.fail("No exception thrown when sampling with invalid size.");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            Assert.fail("Incorrect exception thrown when sampling with invalid size: " + e.getMessage());
        }
    }
}