package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet iset = new IntervalsSet(new Line(new Vector2D(0, 0), new Vector2D(0.5, 0.5)),
                                               new Line(new Vector2D(0.5, 0.5), new Vector2D(1, 1)));
        SubLine subLine = new SubLine(line, iset);
        assertEquals(line, subLine.getHyperplane());
        assertEquals(iset, subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet iset = new IntervalsSet(new Line(new Vector2D(0, 0), new Vector2D(0.5, 0.5)),
                                               new Line(new Vector2D(0.5, 0.5), new Vector2D(1, 1)));
        SubLine subLine = new SubLine(line, iset);
        List<Segment> segments = subLine.getSegments();
        List<Segment> expectedSegments = new ArrayList<>();
        expectedSegments.add(new Segment(new Vector2D(0, 0), new Vector2D(0.5, 0.5), line));
        expectedSegments.add(new Segment(new Vector2D(0.5, 0.5), new Vector2D(1, 1), line));
        assertEquals(expectedSegments, segments);
    }

    @Test
    public void testSubLineIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Line(new Vector2D(1, 1), new Vector2D(2, 2)),
                                                                 new Line(new Vector2D(2, 2), new Vector2D(3, 3))));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                                                                 new Line(new Vector2D(1, 1), new Vector2D(2, 2))));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(1, 1), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1, 1), intersectionPoint);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        subLine1 = new SubLine(line1, new IntervalsSet(new Line(new Vector2D(1, 1), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY))));
        subLine2 = new SubLine(line2, new IntervalsSet(new Line(new Vector2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
                                                                 new Vector2D(1, 1))));
        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSubLineSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Line(new Vector2D(1, 1), new Vector2D(2, 2)),
                                                                 new Line(new Vector2D(2, 2), new Vector2D(3, 3))));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Side side = subLine1.side(hyperplane);
        assertEquals(Side.PLUS, side);

        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(3, 3), new Vector2D(4, 4));
        side = subLine1.side(hyperplane2);
        assertEquals(Side.MINUS, side);

        Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(1.5, 1.5), new Vector2D(2.5, 2.5));
        side = subLine1.side(hyperplane3);
        assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSubLineSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Line(new Vector2D(1, 1), new Vector2D(2, 2)),
                                                                 new Line(new Vector2D(2, 2), new Vector2D(3, 3))));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.5, 1.5), new Vector2D(2.5, 2.5));
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);

        assertTrue(split instanceof SplitSubHyperplane);
        assertFalse(split.getLeft() instanceof SubLine);
        assertFalse(split.getRight() instanceof SubLine);
        assertEquals(subLine1.getHyperplane(), split.getLeft().getHyperplane());
        assertEquals(subLine1.getRemainingRegion(), split.getLeft().getRemainingRegion());

        List<SubHyperplane<Euclidean1D>> leftTree = ((BSPTree<Euclidean1D>) split.getLeft().getRemainingRegion().getTree(false))
                .getSubHyperplanes();
        assertEquals(2, leftTree.size());
        Hyperplane<Euclidean1D> hyperplane1 = new Line(new Vector1D(0), new Vector1D(1));
        Hyperplane<Euclidean1D> hyperplane2 = new Line(new Vector1D(1), new Vector1D(2));
        assertEquals(hyperplane1, leftTree.get(0).getHyperplane());
        assertEquals(hyperplane2, leftTree.get(1).getHyperplane());

        List<SubHyperplane<Euclidean1D>> rightTree = ((BSPTree<Euclidean1D>) split.getRight().getRemainingRegion().getTree(false))
                .getSubHyperplanes();
        assertEquals(0, rightTree.size());
    }
}