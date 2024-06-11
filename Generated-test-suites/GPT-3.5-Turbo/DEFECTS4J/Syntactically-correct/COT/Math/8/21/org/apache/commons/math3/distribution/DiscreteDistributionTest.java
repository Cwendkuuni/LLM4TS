import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<Object, Double>> samples = new ArrayList<Pair<Object, Double>>();
        samples.add(new Pair<Object, Double>("A", 0.4));
        samples.add(new Pair<Object, Double>("B", 0.6));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(samples);
        
        double pA = distribution.probability("A");
        double pB = distribution.probability("B");
        
        Assert.assertEquals(0.4, pA, 0.00001);
        Assert.assertEquals(0.6, pB, 0.00001);
    }
    
    @Test
    public void testGetSamples() {
        List<Pair<Object, Double>> samples = new ArrayList<Pair<Object, Double>>();
        samples.add(new Pair<Object, Double>("A", 0.4));
        samples.add(new Pair<Object, Double>("B", 0.6));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(samples);
        
        List<Pair<Object, Double>> retrievedSamples = distribution.getSamples();
        
        Assert.assertEquals(samples, retrievedSamples);
    }
    
    @Test
    public void testSample() {
        List<Pair<Object, Double>> samples = new ArrayList<Pair<Object, Double>>();
        samples.add(new Pair<Object, Double>("A", 0.4));
        samples.add(new Pair<Object, Double>("B", 0.6));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(samples);
        
        Object sample = distribution.sample();
        boolean validSample = sample.equals("A") || sample.equals("B");
        
        Assert.assertTrue(validSample);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNegativeSampleSize() {
        List<Pair<Object, Double>> samples = new ArrayList<Pair<Object, Double>>();
        samples.add(new Pair<Object, Double>("A", 0.4));
        samples.add(new Pair<Object, Double>("B", 0.6));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(samples);
        
        distribution.sample(-1);
    }
    
    @Test
    public void testSampleWithPositiveSampleSize() {
        List<Pair<Object, Double>> samples = new ArrayList<Pair<Object, Double>>();
        samples.add(new Pair<Object, Double>("A", 0.4));
        samples.add(new Pair<Object, Double>("B", 0.6));
        DiscreteDistribution<Object> distribution = new DiscreteDistribution<Object>(samples);
        
        Object[] sample = distribution.sample(5);
        
        Assert.assertEquals(5, sample.length);
        
        boolean validSamples = true;
        for (Object obj : sample) {
            if (!obj.equals("A") && !obj.equals("B")) {
                validSamples = false;
                break;
            }
        }
        
        Assert.assertTrue(validSamples);
    }

}