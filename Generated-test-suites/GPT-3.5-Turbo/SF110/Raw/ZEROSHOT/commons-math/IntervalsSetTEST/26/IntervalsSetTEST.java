package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class IntervalsSetTest {

    private IntervalsSet intervalsSet;

    @Before
    public void setUp() {
        intervalsSet = new IntervalsSet();
    }

    // Test constructor
    @Test
    public void testConstructor() {
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertNull(tree.getCut());
        assertNull(tree.getAttribute());
    }

    // Test buildNew method
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertSame(tree, newIntervalsSet.getTree());
    }

    // Test computeGeometricalProperties method
    @Test
    public void testComputeGeometricalPropertiesWithoutCut() {
        intervalsSet.computeGeometricalProperties();
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        assertEquals(0.0, intervalsSet.getSize(), 0.0);
    }

    @Test
    public void testComputeGeometricalPropertiesWithCut() {
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        tree.insertCut(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane());
        intervalsSet.computeGeometricalProperties();
        assertEquals(1.0, intervalsSet.getBarycenter().getX(), Precision.SAFE_MIN);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
    }

    @Test
    public void testGetInf() {
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        tree.setAttribute(Boolean.TRUE);
        Interval interval = new Interval(1.0, 2.0);
        tree.insertCut(new OrientedPoint(new Vector1D(interval.getInf()), true).wholeHyperplane());
        assertEquals(interval.getInf(), intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
        tree.setAttribute(Boolean.TRUE);
        Interval interval = new Interval(1.0, 2.0);
        tree.insertCut(new OrientedPoint(new Vector1D(interval.getSup()), true).wholeHyperplane());
        assertEquals(interval.getSup(), intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        List<Interval> expectedList = Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(expectedList, intervalsSet.asList());
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        tree.setAttribute(Boolean.TRUE);
        tree.insertCut(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane());
        expectedList = Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, 1.0), new Interval(1.0, Double.POSITIVE_INFINITY));
        assertEquals(expectedList, intervalsSet.asList());
    }

    // Other test methods for the remaining methods in the class

}