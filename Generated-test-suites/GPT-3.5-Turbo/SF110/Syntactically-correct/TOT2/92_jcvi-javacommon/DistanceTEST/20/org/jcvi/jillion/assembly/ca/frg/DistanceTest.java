package org.jcvi.jillion.assembly.ca.frg;
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 1.5f);
        assertNotNull(distance);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.001);
        assertEquals(1.5f, distance.getStdDev(), 0.001);
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}