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
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        Interval interval = new Interval(1.5, 2.5);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(interval);
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithStartAndEnd() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        SubLine subLine = new SubLine(start, end);
        Line line = (Line) subLine.getHyperplane();
        assertEquals(start, line.getStartVector());
        assertEquals(end, line.getEndVector());
        assertEquals(subLine.getRemainingRegion().asList(), new Interval(start.getX(), end.getX()).asList());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        Segment segment = new Segment(start, end);
        SubLine subLine = new SubLine(segment);
        Line line = (Line) subLine.getHyperplane();
        assertEquals(segment.getStart(), line.getStartVector());
        assertEquals(segment.getEnd(), line.getEndVector());
        assertEquals(subLine.getRemainingRegion().asList(), new Interval(start.getX(), end.getX()).asList());
    }

    @Test
    public void testGetSegments() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1   = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(4.0, 5.0);
        Vector2D end2   = new Vector2D(6.0, 7.0);
        List<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(start1.getX(), end1.getX()));
        intervals.add(new Interval(start2.getX(), end2.getX()));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(intervals);
        SubLine subLine = new SubLine(new Line(start1, end1), remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        assertEquals(start1, segments.get(0).getStart());
        assertEquals(end1, segments.get(0).getEnd());
        assertEquals(start2, segments.get(1).getStart());
        assertEquals(end2, segments.get(1).getEnd());
    }

    @Test
    public void testIntersectionWithNullSubLine() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);
        SubLine subLine = null;
        Vector2D result = subLine.intersection(subLine, true);
        assertNull(result);
    }

    @Test
    public void testIntersectionWithParallelLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1   = new Vector2D(2.0, 0.0);
        Vector2D start2 = new Vector2D(2.0, 2.0);
        Vector2D end2   = new Vector2D(4.0, 2.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 2.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.0, 4.0)));
        Vector2D result = subLine1.intersection(subLine2, true);
        assertNull(result);
    }

    @Test
    public void testIntersectionWithIntersectingLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1   = new Vector2D(2.0, 2.0);
        Vector2D start2 = new Vector2D(2.0, 0.0);
        Vector2D end2   = new Vector2D(4.0, 2.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 2.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.0, 4.0)));
        Vector2D result = subLine1.intersection(subLine2, true);
        Vector2D expected = new Vector2D(1.0, 1.0);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNonIntersectingLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1   = new Vector2D(2.0, 2.0);
        Vector2D start2 = new Vector2D(2.0, 2.0);
        Vector2D end2   = new Vector2D(4.0, 0.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 2.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.0, 4.0)));
        subLine1.intersection(subLine2, true);
    }

    @Test
    public void testIntersectionWithSameInstance() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(start.getX(), end.getX())));
        Vector2D result = subLine.intersection(subLine, true);
        assertNull(result);
    }

    @Test
    public void testIntersectionWithOutsideEndpoints() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1   = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(-1.0, -2.0);
        Vector2D end2   = new Vector2D(5.0, 6.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(start1.getX(), end1.getX())));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(start2.getX(), end2.getX())));
        Vector2D result = subLine1.intersection(subLine2, false);
        assertNull(result);
    }

    @Test
    public void testIntersectionWithInsideEndpoints() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1   = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(2.0, 2.0);
        Vector2D end2   = new Vector2D(4.0, 4.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(start1.getX(), end1.getX())));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(start2.getX(), end2.getX())));
        Vector2D result = subLine1.intersection(subLine2, false);
        assertNull(result);
    }

    @Test
    public void testIntersectionWithInsidePoint() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1   = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(2.0, 2.0);
        Vector2D end2   = new Vector2D(4.0, 4.0);
        Vector2D point = new Vector2D(2.5, 2.5);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(start1.getX(), end1.getX())));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(start2.getX(), end2.getX())));
        Vector2D result = subLine1.intersection(subLine2, false);
        assertEquals(point, result);
    }

}