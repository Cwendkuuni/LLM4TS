import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        // Test simple constructor
        SubLine subLine = new SubLine(new Hyperplane<Euclidean2D>(new Line(new Vector2D(0, 0), new Vector2D(1, 0))),
                                     new IntervalsSet(new Interval(0, 1), new Interval(2, 3)));
        assertEquals(new Line(new Vector2D(0, 0), new Vector2D(1, 0)), subLine.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(0, 1), new Interval(2, 3)), subLine.getRemainingRegion());

        // Test constructor from two endpoints
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(3, 2);
        SubLine subLine1 = new SubLine(start, end);
        assertEquals(new Line(start, end), subLine1.getHyperplane());
        List<Segment> segments = subLine1.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());

        // Test constructor from a segment
        Segment segment1 = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(1, 1)));
        SubLine subLine2 = new SubLine(segment1);
        assertEquals(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), subLine2.getHyperplane());
        segments = subLine2.getSegments();
        assertEquals(1, segments.size());
        assertEquals(segment1, segments.get(0));
    }

    @Test
    public void testGetSegments() {
        // Test getSegments method
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet intervals = new IntervalsSet(new Interval(0, 0.5), new Interval(0.5, 1), new Interval(1.5, 2));
        SubLine subLine = new SubLine(line, intervals);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        assertEquals(new Vector2D(0, 0), segment1.getStart());
        assertEquals(new Vector2D(0.5, 0.5), segment1.getEnd());
        Segment segment2 = segments.get(1);
        assertEquals(new Vector2D(0.5, 0.5), segment2.getStart());
        assertEquals(new Vector2D(1.5, 1), segment2.getEnd());

        // Test empty sub-line
        Line emptyLine = new Line(new Vector2D(0, 0), new Vector2D(Double.POSITIVE_INFINITY, 0));
        SubLine emptySubLine = new SubLine(emptyLine, new IntervalsSet());
        segments = emptySubLine.getSegments();
        assertTrue(segments.isEmpty());
    }

    @Test
    public void testIntersection() {
        // Test intersection method with includeEndPoints = true
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2), new Interval(2, 3)));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1, 1), intersectionPoint);

        // Test intersection method with includeEndPoints = false
        Line line3 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line4 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine3 = new SubLine(line3, new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2)));
        SubLine subLine4 = new SubLine(line4, new IntervalsSet(new Interval(1, 2), new Interval(2, 3)));
        intersectionPoint = subLine3.intersection(subLine4, false);
        assertNull(intersectionPoint);

        // Test intersection method with lines parallel but not intersecting
        Line line5 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Line line6 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine5 = new SubLine(line5, new IntervalsSet(new Interval(0, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)));
        SubLine subLine6 = new SubLine(line6, new IntervalsSet(new Interval(0, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)));
        intersectionPoint = subLine5.intersection(subLine6, true);
        assertNull(intersectionPoint);

        // Test intersection method with lines intersecting at a point
        Line line7 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line8 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine7 = new SubLine(line7, new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2)));
        SubLine subLine8 = new SubLine(line8, new IntervalsSet(new Interval(1, 2), new Interval(2, 3)));
        intersectionPoint = subLine7.intersection(subLine8, true);
        assertEquals(new Vector2D(1, 1), intersectionPoint);
    }
}