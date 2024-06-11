import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionWithValidParameters() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(3, distribution.singletons.size());
        assertEquals(3, distribution.probabilities.length);
        for (int i = 0; i < distribution.probabilities.length; i++) {
            assertEquals("Unexpected probability value", (double) (i + 1) / 3.0, distribution.probabilities[i], 0.001);
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.1));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<Integer>(new Well19937c(), samples);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionWithZeroProbabilitiesSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.1));
        samples.add(new Pair<>(3, -0.2));

        try {
            new DiscreteDistribution<Integer>(new Well19937c(), samples);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<Integer>(new Well19937c(), samples);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
        }
    }

    @Test
    public void testProbabilityMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(0.1, distribution.probability((Integer) 1), 0.001);
        assertEquals(0.2, distribution.probability((Integer) 2), 0.001);
        assertEquals(0.3, distribution.probability((Integer) 3), 0.001);
        assertEquals(0.0, distribution.probability((Integer) 4), 0.001);
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        List<Pair<Integer, Double>> distributionSamples = distribution.getSamples();

        assertEquals(3, distributionSamples.size());
        for (int i = 0; i < distributionSamples.size(); i++) {
            assertEquals("Unexpected singleton value", (Integer) (i + 1), distributionSamples.get(i).getKey());
            assertEquals("Unexpected probability value", distribution.probabilities[i], distributionSamples.get(i).getValue(), 0.001);
        }
    }

    @Test
    public void testSampleMethodReturnExpectedValue() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        Integer sampledValue = distribution.sample();

        for (int i = 0; i < distribution.singletons.size(); i++) {
            if (sampledValue.equals(distribution.singletons.get(i))) {
                assertEquals("Unexpected probability value", distribution.probabilities[i], 1, 0.001);
                return;
            }
        }
        fail("Unexpected sample value");
    }

    @Test
    public void testSampleMethodReturnExpectedValues() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        Integer[] sampledValues = distribution.sample(5);

        for (int i = 0; i < sampledValues.length; i++) {
            for (int j = 0; j < distribution.singletons.size(); j++) {
                if (sampledValues[i].equals(distribution.singletons.get(j))) {
                    assertEquals("Unexpected probability value", distribution.probabilities[j], 1 / 5.0, 0.001);
                    return;
                }
            }
        }
        fail("Unexpected sample values");
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleMethodWithNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.1));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        distribution.sample(-1);
        fail("Expected NotStrictlyPositiveException");
    }

}