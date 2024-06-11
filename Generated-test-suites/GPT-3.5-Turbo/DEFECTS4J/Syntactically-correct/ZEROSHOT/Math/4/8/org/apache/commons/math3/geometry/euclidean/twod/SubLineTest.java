import org.junit.Test;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        SubLine subLine = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                new IntervalsSet(0, 1));
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(new Vector2D(0, 0), segment.getStart());
        assertEquals(new Vector2D(1, 1), segment.getEnd());
        assertEquals(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), segment.getLine());
    }

    @Test
    public void testIntersection() {
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                new IntervalsSet(0, 1));
        SubLine subLine2 = new SubLine(new Line(new Vector2D(0, 1), new Vector2D(1, 0)),
                new IntervalsSet(0, 1));
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }
}