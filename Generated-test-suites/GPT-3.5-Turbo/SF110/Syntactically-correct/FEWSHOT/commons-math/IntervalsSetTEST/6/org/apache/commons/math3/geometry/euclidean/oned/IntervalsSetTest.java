
package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.geometry.Vector1D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IntervalsSetTest {
    @Test
    public void testConstructor() {
        IntervalsSet intervalsSet1 = new IntervalsSet();
        assertEquals(Boolean.TRUE, intervalsSet1.buildNew(new BSPTree<>(Boolean.TRUE)).getTree().getAttribute());

        IntervalsSet intervalsSet2 = new IntervalsSet(1.0, 2.0);
        assertEquals(1.0, intervalsSet2.getInf(), 0.0);
        assertEquals(2.0, intervalsSet2.getSup(), 0.0);

        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet3 = new IntervalsSet(tree);
        assertEquals(tree, intervalsSet3.getTree());

        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet4 = new IntervalsSet(boundary);
        assertEquals(boundary, intervalsSet4.getBoundary());
    }

    @Test
    public void testBuildTree() {
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        BSPTree<Euclidean1D> expectedTree1 = new BSPTree<>(Boolean.TRUE);
        assertEquals(expectedTree1, IntervalsSet.buildTree(lower, upper));

        lower = Double.NEGATIVE_INFINITY;
        upper = 1.0;
        SubHyperplane<Euclidean1D> upperCut2 = new OrientedPoint(new Vector1D(upper), true).wholeHyperplane();
        BSPTree<Euclidean1D> expectedTree2 = new BSPTree<>(upperCut2, new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        assertEquals(expectedTree2, IntervalsSet.buildTree(lower, upper));

        lower = 1.0;
        upper = Double.POSITIVE_INFINITY;
        SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(lower), false).wholeHyperplane();
        BSPTree<Euclidean1D> expectedTree3 = new BSPTree<>(lowerCut, new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        assertEquals(expectedTree3, IntervalsSet.buildTree(lower, upper));

        lower = 1.0;
        upper = 2.0;
        SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(upper), true).wholeHyperplane();
        BSPTree<Euclidean1D> expectedTree4 = new BSPTree<>(lowerCut, new BSPTree<>(Boolean.FALSE), new BSPTree<>(upperCut, new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null), null);
        assertEquals(expectedTree4, IntervalsSet.buildTree(lower, upper));
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.getTree(false).setAttribute(Boolean.TRUE);
        intervalsSet.computeGeometricalProperties();
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);

        intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertNull(intervalsSet.getBarycenter());
        assertEquals(0.0, intervalsSet.getSize(), 0.0);

        intervalsSet = new IntervalsSet();
        SubHyperplane<Euclidean1D> hyperplane = new OrientedPoint(Vector1D.ZERO, true).wholeHyperplane();
        BSPTree<Euclidean1D> tree = new BSPTree<>(hyperplane, new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        intervalsSet.setTree(tree);
        intervalsSet.computeGeometricalProperties();
        assertEquals(Vector1D.ZERO, intervalsSet.getBarycenter());
        assertEquals(0.0, intervalsSet.getSize(), 0.0);
    }

    @Test
    public void testGetInf() {
        BSPTree<Euclidean1D> node = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(node);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);

        SubHyperplane<Euclidean1D> upperHyperplane = new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane();
        BSPTree<Euclidean1D> upperTree = new BSPTree<>(upperHyperplane, new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        node.setMinus(upperTree);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);

        SubHyperplane<Euclidean1D> lowerHyperplane = new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane();
        BSPTree<Euclidean1D> lowerTree = new BSPTree<>(lowerHyperplane, new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        node.setMinus(lowerTree);
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        BSPTree<Euclidean1D> node = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(node);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);

        SubHyperplane<Euclidean1D> lowerHyperplane = new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane();
        BSPTree<Euclidean1D> lowerTree = new BSPTree<>(lowerHyperplane, new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        node.setMinus(lowerTree);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);

        SubHyperplane<Euclidean1D> upperHyperplane = new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane();
        BSPTree<Euclidean1D> upperTree = new BSPTree<>(upperHyperplane, new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        node.setMinus(upperTree);
        assertEquals(1.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> emptyList = new ArrayList<>();
        assertEquals(emptyList, intervalsSet.asList());

        SubHyperplane<Euclidean1D> hyperplane = new OrientedPoint(Vector1D.ZERO, false).wholeHyperplane();
        BSPTree<Euclidean1D> tree = new BSPTree<>(hyperplane, new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        intervalsSet.setTree(tree);
        List<Interval> singleList = new ArrayList<>();
        singleList.add(new Interval(Double.NEGATIVE_INFINITY, 0.0));
        assertEquals(singleList, intervalsSet.asList());

        tree.setAttribute(Boolean.FALSE);
        List<Interval> singleFalseList = new ArrayList<>();
        singleFalseList.add(new Interval(Double.NEGATIVE_INFINITY, 0.0));
        singleFalseList.add(new Interval(0.0, Double.POSITIVE_INFINITY));
        assertEquals(singleFalseList, intervalsSet.asList());

    }
}
