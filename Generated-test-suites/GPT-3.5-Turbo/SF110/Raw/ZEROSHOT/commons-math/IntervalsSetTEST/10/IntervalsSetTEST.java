package org.apache.commons.math3.geometry.euclidean.oned;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testEmptyConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        Assert.assertNotNull(intervalsSet);
    }

    @Test
    public void testLowerUpperConstructor() {
        double lower = 1.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        Assert.assertNotNull(intervalsSet);
    }

    @Test
    public void testTreeConstructor() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        Assert.assertNotNull(intervalsSet);
    }

    @Test
    public void testBoundaryConstructor() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        Assert.assertNotNull(intervalsSet);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        Assert.assertNotNull(intervalsSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        // Verify the properties
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        // Verify inf value
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        // Verify sup value
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> list = intervalsSet.asList();
        // Verify the list
    }
}