import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistanceWithInts() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithFloats() {
        Distance distance = Distance.buildDistance(15.0f, 2.5f);
        assertEquals(12, distance.getMin());
        assertEquals(18, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(2.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithIntsAndCalculateMeanAndStdDev() {
        Distance distance = Distance.buildDistance(10, 20);
        assertEquals(10, distance.getMin());
        assertEquals(20, distance.getMax());
        assertEquals(15.0f, distance.getMean(), 0.001);
        assertEquals(1.666f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(10, transformedDistance.getMin());
        assertEquals(20, transformedDistance.getMax());
        assertEquals(15.0f, transformedDistance.getMean(), 0.001);
        assertEquals(4.328f, transformedDistance.getStdDev(), 0.001);
        
        distance = Distance.buildDistance(5, 10, 7.0f, 1.5f);
        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(5, transformedDistance.getMin());
        assertEquals(10, transformedDistance.getMax());
        assertEquals(7.0f, transformedDistance.getMean(), 0.001);
        assertEquals(1.995f, transformedDistance.getStdDev(), 0.001);
        
        distance = Distance.buildDistance(3, 8, 5.0f, 1.0f);
        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(3, transformedDistance.getMin());
        assertEquals(8, transformedDistance.getMax());
        assertEquals(5.0f, transformedDistance.getMean(), 0.001);
        assertEquals(1.328f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
        
        distance2 = Distance.buildDistance(12, 20, 15.0f, 2.5f);
        assertNotEquals(distance1.hashCode(), distance2.hashCode());
        
        distance2 = Distance.buildDistance(10, 20, 18.0f, 2.5f);
        assertNotEquals(distance1.hashCode(), distance2.hashCode());
        
        distance2 = Distance.buildDistance(10, 20, 15.0f, 1.8f);
        assertNotEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertEquals(distance1, distance2);
        
        distance2 = Distance.buildDistance(12, 20, 15.0f, 2.5f);
        assertNotEquals(distance1, distance2);
        
        distance2 = Distance.buildDistance(10, 20, 18.0f, 2.5f);
        assertNotEquals(distance1, distance2);
        
        distance2 = Distance.buildDistance(10, 20, 15.0f, 1.8f);
        assertNotEquals(distance1, distance2);
    }
    
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        assertEquals("15.000, std: 2.500", distance.toString());
        
        distance = Distance.buildDistance(5, 10, 7.0f, 1.5f);
        assertEquals("7.000, std: 1.500", distance.toString());
        
        distance = Distance.buildDistance(3, 8, 5.0f, 1.0f);
        assertEquals("5.000, std: 1.000", distance.toString());
    }
}