package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.geometry.Vector1D;
import org.apache.commons.math3.geometry.euclidean.oned.BSPTree;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IntervalsSetTest {
    @Test
    public void testConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(null, intervalsSet.buildNew(null));

        BSPTree<Euclidean1D> bspTree = new BSPTree<>();
        intervalsSet = new IntervalsSet(bspTree);
        assertEquals(bspTree, intervalsSet.buildNew(bspTree));

        intervalsSet = new IntervalsSet(new ArrayList<>());
        assertEquals(new ArrayList<>(), intervalsSet.buildNew(null).getBoundaries());

        Interval interval1 = new Interval(1.0, 2.0);
        Interval interval2 = new Interval(3.0, 4.0);
        List<SubHyperplane<Euclidean1D>> boundaries = new ArrayList<>();
        boundaries.add(interval1);
        boundaries.add(interval2);
        intervalsSet = new IntervalsSet(boundaries);
        assertEquals(boundaries, intervalsSet.buildNew(null).getBoundaries());
    }

    @Test
    public void testBuildTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> bspTree = new BSPTree<>();
        assertEquals(bspTree, intervalsSet.buildTree(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        bspTree = new BSPTree<>(Boolean.TRUE);
        assertEquals(bspTree, intervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        bspTree = new BSPTree<>(Boolean.FALSE);
        assertEquals(bspTree, intervalsSet.buildTree(1.0, Double.POSITIVE_INFINITY));
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), Precision.EPSILON);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());

        BSPTree<Euclidean1D> bspTree = new BSPTree<>();
        intervalsSet = new IntervalsSet(bspTree);
        intervalsSet.computeGeometricalProperties();
        assertEquals(0.0, intervalsSet.getSize(), Precision.EPSILON);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());

        Interval interval1 = new Interval(1.0, 2.0);
        Interval interval2 = new Interval(3.0, 4.0);
        List<SubHyperplane<Euclidean1D>> boundaries = new ArrayList<>();
        boundaries.add(interval1);
        boundaries.add(interval2);
        intervalsSet = new IntervalsSet(boundaries);
        intervalsSet.computeGeometricalProperties();
        assertEquals(3.0, intervalsSet.getSize(), Precision.EPSILON);
        assertEquals(new Vector1D(2.125), intervalsSet.getBarycenter());

    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), Precision.EPSILON);

        BSPTree<Euclidean1D> bspTree = new BSPTree<>();
        intervalsSet = new IntervalsSet(bspTree);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), Precision.EPSILON);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), Precision.EPSILON);

        BSPTree<Euclidean1D> bspTree = new BSPTree<>();
        intervalsSet = new IntervalsSet(bspTree);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), Precision.EPSILON);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervalList = new ArrayList<>();
        intervalsSet.recurseList(null, intervalList, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(new ArrayList<>(), intervalsSet.asList());

        BSPTree<Euclidean1D> bspTree = new BSPTree<>();
        intervalsSet = new IntervalsSet(bspTree);
        intervalList = new ArrayList<>();
        intervalsSet.recurseList(null, intervalList, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(new ArrayList<>(), intervalsSet.asList());

        Interval interval1 = new Interval(1.0, 2.0);
        Interval interval2 = new Interval(3.0, 4.0);
        List<SubHyperplane<Euclidean1D>> boundaries = new ArrayList<>();
        boundaries.add(interval1);
        boundaries.add(interval2);
        intervalsSet = new IntervalsSet(boundaries);
        intervalList = new ArrayList<>();
        intervalsSet.recurseList(null, intervalList, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(boundaries, intervalsSet.asList());
    }
}