import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.*;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0)).toHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.1, 0.3));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineConstructorWithStartAndEnd() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        Line line      = (Line) subLine.getHyperplane();
        assertEquals(line.getStart(), start);
        assertEquals(line.getEnd(), end);
        IntervalsSet expectedIntervalSet = new IntervalsSet(new Interval(start.getX(), end.getX()));
        IntervalsSet actualIntervalSet = ((SubLine) subLine).getRemainingRegion();
        assertEquals(expectedIntervalSet, actualIntervalSet);
    }

    @Test
    public void testSubLineConstructorWithSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 1);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        Line line      = (Line) subLine.getHyperplane();
        assertEquals(line.getStart(), start);
        assertEquals(line.getEnd(), end);
        IntervalsSet expectedIntervalSet = new IntervalsSet(new Interval(start.getX(), end.getX()));
        IntervalsSet actualIntervalSet = ((SubLine) subLine).getRemainingRegion();
        assertEquals(expectedIntervalSet, actualIntervalSet);
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 0);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2   = new Vector2D(2, 1);
        Segment segment1 = new Segment(start, end, new Line(start, end));
        Segment segment2 = new Segment(start2, end2, new Line(start2, end2));
        Line line = new Line(start, end2);
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(start.getX(), end.getX()),
                                                              new Interval(start2.getX(), end2.getX())));
        List<Segment> segments = subLine.getSegments();
        List<Segment> expectedSegments = new ArrayList<>();
        expectedSegments.add(segment1);
        expectedSegments.add(segment2);
        assertEquals(expectedSegments, segments);
    }

    @Test
    public void testIntersectionWithSameLine() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 0);
        Line line1 = new Line(start, end);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(start.getX(), end.getX())));
        Vector2D intersection = subLine1.intersection(subLine1, true);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionWithDifferentLines() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 0);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2   = new Vector2D(2, 1);
        Line line1 = new Line(start, end);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(start.getX(), end.getX())));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(start2.getX(), end2.getX())));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        // intersection point is not important here, just check that it's not null
        assertNotNull(intersection);
    }

    @Test(expected = AssertionError.class)
    public void testIntersectionWithParallelLines() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 0);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2   = new Vector2D(2, 1);
        Line line1 = new Line(start, end);
        Line line2 = new Line(start2, end2);
        Line line1Perpendicular = new Line(new Vector2D(0, -1), new Vector2D(0, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(start.getX(), end.getX())));
        SubLine subLine2 = new SubLine(line1Perpendicular, new IntervalsSet(new Interval(start2.getX(), end2.getX())));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNullSubLine() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 0);
        Line line = new Line(start, end);
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(start.getX(), end.getX())));
        SubLine nullSubLine = null;
        Vector2D intersection = subLine.intersection(nullSubLine, true);
        assertNull(intersection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNullIncludeEndPoints() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 0);
        Line line1 = new Line(start, end);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(start.getX(), end.getX())));
        Line line2 = new Line(new Vector2D(-1, 0), new Vector2D(2, 0));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(start.getX(), end.getX())));
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }

}