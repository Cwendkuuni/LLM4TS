
package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet.Interval;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.Precision;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testConstructor() {
        IntervalsSet set = new IntervalsSet();
        assertNotNull(set);
    }

    @Test
    public void testConstructorWithLowerAndUpper() {
        IntervalsSet set = new IntervalsSet(1.0, 3.0);
        assertNotNull(set);
        assertEquals(1.0, set.getInf(), Precision.EPSILON);
        assertEquals(3.0, set.getSup(), Precision.EPSILON);
    }

    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet set = new IntervalsSet(tree);
        assertNotNull(set);
        assertEquals(tree, set.getTree(false));
    }

    @Test
    public void testConstructorWithBoundary() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet set = new IntervalsSet(boundary);
        assertNotNull(set);
        assertEquals(boundary, set.getBoundaryLoops());
    }

    @Test
    public void testBuildTree() {
        IntervalsSet set = new IntervalsSet();
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        BSPTree<Euclidean1D> tree = set.buildTree(lower, upper);
        assertNotNull(tree);
        assertEquals(lower, set.getInf(), Precision.EPSILON);
        assertEquals(upper, set.getSup(), Precision.EPSILON);
    }

    @Test
    public void testBuildNew() {
        IntervalsSet set = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet newSet = set.buildNew(tree);
        assertNotNull(newSet);
        assertEquals(tree, newSet.getTree(false));
    }

    @Test
    public void testComputeGeometricalPropertiesWithNullCut() {
        IntervalsSet set = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.FALSE);
        set.getTree(false).insertCut(tree);
        set.computeGeometricalProperties();
        assertEquals(Vector1D.NaN, set.getBarycenter());
        assertEquals(0.0, set.getSize(), Precision.EPSILON);
    }

    @Test
    public void testComputeGeometricalPropertiesWithFiniteSize() {
        IntervalsSet set = new IntervalsSet();
        double lower = 0.0;
        double upper = 1.0;
        BSPTree<Euclidean1D> tree = set.buildTree(lower, upper);
        tree.setAttribute(Boolean.TRUE);
        set.computeGeometricalProperties();
        assertEquals(new Vector1D(0.5), set.getBarycenter());
        assertEquals(1.0, set.getSize(), Precision.EPSILON);
    }

    @Test
    public void testComputeGeometricalPropertiesWithInfiniteSize() {
        IntervalsSet set = new IntervalsSet();
        BSPTree<Euclidean1D> tree = set.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        tree.setAttribute(Boolean.TRUE);
        set.computeGeometricalProperties();
        assertEquals(Vector1D.NaN, set.getBarycenter());
        assertEquals(Double.POSITIVE_INFINITY, set.getSize(), Precision.EPSILON);
    }

    @Test
    public void testGetInf() {
        IntervalsSet set = new IntervalsSet();
        double inf = Double.NEGATIVE_INFINITY;
        assertEquals(inf, set.getInf(), Precision.EPSILON);
    }

    @Test
    public void testGetSup() {
        IntervalsSet set = new IntervalsSet();
        double sup = Double.POSITIVE_INFINITY;
        assertEquals(sup, set.getSup(), Precision.EPSILON);
    }

    @Test
    public void testAsList() {
        IntervalsSet set = new IntervalsSet();
        List<Interval> list = new ArrayList<>();
        set.asList(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testRecurseListWithNullCut() {
        IntervalsSet set = new IntervalsSet();
        List<Interval> list = new ArrayList<>();
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        set.recurseList(set.getTree(false), list, lower, upper);
        assertEquals(0, list.size());
    }

    @Test
    public void testRecurseListWithFiniteCut() {
        IntervalsSet set = new IntervalsSet();
        List<Interval> list = new ArrayList<>();
        double lower = 0.0;
        double upper = 1.0;
        SubHyperplane<Euclidean1D> cut = new OrientedPoint(new Vector1D(0.5), true).wholeHyperplane();
        BSPTree<Euclidean1D> tree = new BSPTree<>(cut, new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        set.getTree(false).insertCut(tree);
        set.recurseList(set.getTree(false), list, lower, upper);
        assertEquals(1, list.size());
        Interval interval = list.get(0);
        assertEquals(lower, interval.getInf(), Precision.EPSILON);
        assertEquals(upper, interval.getSup(), Precision.EPSILON);
    }

}
