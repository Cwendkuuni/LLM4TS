import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;


public class DiscreteDistributionTest {
    
    private DiscreteDistribution<Integer> distribution;
    private DiscreteDistribution<String> distribution2;
    
    @Before
    public void setUp() {
        try {
            Pair<Integer, Double> pair1 = new Pair<Integer, Double>(1, 0.2);
            Pair<Integer, Double> pair2 = new Pair<Integer, Double>(2, 0.3);
            Pair<Integer, Double> pair3 = new Pair<Integer, Double>(3, 0.5);
            
            List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
            samples.add(pair1);
            samples.add(pair2);
            samples.add(pair3);
            
            distribution = new DiscreteDistribution<Integer>(samples);
            
            Pair<String, Double> pair4 = new Pair<String, Double>("A", 0.1);
            Pair<String, Double> pair5 = new Pair<String, Double>("B", 0.8);
            Pair<String, Double> pair6 = new Pair<String, Double>("C", 0.1);
            
            List<Pair<String, Double>> samples2 = new ArrayList<Pair<String, Double>>();
            samples2.add(pair4);
            samples2.add(pair5);
            samples2.add(pair6);
            
            distribution2 = new DiscreteDistribution<String>(samples2);
            
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testProbability_Integer() {
        assertEquals(0.2, distribution.probability(1), 0.00001);
        assertEquals(0.3, distribution.probability(2), 0.00001);
        assertEquals(0.5, distribution.probability(3), 0.00001);
        assertEquals(0, distribution.probability(4), 0.00001);
        assertNull(distribution.probability(null));
    }
    
    @Test
    public void testGetSamples_Integer() {
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<Pair<Integer, Double>>();
        expectedSamples.add(new Pair<Integer, Double>(1, 0.2));
        expectedSamples.add(new Pair<Integer, Double>(2, 0.3));
        expectedSamples.add(new Pair<Integer, Double>(3, 0.5));
        
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        
        assertEquals(expectedSamples.size(), samples.size());
        for (int i = 0; i < expectedSamples.size(); i++) {
            assertEquals(expectedSamples.get(i).getKey(), samples.get(i).getKey());
            assertEquals(expectedSamples.get(i).getValue(), samples.get(i).getValue(), 0.00001);
        }
    }
    
    @Test
    public void testSample_Integer() {
        boolean has1 = false;
        boolean has2 = false;
        boolean has3 = false;
        
        for (int i = 0; i < 1000; i++) {
            int value = distribution.sample();
            if (value == 1) {
                has1 = true;
            } else if (value == 2) {
                has2 = true;
            } else if (value == 3) {
                has3 = true;
            }
        }
        
        assertTrue(has1);
        assertTrue(has2);
        assertTrue(has3);
    }
    
    @Test
    public void testSample_Integer_SampleSize() {
        int sampleSize = 5;
        Integer[] sample = distribution.sample(sampleSize);
        
        assertEquals(sampleSize, sample.length);
        
        boolean has1 = false;
        boolean has2 = false;
        boolean has3 = false;
        
        for (int i = 0; i < sampleSize; i++) {
            if (sample[i] == 1) {
                has1 = true;
            } else if (sample[i] == 2) {
                has2 = true;
            } else if (sample[i] == 3) {
                has3 = true;
            }
        }
        
        assertTrue(has1);
        assertTrue(has2);
        assertTrue(has3);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_Integer_SampleSize_Exception() throws NotStrictlyPositiveException {
        int sampleSize = -5;
        distribution.sample(sampleSize);
    }
    
    @Test
    public void testProbability_String() {
        assertEquals(0.1, distribution2.probability("A"), 0.00001);
        assertEquals(0.8, distribution2.probability("B"), 0.00001);
        assertEquals(0.1, distribution2.probability("C"), 0.00001);
        assertEquals(0, distribution2.probability("D"), 0.00001);
        assertNull(distribution2.probability(null));
    }
    
    @Test
    public void testGetSamples_String() {
        List<Pair<String, Double>> expectedSamples = new ArrayList<Pair<String, Double>>();
        expectedSamples.add(new Pair<String, Double>("A", 0.1));
        expectedSamples.add(new Pair<String, Double>("B", 0.8));
        expectedSamples.add(new Pair<String, Double>("C", 0.1));
        
        List<Pair<String, Double>> samples = distribution2.getSamples();
        
        assertEquals(expectedSamples.size(), samples.size());
        for (int i = 0; i < expectedSamples.size(); i++) {
            assertEquals(expectedSamples.get(i).getKey(), samples.get(i).getKey());
            assertEquals(expectedSamples.get(i).getValue(), samples.get(i).getValue(), 0.00001);
        }
    }
    
    @Test
    public void testSample_String() {
        boolean hasA = false;
        boolean hasB = false;
        boolean hasC = false;
        
        for (int i = 0; i < 1000; i++) {
            String value = distribution2.sample();
            if (value.equals("A")) {
                hasA = true;
            } else if (value.equals("B")) {
                hasB = true;
            } else if (value.equals("C")) {
                hasC = true;
            }
        }
        
        assertTrue(hasA);
        assertTrue(hasB);
        assertTrue(hasC);
    }
    
    @Test
    public void testSample_String_SampleSize() {
        int sampleSize = 5;
        String[] sample = distribution2.sample(sampleSize);
        
        assertEquals(sampleSize, sample.length);
        
        boolean hasA = false;
        boolean hasB = false;
        boolean hasC = false;
        
        for (int i = 0; i < sampleSize; i++) {
            if (sample[i].equals("A")) {
                hasA = true;
            } else if (sample[i].equals("B")) {
                hasB = true;
            } else if (sample[i].equals("C")) {
                hasC = true;
            }
        }
        
        assertTrue(hasA);
        assertTrue(hasB);
        assertTrue(hasC);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_String_SampleSize_Exception() throws NotStrictlyPositiveException {
        int sampleSize = -5;
        distribution2.sample(sampleSize);
    }
    
}