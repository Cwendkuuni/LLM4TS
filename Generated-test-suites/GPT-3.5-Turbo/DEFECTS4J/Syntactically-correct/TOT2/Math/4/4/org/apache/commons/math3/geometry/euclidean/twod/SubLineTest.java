import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    // Test cases for constructor1
    @Test
    public void testConstructor1() {
        // Test constructor with valid input
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        assertNotNull(subLine);
    }

    // Test cases for constructor2
    @Test
    public void testConstructor2() {
        // Test constructor with valid input
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Line line = new Line(start, end);
        SubLine subLine = new SubLine(line, new IntervalsSet(0, 1));
        assertNotNull(subLine);
    }

    // Test cases for constructor3
    @Test
    public void testConstructor3() {
        // Test constructor with valid input
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        assertNotNull(subLine);
    }

    // Test cases for getSegments
    @Test
    public void testGetSegments() {
        // Test for getting segments from the sub-line
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
    }

    // Test cases for intersection
    @Test
    public void testIntersection() {
        // Test for intersection of two sub-lines
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(0, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);
    }

    // Test cases for side
    @Test
    public void testSide() {
        // Test for determining side of a hyperplane
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        Line hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Side side = subLine.side(hyperplane);
        assertEquals(Side.MINUS, side);
    }

    // Test cases for split
    @Test
    public void testSplit() {
        // Test for splitting a sub-line by a hyperplane
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        Line hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        SplitSubHyperplane<Euclidean2D> splitSubLine = subLine.split(hyperplane);
        assertNotNull(splitSubLine);
    }
}