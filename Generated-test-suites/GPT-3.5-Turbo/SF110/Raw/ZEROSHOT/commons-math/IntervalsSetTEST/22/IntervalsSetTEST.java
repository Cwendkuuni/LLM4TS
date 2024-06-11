package org.apache.commons.math3.geometry.euclidean.oned;

import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.Vector;
import java.util.List;
import java.util.ArrayList;

public class IntervalsSetTest {

    @Test
    public void testConstructorEmpty() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);

        Assert.assertNull(tree.getCut());
        Assert.assertTrue(tree.getAttribute());
    }

    @Test
    public void testConstructorWithBounds() {
        double lower = -1.0;
        double upper = 1.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);

        Assert.assertNotNull(tree.getCut());
        Assert.assertFalse(tree.getAttribute());
    }

    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);

        Assert.assertEquals(tree, intervalsSet.getTree(false));
    }

    @Test
    public void testConstructorWithBoundary() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);

        Assert.assertEquals(boundary, intervalsSet.getTree(false).getBoundary());
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);

        Assert.assertEquals(tree, intervalsSet.getTree(false));
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        intervalsSet.computeGeometricalProperties();

        Assert.assertNotNull(intervalsSet.getBarycenter());
        Assert.assertTrue(intervalsSet.getSize() >= 0.0);
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 1.0);
        double inf = intervalsSet.getInf();

        Assert.assertEquals(-1.0, inf, 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 1.0);
        double sup = intervalsSet.getSup();

        Assert.assertEquals(1.0, sup, 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 1.0);
        List<Interval> list = intervalsSet.asList();

        Assert.assertEquals(1, list.size());
        Assert.assertEquals(-1.0, list.get(0).getInf(), 0.0);
        Assert.assertEquals(1.0, list.get(0).getSup(), 0.0);
    }

    @Test
    public void testRecurseList() {
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 1.0);
        BSPTree<Euclidean1D> node = intervalsSet.getTree(false);
        List<Interval> list = new ArrayList<>();
        intervalsSet.recurseList(node, list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        Assert.assertEquals(1, list.size());
        Assert.assertEquals(-1.0, list.get(0).getInf(), 0.0);
        Assert.assertEquals(1.0, list.get(0).getSup(), 0.0);
    }
}