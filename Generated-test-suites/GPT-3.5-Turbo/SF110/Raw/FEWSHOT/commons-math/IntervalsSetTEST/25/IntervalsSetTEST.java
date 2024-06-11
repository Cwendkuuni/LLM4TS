package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.geometry.Vector1D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IntervalsSetTest {
    @Test
    public void testConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);

        intervalsSet = new IntervalsSet(1.0, 2.0);
        assertNotNull(intervalsSet);

        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);

        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testBuildTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = intervalsSet.buildTree(1.0, 2.0);
        assertNotNull(tree);

        tree = intervalsSet.buildTree(Double.POSITIVE_INFINITY, 2.0);
        assertNotNull(tree);

        tree = intervalsSet.buildTree(1.0, Double.POSITIVE_INFINITY);
        assertNotNull(tree);

        tree = intervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertNotNull(tree);
    }

    @Test
    public void testBuildNew() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertNotNull(newIntervalsSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        intervalsSet.computeGeometricalProperties();
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);

        intervalsSet = new IntervalsSet(Double.POSITIVE_INFINITY, 2.0);
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);

        intervalsSet = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
        intervalsSet.computeGeometricalProperties();
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);

        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);

        intervalsSet = new IntervalsSet(Double.POSITIVE_INFINITY, 2.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);

        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 2.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);

        intervalsSet = new IntervalsSet(Double.POSITIVE_INFINITY, 2.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);

        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 2.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        List<Interval> list = intervalsSet.asList();
        assertNotNull(list);
    }

    @Test
    public void testRecurseList() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        BSPTree<Euclidean1D> node = new BSPTree<Euclidean1D>(Boolean.TRUE);
        List<Interval> list = new ArrayList<>();
        intervalsSet.recurseList(node, list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertNotNull(list);
    }
}