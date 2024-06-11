package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.subhyperplane.OrientedPoint;
import org.junit.Test;

import static org.junit.Assert.*;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetEmptyConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetBoundariesConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);

        assertEquals(2.0, intervalsSet.getInf(), 0.0);
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetBSPTreeConstructor() {
        BSPTree<Euclidean1D> bspTree = new BSPTree<>(new OrientedPoint(3.0, true).wholeHyperplane());
        IntervalsSet intervalsSet = new IntervalsSet(bspTree);

        assertEquals(3.0, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetBoundaryConstructor() {
        OrientedPoint op1 = new OrientedPoint(2.0, true);
        OrientedPoint op2 = new OrientedPoint(5.0, false);

        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(op1.wholeHyperplane(), op2.wholeHyperplane()));

        assertEquals(2.0, intervalsSet.getInf(), 0.0);
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> bspTree = new BSPTree<>(new OrientedPoint(3.0, true).wholeHyperplane());
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(bspTree);

        assertEquals(bspTree, newIntervalsSet.getTree());
    }

    @Test
    public void testComputeGeometricalPropertiesWithNoCut() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);
        intervalsSet.computeGeometricalProperties();

        assertEquals(2.0, intervalsSet.getInf(), 0.0);
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
        assertEquals(3.5, intervalsSet.getBarycenter().getX(), 0.0);
        assertEquals(3.0, intervalsSet.getSize(), 0.0);
    }

    @Test
    public void testComputeGeometricalPropertiesWithCuts() {
        OrientedPoint op1 = new OrientedPoint(2.0, true);
        OrientedPoint op2 = new OrientedPoint(4.0, false);
        OrientedPoint op3 = new OrientedPoint(6.0, true);

        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(op1.wholeHyperplane(), op2.wholeHyperplane(), op3.wholeHyperplane()));
        intervalsSet.computeGeometricalProperties();

        assertEquals(2.0, intervalsSet.getInf(), 0.0);
        assertEquals(6.0, intervalsSet.getSup(), 0.0);
        assertEquals(4.0, intervalsSet.getBarycenter().getX(), 0.0);
        assertEquals(2.0, intervalsSet.getSize(), 0.0);
    }

    @Test
    public void testGetInfWithNoCut() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);

        assertEquals(2.0, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetInfWithCuts() {
        OrientedPoint op1 = new OrientedPoint(2.0, true);
        OrientedPoint op2 = new OrientedPoint(4.0, false);
        OrientedPoint op3 = new OrientedPoint(6.0, true);

        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(op1.wholeHyperplane(), op2.wholeHyperplane(), op3.wholeHyperplane()));

        assertEquals(2.0, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSupWithNoCut() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);

        assertEquals(5.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testGetSupWithCuts() {
        OrientedPoint op1 = new OrientedPoint(2.0, true);
        OrientedPoint op2 = new OrientedPoint(4.0, false);
        OrientedPoint op3 = new OrientedPoint(6.0, true);

        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(op1.wholeHyperplane(), op2.wholeHyperplane(), op3.wholeHyperplane()));

        assertEquals(6.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsListWithNoCut() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);

        assertEquals(1, intervalsSet.asList().size());
        assertEquals(2.0, intervalsSet.asList().get(0).getInf(), 0.0);
        assertEquals(5.0, intervalsSet.asList().get(0).getSup(), 0.0);
    }

    @Test
    public void testAsListWithCuts() {
        OrientedPoint op1 = new OrientedPoint(2.0, true);
        OrientedPoint op2 = new OrientedPoint(4.0, false);
        OrientedPoint op3 = new OrientedPoint(6.0, true);

        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(op1.wholeHyperplane(), op2.wholeHyperplane(), op3.wholeHyperplane()));

        assertEquals(2, intervalsSet.asList().size());
        assertEquals(2.0, intervalsSet.asList().get(0).getInf(), 0.0);
        assertEquals(4.0, intervalsSet.asList().get(0).getSup(), 0.0);
        assertEquals(4.0, intervalsSet.asList().get(1).getInf(), 0.0);
        assertEquals(6.0, intervalsSet.asList().get(1).getSup(), 0.0);
    }

    @Test
    public void testRecurseListWithNoCut() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);

        List<Interval> list = new ArrayList<>();
        intervalsSet.recurseList(intervalsSet.getTree(), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        assertEquals(1, list.size());
        assertEquals(2.0, list.get(0).getInf(), 0.0);
        assertEquals(5.0, list.get(0).getSup(), 0.0);
    }

    @Test
    public void testRecurseListWithCuts() {
        OrientedPoint op1 = new OrientedPoint(2.0, true);
        OrientedPoint op2 = new OrientedPoint(4.0, false);
        OrientedPoint op3 = new OrientedPoint(6.0, true);

        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(op1.wholeHyperplane(), op2.wholeHyperplane(), op3.wholeHyperplane()));

        List<Interval> list = new ArrayList<>();
        intervalsSet.recurseList(intervalsSet.getTree(), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        assertEquals(2, list.size());
        assertEquals(2.0, list.get(0).getInf(), 0.0);
        assertEquals(4.0, list.get(0).getSup(), 0.0);
        assertEquals(4.0, list.get(1).getInf(), 0.0);
        assertEquals(6.0, list.get(1).getSup(), 0.0);
    }
}