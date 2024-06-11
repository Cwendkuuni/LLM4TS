package org.apache.commons.math3.geometry.euclidean.twod.test;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.testing.EqualityTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1)).toHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithStartAndEnd() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        Line line = new Line(start, end);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(line.toSubSpace(start), line.toSubSpace(end));
        assertEquals(line, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end);
        SubLine subLine = new SubLine(segment);

        Line line = segment.getLine();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(line.toSubSpace(start), line.toSubSpace(end));
        assertEquals(line, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(3, 3);
        Line line = new Line(start1, end1);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(line.toSubSpace(start1), line.toSubSpace(end1),
                                                              line.toSubSpace(start2), line.toSubSpace(end2));
        SubLine subLine = new SubLine(line, remainingRegion);

        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        assertEquals(start1, segment1.getStart());
        assertEquals(end1, segment1.getEnd());
        Segment segment2 = segments.get(1);
        assertEquals(start2, segment2.getStart());
        assertEquals(end2, segment2.getEnd());
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(2, 2);

        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(line1.toSubSpace(start1), line1.toSubSpace(end1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(line2.toSubSpace(start2), line2.toSubSpace(end2)));

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        start2 = new Vector2D(1, 0);
        end2 = new Vector2D(1, 2);
        subLine2 = new SubLine(line2, new IntervalsSet(line2.toSubSpace(start2), line2.toSubSpace(end2)));
        intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1, 1), intersection);

        start1 = new Vector2D(0, 0);
        end1 = new Vector2D(3, 3);
        line1 = new Line(start1, end1);
        subLine1 = new SubLine(line1, new IntervalsSet(line1.toSubSpace(start1), line1.toSubSpace(end1)));
        start2 = new Vector2D(1, 1);
        end2 = new Vector2D(2, 2);
        line2 = new Line(start2, end2);
        subLine2 = new SubLine(line2, new IntervalsSet(line2.toSubSpace(start2), line2.toSubSpace(end2)));

        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);

        start2 = new Vector2D(1, 0);
        end2 = new Vector2D(1, 2);
        subLine2 = new SubLine(line2, new IntervalsSet(line2.toSubSpace(start2), line2.toSubSpace(end2)));
        intersection = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(1, 1), intersection);
    }

    @Test
    public void testSide() {
        Vector2D p1 = new Vector2D(0, 0);
        Vector2D p2 = new Vector2D(1, 1);
        Vector2D p3 = new Vector2D(1, 2);
        Vector2D p4 = new Vector2D(2, 2);

        Line line1 = new Line(p1, p2);
        Line line2 = new Line(p3, p4);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(line1.toSubSpace(p1), line1.toSubSpace(p2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(line2.toSubSpace(p3), line2.toSubSpace(p4)));

        Hyperplane<Euclidean2D> hyperplane = new Line(p1, p3);
        assertEquals(Side.MINUS, subLine1.side(hyperplane));
        assertEquals(Side.PLUS, subLine2.side(hyperplane));

        hyperplane = new Line(p2, p4);
        assertEquals(Side.PLUS, subLine1.side(hyperplane));
        assertEquals(Side.MINUS, subLine2.side(hyperplane));

        hyperplane = new Line(p1, p4);
        assertEquals(Side.HYPER, subLine1.side(hyperplane));
        assertEquals(Side.HYPER, subLine2.side(hyperplane));
    }

    @Test
    public void testSplit() {
        Vector2D p1 = new Vector2D(0, 0);
        Vector2D p2 = new Vector2D(1, 1);
        Vector2D p3 = new Vector2D(1, 2);
        Vector2D p4 = new Vector2D(2, 2);

        Line line1 = new Line(p1, p2);
        Line line2 = new Line(p3, p4);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(line1.toSubSpace(p1), line1.toSubSpace(p2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(line2.toSubSpace(p3), line2.toSubSpace(p4)));

        Hyperplane<Euclidean2D> hyperplane1 = new Line(p1, p3);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(p2, p4);
        SubHyperplane<Euclidean1D> subHyperplanePlus = new OrientedPoint(new Vector1D(1), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> subHyperplaneMinus = new OrientedPoint(new Vector1D(0), false).wholeHyperplane();

        BSPTree<Euclidean1D> splitTree = getRemainingRegion(subLine1.getRemainingRegion()).getTree(false);
        SubLine subLinePlus = new SubLine(line1.copySelf(), new IntervalsSet(splitTree.getPlus()));
        SubLine subLineMinus = new SubLine(line1.copySelf(), new IntervalsSet(splitTree.getMinus()));
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane1);
        assertEquals(subLinePlus, split.getSubHyperplane1());
        assertEquals(subLineMinus, split.getSubHyperplane2());

        splitTree = getRemainingRegion(subLine2.getRemainingRegion()).getTree(false);
        subLinePlus = new SubLine(line2.copySelf(), new IntervalsSet(splitTree.getPlus()));
        subLineMinus = new SubLine(line2.copySelf(), new IntervalsSet(splitTree.getMinus()));
        split = subLine2.split(hyperplane2);
        assertEquals(subLinePlus, split.getSubHyperplane1());
        assertEquals(subLineMinus, split.getSubHyperplane2());

        Hyperplane<Euclidean2D> hyperplane3 = new Line(p1, p4);
        split = subLine1.split(hyperplane3);
        assertNull(split.getSubHyperplane1());
        assertNull(split.getSubHyperplane2());

        split = subLine2.split(hyperplane3);
        assertNull(split.getSubHyperplane1());
        assertNull(split.getSubHyperplane2());
    }

    private Region<Euclidean1D> getRemainingRegion(Region<Euclidean1D> region) {
        return new IntervalsSet(region.getMin(), region.getMax());
    }

    @Test
    public void testEquality() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(2, 2);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(line1.toSubSpace(start1), line1.toSubSpace(end1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(line2.toSubSpace(start2), line2.toSubSpace(end2)));
        SubLine subLine3 = new SubLine(line1, new IntervalsSet(line1.toSubSpace(start1), line1.toSubSpace(end1)));

        new EqualityTester()
                .addEqualityGroup(subLine1, subLine3)
                .testEquals();
        new EqualityTester()
                .addEqualityGroup(subLine1, subLine2)
                .testNotEquals();
    }

}