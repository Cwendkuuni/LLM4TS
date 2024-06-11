package org.jcvi.jillion.assembly.ca.frg.tests;

import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithIntMinAndMaxAndMeanAndStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance expected = new Distance(1, 10, 5.5f, 2.5f);
        Assert.assertEquals(expected, distance);
    }

    @Test
    public void testBuildDistanceWithFloatMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.5f, 2.5f);
        float delta = Distance.calculateDelta(2.5f);
        int plusDelta = (int)(5.5f + delta);
        int minusDelta = (int)(5.5f - delta);
        Distance expected = new Distance(Math.min(plusDelta, minusDelta), Math.max(plusDelta, minusDelta), 5.5f, 2.5f);
        Assert.assertEquals(expected, distance);
    }

    @Test
    public void testBuildDistanceWithIntMinAndMax() {
        Distance distance = Distance.buildDistance(1, 10);
        float mean = Distance.computeMean(1, 10);
        float stdDev = Distance.computeStandardDeviation(1, 10);
        Distance expected = new Distance(1, 10, mean, stdDev);
        Assert.assertEquals(expected, distance);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        Distance expected = distance;
        
        if (distance.getMean() < Distance.calculateDelta(distance.getStdDev())) {
            float correctedStdDev = (distance.getMean() - 1.015f) / 3.0f;
            expected = new Distance(distance.getMin(), distance.getMax(), distance.getMean(), correctedStdDev);
        }
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(expected, transformedDistance);
    }

    @Test
    public void testGetMean() {
        Distance distance = new Distance(1, 10, 5.5f, 2.5f);
        float expected = 5.5f;
        float actual = distance.getMean();
        Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testGetStdDev() {
        Distance distance = new Distance(1, 10, 5.5f, 2.5f);
        float expected = 2.5f;
        float actual = distance.getStdDev();
        Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testGetMin() {
        Distance distance = new Distance(1, 10, 5.5f, 2.5f);
        int expected = 1;
        int actual = distance.getMin();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMax() {
        Distance distance = new Distance(1, 10, 5.5f, 2.5f);
        int expected = 10;
        int actual = distance.getMax();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
        Distance distance2 = new Distance(1, 10, 5.5f, 2.5f);
        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
        Distance distance2 = new Distance(1, 10, 5.5f, 2.5f);
        Assert.assertEquals(distance1, distance2);
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(1, 10, 5.5f, 2.5f);
        String expected = "5.500, std: 2.500";
        String actual = distance.toString();
        Assert.assertEquals(expected, actual);
    }

}