
package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IntervalsSetTest {
    @Test
    public void testConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNull(intervalsSet.getTree());
    }

    @Test
    public void testConstructor2() {
        double lower = 0.0;
        double upper = 1.0;
        
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertEquals(lower, intervalsSet.getInf(), 0.0);
        assertEquals(upper, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testConstructor3() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(tree, intervalsSet.getTree());
    }

    @Test
    public void testConstructor4() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertEquals(boundary, intervalsSet.getBoundary());
    }

    @Test
    public void testBuildTree1() {
        double lower = Double.POSITIVE_INFINITY;
        double upper = Double.NEGATIVE_INFINITY;
        
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        assertEquals(Boolean.TRUE, tree.getAttribute());
        assertNull(tree.getCut());
        assertNull(tree.getMinus());
        assertNull(tree.getPlus());
    }

    @Test
    public void testBuildTree2() {
        double lower = Double.POSITIVE_INFINITY;
        double upper = 0.0;
        
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        assertEquals(new OrientedPoint(new Vector1D(upper), true).wholeHyperplane(), tree.getCut().getHyperplane());
        assertNotNull(tree.getMinus());
        assertNotNull(tree.getPlus());
        assertNull(tree.getRemoved());

        BSPTree<Euclidean1D> minusNode = tree.getMinus();
        assertEquals(Boolean.FALSE, minusNode.getAttribute());
        assertNull(minusNode.getCut());
        assertNull(minusNode.getMinus());
        assertNull(minusNode.getPlus());

        BSPTree<Euclidean1D> plusNode = tree.getPlus();
        assertEquals(Boolean.TRUE, plusNode.getAttribute());
        assertNull(plusNode.getCut());
        assertNull(plusNode.getMinus());
        assertNull(plusNode.getPlus());
    }

    @Test
    public void testBuildTree3() {
        double lower = 0.0;
        double upper = Double.POSITIVE_INFINITY;
        
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        assertEquals(new OrientedPoint(new Vector1D(lower), false).wholeHyperplane(), tree.getCut().getHyperplane());
        assertNotNull(tree.getMinus());
        assertNotNull(tree.getPlus());
        assertNull(tree.getRemoved());

        BSPTree<Euclidean1D> minusNode = tree.getMinus();
        assertEquals(Boolean.FALSE, minusNode.getAttribute());
        assertNull(minusNode.getCut());
        assertNull(minusNode.getMinus());
        assertNull(minusNode.getPlus());

        BSPTree<Euclidean1D> plusNode = tree.getPlus();
        assertEquals(new OrientedPoint(new Vector1D(upper), true).wholeHyperplane(), plusNode.getCut().getHyperplane());
        assertNotNull(plusNode.getMinus());
        assertNotNull(plusNode.getPlus());
        assertNull(plusNode.getRemoved());

        BSPTree<Euclidean1D> minusMinusNode = plusNode.getMinus();
        assertEquals(Boolean.FALSE, minusMinusNode.getAttribute());
        assertNull(minusMinusNode.getCut());
        assertNull(minusMinusNode.getMinus());
        assertNull(minusMinusNode.getPlus());

        BSPTree<Euclidean1D> minusPlusNode = plusNode.getPlus();
        assertEquals(Boolean.TRUE, minusPlusNode.getAttribute());
        assertNull(minusPlusNode.getCut());
        assertNull(minusPlusNode.getMinus());
        assertNull(minusPlusNode.getPlus());
    }

    @Test
    public void testBuildTree4() {
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        assertEquals(new OrientedPoint(new Vector1D(upper), true).wholeHyperplane(), tree.getCut().getHyperplane());
        assertNotNull(tree.getMinus());
        assertNotNull(tree.getPlus());
        assertNull(tree.getRemoved());

        BSPTree<Euclidean1D> minusNode = tree.getMinus();
        assertEquals(Boolean.FALSE, minusNode.getAttribute());
        assertNull(minusNode.getCut());
        assertNull(minusNode.getMinus());
        assertNull(minusNode.getPlus());

        BSPTree<Euclidean1D> plusNode = tree.getPlus();
        assertEquals(new OrientedPoint(new Vector1D(upper), true).wholeHyperplane(), plusNode.getCut().getHyperplane());
        assertNotNull(plusNode.getMinus());
        assertNotNull(plusNode.getPlus());
        assertNull(plusNode.getRemoved());

        BSPTree<Euclidean1D> minusMinusNode = plusNode.getMinus();
        assertEquals(Boolean.FALSE, minusMinusNode.getAttribute());
        assertNull(minusMinusNode.getCut());
        assertNull(minusMinusNode.getMinus());
        assertNull(minusMinusNode.getPlus());

        BSPTree<Euclidean1D> minusPlusNode = plusNode.getPlus();
        assertEquals(Boolean.TRUE, minusPlusNode.getAttribute());
        assertNull(minusPlusNode.getCut());
        assertNull(minusPlusNode.getMinus());
        assertNull(minusPlusNode.getPlus());
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertEquals(tree, newIntervalsSet.getTree());
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        // TODO: Add assertions
    }

    @Test
    public void testGetInf1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        assertEquals(Double.POSITIVE_INFINITY, inf, 0.0);
    }

    @Test
    public void testGetInf2() {
        BSPTree<Euclidean1D> node = new BSPTree<>();
        node.setCut(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane());
        
        IntervalsSet intervalsSet = new IntervalsSet(node);
        double inf = intervalsSet.getInf();
        assertEquals(2.0, inf, 0.0);
    }

    @Test
    public void testGetSup1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        assertEquals(Double.NEGATIVE_INFINITY, sup, 0.0);
    }

    @Test
    public void testGetSup2() {
        BSPTree<Euclidean1D> node = new BSPTree<>();
        node.setCut(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane());
        
        IntervalsSet intervalsSet = new IntervalsSet(node);
        double sup = intervalsSet.getSup();
        assertEquals(2.0, sup, 0.0);
    }

    @Test
    public void testAsList1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervals = intervalsSet.asList();
        assertTrue(intervals.isEmpty());
    }

    @Test
    public void testAsList2() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        tree.setMinus(new BSPTree<Euclidean1D>());
        tree.setPlus(new BSPTree<Euclidean1D>());
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        List<Interval> intervals = intervalsSet.asList();
        assertTrue(intervals.isEmpty());
    }
}
