package org.jcvi.jillion.assembly.ca.frg;
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        assertTrue(distance.getMin() <= distance.getMean());
        assertTrue(distance.getMean() <= distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.001);
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    // Add more test cases for other methods

}