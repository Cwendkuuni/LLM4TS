import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0)).toHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.5, 1.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertNotNull(subLine);
    }

    @Test
    public void testConstructorWithStartAndEnd() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        assertNotNull(subLine);
        assertEquals(start, subLine.getStart());
        assertEquals(end, subLine.getEnd());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 1);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        assertNotNull(subLine);
        assertEquals(start, subLine.getStart());
        assertEquals(end, subLine.getEnd());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Interval interval1 = new Interval(0.5, 0.7);
        Interval interval2 = new Interval(0.9, 1.1);
        Region<Euclidean1D> region = new IntervalsSet(interval1, interval2);
        SubLine subLine = new SubLine(line, region);

        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        assertEquals(new Vector2D(0.5, 0.5), segment1.getStart());
        assertEquals(new Vector2D(0.65, 0.65), segment1.getEnd());
        Segment segment2 = segments.get(1);
        assertEquals(new Vector2D(0.9, 0.9), segment2.getStart());
        assertEquals(new Vector2D(1.0, 1.0), segment2.getEnd());
    }

    @Test
    public void testIntersectionWithNullSubLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        SubLine subLine = null;
        Vector2D intersection = subLine.intersection(line1, false);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionWithSameLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Vector2D intersection = subLine1.intersection(subLine1, false);
        assertNotNull(intersection);
        assertEquals(new Vector2D(0.5, 0), intersection);
    }

    @Test
    public void testIntersectionWithParallelLines() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(0, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalSet.Empty());
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionWithIntersectingLines() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5)));
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNotNull(intersection);
        assertEquals(new Vector2D(1, 1), intersection);
    }

    @Test
    public void testIntersectionWithEndpointsIncluded() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1), new Interval(0.9, 1.1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1), new Interval(0.9, 1.1)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNullIncludeEndPoints() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = null;
        subLine1.intersection(subLine2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNullSubLineAndIncludeEndPoints() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = null;
        subLine1.intersection(subLine2, true);
    }

}