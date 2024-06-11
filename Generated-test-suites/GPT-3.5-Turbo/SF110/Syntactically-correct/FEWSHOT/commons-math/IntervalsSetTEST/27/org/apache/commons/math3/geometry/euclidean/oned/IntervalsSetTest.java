package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.AbstractRegion;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSet() {
        IntervalsSet set = new IntervalsSet();
        assertTrue(set.getTree(false).getCut() == null);
        List<Interval> list = new ArrayList<>();
        set.recurseList(set.getTree(false), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testIntervalsSetWithLowerAndUpper() {
        IntervalsSet set = new IntervalsSet(1.0, 3.0);
        BSPTree<Euclidean1D> tree = set.getTree(false);
        assertTrue(tree.getCut() != null);
        assertTrue(tree.getCut().getHyperplane() instanceof OrientedPoint);
        OrientedPoint cutHyperplane = (OrientedPoint) tree.getCut().getHyperplane();
        assertEquals(1.0, cutHyperplane.getLocation().getX(), 1E-15);
    }

    @Test
    public void testIntervalsSetWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet set = new IntervalsSet(tree);
        assertTrue(set.getTree(false) == tree);
    }

    @Test
    public void testIntervalsSetWithBoundary() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet set = new IntervalsSet(boundary);
        assertTrue(set.getTree(false).getCut() == null);
        List<Interval> list = new ArrayList<>();
        set.recurseList(set.getTree(false), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testBuildTreeWithLowerAndUpper() {
        double lower = -1.0;
        double upper = 1.0;
        
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        
        assertTrue(tree.getCut() != null);
        assertTrue(tree.getCut().getHyperplane() instanceof OrientedPoint);
        OrientedPoint cutHyperplane = (OrientedPoint) tree.getCut().getHyperplane();
        assertEquals(-1.0, cutHyperplane.getLocation().getX(), 1E-15);
    }

    @Test
    public void testBuildTreeWithDoubleInfinityLowerAndUpper() {
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        
        assertTrue(tree.getCut() == null);
        assertTrue(tree.getPlus().getAttribute() == Boolean.TRUE);
    }

    @Test
    public void testBuildTreeWithLowerAndDoubleInfinityUpper() {
        double lower = 0.0;
        double upper = Double.POSITIVE_INFINITY;
        
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        
        assertTrue(tree.getCut() != null);
        assertTrue(tree.getMinus().getAttribute() == Boolean.FALSE);
        assertTrue(tree.getMinus().getHyperplane() instanceof Cut);
    }

    @Test
    public void testBuildTreeWithDoubleInfinityLowerAndUpper() {
        double lower = Double.NEGATIVE_INFINITY;
        double upper = 0.0;
        
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        
        assertTrue(tree.getCut() != null);
        assertTrue(tree.getPlus().getAttribute() == Boolean.FALSE);
        assertTrue(tree.getPlus().getHyperplane() instanceof Cut);
    }

    @Test
    public void testComputeGeometricalPropertiesWithoutCut() {
        IntervalsSet set = new IntervalsSet();
        
        set.computeGeometricalProperties();
        
        assertTrue(Double.isNaN(((AbstractRegion<Euclidean1D, Euclidean1D>)set).getBarycenter().getX()));
        assertEquals(0.0, set.getSize(), 1E-15);
    }

    @Test
    public void testComputeGeometricalPropertiesWithCut() {
        double lower = -1.0;
        double upper = 1.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet set = new IntervalsSet(tree);
        
        set.computeGeometricalProperties();
        
        assertEquals(2.0, set.getSize(), 1E-15);
        assertEquals(0.0, ((AbstractRegion<Euclidean1D, Euclidean1D>)set).getBarycenter().getX(), 1E-15);
    }

    @Test
    public void testGetInf() {
        IntervalsSet set = new IntervalsSet(1.0, 3.0);
        
        double inf = set.getInf();
        
        assertEquals(Double.NEGATIVE_INFINITY, inf, 1E-15);
    }

    @Test
    public void testGetSup() {
        IntervalsSet set = new IntervalsSet(1.0, 3.0);
        
        double sup = set.getSup();
        
        assertEquals(Double.POSITIVE_INFINITY, sup, 1E-15);
    }

    @Test
    public void testAsList() {
        double lower1 = -1.0;
        double upper1 = 0.0;
        double lower2 = 1.0;
        double upper2 = 2.0;
        IntervalsSet set = new IntervalsSet(lower1, upper1);
        set.getTree(false).insertInTree(new OrientedPoint(new Vector1D(lower2), true).wholeHyperplane(), null);
        set.getTree(false).insertInTree(new OrientedPoint(new Vector1D(upper2), true).wholeHyperplane(), null);
        
        List<Interval> list = set.asList();
        
        assertEquals(2, list.size());
        assertEquals(lower1, list.get(0).getInf(), 1E-15);
        assertEquals(upper1, list.get(0).getSup(), 1E-15);
        assertEquals(lower2, list.get(1).getInf(), 1E-15);
        assertEquals(upper2, list.get(1).getSup(), 1E-15);
    }
}