package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanAndStdDev() {
        Distance distance = Distance.buildDistance(1, 5, 3.0f, 0.5f);
        assertEquals(1, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.0f, distance.getMean(), 0.001);
        assertEquals(0.5f, distance.getStdDev(), 0.001);
    }

    // More test cases can be added for other methods

}