import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    private double epsilon = 1e-10;

    @Test
    public void testIntersection() {
        // create two sub-lines with non-empty intersection
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertTrue(Math.abs(intersection.getX() - 0.5) < epsilon);
        assertTrue(Math.abs(intersection.getY() - 0.5) < epsilon);
    }

    @Test
    public void testNoIntersection() {
        // create two sub-lines with empty intersection
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 0);
        Vector2D end2 = new Vector2D(3, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test
    public void testIntersectAtStartPoint() {
        // create two sub-lines with intersection at start point
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 0);
        Vector2D end2 = new Vector2D(2, 2);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertTrue(Math.abs(intersection.getX() - 0) < epsilon);
        assertTrue(Math.abs(intersection.getY() - 0) < epsilon);
    }

    @Test
    public void testIntersectAtEndPoint() {
        // create two sub-lines with intersection at end point
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(1, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertTrue(Math.abs(intersection.getX() - 1) < epsilon);
        assertTrue(Math.abs(intersection.getY() - 1) < epsilon);
    }

    @Test
    public void testNoIntersectionIncludeEndPoints() {
        // create two sub-lines with empty intersection
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 0);
        Vector2D end2 = new Vector2D(3, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionSegmentOnLine() {
        // create two sub-lines with non-empty intersection on a line
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 0);
        Vector2D end2 = new Vector2D(2, 2);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertTrue(Math.abs(intersection.getX() - 0) < epsilon);
        assertTrue(Math.abs(intersection.getY() - 0) < epsilon);
    }
}