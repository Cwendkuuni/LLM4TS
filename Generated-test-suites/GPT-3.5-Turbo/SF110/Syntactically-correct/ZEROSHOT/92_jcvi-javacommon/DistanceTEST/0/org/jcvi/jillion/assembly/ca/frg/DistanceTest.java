package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    private Distance distance;

    @Before
    public void setup() {
        distance = Distance.buildDistance(10, 20, 15.0f, 4.0f);
    }

    @Test
    public void testBuildDistanceWithMinAndMaxMeanAndStdDev() {
        Distance expectedDistance = new Distance(10, 20, 15.0f, 4.0f);
        assertEquals(expectedDistance, distance);
    }

    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance expectedDistance = new Distance(11, 19, 15.0f, 4.0f);
        assertEquals(expectedDistance, distance);
    }

    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance expectedDistance = new Distance(10, 20, 15.0f, 5.0f);
        assertEquals(expectedDistance, distance);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Distance expectedDistance = new Distance(10, 20, 15.0f, 4.238333f);
        assertEquals(expectedDistance, transformedDistance);
    }

    @Test
    public void testGetMean() {
        float expectedMean = 15.0f;
        assertEquals(expectedMean, distance.getMean(), 0.0f);
    }

    @Test
    public void testGetStdDev() {
        float expectedStdDev = 4.0f;
        assertEquals(expectedStdDev, distance.getStdDev(), 0.0f);
    }

    @Test
    public void testGetMin() {
        int expectedMin = 10;
        assertEquals(expectedMin, distance.getMin());
    }

    @Test
    public void testGetMax() {
        int expectedMax = 20;
        assertEquals(expectedMax, distance.getMax());
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = 217326897;
        assertEquals(expectedHashCode, distance.hashCode());
    }

    @Test
    public void testEquals() {
        Distance equalDistance = new Distance(10, 20, 15.0f, 4.0f);
        assertTrue(distance.equals(equalDistance));
    }

    @Test
    public void testNotEquals() {
        Distance notEqualDistance = new Distance(100, 200, 150.0f, 40.0f);
        assertFalse(distance.equals(notEqualDistance));
    }

    @Test
    public void testToString() {
        String expectedToString = "15.000, std: 4.000";
        assertEquals(expectedToString, distance.toString());
    }

}