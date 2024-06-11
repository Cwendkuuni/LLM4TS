import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        // Create a SubLine instance
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Get the segments
        List<Segment> segments = subLine.getSegments();

        // Check if the segments are correctly retrieved
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        // Create two SubLine instances
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check intersection
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(0.5, 0.5), intersectionPoint);
    }

    // Add more test cases for other methods in SubLine class

}