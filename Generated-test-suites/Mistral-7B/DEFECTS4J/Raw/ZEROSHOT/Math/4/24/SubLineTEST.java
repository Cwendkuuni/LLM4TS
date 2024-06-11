package org.apache.commons.math3.geometry.euclidean.twod.test;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.AbstractHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Euclidean2DSpace space = new Euclidean2DSpace(2);
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        IntervalsSet intervals = new IntervalsSet(new Interval(0.0, 1.0), new Interval(3.0, 4.0));
        SubLine subLine = new SubLine(hyperplane, intervals);
        assertEquals(hyperplane, subLine.getHyperplane());
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        Segment segment2 = segments.get(1);
        assertEquals(start, segment1.getStart());
        assertEquals(new Vector2D(1.0, 2.0 + (end.getX() - start.getX()) * 0.5, 1.0 + (end.getY() - start.getY()) * 0.5), segment1.getEnd());
        assertEquals(end, segment2.getStart());
        assertEquals(new Vector2D(3.0, 2.0 + (end.getX() - start.getX()) * 0.5, 2.0 + (end.getY() - start.getY()) * 0.5), segment2.getEnd());
    }

    @Test
    public void testSubLineIntersection() {
        Euclidean2DSpace space = new Euclidean2DSpace(2);
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1 = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(4.0, 5.0);
        Vector2D end2 = new Vector2D(6.0, 7.0);
        Hyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2);
        IntervalsSet intervals1 = new IntervalsSet(new Interval(0.0, 1.0), new Interval(3.0, 4.0));
        IntervalsSet intervals2 = new IntervalsSet(new Interval(2.0, 5.0));
        SubLine subLine1 = new SubLine(hyperplane1, intervals1);
        SubLine subLine2 = new SubLine(hyperplane2, intervals2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
        start1 = new Vector2D(1.0, 2.0);
        start2 = new Vector2D(3.0, 4.0);
        end1 = new Vector2D(3.0, 2.0);
        end2 = new Vector2D(5.0, 6.0);
        intervals1 = new IntervalsSet(new Interval(0.0, 1.0), new Interval(3.0, 4.0));
        intervals2 = new IntervalsSet(new Interval(2.0, 5.0));
        subLine1 = new SubLine(hyperplane1, intervals1);
        subLine2 = new SubLine(hyperplane2, intervals2);
        intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(2.5, 4.5), intersection);
        intersection = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(2.5, 4.5), intersection);
    }

    @Test
    public void testSubLineSide() {
        Euclidean2DSpace space = new Euclidean2DSpace(2);
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(0.0, 1.0));
        Hyperplane<Euclidean2D> hyperplane1 = line1;
        Hyperplane<Euclidean2D> hyperplane2 = line2;
        IntervalsSet intervals = new IntervalsSet(new Interval(0.0, 1.0), new Interval(0.0, 1.0));
        SubLine subLine = new SubLine(hyperplane1, intervals);
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        Side side = subLine.side(hyperplane);
        assertEquals(Side.MINUS, side);
        side = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 0.5)).side(subLine.getHyperplane());
        assertEquals(Side.PLUS, side);
    }

    @Test
    public void testSubLineSplit() {
        Euclidean2DSpace space = new Euclidean2DSpace(2);
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(0.0, 1.0), new Vector2D(1.0, 2.0));
        Hyperplane<Euclidean2D> hyperplane1 = line1;
        Hyperplane<Euclidean2D> hyperplane2 = line2;
        IntervalsSet intervals = new IntervalsSet(new Interval(0.0, 1.0), new Interval(1.0, 2.0));
        SubLine subLine = new SubLine(hyperplane1, intervals);
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        BSPTree<Euclidean1D> tree = subLine.getRemainingRegion().getTree(false);
        SubLine subLineLeft = (SubLine) tree.split(new Hyperplane<Euclidean2D>(new Euclidean2DPoint(0.5, 0.5), new Vector2D(0.5, 1.0))).getData();
        SubLine subLineRight = (SubLine) tree.getLeaf().getData();
        assertEquals(subLineLeft.getRemainingRegion().getVolume(), 0.5);
        assertEquals(subLineRight.getRemainingRegion().getVolume(), 0.5);
        Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(1.5, 0.5), new Vector2D(2.5, 1.5));
        Side side = subLineLeft.side(hyperplane3);
        assertEquals(Side.MINUS, side);
        side = subLineRight.side(hyperplane3);
        assertEquals(Side.PLUS, side);
        Hyperplane<Euclidean2D> hyperplane4 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        side = subLineLeft.side(hyperplane4);
        assertEquals(Side.PLUS, side);
        side = subLineRight.side(hyperplane4);
        assertEquals(Side.MINUS, side);
    }

}