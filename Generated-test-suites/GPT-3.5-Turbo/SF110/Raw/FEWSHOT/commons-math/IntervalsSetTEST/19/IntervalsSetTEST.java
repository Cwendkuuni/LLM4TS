package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class IntervalsSetTest {

    private IntervalsSet intervalsSet;

    @Before
    public void setUp() {
        intervalsSet = new IntervalsSet();
    }

    @Test
    public void testConstructor() {
        IntervalsSet set = new IntervalsSet(1.0, 3.0);
        List<Interval> intervals = set.asList();
        assertEquals(1, intervals.size());
        Interval interval = intervals.get(0);
        assertEquals(1.0, interval.getInf(), Precision.EPSILON);
        assertEquals(3.0, interval.getSup(), Precision.EPSILON);
    }

    @Test
    public void testBuildTree() {
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(2.0, 4.0);
        assertTrue(tree.getCut() instanceof OrientedPoint);
        OrientedPoint point = (OrientedPoint) tree.getCut().getHyperplane();
        assertEquals(3.0, point.getLocation().getX(), Precision.EPSILON);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 2.0);
        IntervalsSet set = intervalsSet.buildNew(tree);
        assertSame(tree, set.getTree());
    }

    @Test
    public void testComputeGeometricalProperties() {
        intervalsSet.computeGeometricalProperties();
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), Precision.EPSILON);
    }

    @Test
    public void testGetInf() {
        double inf = intervalsSet.getInf();
        assertEquals(Double.POSITIVE_INFINITY, inf, Precision.EPSILON);
    }

    @Test
    public void testGetSup() {
        double sup = intervalsSet.getSup();
        assertEquals(Double.NEGATIVE_INFINITY, sup, Precision.EPSILON);
    }

    @Test
    public void testAsList() {
        List<Interval> intervals = intervalsSet.asList();
        assertTrue(intervals.isEmpty());
    }

    @Test
    public void testRecurseList() {
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 3.0);
        List<Interval> list = new ArrayList<>();
        intervalsSet.recurseList(tree, list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(1, list.size());
        Interval interval = list.get(0);
        assertEquals(1.0, interval.getInf(), Precision.EPSILON);
        assertEquals(3.0, interval.getSup(), Precision.EPSILON);
    }
}