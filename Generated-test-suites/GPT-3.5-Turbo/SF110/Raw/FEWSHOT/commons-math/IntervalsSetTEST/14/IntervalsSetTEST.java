
package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        BSPTree<Euclidean1D> tree = intervalsSet.getTree();
        double lowerCutLoc = ((OrientedPoint) tree.getCut().getHyperplane()).getLocation().getX();
        assertEquals(lower, lowerCutLoc, 0.0);
        BSPTree<Euclidean1D> plus = tree.getPlus();
        double upperCutLoc = ((OrientedPoint) plus.getCut().getHyperplane()).getLocation().getX();
        assertEquals(upper, upperCutLoc, 0.0);
    }

    @Test
    public void testConstructor3() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(tree, intervalsSet.getTree());
    }

    @Test
    public void testConstructor4() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNull(intervalsSet.getTree());
    }

    @Test
    public void testBuildTree1() {
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        assertNull(tree.getCut());
        assertEquals(Boolean.TRUE, tree.getAttribute());
    }

    @Test
    public void testBuildTree2() {
        double lower = Double.NEGATIVE_INFINITY;
        double upper = 1.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        assertNull(tree.getCut());
        assertEquals(Boolean.TRUE, tree.getAttribute());
        BSPTree<Euclidean1D> plus = tree.getPlus();
        OrientedPoint upperCut = (OrientedPoint) plus.getCut().getHyperplane();
        assertEquals(upper, upperCut.getLocation().getX(), 0.0);
        assertEquals(true, upperCut.isDirect());
    }

    @Test
    public void testBuildTree3() {
        double lower = 0.0;
        double upper = Double.POSITIVE_INFINITY;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        OrientedPoint lowerCut = (OrientedPoint) tree.getCut().getHyperplane();
        assertEquals(lower, lowerCut.getLocation().getX(), 0.0);
        assertEquals(false, lowerCut.isDirect());

        assertEquals(Boolean.FALSE, tree.getAttribute());

        BSPTree<Euclidean1D> plus = tree.getPlus();
        OrientedPoint upperCut = (OrientedPoint) plus.getCut().getHyperplane();
        assertEquals(upper, upperCut.getLocation().getX(), 0.0);
        assertEquals(true, upperCut.isDirect());
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals("foo", intervalsSet.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", intervalsSet.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("foo\" bar", intervalsSet.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("foo and bar", intervalsSet.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", intervalsSet.stripLeadingAndTrailingQuotes("\"\""));
    }

    @Test
    public void testStripLeadingHyphens() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals("f", intervalsSet.stripLeadingHyphens("-f"));
        assertEquals("foo", intervalsSet.stripLeadingHyphens("--foo"));
        assertEquals("-foo", intervalsSet.stripLeadingHyphens("---foo"));
        assertNull(intervalsSet.stripLeadingHyphens(null));
    }

    @Test
    public void testBuildNew() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertEquals(tree, newIntervalsSet.getTree());
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertNull(intervalsSet.getTree());
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        assertEquals(0.0, intervalsSet.getSize(), 0.0);
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        intervalsSet.setTree(tree);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        intervalsSet.setTree(tree);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        intervalsSet.setTree(tree);
        List<Interval> list = intervalsSet.asList();
        assertEquals(0, list.size());
    }

    @Test
    public void testAsList2() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.FALSE);
        intervalsSet.setTree(tree);
        List<Interval> list = intervalsSet.asList();
        assertEquals(1, list.size());
        assertEquals(Double.NEGATIVE_INFINITY, list.get(0).getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, list.get(0).getSup(), 0.0);
    }

    @Test
    public void testAsList3() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.FALSE);
        intervalsSet.setTree(tree);
        BSPTree<Euclidean1D> plus = new BSPTree<>(Boolean.TRUE);
        SubHyperplane<Euclidean1D> cut = new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane();
        plus.setAttribute(cut);
        tree.insertIn(plus);
        List<Interval> list = intervalsSet.asList();
        assertEquals(0, list.size());
    }

    @Test
    public void testAsList4() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.FALSE);
        intervalsSet.setTree(tree);
        BSPTree<Euclidean1D> minus = new BSPTree<>(Boolean.TRUE);
        SubHyperplane<Euclidean1D> cut = new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane();
        minus.setAttribute(cut);
        tree.insertIn(minus);
        List<Interval> list = intervalsSet.asList();
        assertEquals(1, list.size());
        assertEquals(Double.NEGATIVE_INFINITY, list.get(0).getInf(), 0.0);
        assertEquals(1.0, list.get(0).getSup(), 0.0);
    }

    @Test
    public void testAsList5() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.FALSE);
        intervalsSet.setTree(tree);
        BSPTree<Euclidean1D> plus = new BSPTree<>(Boolean.FALSE);
        tree.insertIn(plus);

        BSPTree<Euclidean1D> minus = new BSPTree<>(Boolean.TRUE);
        SubHyperplane<Euclidean1D> cut = new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane();
        minus.setAttribute(cut);
        plus.insertIn(minus);

        List<Interval> list = intervalsSet.asList();
        assertEquals(2, list.size());
        assertEquals(Double.NEGATIVE_INFINITY, list.get(0).getInf(), 0.0);
        assertEquals(1.0, list.get(0).getSup(), 0.0);
        assertEquals(1.0, list.get(1).getInf(), 0.0);
        assertEquals(2.0, list.get(1).getSup(), 0.0);
    }

}
