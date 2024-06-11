import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    // Test constructor with two endpoints
    @Test
    public void testConstructorWithEndpoints() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        assertNotNull(subLine);
    }

    // Test constructor with a segment
    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end);
        SubLine subLine = new SubLine(segment);
        assertNotNull(subLine);
    }

    // Test getting segments for a sub-line with one segment
    @Test
    public void testGetSegmentsOneSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
    }

    // Test getting segments for a sub-line with no segments
    @Test
    public void testGetSegmentsNoSegments() {
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(0, 0));
        List<Segment> segments = subLine.getSegments();
        assertEquals(0, segments.size());
    }

    // Test intersection with another sub-line
    @Test
    public void testIntersection() {
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Vector2D(1, 0), new Vector2D(0, 1));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
    }

    // Test building interval set from two points
    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        IntervalsSet intervalsSet = SubLine.buildIntervalSet(start, end);
        assertNotNull(intervalsSet);
    }

    // Test side method
    @Test
    public void testSide() {
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line = new Line(new Vector2D(1, 0), new Vector2D(0, 1));
        Side side = subLine.side(line);
        assertNotNull(side);
    }

    // Test split method
    @Test
    public void testSplit() {
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line = new Line(new Vector2D(1, 0), new Vector2D(0, 1));
        SplitSubHyperplane splitSubHyperplane = subLine.split(line);
        assertNotNull(splitSubHyperplane);
    }
}