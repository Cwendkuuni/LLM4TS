import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0)).toImplicitForm();
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
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        assertEquals(line, subLine.getHyperplane());
        assertEquals(intervalSet, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        Line line = new Line(start, end);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        assertEquals(line, subLine.getHyperplane());
        assertEquals(intervalSet, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 1);
        Line line = new Line(start, end1);
        Interval interval1 = new Interval(0, 1);
        Interval interval2 = new Interval(1, 2);
        IntervalsSet intervalSet = new IntervalsSet(interval1, interval2);
        SubLine subLine = new SubLine(line, intervalSet);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        assertEquals(start, segment1.getStart());
        assertEquals(new Vector2D(0, 1), segment1.getEnd());
        Segment segment2 = segments.get(1);
        assertEquals(new Vector2D(1, 1), segment2.getStart());
        assertEquals(end2, segment2.getEnd());
    }

    @Test
    public void testIntersectionWithSameLine() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Line line1 = new Line(start1, end1);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));

        Vector2D start2 = new Vector2D(0, 0);
        Vector2D end2 = new Vector2D(1, 1);
        Line line2 = new Line(start2, end2);
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        Vector2D expected = new Vector2D(0.5, 0.5);
        Vector2D result = subLine1.intersection(subLine2, true);
        assertEquals(expected, result);
    }

    @Test
    public void testIntersectionWithDifferentLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 0);
        Line line1 = new Line(start1, end1);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 1);
        Line line2 = new Line(start2, end2);
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        assertNull(subLine1.intersection(subLine2, true));
    }

    @Test
    public void testIntersectionWithEndpoints() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Line line1 = new Line(start1, end1);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 1);
        Line line2 = new Line(start2, end2);
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        assertNull(subLine1.intersection(subLine2, false));
    }

    @Test
    public void testIntersectionWithEndpointsInclude() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Line line1 = new Line(start1, end1);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 1);
        Line line2 = new Line(start2, end2);
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        Vector2D expected = new Vector2D(0.5, 0.5);
        Vector2D result = subLine1.intersection(subLine2, true);
        assertEquals(expected, result);
    }

}