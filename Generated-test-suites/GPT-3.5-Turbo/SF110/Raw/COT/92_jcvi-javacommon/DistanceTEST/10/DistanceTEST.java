package org.jcvi.jillion.assembly.ca.frg;
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    // Test buildDistance(final int min, final int max, final float mean, final float stdDev) method
    @Test
    public void testBuildDistance1() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(2.5f, distance.getStdDev(), 0.001);
    }
    
    // Test buildDistance(final float mean, final float stdDev) method
    @Test
    public void testBuildDistance2() {
        Distance distance = Distance.buildDistance(5.5f, 2.5f);
        // Check that min <= mean <= max
        assertTrue(distance.getMin() <= distance.getMean());
        assertTrue(distance.getMean() <= distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(2.5f, distance.getStdDev(), 0.001);
    }
    
    // Test buildDistance(final int min, final int max) method
    @Test
    public void testBuildDistance3() {
        Distance distance = Distance.buildDistance(1, 10);
        float mean = (float)(1 + 10) / 2;
        float stdDev = (float)(10 - 1) / 6;
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(mean, distance.getMean(), 0.001);
        assertEquals(stdDev, distance.getStdDev(), 0.001);
    }
    
    // Test transformIntoCeleraAssemblerDistance(final Distance distance) method
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(2, 12, 7.0f, 1.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(2, transformedDistance.getMin());
        assertEquals(12, transformedDistance.getMax());
        assertEquals(7.0f, transformedDistance.getMean(), 0.001);
        assertEquals(5.0f / 3.0f, transformedDistance.getStdDev(), 0.001);
    }

    // Test hashCode() method
    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    // Test equals(Object obj) method
    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertTrue(distance1.equals(distance2));
    }
    
    // Test toString() method
    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(5.5f, 2.5f);
        assertEquals("5.500, std: 2.500", distance.toString());
    }
    
}