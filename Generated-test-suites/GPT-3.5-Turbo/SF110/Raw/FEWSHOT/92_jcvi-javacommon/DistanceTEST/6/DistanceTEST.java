package org.jcvi.jillion.assembly.ca.frg;

import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {
    
    @Test
    public void testBuildDistanceWithMinMaxAndMeanStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15f, 2.5f);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
        Assert.assertEquals(15f, distance.getMean(), 0.01);
        Assert.assertEquals(2.5f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(15f, 2.5f);
        Assert.assertEquals(11, distance.getMin());
        Assert.assertEquals(19, distance.getMax());
        Assert.assertEquals(15f, distance.getMean(), 0.01);
        Assert.assertEquals(2.5f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(10, 20);
        Assert.assertEquals(10, distance.getMin());
        Assert.assertEquals(20, distance.getMax());
        Assert.assertEquals(15f, distance.getMean(), 0.01);
        Assert.assertEquals(1.666f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(10, 20, 15f, 2.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(10, transformedDistance.getMin());
        Assert.assertEquals(20, transformedDistance.getMax());
        Assert.assertEquals(15f, transformedDistance.getMean(), 0.01);
        Assert.assertEquals(4.328f, transformedDistance.getStdDev(), 0.01);
    }
}