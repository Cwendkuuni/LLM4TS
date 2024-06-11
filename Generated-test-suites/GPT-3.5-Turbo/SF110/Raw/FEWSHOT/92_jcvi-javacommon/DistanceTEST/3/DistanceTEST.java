package org.jcvi.jillion.assembly.ca.frg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class DistanceTest {
    @Test
    public void testBuildDistance_withMinAndMaxAndMeanAndStdDev_shouldReturnDistanceObject() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMeanAndStdDev_shouldReturnDistanceObject() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
        // check if min and max are within the acceptable range based on mean and stdDev
        assertTrue(distance.getMin() <= distance.getMax());
        assertTrue(distance.getMean() - (3 * distance.getStdDev()) <= distance.getMin());
        assertTrue(distance.getMean() + (3 * distance.getStdDev()) >= distance.getMax());
    }
    
    @Test
    public void testBuildDistance_withMinAndMax_shouldReturnDistanceObjectWithComputedMeanAndStdDev() {
        Distance distance = Distance.buildDistance(1, 10);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        // check if mean and stdDev are computed correctly
        assertEquals((1.0f + 10.0f) / 2.0f, distance.getMean(), 0.001);
        assertEquals((10 - 1) / 6.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceMeanLessThanDelta_shouldReturnDistanceWithCorrectedStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 1.0f, 0.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001);
        // check if stdDev is corrected correctly
        assertEquals((distance.getMean() - Distance.MAX_ROUNDING_ERROR) / Distance.SIX, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceMeanGreaterThanEqualDelta_shouldReturnOriginalDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance, transformedDistance);
    }
    
    @Test
    public void testEquals_withSameDistanceObject_shouldReturnTrue() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertTrue(distance.equals(distance));
    }
    
    @Test
    public void testEquals_withEquivalentDistanceObjects_shouldReturnTrue() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_withDifferentDistanceObjects_shouldReturnFalse() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(2, 11, 4.0f, 1.0f);
        assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testHashCode_withEqualDistanceObjects_shouldReturnEqualHashCode() {
        Distance distance1 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Distance distance2 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testToString_shouldReturnFormattedString() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        assertEquals("5.000, std: 2.000", distance.toString());
    }
}