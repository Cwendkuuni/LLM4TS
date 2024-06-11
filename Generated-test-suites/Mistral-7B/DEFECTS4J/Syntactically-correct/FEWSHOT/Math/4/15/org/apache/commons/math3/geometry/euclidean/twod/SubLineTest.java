package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.apache.commons.math3.geometry.euclidean.oned.Interval.INTERVAL;
import static org.apache.commons.math3.geometry.euclidean.twod.Vector2D.ZERO;
import static org.apache.commons.math3.geometry.euclidean.twod.Vector2D.UNIT_X;
import static org.apache.commons.math3.geometry.euclidean.twod.Vector2D.UNIT_Y;
import static org.apache.commons.math3.geometry.partitioning.Side.HYPER;
import static org.apache.commons.math3.geometry.partitioning.Side.MINUS;
import static org.apache.commons.math3.geometry.partitioning.Side.PLUS;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 0.0), new Vector2D(0.0, 1.0));
        SubLine subLine = new SubLine(hyperplane, INTERVAL.open(0.0, 1.0));

        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(INTERVAL.open(0.0, 1.0), subLine.getRemainingRegion());
        assertEquals(2, subLine.getSegments().size());
    }

    @Test
    public void testSubLineFromTwoPoints() {
        Vector2D start = new Vector2D(1.0, 0.0);
        Vector2D end = new Vector2D(0.0, 1.0);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        SubLine subLine = new SubLine(start, end);

        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(INTERVAL.open(0.0, 1.0), subLine.getRemainingRegion());
        assertEquals(2, subLine.getSegments().size());
    }

    @Test
    public void testSubLineFromSegment() {
        Segment segment = new Segment(new Vector2D(1.0, 0.0), new Vector2D(0.0, 1.0), new Line(new Vector2D(1.0, 0.0),
                new Vector2D(0.0, 1.0)));
        SubLine subLine = new SubLine(segment);

        assertEquals(segment.getLine(), subLine.getHyperplane());
        assertEquals(segment.getRemainingRegion(), subLine.getRemainingRegion());
        assertEquals(1, subLine.getSegments().size());
    }

    @Test
    public void testGetSegments() {
        Vector2D start1 = new Vector2D(1.0, 0.0);
        Vector2D end1 = new Vector2D(2.0, 1.0);
        Vector2D start2 = new Vector2D(3.0, 0.0);
        Vector2D end2 = new Vector2D(4.0, 1.0);
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 0.0), new Vector2D(4.0, 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 4.0));

        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        List<Segment> segments = subLine.getSegments();

        assertEquals(2, segments.size());
        assertEquals(new Segment(new Vector2D(1.0, 0.0), new Vector2D(2.0, 1.0), new Line(new Vector2D(1.0, 0.0),
                new Vector2D(2.0, 1.0))), segments.get(0));
        assertEquals(new Segment(new Vector2D(3.0, 0.0), new Vector2D(4.0, 1.0), new Line(new Vector2D(1.0, 0.0),
                new Vector2D(4.0, 1.0))), segments.get(1));
    }

    @Test
    public void testIntersection() {
        Vector2D p1 = new Vector2D(1.0, 0.0);
        Vector2D p2 = new Vector2D(3.0, 2.0);
        Vector2D q1 = new Vector2D(0.0, 1.0);
        Vector2D q2 = new Vector2D(4.0, 1.0);

        Line line1 = new Line(p1, q1);
        Line line2 = new Line(p2, q2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 2.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(3.0, 5.0)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);

        assertNull(intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);

        assertNull(intersectionPoint);

        p1 = new Vector2D(0.0, 0.0);
        p2 = new Vector2D(4.0, 0.0);
        q1 = new Vector2D(1.0, 1.0);
        q2 = new Vector2D(3.0, 1.0);

        line1 = new Line(p1, q1);
        line2 = new Line(p2, q2);
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 2.0)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.0, 4.0)));

        intersectionPoint = subLine1.intersection(subLine2, true);

        assertEquals(new Vector2D(1.5, 1.0), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);

        assertEquals(new Vector2D(1.5, 1.0), intersectionPoint);

        p1 = new Vector2D(0.0, 0.0);
        p2 = new Vector2D(4.0, 0.0);
        q1 = new Vector2D(1.0, 1.0);
        q2 = new Vector2D(3.0, 1.0);

        line1 = new Line(p1, q1);
        line2 = new Line(p2, q2);
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 2.0)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(4.0, 5.0)));

        intersectionPoint = subLine1.intersection(subLine2, true);

        assertNull(intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);

        assertNull(intersectionPoint);

        p1 = new Vector2D(1.0, 0.0);
        p2 = new Vector2D(3.0, 0.0);
        q1 = new Vector2D(2.0, 1.0);
        q2 = new Vector2D(4.0, 1.0);

        line1 = new Line(p1, q1);
        line2 = new Line(p2, q2);
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2.5)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.5, 3.5)));

        intersectionPoint = subLine1.intersection(subLine2, true);

        assertEquals(new Vector2D(2.5, 0.0), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);

        assertEquals(new Vector2D(2.5, 0.0), intersectionPoint);
    }

    @Test
    public void testIntersectionWithNullSubLine() {
        Vector2D p1 = new Vector2D(1.0, 0.0);
        Vector2D p2 = new Vector2D(3.0, 2.0);

        Line line1 = new Line(p1, new Vector2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 2.0)));

        assertNull(subLine1.intersection(null, true));
        assertNull(subLine1.intersection(null, false));
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 0.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        assertEquals(HYPER, subLine1.side(line2));

        line2 = new Line(new Vector2D(-1.0, -1.0), new Vector2D(1.0, 1.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        assertEquals(MINUS, subLine1.side(line2));

        line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.0, 1.0)));

        assertEquals(PLUS, subLine1.side(line2));
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 0.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(line2);

        assertEquals(null, splitSubHyperplane.getSubHyperplane1());
        assertEquals(subLine1, splitSubHyperplane.getSubHyperplane2());

        line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        splitSubHyperplane = subLine1.split(line2);

        assertEquals(subLine2, splitSubHyperplane.getSubHyperplane1());
        assertEquals(subLine1, splitSubHyperplane.getSubHyperplane2());

        line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        splitSubHyperplane = subLine1.split(line2);

        assertEquals(subLine1, splitSubHyperplane.getSubHyperplane1());
        assertEquals(subLine2, splitSubHyperplane.getSubHyperplane2());
    }

    @Test
    public void testSplitWithNullSubLine() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));

        assertNull(subLine1.split(null));
    }
}