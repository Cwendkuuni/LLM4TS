import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSimpleConstructor() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithEndpoints() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(start, end);
        SubLine subLine = new SubLine(start, end);

        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Line line = new Line(start, end);
        Segment segment = new Segment(start, end, line);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(start, end);
        SubLine subLine = new SubLine(segment);

        assertEquals(line, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(3, 3));
        IntervalsSet intervalSet = new IntervalsSet(
                new Interval(1, 2),
                new Interval(3, 4),
                new Interval(5, 6)
        );
        SubLine subLine = new SubLine(line, intervalSet);

        List<Segment> segments = subLine.getSegments();
        assertEquals(3, segments.size());

        Segment segment1 = segments.get(0);
        Vector2D start1 = segment1.getStart();
        Vector2D end1 = segment1.getEnd();
        assertEquals(new Vector2D(1, 1), start1);
        assertEquals(new Vector2D(2, 2), end1);

        Segment segment2 = segments.get(1);
        Vector2D start2 = segment2.getStart();
        Vector2D end2 = segment2.getEnd();
        assertEquals(new Vector2D(3, 3), start2);
        assertEquals(new Vector2D(4, 4), end2);

        Segment segment3 = segments.get(2);
        Vector2D start3 = segment3.getStart();
        Vector2D end3 = segment3.getEnd();
        // Infinite point in positive infinity direction
        assertEquals(new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), start3);
        assertEquals(new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), end3);
    }

    @Test
    public void testIntersectionWithNullSubLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine = null;
        Vector2D result = subLine.intersection(line1, false);
        assertNull(result);
    }

    @Test
    public void testIntersectionWithSameLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Vector2D result = subLine1.intersection(subLine1, false);
        assertNull(result);
    }

    @Test
    public void testIntersectionWithParallelogramSubLines() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));
        Vector2D result = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(1, 1), result);
    }

    @Test
    public void testIntersectionWithIntersectingSubLines() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));
        Vector2D result = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(1, 1), result);
    }

    @Test
    public void testIntersectionWithOverlappingSubLines() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 2)));
        Vector2D result = subLine1.intersection(subLine2, false);
        assertNull(result);
    }

    @Test
    public void testIntersectionWithDisjointSubLines() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(2, 2), new Vector2D(3, 3));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 3)));
        Vector2D result = subLine1.intersection(subLine2, false);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithInvalidIncludeEndPoints() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        subLine1.intersection(subLine2, true);
    }

}