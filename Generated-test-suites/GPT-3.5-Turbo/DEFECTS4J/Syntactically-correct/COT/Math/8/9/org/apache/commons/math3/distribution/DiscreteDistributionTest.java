import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    // 
    
    @Test
    public void testConstructor() throws NotPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        
        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            Assert.fail("Constructor test failed.");
        }
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeProbability() throws NotPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, -0.3));
        samples.add(new Pair<>(3, 0.2));
        
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
    }
    
    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorInfiniteProbability() throws NotPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, Double.POSITIVE_INFINITY));
        
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
    }
    
    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        
        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            Assert.assertEquals(0.5, distribution.probability(1), 0.0001);
            Assert.assertEquals(0.3, distribution.probability(2), 0.0001);
            Assert.assertEquals(0.2, distribution.probability(3), 0.0001);
        } catch (Exception e) {
            Assert.fail("Probability test failed.");
        }
    }
    
    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        
        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            
            boolean oneFound = false;
            boolean twoFound = false;
            boolean threeFound = false;
            
            for (int i = 0; i < 1000; i++) {
                int sample = distribution.sample();
                
                if (sample == 1) {
                    oneFound = true;
                } else if (sample == 2) {
                    twoFound = true;
                } else if (sample == 3) {
                    threeFound = true;
                }
            }
            
            Assert.assertTrue(oneFound);
            Assert.assertTrue(twoFound);
            Assert.assertTrue(threeFound);
        } catch (Exception e) {
            Assert.fail("Sample test failed.");
        }
    }
    
    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void testSampleInvalidSize() throws org.apache.commons.math3.exception.NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        distribution.sample(-1);
    }
    
    @Test
    public void testSampleArraySize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));
        
        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            Integer[] sampleArray = distribution.sample(100);
            
            Assert.assertEquals(100, sampleArray.length);
        } catch (Exception e) {
            Assert.fail("Sample array size test failed.");
        }
    }
    
    // 
}