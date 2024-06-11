import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setup() {
        distribution = null;
    }

    @Test
    public void testConstructorWithValidSamples() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, 0.3));
            samples.add(new Pair<>(3, 0.5));
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Constructor with valid samples should not throw an exception");
        }
        assertNotNull(distribution);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeProbability() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.2));
        distribution = new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroProbabilitySum() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.0));
        distribution = new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInfiniteProbability() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, Double.POSITIVE_INFINITY));
        distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbabilityWithValidInputs() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, 0.3));
            samples.add(new Pair<>(3, 0.5));
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Constructor with valid samples should not throw an exception");
        }
        double prob = distribution.probability(2);
        assertEquals(0.3, prob, 0.00001);
    }

    @Test
    public void testProbabilityWithNullInput() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(null, 0.3));
            samples.add(new Pair<>(3, 0.5));
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Constructor with valid samples should not throw an exception");
        }
        double prob = distribution.probability(null);
        assertEquals(0.3, prob, 0.00001);
    }

    @Test
    public void testGetSamples() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, 0.3));
            samples.add(new Pair<>(3, 0.5));
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Constructor with valid samples should not throw an exception");
        }
        List<Pair<Integer, Double>> result = distribution.getSamples();
        assertEquals(samples, result);
    }

    @Test
    public void testSample() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, 0.3));
            samples.add(new Pair<>(3, 0.5));
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Constructor with valid samples should not throw an exception");
        }
        Integer result = distribution.sample();
        assertTrue(result >= 1 && result <= 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNonPositiveSampleSize() throws Exception {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, 0.3));
            samples.add(new Pair<>(3, 0.5));
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Constructor with valid samples should not throw an exception");
        }
        Integer[] result = distribution.sample(0);
    }

    @Test
    public void testSampleWithValidSampleSize() {
        try {
            List<Pair<Integer, Double>> samples = new ArrayList<>();
            samples.add(new Pair<>(1, 0.2));
            samples.add(new Pair<>(2, 0.3));
            samples.add(new Pair<>(3, 0.5));
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Constructor with valid samples should not throw an exception");
        }
        Integer[] result = distribution.sample(5);
        assertEquals(5, result.length);
    }
}