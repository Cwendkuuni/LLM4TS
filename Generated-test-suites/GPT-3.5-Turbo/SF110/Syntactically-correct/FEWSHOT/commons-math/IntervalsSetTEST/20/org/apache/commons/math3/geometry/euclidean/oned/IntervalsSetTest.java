package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertTrue(intervalsSet.asList().isEmpty());
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testConstructor2() {
        IntervalsSet intervalsSet = new IntervalsSet(0, 1);
        List<Interval> expectedList = Arrays.asList(new Interval(0, 1));
        assertEquals(expectedList, intervalsSet.asList());
        assertEquals(0, intervalsSet.getInf(), 0.0);
        assertEquals(1, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testConstructor3() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(tree, intervalsSet.getTree(false));
    }

    @Test
    public void testConstructor4() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertEquals(boundary, intervalsSet.getBoundaryLoops());
    }

    @Test
    public void testBuildTree() {
        double lower = 0;
        double upper = 1;
        BSPTree<Euclidean1D> expectedTree = new BSPTree<>(new OrientedPoint(new Vector1D(lower), false).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                new BSPTree<>(new OrientedPoint(new Vector1D(upper), true).wholeHyperplane(),
                           new BSPTree<>(Boolean.FALSE),
                           new BSPTree<>(Boolean.TRUE),
                           null),
                null);
        assertEquals(expectedTree, IntervalsSet.buildTree(lower, upper));
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertEquals(tree, intervalsSet.getTree(false));
    }

    @Test
    public void testGetInf() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        tree.insertCut(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                new BSPTree<>(Boolean.TRUE),
                null);
        tree.insertCut(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                tree.getMinus(),
                null);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        tree.insertCut(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                new BSPTree<>(Boolean.TRUE),
                null);
        tree.insertCut(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(),
                tree.getMinus(),
                new BSPTree<>(Boolean.FALSE),
                null);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        Interval interval1 = new Interval(0, 1);
        Interval interval2 = new Interval(2, 3);
        Interval interval3 = new Interval(4, 5);
        tree.insertCut(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                new BSPTree<>(Boolean.TRUE),
                null);
        tree.insertCut(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                tree.getMinus(),
                null);
        tree.insertCut(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                tree.getPlus(),
                null);
        tree.insertCut(new OrientedPoint(new Vector1D(4.0), false).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                tree.getPlus(),
                null);
        tree.insertCut(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane(),
                tree.getPlus().getMinus(),
                new BSPTree<>(Boolean.FALSE),
                null);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        List<Interval> expectedList = Arrays.asList(interval1, interval2, interval3);
        assertEquals(expectedList, intervalsSet.asList());
    }

    @Test
    public void testRecurseList_withCutNullAndNoAttribute() {
        BSPTree<Euclidean1D> node = new BSPTree<>();
        List<Interval> list = new ArrayList<>();
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.recurseList(node, list, lower, upper);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testRecurseList_withCutNullAndAttributeTrue() {
        BSPTree<Euclidean1D> node = new BSPTree<>(Boolean.TRUE);
        List<Interval> list = new ArrayList<>();
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.recurseList(node, list, lower, upper);
        assertEquals(Arrays.asList(new Interval(lower, upper)), list);
    }

    @Test
    public void testRecurseList_withCutNotNullAndCheckPointInside() {
        BSPTree<Euclidean1D> node = new BSPTree<>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                new BSPTree<>(Boolean.TRUE),
                null);
        List<Interval> list = new ArrayList<>();
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.recurseList(node, list, lower, upper);
        List<Interval> expectedList = Arrays.asList(new Interval(lower, 1.0));
        assertEquals(expectedList, list);
    }

    @Test
    public void testSize() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(new OrientedPoint(new Vector1D(0.5), false).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                new BSPTree<>(Boolean.TRUE),
                null);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        intervalsSet.setSize(1.0);
        assertEquals(1.0, intervalsSet.getSize(), 0.0);
    }

    @Test
    public void testSetBarycenter_and_SetBarycenter_withLocationAndSafeMin() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        tree.insertCut(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                new BSPTree<>(Boolean.TRUE),
                null);
        tree.insertCut(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                tree.getMinus(),
                null);
        Interval interval1 = new Interval(0, 1);
        List<Interval> list = new ArrayList<>();
        list.add(interval1);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        intervalsSet.setBarycenter(new Vector1D(0.5));
        intervalsSet.setBarycenter(intervalsSet.getTree(false).getCut().getHyperplane().getLocation());
        assertEquals(new Vector1D(0.5), ((AbstractRegion<Euclidean1D, T>)intervalsSet).getBarycenter());
    }

    @Test
    public void testCheckPoint() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        tree.insertCut(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                new BSPTree<>(Boolean.TRUE),
                null);
        tree.insertCut(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(),
                new BSPTree<>(Boolean.FALSE),
                tree.getMinus(),
                null);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(Location.INSIDE, ((AbstractRegion<Euclidean1D, T>)intervalsSet).checkPoint(tree.getMinus(), new Vector1D(0.5)));
    }

}