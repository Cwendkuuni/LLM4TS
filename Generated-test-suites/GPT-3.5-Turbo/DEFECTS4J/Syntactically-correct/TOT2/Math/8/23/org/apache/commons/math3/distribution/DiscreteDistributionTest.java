
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {
    
    @Test
    public void testProbability() {
        // Create a sample distribution
        Pair<Integer, Double> sample1 = new Pair<>(1, 0.5);
        Pair<Integer, Double> sample2 = new Pair<>(2, 0.3);
        Pair<Integer, Double> sample3 = new Pair<>(3, 0.2);
        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(new Well19937c(), List.of(sample1, sample2, sample3));
            assertEquals(0.5, dist.probability(1), 0.0001);
            assertEquals(0.3, dist.probability(2), 0.0001);
            assertEquals(0.2, dist.probability(3), 0.0001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception thrown while creating the distribution");
        }
    }
    
    @Test
    public void testGetSamples() {
        Pair<Integer, Double> sample1 = new Pair<>(1, 0.5);
        Pair<Integer, Double> sample2 = new Pair<>(2, 0.3);
        Pair<Integer, Double> sample3 = new Pair<>(3, 0.2);
        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(new Well19937c(), List.of(sample1, sample2, sample3));
            assertEquals(List.of(sample1, sample2, sample3), dist.getSamples());
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception thrown while creating the distribution");
        }
    }
    
    @Test
    public void testSample() {
        Pair<Integer, Double> sample1 = new Pair<>(1, 0.5);
        Pair<Integer, Double> sample2 = new Pair<>(2, 0.3);
        Pair<Integer, Double> sample3 = new Pair<>(3, 0.2);
        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(new Well19937c(), List.of(sample1, sample2, sample3));
            assertNotNull(dist.sample());
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception thrown while creating the distribution");
        }
    }
    
    @Test
    public void testSampleWithSize() {
        Pair<Integer, Double> sample1 = new Pair<>(1, 0.5);
        Pair<Integer, Double> sample2 = new Pair<>(2, 0.3);
        Pair<Integer, Double> sample3 = new Pair<>(3, 0.2);
        try {
            DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(new Well19937c(), List.of(sample1, sample2, sample3));
            Integer[] sampleArray = dist.sample(5);
            assertEquals(5, sampleArray.length);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException | NotStrictlyPositiveException e) {
            fail("Exception thrown while creating the distribution");
        }
    }
}
