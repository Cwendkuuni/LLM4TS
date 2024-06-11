import org.junit.Test;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        // Arrange
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("Apple", 0.2));
        samples.add(new Pair<>("Banana", 0.3));
        samples.add(new Pair<>("Orange", 0.5));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        // Act
        double result1 = distribution.probability("Apple");
        double result2 = distribution.probability("Banana");
        double result3 = distribution.probability("Orange");
        
        // Assert
        assertEquals(0.2, result1, 0.0001);
        assertEquals(0.3, result2, 0.0001);
        assertEquals(0.5, result3, 0.0001);
    }

    @Test
    public void testGetSamples() {
        // Arrange
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("Apple", 0.2));
        samples.add(new Pair<>("Banana", 0.3));
        samples.add(new Pair<>("Orange", 0.5));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        // Act
        List<Pair<String, Double>> result = distribution.getSamples();
        
        // Assert
        assertEquals(samples, result);
    }

    @Test
    public void testSample() {
        // Arrange
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("Apple", 0.2));
        samples.add(new Pair<>("Banana", 0.3));
        samples.add(new Pair<>("Orange", 0.5));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        // Act
        String result = distribution.sample();
        
        // Assert
        assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(result)));
    }

    @Test
    public void testSampleWithSampleSize1() {
        // Arrange
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("Apple", 0.2));
        samples.add(new Pair<>("Banana", 0.3));
        samples.add(new Pair<>("Orange", 0.5));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        // Act
        String[] result = distribution.sample(1);
        
        // Assert
        assertEquals(1, result.length);
        assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(result[0])));
    }

    @Test
    public void testSampleWithSampleSize5() {
        // Arrange
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("Apple", 0.2));
        samples.add(new Pair<>("Banana", 0.3));
        samples.add(new Pair<>("Orange", 0.5));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        // Act
        String[] result = distribution.sample(5);
        
        // Assert
        assertEquals(5, result.length);
        for (String value : result) {
            assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(value)));
        }
    }
}