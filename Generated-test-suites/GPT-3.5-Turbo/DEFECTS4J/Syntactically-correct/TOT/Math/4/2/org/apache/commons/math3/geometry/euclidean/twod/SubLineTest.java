import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        assertEquals(subLine.getHyperplane().toSpace(new Vector1D(0)), start);
        assertEquals(subLine.getHyperplane().toSpace(new Vector1D(1)), end);
        assertEquals(subLine.getRemainingRegion().getSize(), 1.0, 1e-10);
    }

    @Test
    public void testSubLineSegmentConstructor() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end);
        SubLine subLine = new SubLine(segment);

        assertEquals(subLine.getHyperplane().toSpace(new Vector1D(0)), start);
        assertEquals(subLine.getHyperplane().toSpace(new Vector1D(1)), end);
        assertEquals(subLine.getRemainingRegion().getSize(), 1.0, 1e-10);
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D middle = new Vector2D(1, 1);
        Vector2D end = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end);

        List<Segment> segments = subLine.getSegments();

        assertEquals(segments.size(), 1);
        assertEquals(segments.get(0).getStart(), start);
        assertEquals(segments.get(0).getEnd(), end);
        assertEquals(segments.get(0).getLine().toSpace(new Vector1D(0)), start);
        assertEquals(segments.get(0).getLine().toSpace(new Vector1D(2)), end);
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 0);
        Vector2D end2 = new Vector2D(0, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
      
        assertNotNull(intersectionPoint);
        assertEquals(intersectionPoint.getX(), 0, 1e-10);
        assertEquals(intersectionPoint.getY(), 0, 1e-10);
    }

    @Test
    public void testIntersectionNoEndPoint() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 0);
        Vector2D end2 = new Vector2D(0, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
      
        assertNull(intersectionPoint);
    }

}