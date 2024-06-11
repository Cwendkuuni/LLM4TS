import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> discreteDistribution;
    private List<Pair<Integer, Double>> samples;

    @Before
    public void setUp() {
        samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        try {
            discreteDistribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Failed to initialize DiscreteDistribution");
        }
    }

    @Test
    public void probability_ShouldCalculateProbabilityOfGivenValue() {
        try {
            assertEquals(0.2, discreteDistribution.probability(1), 0.0);
            assertEquals(0.3, discreteDistribution.probability(2), 0.0);
            assertEquals(0.5, discreteDistribution.probability(3), 0.0);
        } catch (Exception e) {
            fail("Failed to calculate probability");
        }
    }

    @Test
    public void getSamples_ShouldReturnProbabilityMassFunction() {
        try {
            List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
            expectedSamples.add(new Pair<>(1, 0.2));
            expectedSamples.add(new Pair<>(2, 0.3));
            expectedSamples.add(new Pair<>(3, 0.5));

            List<Pair<Integer, Double>> actualSamples = discreteDistribution.getSamples();

            assertEquals(expectedSamples.size(), actualSamples.size());

            for (int i = 0; i < expectedSamples.size(); i++) {
                Pair<Integer, Double> expected = expectedSamples.get(i);
                Pair<Integer, Double> actual = actualSamples.get(i);
                assertEquals(expected.getKey(), actual.getKey());
                assertEquals(expected.getValue(), actual.getValue(), 0.0);
            }
        } catch (Exception e) {
            fail("Failed to get probability mass function");
        }
    }

    @Test
    public void sample_ShouldGenerateRandomValue() {
        try {
            List<Integer> generatedValues = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                generatedValues.add(discreteDistribution.sample());
            }

            assertTrue(generatedValues.contains(1));
            assertTrue(generatedValues.contains(2));
            assertTrue(generatedValues.contains(3));
        } catch (Exception e) {
            fail("Failed to generate random value");
        }
    }

    @Test
    public void sample_ShouldGenerateRandomSampleOfGivenSize() {
        try {
            int sampleSize = 1000;
            Integer[] generatedSample = discreteDistribution.sample(sampleSize);

            assertEquals(sampleSize, generatedSample.length);

            int count1 = 0;
            int count2 = 0;
            int count3 = 0;
            for (Integer value : generatedSample) {
                if (value == 1) {
                    count1++;
                } else if (value == 2) {
                    count2++;
                } else if (value == 3) {
                    count3++;
                } else {
                    fail("Invalid value in generated sample");
                }
            }

            assertTrue(count1 > 0);
            assertTrue(count2 > 0);
            assertTrue(count3 > 0);
        } catch (Exception e) {
            fail("Failed to generate random sample");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void sample_ShouldThrowNotStrictlyPositiveException_WhenNumberOfSamplesIsNotPositive() throws Exception {
        int sampleSize = -1;
        discreteDistribution.sample(sampleSize);
    }

    @Test
    public void reseedRandomGenerator_ShouldReseedRandomGeneratorWithGivenSeed() {
        try {
            discreteDistribution.reseedRandomGenerator(12345);
        } catch (Exception e) {
            fail("Failed to reseed random generator");
        }
    }

    @Test
    public void constructor_ShouldThrowNotPositiveException_WhenProbabilityIsNegative() {
        List<Pair<Integer, Double>> samplesWithNegativeProbability = new ArrayList<>();
        samplesWithNegativeProbability.add(new Pair<>(1, 0.2));
        samplesWithNegativeProbability.add(new Pair<>(2, -0.3));
        samplesWithNegativeProbability.add(new Pair<>(3, 0.5));

        try {
            discreteDistribution = new DiscreteDistribution<>(samplesWithNegativeProbability);
            fail("Failed to throw NotPositiveException");
        } catch (NotPositiveException e) {
            // success
        } catch (Exception e) {
            fail("Failed to throw NotPositiveException");
        }
    }

    @Test
    public void constructor_ShouldThrowMathArithmeticException_WhenProbabilitiesDoNotSumToOne() {
        List<Pair<Integer, Double>> samplesWithInvalidProbabilitySum = new ArrayList<>();
        samplesWithInvalidProbabilitySum.add(new Pair<>(1, 0.2));

        try {
            discreteDistribution = new DiscreteDistribution<>(samplesWithInvalidProbabilitySum);
            fail("Failed to throw MathArithmeticException");
        } catch (MathArithmeticException e) {
            // success
        } catch (Exception e) {
            fail("Failed to throw MathArithmeticException");
        }
    }

    @Test
    public void constructor_ShouldThrowMathIllegalArgumentException_WhenProbabilityIsInfinite() {
        List<Pair<Integer, Double>> samplesWithInfiniteProbability = new ArrayList<>();
        samplesWithInfiniteProbability.add(new Pair<>(1, 0.2));
        samplesWithInfiniteProbability.add(new Pair<>(2, Double.POSITIVE_INFINITY));
        samplesWithInfiniteProbability.add(new Pair<>(3, 0.5));

        try {
            discreteDistribution = new DiscreteDistribution<>(samplesWithInfiniteProbability);
            fail("Failed to throw MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // success
        } catch (Exception e) {
            fail("Failed to throw MathIllegalArgumentException");
        }
    }

}