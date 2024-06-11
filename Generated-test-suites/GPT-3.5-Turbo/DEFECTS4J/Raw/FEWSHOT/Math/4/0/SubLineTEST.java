import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testConstructor() {
        Line line = new Line(new Vector2D(1, 2), new Vector2D(3, 4));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, remainingRegion);

        assertEquals(line, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithPoints() {
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        Line line = new Line(start, end);
        SubLine subLine = new SubLine(start, end);

        assertEquals(line, subLine.getHyperplane());
        assertEquals(buildIntervalSet(start, end), subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        Line line = new Line(start, end);
        Segment segment = new Segment(start, end, line);
        SubLine subLine = new SubLine(segment);

        assertEquals(line, subLine.getHyperplane());
        assertEquals(buildIntervalSet(start, end), subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        Line line = new Line(start, end);
        SubLine subLine = new SubLine(start, end);

        List<Segment> segments = subLine.getSegments();

        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
        assertEquals(line, segments.get(0).getLine());
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(1, 2);
        Vector2D end1 = new Vector2D(3, 4);
        Vector2D start2 = new Vector2D(2, 3);
        Vector2D end2 = new Vector2D(4, 5);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);

        assertEquals(new Vector2D(2, 3), intersection);
    }

    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(1, 1);
        Vector2D end1 = new Vector2D(3, 3);
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(4, 4);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        assertEquals(Side.HYPER, subLine1.side(line2));
        assertEquals(Side.HYPER, subLine2.side(line1));

        Line line3 = new Line(start1, start2);
        Line line4 = new Line(end1, end2);

        assertEquals(Side.MINUS, subLine1.side(line3));
        assertEquals(Side.PLUS, subLine1.side(line4));
    }

    @Test
    public void testSplit() {
        Vector2D start = new Vector2D(1, 2);
        Vector2D end1 = new Vector2D(2, 4);
        Vector2D end2 = new Vector2D(3, 6);
        Vector2D end3 = new Vector2D(4, 8);
        Line line = new Line(start, end3);
        SubLine subLine = new SubLine(start, end3);

        SubLine splitSubLine = subLine.split(new Line(end1, end2)).getPlus();

        assertEquals(line, splitSubLine.getHyperplane());
        assertEquals(buildIntervalSet(end1, end2), splitSubLine.getRemainingRegion());
    }

    private IntervalsSet buildIntervalSet(final Vector2D start, final Vector2D end) {
        final Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start).getX(),
                                line.toSubSpace(end).getX());
    }
}