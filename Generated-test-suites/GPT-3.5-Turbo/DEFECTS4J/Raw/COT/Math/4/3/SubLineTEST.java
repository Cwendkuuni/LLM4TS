import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.twod.*;

import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructor() {
        // Create a sub-line from two endpoints
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        assertNotNull(subLine);
    }

    @Test
    public void testGetSegments() {
        // Create a sub-line from two endpoints
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Get the segments of the sub-line
        List<Segment> segments = subLine.getSegments();

        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        // Create two sub-lines
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        // Perform intersection
        Vector2D intersection = subLine1.intersection(subLine2, true);

        assertNotNull(intersection);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    // Additional test cases for edge cases and exception handling scenarios go here

}

