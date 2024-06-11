package org.apache.commons.math3.geometry.euclidean.twod;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Triangle;
import org.apache.commons.math3.geometry.partitioning.Triangle3D;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end = new Vector2D(2.0, 2.0);
        Line line = new Line(start, end);
        IntervalsSet intervalsSet = new IntervalsSet(new LinearInterval(start.getX(), end.getX()));
        SubLine subLine = new SubLine(line, intervalsSet);
        assertEquals(line, subLine.getHyperplane());
        assertEquals(intervalsSet, subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineFromSegments() {
        Vector2D start1 = new Vector2D(1.0, 1.0);
        Vector2D end1 = new Vector2D(2.0, 2.0);
        Vector2D start2 = new Vector2D(3.0, 3.0);
        Vector2D end2 = new Vector2D(4.0, 4.0);
        Line line = new Line(start1, end1);
        IntervalsSet intervalsSet = new IntervalsSet(
                new LinearInterval(start1.getX(), end1.getX()),
                new LinearInterval(start2.getX(), end2.getX()));
        SubLine subLine = new SubLine(line, intervalsSet);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        List<Segment> expectedSegments = Arrays.asList(
                new Segment(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0), line),
                new Segment(new Vector2D(3.0, 3.0), new Vector2D(4.0, 4.0), line)
        );
        assertEquals(expectedSegments, segments);
    }

    @Test
    public void testSubLineIntersection() {
        Vector2D start1 = new Vector2D(1.0, 1.0);
        Vector2D end1 = new Vector2D(2.0, 2.0);
        Vector2D start2 = new Vector2D(3.0, 3.0);
        Vector2D end2 = new Vector2D(4.0, 4.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new LinearInterval(start1.getX(), end1.getX())));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new LinearInterval(start2.getX(), end2.getX())));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        start2 = new Vector2D(2.0, 2.0);
        end2 = new Vector2D(3.0, 3.0);
        line2 = new Line(start2, end2);
        subLine2 = new SubLine(line2, new IntervalsSet(new LinearInterval(start2.getX(), end2.getX())));
        intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        start1 = new Vector2D(1.0, 1.0);
        end1 = new Vector2D(3.0, 3.0);
        line1 = new Line(start1, end1);
        subLine1 = new SubLine(line1, new IntervalsSet(new LinearInterval(start1.getX(), end1.getX())));
        intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        start2 = new Vector2D(2.0, 2.0);
        end2 = new Vector2D(4.0, 4.0);
        line2 = new Line(start2, end2);
        subLine2 = new SubLine(line2, new IntervalsSet(new LinearInterval(start2.getX(), end2.getX())));
        intersectionPoint = subLine1.intersection(subLine2, true);
        Vector2D expectedIntersectionPoint = new Vector2D(2.0, 2.0);
        assertEquals(expectedIntersectionPoint, intersectionPoint);
    }

    @Test
    public void testSubLineSide() {
        Vector2D start1 = new Vector2D(1.0, 1.0);
        Vector2D end1 = new Vector2D(2.0, 2.0);
        Vector2D start2 = new Vector2D(3.0, 3.0);
        Vector2D end2 = new Vector2D(4.0, 4.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new LinearInterval(start1.getX(), end1.getX())));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new LinearInterval(start2.getX(), end2.getX())));
        Side side = subLine1.side(subLine2);
        assertEquals(Side.HYPER, side);

        start1 = new Vector2D(0.0, 0.0);
        end1 = new Vector2D(2.0, 2.0);
        line1 = new Line(start1, end1);
        subLine1 = new SubLine(line1, new IntervalsSet(new LinearInterval(start1.getX(), end1.getX())));
        start2 = new Vector2D(1.0, 1.0);
        end2 = new Vector2D(3.0, 3.0);
        line2 = new Line(start2, end2);
        subLine2 = new SubLine(line2, new IntervalsSet(new LinearInterval(start2.getX(), end2.getX())));
        side = subLine1.side(subLine2);
        assertEquals(Side.MINUS, side);

        start1 = new Vector2D(1.0, 1.0);
        end1 = new Vector2D(3.0, 3.0);
        line1 = new Line(start1, end1);
        subLine1 = new SubLine(line1, new IntervalsSet(new LinearInterval(start1.getX(), end1.getX())));
        start2 = new Vector2D(2.0, 2.0);
        end2 = new Vector2D(4.0, 4.0);
        line2 = new Line(start2, end2);
        subLine2 = new SubLine(line2, new IntervalsSet(new LinearInterval(start2.getX(), end2.getX())));
        side = subLine1.side(subLine2);
        assertEquals(Side.PLUS, side);
    }

    @Test
    public void testSplitSubLine() {
        Vector2D start1 = new Vector2D(1.0, 1.0);
        Vector2D end1 = new Vector2D(2.0, 2.0);
        Vector2D start2 = new Vector2D(3.0, 3.0);
        Vector2D end2 = new Vector2D(4.0, 4.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubHyperplane<Euclidean2D> hyperplane1 = new Line(start1, end1);
        SubHyperplane<Euclidean2D> hyperplane2 = new Line(start2, end2);
        IntervalsSet intervalsSet1 = new IntervalsSet(new LinearInterval(start1.getX(), end1.getX()));
        IntervalsSet intervalsSet2 = new IntervalsSet(new LinearInterval(start2.getX(), end2.getX()));
        SubLine subLine1 = new SubLine(line1, intervalsSet1);
        SubLine subLine2 = new SubLine(line2, intervalsSet2);
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(hyperplane2);
        SubLine expectedSubLinePlus = new SubLine(new Line(start1, new Vector2D(1.5, 1.5)),
                new IntervalsSet(new LinearInterval(start1.getX(), 1.5)));
        SubLine expectedSubLineMinus = new SubLine(new Line(start1, new Vector2D(2.0, 2.0)),
                new IntervalsSet(new LinearInterval(1.5, start1.getX())));
        assertEquals(expectedSubLinePlus, splitSubHyperplane.getSubHyperplane1());
        assertEquals(expectedSubLineMinus, splitSubHyperplane.getSubHyperplane2());

        Hyperplane<Euclidean2D> hyperplane3 = new Hyperplane<Euclidean2D>(new Triangle(new Point2D[]{new Point2D(2.0, 2.0),
                new Point2D(3.0, 3.0), new Point2D(4.0, 4.0)}), new Euclidean2DSpace());
        splitSubHyperplane = subLine1.split(hyperplane3);
        assertNull(splitSubHyperplane);

        Hyperplane<Euclidean2D> hyperplane4 = new Hyperplane<Euclidean2D>(new OrientedPoint(new Vector2D(0.0, 0.0), false),
                new Euclidean2DSpace());
        splitSubHyperplane = subLine1.split(hyperplane4);
        assertNull(splitSubHyperplane);
    }
}