package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Assert.assertEquals(5, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(7.5f, distance.getMean(), 0.001f);
        Assert.assertEquals(1.5f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(7.5f, 1.5f);
        float delta = 1.5f * 3.0f;
        int plusDelta = (int) (7.5f + delta);
        int minusDelta = (int) (7.5f - delta);
        Assert.assertEquals(Math.min(plusDelta, minusDelta), distance.getMin());
        Assert.assertEquals(Math.max(plusDelta, minusDelta), distance.getMax());
        Assert.assertEquals(7.5f, distance.getMean(), 0.001f);
        Assert.assertEquals(1.5f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(5, 10);
        float mean = (5 / 2.0f) + (10 / 2.0f);
        float stdDev = (10 - 5) / 6.0f;
        Assert.assertEquals(5, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(mean, distance.getMean(), 0.001f);
        Assert.assertEquals(stdDev, distance.getStdDev(), 0.001f);
    }

    @Test 
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        if(distance.getMean() < calculateDelta(distance.getStdDev())) {
            float correctedStdDev = (distance.getMean() - 1.015f) / 3.0f;
            Assert.assertEquals(distance.getMin(), transformedDistance.getMin());
            Assert.assertEquals(distance.getMax(), transformedDistance.getMax());
            Assert.assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001f);
            Assert.assertEquals(correctedStdDev, transformedDistance.getStdDev(), 0.001f);
        }
        else {
            Assert.assertEquals(distance.getMin(), transformedDistance.getMin());
            Assert.assertEquals(distance.getMax(), transformedDistance.getMax());
            Assert.assertEquals(distance.getMean(), transformedDistance.getMean(), 0.001f);
            Assert.assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0.001f);
        }
    }

    @Test
    public void testGetMean() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Assert.assertEquals(7.5f, distance.getMean(), 0.001f);
    }

    @Test
    public void testGetStdDev() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Assert.assertEquals(1.5f, distance.getStdDev(), 0.001f);
    }

    @Test
    public void testGetMin() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Assert.assertEquals(5, distance.getMin());
    }

    @Test
    public void testGetMax() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Assert.assertEquals(10, distance.getMax());
    }

    @Test
    public void testHashCode() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        int hashCode = 1;
        hashCode = 31 * hashCode + 10;
        hashCode = 31 * hashCode + Float.floatToIntBits(7.5f);
        hashCode = 31 * hashCode + 5;
        hashCode = 31 * hashCode + Float.floatToIntBits(1.5f);
        Assert.assertEquals(hashCode, distance.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Distance distance2 = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Assert.assertEquals(distance1, distance2);
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        String expectedString = String.format("%.3f, std: %.3f", 7.5f, 1.5f);
        Assert.assertEquals(expectedString, distance.toString());
    }
}