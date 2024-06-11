package org.apache.commons.math3.geometry.euclidean.twod.SubLineTest;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link SubLine} class
 */
public class SubLineTest {

    @Test
    public void testConstructorFromHyperplaneAndRegion() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Point2D p1 = new Point2D(1.0, 1.0);
        Point2D p2 = new Point2D(2.0, 2.0);
        Hyperplane<Euclidean2D> hyperplane = new Line(p1, p2).getHyperplane(false);
        Interval i1 = new Interval(1.5, 2.5);
        Interval i2 = new Interval(3.0, 4.0);
        Region<Euclidean1D> region = new IntervalsSet(i1, i2);
        SubLine subLine = new SubLine(hyperplane, region);
        Line line = (Line) subLine.getHyperplane();
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        assertEquals(p1, line.toSpace(segments.get(0).getStart()));
        assertEquals(p2, line.toSpace(segments.get(0).getEnd()));
        assertEquals(p2, line.toSpace(segments.get(1).getStart()));
        assertEquals(Double.POSITIVE_INFINITY, segments.get(1).getEnd().getX(), 1e-14);
    }

    @Test
    public void testConstructorFromStartAndEnd() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Point2D p1 = new Point2D(1.0, 1.0);
        Point2D p2 = new Point2D(2.0, 2.0);
        SubLine subLine = new SubLine(p1, p2);
        Line line = (Line) subLine.getHyperplane();
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(p1, line.toSpace(segments.get(0).getStart()));
        assertEquals(p2, line.toSpace(segments.get(0).getEnd()));
        assertEquals(Double.POSITIVE_INFINITY, segments.get(0).getEnd().getX(), 1e-14);
    }

    @Test
    public void testConstructorFromSegment() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Point2D p1 = new Point2D(1.0, 1.0);
        Point2D p2 = new Point2D(2.0, 2.0);
        Segment segment = new Segment(p1, p2);
        SubLine subLine = new SubLine(segment);
        Line line = (Line) subLine.getHyperplane();
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(p1, line.toSpace(segments.get(0).getStart()));
        assertEquals(p2, line.toSpace(segments.get(0).getEnd()));
        assertEquals(Double.POSITIVE_INFINITY, segments.get(0).getEnd().getX(), 1e-14);
    }

    @Test
    public void testGetSegments() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Point2D p1 = new Point2D(1.0, 1.0);
        Point2D p2 = new Point2D(2.0, 2.0);
        Point2D p3 = new Point2D(3.0, 3.0);
        Point2D p4 = new Point2D(4.0, 4.0);
        Line line = new Line(p1, p2);
        Interval i1 = new Interval(1.5, 2.5);
        Interval i2 = new Interval(3.0, 4.0);
        Interval i3 = new Interval(5.0, Double.POSITIVE_INFINITY);
        Region<Euclidean1D> region = new IntervalsSet(i1, i2, i3);
        SubLine subLine = new SubLine(line, region);
        List<Segment> segments = subLine.getSegments();
        assertEquals(3, segments.size());
        assertEquals(p1, line.toSpace(segments.get(0).getStart()));
        assertEquals(new Point2D(p1.getX() + 0.5 * (p2.getX() - p1.getX()), p1.getY() + 0.5 * (p2.getY() - p1.getY())),
                line.toSpace(segments.get(0).getEnd()));
        assertEquals(p2, line.toSpace(segments.get(1).getStart()));
        assertEquals(p3, line.toSpace(segments.get(1).getEnd()));
        assertEquals(Double.POSITIVE_INFINITY, segments.get(2).getEnd().getX(), 1e-14);
    }

    @Test
    public void testIntersectionWithSameLine() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Point2D p1 = new Point2D(1.0, 1.0);
        Point2D p2 = new Point2D(2.0, 2.0);
        Line line = new Line(p1, p2);
        SubLine subLine1 = new SubLine(line, new IntervalsSet(new Interval(1.5, 2.5)));
        SubLine subLine2 = new SubLine(line, new IntervalsSet(new Interval(2.5, 3.5)));
        Vector2D expected = line.intersection(line);
        Vector2D actual1 = subLine1.intersection(subLine1, true);
        Vector2D actual2 = subLine2.intersection(subLine2, true);
        Vector2D actual = subLine1.intersection(subLine2, true);
        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
        assertEquals(expected, actual);
    }

    @Test
    public void testIntersectionWithDifferentLines() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Point2D p1 = new Point2D(1.0, 1.0);
        Point2D p2 = new Point2D(2.0, 2.0);
        Point2D p3 = new Point2D(3.0, 3.0);
        Point2D p4 = new Point2D(4.0, 4.0);
        Line line1 = new Line(p1, p2);
        Line line2 = new Line(p3, p4);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(3.5, 4.5)));
        assertNull(subLine1.intersection(subLine2, true));
    }

    @Test
    public void testIntersectionWithDifferentLinesAndIncludeEndPoints() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Point2D p1 = new Point2D(1.0, 1.0);
        Point2D p2 = new Point2D(2.0, 2.0);
        Point2D p3 = new Point2D(3.0, 3.0);
        Point2D p4 = new Point2D(4.0, 4.0);
        Line line1 = new Line(p1, p2);
        Line line2 = new Line(p3, p4);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(3.5, 4.5)));
        Vector2D expected = line1.intersection(line2);
        if (expected != null) {
            assertEquals(expected, subLine1.intersection(subLine2, false));
            assertEquals(expected, subLine2.intersection(subLine1, false));
        } else {
            assertNull(subLine1.intersection(subLine2, false));
            assertNull(subLine2.intersection(subLine1, false));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNullSubLine() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Point2D p1 = new Point2D(1.0, 1.0);
        Point2D p2 = new Point2D(2.0, 2.0);
        Line line = new Line(p1, p2);
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(1.5, 2.5)));
        assertNotNull(subLine.intersection(subLine, true));
        assertNotNull(subLine.getHyperplane());
        assertNull(subLine.intersection(null, true));
    }

    @Test
    public void testSide() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Point2D p1 = new Point2D(1.0, 1.0);
        Point2D p2 = new Point2D(2.0, 2.0);
        Line line1 = new Line(p1, p2);
        Line line2 = new Line(new Point2D(0.0, 0.0), new Point2D(3.0, 3.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.5)));
        Side expected1 = Side.MINUS;
        Side expected2 = Side.PLUS;
        Side actual1 = subLine1.side(subLine2);
        Side actual2 = subLine2.side(subLine1);
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testSplit() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Point2D p1 = new Point2D(1.0, 1.0);
        Point2D p2 = new Point2D(2.0, 2.0);
        Point2D p3 = new Point2D(3.0, 3.0);
        Line line1 = new Line(p1, p2);
        Line line2 = new Line(new Point2D(0.0, 0.0), p3);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2.5)));
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Point2D(1.0, 1.5), new Point2D(2.0, 1.5)).getHyperplane(false);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Point2D(2.0, 2.5), new Point2D(3.0, 2.5)).getHyperplane(false);
        SubHyperplane<Euclidean2D> subHyperplane1 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 1.5), new Interval(2.5, 3.0)));
        SubHyperplane<Euclidean2D> subHyperplane2 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2.0)));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(hyperplane1);
        assertEquals(subHyperplane1, splitSubHyperplane.getSubHyperplane1());
        assertEquals(subHyperplane2, splitSubHyperplane.getSubHyperplane2());
        splitSubHyperplane = subLine1.split(hyperplane2);
        assertEquals(subHyperplane1, splitSubHyperplane.getSubHyperplane1());
        assertNull(splitSubHyperplane.getSubHyperplane2());
    }

}