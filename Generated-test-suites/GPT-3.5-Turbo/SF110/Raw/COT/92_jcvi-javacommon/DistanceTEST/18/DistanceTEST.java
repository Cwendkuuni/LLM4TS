
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance_withMinAndMax() {
        Distance distance = Distance.buildDistance(1, 5);
        
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        
        float expectedMean = (1/2.0f) + (5/2.0f);
        assertEquals(expectedMean, distance.getMean(), 0.001);
        
        float expectedStdDev = (5-1) / 6.0f;
        assertEquals(expectedStdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMeanAndStdDev() {
        Distance distance = Distance.buildDistance(2.5f, 1.2f);
        
        // Check that the given values are set correctly
        assertEquals(2.5f, distance.getMean(), 0.001);
        assertEquals(1.2f, distance.getStdDev(), 0.001);
        
        // Check that the min and max are calculated correctly
        float expectedDelta = 3.0f * 1.2f;
        int expectedPlusDelta = (int)(2.5f + expectedDelta);
        int expectedMinusDelta = (int)(2.5f - expectedDelta);
        assertEquals(expectedMinusDelta, distance.getMin());
        assertEquals(expectedPlusDelta, distance.getMax());
        
        float expectedMean = (expectedMinusDelta + expectedPlusDelta) / 2.0f;
        assertEquals(expectedMean, distance.getMean(), 0.001);
        
        assertEquals(1.2f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMeanAndStdDev_zeroDelta() {
        Distance distance = Distance.buildDistance(2.5f, 0.0f);
        
        // Check that the given values are set correctly
        assertEquals(2.5f, distance.getMean(), 0.001);
        assertEquals(0.0f, distance.getStdDev(), 0.001);
        
        // Check that the min and max are calculated correctly
        int expectedMin = (int)(2.5f);
        int expectedMax = (int)(2.5f);
        assertEquals(expectedMin, distance.getMin());
        assertEquals(expectedMax, distance.getMax());
        
        float expectedMean = (expectedMin + expectedMax) / 2.0f;
        assertEquals(expectedMean, distance.getMean(), 0.001);
        
        assertEquals(0.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_meanLessThanDelta() {
        Distance distance = Distance.buildDistance(2.0f, 0.1f);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        
        float expectedMean = distance.getMean();
        assertEquals(expectedMean, transformedDistance.getMean(), 0.001);
        
        float expectedCorrectedStdDev = (expectedMean - 1.015f) / 3.0f;
        assertEquals(expectedCorrectedStdDev, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_meanGreaterThanDelta() {
        Distance distance = Distance.buildDistance(5.0f, 1.0f);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.001);
    }

    // Test for hashCode, equals and toString methods can be added based on the requirements
    
}

