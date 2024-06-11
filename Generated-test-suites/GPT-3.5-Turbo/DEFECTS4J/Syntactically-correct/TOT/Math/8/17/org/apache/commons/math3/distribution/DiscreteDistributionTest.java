import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
        
        // Typical use case
        double prob1 = distribution.probability(1);
        Assert.assertEquals(0.2, prob1, 0.0001);
        
        // Edge case: probability at lower bound
        double prob2 = distribution.probability(0);
        Assert.assertEquals(0.0, prob2, 0.0001);
        
        // Error scenario: probability at non-existent value
        double prob3 = distribution.probability(4);
        Assert.assertEquals(0.0, prob3, 0.0001);
    }

    @Test
    public void testSample() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
        
        // Typical use case
        Integer sample1 = distribution.sample();
        Assert.assertNotNull(sample1);
        
        // Edge case: sample size of 0
        Integer[] sample2 = distribution.sample(0);
        Assert.assertEquals(0, sample2.length);
        
        // Error scenario: non-positive sample size
        try {
            Integer[] sample3 = distribution.sample(-1);
            Assert.fail("NotStrictlyPositiveException should have been thrown");
        } catch (org.apache.commons.math3.exception.NotStrictlyPositiveException e) {
            // Expected exception
        }
    }
}