import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    // Test case for probability method
    @Test
    public void testProbability() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.4));
        samples.add(new Pair<>("B", 0.6));
        try {
            DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
            assertEquals(0.4, distribution.probability("A"), 0.0001);
            assertEquals(0.6, distribution.probability("B"), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test case for sample method
    @Test
    public void testSample() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        try {
            DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
            String sample = distribution.sample();
            assertNotNull(sample);
            assertTrue(sample.equals("A") || sample.equals("B") || sample.equals("C"));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test case for sample method with 0 sample size
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleZeroSampleSize() throws NotStrictlyPositiveException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        distribution.sample(0);
    }

    // Test case for sample method with positive sample size
    @Test
    public void testSamplePositiveSampleSize() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        try {
            DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
            String[] sampleArray = distribution.sample(3);
            assertNotNull(sampleArray);
            assertEquals(3, sampleArray.length);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test case for creating distribution with negative probability
    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", -0.2));
        new DiscreteDistribution<>(samples);
    }

}