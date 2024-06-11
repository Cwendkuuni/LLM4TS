
package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.BSPTree;
import org.apache.commons.math3.geometry.OrientedPoint;
import org.apache.commons.math3.geometry.Vector1D;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntervalsSetTest {

    private IntervalsSet intervalsSet;
    private BSPTree<Euclidean1D> tree;

    @Before
    public void setUp() {
        intervalsSet = new IntervalsSet();

        // Create the test tree
        tree = new BSPTree<>(false);
        OrientedPoint op = new OrientedPoint(new Vector1D(0), true);
        tree.insertCut(op, false, false);
        tree.setAttribute(false);
        tree.getMinus().setAttribute(true);
        tree.getPlus().setAttribute(false);
    }
    
    @Test
    public void testBuildTree() {
        double lower = 0.1;
        double upper = 0.2;
        BSPTree<Euclidean1D> result = IntervalsSet.buildTree(lower, upper);

        OrientedPoint opLower = new OrientedPoint(new Vector1D(lower), false);
        OrientedPoint opUpper = new OrientedPoint(new Vector1D(upper), true);

        assertEquals(3, result.getDepth());
        assertTrue(result.getCut() instanceof OrientedPoint);
        assertEquals(opUpper, ((OrientedPoint) result.getCut().getHyperplane()).getLocation());
        assertEquals(opLower, ((OrientedPoint) result.getMinus().getCut().getHyperplane()).getLocation());
        assertEquals(opUpper, ((OrientedPoint) result.getPlus().getCut().getHyperplane()).getLocation());
    }
    
    @Test
    public void testConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();

        assertEquals(0, intervalsSet.asList().size());
        assertTrue(intervalsSet.isEmpty());
    }

    @Test
    public void testConstructor2() {
        double lower = 0.1;
        double upper = 0.2;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        assertEquals(lower, intervals.get(0).getInf(), 0);
        assertEquals(upper, intervals.get(0).getSup(), 0);
    }

    @Test
    public void testConstructor3() {
        IntervalsSet intervalsSet = new IntervalsSet(tree);

        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        assertEquals(tree, intervals.get(0).getInterval());
    }

    @Test
    public void testConstructor4() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(new SubOrientedPoint(new OrientedPoint(new Vector1D(0))));

        IntervalsSet intervalsSet = new IntervalsSet(boundary);

        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        assertEquals(new OrientedPoint(new Vector1D(0)), intervals.get(0).getInterval());
    }

    @Test
    public void testBuildNew() {
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);

        assertEquals(tree, newIntervalsSet.getTree(false));
    }

    @Test
    public void testComputeGeometricalProperties() {
        // Test for empty intervalsSet
        intervalsSet.computeGeometricalProperties();

        assertTrue(Double.isInfinite(intervalsSet.getSize()));
        assertTrue(Double.isNaN(intervalsSet.getBarycenter().getX()));

        // Test for non-empty intervalsSet
        double lower = 0.1;
        double upper = 0.2;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.computeGeometricalProperties();

        assertEquals(upper - lower, intervalsSet.getSize(), 0);
        assertEquals((upper + lower) / 2, intervalsSet.getBarycenter().getX(), 0);
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        double inf = intervalsSet.getInf();

        assertEquals(Double.NEGATIVE_INFINITY, inf, 0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        double sup = intervalsSet.getSup();

        assertEquals(Double.POSITIVE_INFINITY, sup, 0);
    }

    @Test
    public void testAsList() {
        double lower = 0.1;
        double upper = 0.2;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        List<Interval> intervals = intervalsSet.asList();

        assertEquals(1, intervals.size());
        assertEquals(lower, intervals.get(0).getInf(), 0);
        assertEquals(upper, intervals.get(0).getSup(), 0);
    }

    @Test
    public void testRecurseList() {
        double lower = 0.1;
        double upper = 0.2;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        List<Interval> intervals = new ArrayList<>();
        intervalsSet.recurseList(tree, intervals, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        assertEquals(1, intervals.size());
        assertEquals(lower, intervals.get(0).getInf(), 0);
        assertEquals(upper, intervals.get(0).getSup(), 0);
    }
}
