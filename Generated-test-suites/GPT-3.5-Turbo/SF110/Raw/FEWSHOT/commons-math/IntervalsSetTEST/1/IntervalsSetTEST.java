
package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;

import java.util.Arrays;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testConstructor2() {
        double lower = 1.0;
        double upper = 10.0;

        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        assertNotNull(intervalsSet);
        assertEquals(lower, intervalsSet.getInf(), 0);
        assertEquals(upper, intervalsSet.getSup(), 0);
    }

    @Test
    public void testConstructor3() {
        double lower = -5.0;
        double upper = 5.0;

        OrientedPoint op1 = new OrientedPoint(new Vector1D(lower), false);
        OrientedPoint op2 = new OrientedPoint(new Vector1D(upper), true);

        SubHyperplane<Euclidean1D> lowerCut = op1.wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = op2.wholeHyperplane();

        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        tree.insertCut(lowerCut);
        tree.insertCut(upperCut);

        IntervalsSet intervalsSet = new IntervalsSet(tree);

        assertNotNull(intervalsSet);
        assertEquals(lower, intervalsSet.getInf(), 0);
        assertEquals(upper, intervalsSet.getSup(), 0);
    }

    @Test
    public void testConstructor4() {
        double lower = -2.0;
        double upper = 2.0;

        OrientedPoint op1 = new OrientedPoint(new Vector1D(lower), false);
        OrientedPoint op2 = new OrientedPoint(new Vector1D(upper), true);

        SubHyperplane<Euclidean1D> lowerCut = op1.wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = op2.wholeHyperplane();

        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        tree.insertCut(lowerCut);
        tree.insertCut(upperCut);

        List<SubHyperplane<Euclidean1D>> boundary = Arrays.asList(lowerCut, upperCut);

        IntervalsSet intervalsSet = new IntervalsSet(boundary);

        assertNotNull(intervalsSet);
        assertEquals(lower, intervalsSet.getInf(), 0);
        assertEquals(upper, intervalsSet.getSup(), 0);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.FALSE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertNotNull(intervalsSet);
        assertEquals(tree, intervalsSet.getTree(false));
    }

    @Test
        public void testComputeGeometricalProperties1() {
        double lower = -5.0;
        double upper = 5.0;

        OrientedPoint op1 = new OrientedPoint(new Vector1D(lower), false);
        OrientedPoint op2 = new OrientedPoint(new Vector1D(upper), true);

        SubHyperplane<Euclidean1D> lowerCut = op1.wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = op2.wholeHyperplane();

        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        tree.insertCut(lowerCut);
        tree.insertCut(upperCut);

        IntervalsSet intervalsSet = new IntervalsSet(tree);

        intervalsSet.computeGeometricalProperties();

        assertEquals(upper - lower, intervalsSet.getSize(), 0);
        assertEquals((upper + lower) / 2, intervalsSet.getBarycenter().getX(), 0);
    }

    @Test
    public void testComputeGeometricalProperties2() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.FALSE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);

        intervalsSet.computeGeometricalProperties();

        assertEquals(0, intervalsSet.getSize(), 0);
        double nan = Double.NaN;
        assertEquals(nan, intervalsSet.getBarycenter().getX(), 0);
    }

    @Test
    public void testGetInf() {
        double lower = -5.0;
        double upper = 5.0;

        OrientedPoint op1 = new OrientedPoint(new Vector1D(lower), false);
        OrientedPoint op2 = new OrientedPoint(new Vector1D(upper), true);

        SubHyperplane<Euclidean1D> lowerCut = op1.wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = op2.wholeHyperplane();

        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        tree.insertCut(lowerCut);
        tree.insertCut(upperCut);

        IntervalsSet intervalsSet = new IntervalsSet(tree);
        double inf = intervalsSet.getInf();

        assertEquals(lower, inf, 0);
    }

    @Test
    public void testGetSup() {
        double lower = -5.0;
        double upper = 5.0;
        
        OrientedPoint op1 = new OrientedPoint(new Vector1D(lower), false);
        OrientedPoint op2 = new OrientedPoint(new Vector1D(upper), true);

        SubHyperplane<Euclidean1D> lowerCut = op1.wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = op2.wholeHyperplane();

        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        tree.insertCut(lowerCut);
        tree.insertCut(upperCut);

        IntervalsSet intervalsSet = new IntervalsSet(tree);
        double sup = intervalsSet.getSup();

        assertEquals(upper, sup, 0);
    }

    @Test
    public void testAsList1() {
        double lower = -2.0;
        double upper = 2.0;

        OrientedPoint op1 = new OrientedPoint(new Vector1D(lower), false);
        OrientedPoint op2 = new OrientedPoint(new Vector1D(upper), true);

        SubHyperplane<Euclidean1D> lowerCut = op1.wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = op2.wholeHyperplane();

        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        tree.insertCut(lowerCut);
        tree.insertCut(upperCut);

        IntervalsSet intervalsSet = new IntervalsSet(tree);

        List<Interval> list = intervalsSet.asList();

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(lower, list.get(0).getInf(), 0);
        assertEquals(upper, list.get(0).getSup(), 0);
    }

    @Test
    public void testAsList2() {
        double lower = -5.0;
        double upper = 5.0;

        OrientedPoint op1 = new OrientedPoint(new Vector1D(lower), false);
        OrientedPoint op2 = new OrientedPoint(new Vector1D(upper), true);

        SubHyperplane<Euclidean1D> lowerCut = op1.wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = op2.wholeHyperplane();

        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        tree.insertCut(lowerCut);
        tree.insertCut(upperCut);

        IntervalsSet intervalsSet = new IntervalsSet(tree);

        List<Interval> list = intervalsSet.asList();

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(lower, list.get(0).getInf(), 0);
        assertEquals(lower, list.get(0).getSup(), 0);
        assertEquals(upper, list.get(1).getInf(), 0);
        assertEquals(upper, list.get(1).getSup(), 0);
    }

    @Test
    public void testAsList3() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.FALSE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);

        List<Interval> list = intervalsSet.asList();

        assertNotNull(list);
        assertEquals(0, list.size());
    }
}
