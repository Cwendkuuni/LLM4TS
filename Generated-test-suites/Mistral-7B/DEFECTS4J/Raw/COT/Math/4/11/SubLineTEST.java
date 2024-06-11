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
        Vector2D end   = new Vector2D(1, 1);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        Region<Euclidean1D> remainingRegion = buildIntervalSet(start, end);
        SubLine subLine = new SubLine(start, end);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 1);
        Segment segment = new Segment(start, end, new Line(start, end));
        Hyperplane<Euclidean2D> hyperplane = segment.getLine();
        Region<Euclidean1D> remainingRegion = buildIntervalSet(start, end);
        SubLine subLine = new SubLine(segment);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Interval interval1 = new Interval(0, 0.5);
        Interval interval2 = new Interval(0.5, 1);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(interval1, interval2);
        SubLine subLine = new SubLine(line, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        Vector2D start1 = segment1.getStart();
        Vector2D end1   = segment1.getEnd();
        assertEquals(new Vector2D(0, 0), start1);
        assertEquals(new Vector2D(0.5, 0.5), end1);
        Segment segment2 = segments.get(1);
        Vector2D start2 = segment2.getStart();
        Vector2D end2   = segment2.getEnd();
        assertEquals(new Vector2D(0.5, 0.5), start2);
        assertEquals(new Vector2D(1, 1), end2);
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
        line1.setLine(new Line(new Vector2D(0, 0), new Vector2D(1, 2)));
        line2.setLine(new Line(new Vector2D(1, 1), new Vector2D(2, 1)));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));
        intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1, 1), intersection);
        intersection = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(1, 1), intersection);
    }

    private static Region<Euclidean1D> buildIntervalSet(Vector2D start, Vector2D end) {
        Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start).getX(),
                               line.toSubSpace(end).getX());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(0, 0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));
        subLine1.intersection(subLine2, true);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Side side = subLine1.side(hyperplane);
        assertEquals(Side.MINUS, side);
        side = subLine2.side(hyperplane);
        assertEquals(Side.PLUS, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        assertNull(split.getSubHyperplane1());
        assertNull(split.getSubHyperplane2());
        split = subLine2.split(hyperplane);
        assertNull(split.getSubHyperplane1());
        assertNull(split.getSubHyperplane2());
        hyperplane = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        split = subLine1.split(hyperplane);
        assertNotNull(split.getSubHyperplane1());
        assertNotNull(split.getSubHyperplane2());
        split = subLine2.split(hyperplane);
        assertNotNull(split.getSubHyperplane1());
        assertNotNull(split.getSubHyperplane2());
    }

}