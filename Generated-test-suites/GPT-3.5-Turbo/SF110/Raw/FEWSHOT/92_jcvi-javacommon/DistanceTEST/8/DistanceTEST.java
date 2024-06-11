import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistance1() {
        Distance distance = Distance.buildDistance(3, 7, 5.0f, 1.0f);
        
        assertEquals(3, distance.getMin());
        assertEquals(7, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(1.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance2() {
        Distance distance = Distance.buildDistance(5.0f, 1.0f);
        float delta = calculateDelta(1.0f);
        int plusDelta = (int)(5.0f + delta);
        int minusDelta = (int)(5.0f - delta);
     
        assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
    }
    
    @Test
    public void testBuildDistance3() {
        Distance distance = Distance.buildDistance(3, 7);
        
        float mean = computeMean(3, 7);
        float stdDev = computeStandardDeviation(3, 7);
        
        assertEquals(3, distance.getMin());
        assertEquals(7, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance1() {
        Distance distance = new Distance(3, 7, 5.0f, 1.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        float mean = distance.getMean();
        float stdDev = distance.getStdDev();
        
        if (mean < calculateDelta(stdDev)) {
            float correctedStdDev = (mean - MAX_ROUNDING_ERROR) / SIX;
            assertEquals(correctedStdDev, transformedDistance.getStdDev(), 0.001);
        } else {
            assertEquals(stdDev, transformedDistance.getStdDev(), 0.001);
        }
        
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance2() {
        Distance distance = new Distance(3, 7, 6.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        float mean = distance.getMean();
        float stdDev = distance.getStdDev();
        
        if (mean < calculateDelta(stdDev)) {
            float correctedStdDev = (mean - MAX_ROUNDING_ERROR) / SIX;
            assertEquals(correctedStdDev, transformedDistance.getStdDev(), 0.001);
        } else {
            assertEquals(stdDev, transformedDistance.getStdDev(), 0.001);
        }
        
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
    }
    
    private float calculateDelta(float stdDev) {
        return 3.0f * stdDev;
    }
    
    private float computeStandardDeviation(int min, int max) {
        return (max - min) / SIX;
    }
    
    private float computeMean(int min, int max) {
        return min / 2.0f + max / 2.0f;
    }
}